package org.ba7lgj.yuanjinzaixian.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.ba7lgj.yuanjinzaixian.domain.BatteryEstimator
import org.ba7lgj.yuanjinzaixian.domain.DistanceAccounting
import org.ba7lgj.yuanjinzaixian.domain.EstimatorAnchors
import org.ba7lgj.yuanjinzaixian.domain.HistoryCycleView
import org.ba7lgj.yuanjinzaixian.domain.HistoryDayView
import org.ba7lgj.yuanjinzaixian.domain.HistoryMonthView
import org.ba7lgj.yuanjinzaixian.domain.HistoryOverview
import org.ba7lgj.yuanjinzaixian.domain.HistoryPauseView
import org.ba7lgj.yuanjinzaixian.domain.HistorySegmentView
import org.ba7lgj.yuanjinzaixian.domain.HistorySyncResult
import org.ba7lgj.yuanjinzaixian.domain.LiveRangeSnapshot
import org.ba7lgj.yuanjinzaixian.domain.MotionInterval
import org.ba7lgj.yuanjinzaixian.domain.PauseSpan
import org.ba7lgj.yuanjinzaixian.domain.PeopleRules
import org.ba7lgj.yuanjinzaixian.domain.RangeModel
import org.ba7lgj.yuanjinzaixian.domain.RangeProfile
import org.ba7lgj.yuanjinzaixian.domain.RangeTrainingSample
import org.ba7lgj.yuanjinzaixian.domain.TimeKeys
import org.ba7lgj.yuanjinzaixian.domain.TrainedRangeModel
import org.ba7lgj.yuanjinzaixian.domain.TrainingStatus
import org.ba7lgj.yuanjinzaixian.domain.TripAnalytics
import org.ba7lgj.yuanjinzaixian.domain.TripPoint
import org.ba7lgj.yuanjinzaixian.domain.TrainingRules
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

class TripRepository(
    private val context: Context,
    private val database: AppDatabase,
    private val api: BiotApi,
    private val credentials: CredentialStore
) {
    suspend fun currentRangeSnapshot(): LiveRangeSnapshot = withContext(Dispatchers.IO) {
        buildLiveSnapshot(loadRangeState())
    }

    suspend fun observeSample(
        sample: VoltageSampleEntity,
        stable: Boolean,
        trainingGrade: Boolean,
        chargePending: Boolean,
        cycleStartedAt: Long?
    ): LiveRangeSnapshot = withContext(Dispatchers.IO) {
        val settings = loadSettings()
        val simei = settings.deviceSimei ?: return@withContext emptySnapshot()
        var range = updateDistance(loadRangeState(), sample)
        if (chargePending) range = range.copy(anchorInvalidated = true)
        val stableVoltage = sample.voltageV
        val stableSoc = stableVoltage?.let {
            BatteryEstimator.socFromVoltage(it, estimatorAnchors())
        }
        if (stable && stableVoltage != null && stableSoc != null && !chargePending) {
            val anchorKey = "live:$simei:${cycleStartedAt ?: sample.observedAt}"
            val anchor = RestAnchorEntity(
                anchorKey = anchorKey,
                simei = simei,
                observedAt = sample.observedAt,
                voltageV = stableVoltage,
                socPercent = stableSoc,
                restSeconds = sample.restSeconds,
                grade = if (trainingGrade) "TRAINING" else "DISPLAY",
                source = "LIVE",
                dayKey = TimeKeys.dateKey(sample.observedAt),
                dayDistanceKm = sample.dayDistanceKm,
                cycleStartedAt = cycleStartedAt,
                createdAt = System.currentTimeMillis() / 1000
            )
            database.restAnchorDao().insert(anchor)
            if (range.activeAnchorKey != anchorKey && !range.anchorInvalidated) {
                val previous = range.activeAnchorKey?.let { database.restAnchorDao().byKey(it) }
                if (previous != null && range.distanceSinceAnchorKm >= 0.05) {
                    createLiveCycle(previous, anchor, range.distanceSinceAnchorKm)
                }
            }
            range = range.copy(
                activeAnchorKey = anchorKey,
                anchorInvalidated = false,
                distanceSinceAnchorKm = 0.0,
                lastDayKey = TimeKeys.dateKey(sample.observedAt),
                lastDayDistanceKm = sample.dayDistanceKm
            )
        }
        database.rangeStateDao().upsert(range)
        buildLiveSnapshot(range)
    }

    suspend fun setCurrentPeople(peopleCount: Int): LiveRangeSnapshot = withContext(Dispatchers.IO) {
        val range = loadRangeState().copy(currentPeopleCount = PeopleRules.clamp(peopleCount))
        database.rangeStateDao().upsert(range)
        buildLiveSnapshot(range)
    }

    suspend fun historyInitialDone(): Boolean = withContext(Dispatchers.IO) {
        loadRangeState().historyInitialDone
    }

    suspend fun trainingStatus(): TrainingStatus = withContext(Dispatchers.IO) {
        val state = loadRangeState()
        val simei = loadSettings().deviceSimei
        val cycles = database.tripCycleDao().all()
            .filter { simei == null || it.simei == simei }
        val active = database.modelVersionDao().active()
        TrainingStatus(
            cycleCount = cycles.size,
            trainingCycleCount = cycles.count { it.trainingEligible },
            modelKind = active?.kind,
            sampleCount = active?.sampleCount ?: 0,
            maeSocPerKm = active?.maeSocPerKm,
            historyInitialDone = state.historyInitialDone,
            lastSyncAt = state.lastHistorySyncAt
        )
    }

    suspend fun resetRangeState() = withContext(Dispatchers.IO) {
        val previous = loadRangeState()
        // 模型按车辆训练；切换设备或登出时不能让另一台车的模型继续生效。
        database.modelVersionDao().deactivateAll()
        database.rangeStateDao().upsert(
            RangeStateEntity(currentPeopleCount = PeopleRules.clamp(previous.currentPeopleCount))
        )
    }

    /** 只作废当前距离锚点，保留历史导入进度和本地模型。 */
    suspend fun clearActiveAnchor() = withContext(Dispatchers.IO) {
        val previous = loadRangeState()
        database.rangeStateDao().upsert(
            previous.copy(
                activeAnchorKey = null,
                anchorInvalidated = true,
                distanceSinceAnchorKm = 0.0,
                lastDayKey = null,
                lastDayDistanceKm = null
            )
        )
    }

    suspend fun syncHistory(initialBackfill: Boolean = false): HistorySyncResult =
        withContext(Dispatchers.IO) {
            val simei = loadSettings().deviceSimei
                ?: return@withContext HistorySyncResult(0, 0, 0, "没有选择设备")
            val initial = initialBackfill || !loadRangeState().historyInitialDone
            val result = withSession { sid -> importHistory(simei, sid, initial) }
            val range = loadRangeState()
                .let {
                    if (initial && !result.morePending) it.copy(historyInitialDone = true) else it
                }
                .copy(lastHistorySyncAt = System.currentTimeMillis() / 1000)
            database.rangeStateDao().upsert(range)
            cleanupOldData()
            updateMonthlySummaries()
            rebuildAllCycles()
            retrainModel()
            result
        }

    suspend fun historyOverview(): HistoryOverview = withContext(Dispatchers.IO) {
        val simei = loadSettings().deviceSimei
        val cycles = database.tripCycleDao().all()
            .filter { simei == null || it.simei == simei }
        val now = System.currentTimeMillis() / 1000
        val recentThreshold = TimeKeys.daysAgoKey(now, 7)
        val recentCycles = cycles.filter { it.dateKey >= recentThreshold }
        val oldCycles = cycles.filter { it.dateKey < recentThreshold }
        val days = recentCycles.groupBy { it.dateKey }
            .toSortedMap(compareByDescending { it })
            .map { (dateKey, dayCycles) ->
                HistoryDayView(
                    dateKey = dateKey,
                    cycles = dayCycles.sortedByDescending { it.startTime }.map { cycle ->
                        val segments = database.tripSegmentDao().forCycle(cycle.cycleKey)
                        val pauses = database.pauseIntervalDao().forCycle(cycle.cycleKey)
                        HistoryCycleView(
                            cycleKey = cycle.cycleKey,
                            startTime = cycle.startTime,
                            endTime = cycle.endTime,
                            startVoltageV = cycle.startVoltageV,
                            endVoltageV = cycle.endVoltageV,
                            distanceKm = cycle.distanceKm,
                            socDrop = (cycle.startSoc - cycle.endSoc).takeIf { it > 0 },
                            trainingEligible = cycle.trainingEligible,
                            trainingReason = cycle.trainingReason,
                            dateKey = cycle.dateKey,
                            editable = TimeKeys.isEditable(cycle.dateKey, now, 7),
                            segments = segments.map {
                                HistorySegmentView(
                                    segmentKey = it.segmentKey,
                                    cycleKey = it.cycleKey,
                                    sequence = it.sequence,
                                    startTime = it.startTime,
                                    endTime = it.endTime,
                                    distanceKm = it.distanceKm,
                                    avgSpeedKmh = it.avgSpeedKmh,
                                    ascentM = it.ascentM,
                                    descentM = it.descentM,
                                    peopleCount = it.peopleCount
                                )
                            },
                            pauses = pauses.map {
                                HistoryPauseView(
                                    pauseKey = it.pauseKey,
                                    cycleKey = cycle.cycleKey,
                                    startTime = it.startTime,
                                    endTime = it.endTime,
                                    source = it.source
                                )
                            }
                        )
                    }
                )
            }
        val months = oldCycles.groupBy { TimeKeys.monthKey(it.dateKey) }
            .toSortedMap(compareByDescending { it })
            .map { (month, monthCycles) ->
                HistoryMonthView(
                    monthKey = month,
                    cycleCount = monthCycles.size,
                    distanceKm = monthCycles.sumOf { it.distanceKm }
                )
            }
        HistoryOverview(
            recentDays = days,
            olderMonths = months,
            importedDays = database.historyImportDayDao().completedCount(),
            lastImportAt = database.historyImportDayDao().lastImportAt()
        )
    }

    suspend fun setSegmentPeople(segmentKey: String, peopleCount: Int?) =
        withContext(Dispatchers.IO) {
            val segment = database.tripSegmentDao().byKey(segmentKey)
                ?: throw IllegalArgumentException("找不到骑行区间")
            ensureEditable(segment.cycleKey)
            database.tripSegmentDao().setPeople(
                segmentKey,
                PeopleRules.clampOrNull(peopleCount),
                System.currentTimeMillis() / 1000
            )
            evaluateCycle(segment.cycleKey)
            retrainModel()
        }

    suspend fun mergeBoundary(cycleKey: String, boundaryIndex: Int) =
        withContext(Dispatchers.IO) {
            ensureEditable(cycleKey)
            val segments = database.tripSegmentDao().forCycle(cycleKey).sortedBy { it.sequence }
            if (boundaryIndex !in 0 until segments.lastIndex) return@withContext
            val boundaryTime = segments[boundaryIndex].endTime
            val pause = database.pauseIntervalDao().forCycle(cycleKey)
                .filter { it.active }
                .minByOrNull { abs(it.startTime - boundaryTime) }
                ?: return@withContext
            if (abs(pause.startTime - boundaryTime) > 30 * 60) return@withContext
            database.pauseIntervalDao().setActive(
                pause.pauseKey,
                false,
                System.currentTimeMillis() / 1000
            )
            rebuildCycle(cycleKey)
            retrainModel()
        }

    suspend fun splitSegment(segmentKey: String) = withContext(Dispatchers.IO) {
        val segment = database.tripSegmentDao().byKey(segmentKey)
            ?: throw IllegalArgumentException("找不到骑行区间")
        ensureEditable(segment.cycleKey)
        val midpoint = (segment.startTime + segment.endTime) / 2
        database.pauseIntervalDao().insert(
            PauseIntervalEntity(
                pauseKey = "manual:${segment.segmentKey}:$midpoint",
                simei = loadSettings().deviceSimei.orEmpty(),
                startTime = midpoint,
                endTime = midpoint,
                distanceM = 0.0,
                source = "manual",
                active = true,
                cycleKey = segment.cycleKey,
                updatedAt = System.currentTimeMillis() / 1000
            )
        )
        rebuildCycle(segment.cycleKey)
        retrainModel()
    }

    suspend fun shiftPause(pauseKey: String, deltaSeconds: Long) =
        withContext(Dispatchers.IO) {
            val pause = database.pauseIntervalDao().byKey(pauseKey) ?: return@withContext
            val cycleKey = pause.cycleKey ?: return@withContext
            ensureEditable(cycleKey)
            database.pauseIntervalDao().move(
                pauseKey,
                pause.startTime + deltaSeconds,
                pause.endTime + deltaSeconds,
                System.currentTimeMillis() / 1000
            )
            rebuildCycle(cycleKey)
            retrainModel()
        }

    suspend fun retrainModel(): TrainedRangeModel? = withContext(Dispatchers.IO) {
        val candidate = RangeModel.train(trainingSamples()) ?: return@withContext null
        val currentEntity = database.modelVersionDao().active()
        val current = RangeModel.decode(currentEntity?.coefficients)
        val now = System.currentTimeMillis() / 1000
        val encoded = RangeModel.encode(candidate)
        if (RangeModel.betterThan(candidate, current)) {
            database.modelVersionDao().deactivateAll()
            val id = database.modelVersionDao().insert(
                ModelVersionEntity(
                    kind = candidate.kind,
                    sampleCount = candidate.sampleCount,
                    maeSocPerKm = candidate.maeSocPerKm,
                    coefficients = encoded,
                    trainedAt = now,
                    active = true
                )
            )
            database.rangeStateDao().upsert(loadRangeState().copy(modelVersionId = id))
            candidate
        } else {
            database.modelVersionDao().insert(
                ModelVersionEntity(
                    kind = candidate.kind,
                    sampleCount = candidate.sampleCount,
                    maeSocPerKm = candidate.maeSocPerKm,
                    coefficients = encoded,
                    trainedAt = now,
                    active = false
                )
            )
            candidate
        }
    }

    /**
     * 数据库升级把人数规则收窄到 1–2 人后，用新规则重训一次。
     * 样本不足 5 个时 [retrainModel] 返回 null，模型保持物理基准。
     */
    suspend fun retrainPendingModel() = withContext(Dispatchers.IO) {
        val settings = loadSettings()
        if (!settings.modelNeedsRetrain) return@withContext
        saveSettings(settings.copy(modelNeedsRetrain = false))
        retrainModel()
    }

    suspend fun rollbackModel() = withContext(Dispatchers.IO) {
        val active = database.modelVersionDao().active()
        val previous = database.modelVersionDao().all()
            .filter { it.id != active?.id }
            .filter { active == null || it.trainedAt < active.trainedAt }
            .maxByOrNull { it.trainedAt }
        database.modelVersionDao().deactivateAll()
        if (previous != null) {
            database.modelVersionDao().activate(previous.id)
            database.rangeStateDao().upsert(
                loadRangeState().copy(modelVersionId = previous.id)
            )
        } else {
            database.rangeStateDao().upsert(loadRangeState().copy(modelVersionId = null))
        }
    }

    suspend fun resetModel() = withContext(Dispatchers.IO) {
        database.modelVersionDao().deactivateAll()
        val settings = loadSettings()
        if (settings.modelNeedsRetrain) {
            saveSettings(settings.copy(modelNeedsRetrain = false))
        }
        database.rangeStateDao().upsert(loadRangeState().copy(modelVersionId = null))
    }

    private suspend fun importHistory(
        simei: String,
        sid: String,
        initial: Boolean
    ): HistorySyncResult {
        val now = System.currentTimeMillis() / 1000
        val begin = if (initial) now - RETENTION_DAYS * DAY_SECONDS else now - 2 * DAY_SECONDS
        var windowStart = begin
        var importedDays = 0
        var lastError: String? = null
        var budget = if (initial) INITIAL_DAYS_PER_RUN else Int.MAX_VALUE
        var morePending = false
        while (windowStart <= now) {
            val windowEnd = minOf(windowStart + 31 * DAY_SECONDS, now)
            val days = runCatching {
                api.queryTrackDates(simei, windowStart, windowEnd, sid)
            }.getOrElse {
                lastError = it.message
                emptyList()
            }
            for (day in days.sortedBy { it.dateKey }) {
                val existing = database.historyImportDayDao().get(day.dateKey)
                val recent = day.dateKey >= TimeKeys.daysAgoKey(now, 2)
                if (!recent && existing?.status == "complete") continue
                if (budget <= 0) {
                    morePending = true
                    break
                }
                budget--
                try {
                    importDay(simei, sid, day, now)
                    importedDays++
                } catch (error: Exception) {
                    lastError = error.message
                    database.historyImportDayDao().upsert(
                        HistoryImportDayEntity(
                            dateKey = day.dateKey,
                            status = "failed",
                            pointCount = 0,
                            tripCount = 0,
                            pauseCount = 0,
                            lastError = error.message,
                            importedAt = now
                        )
                    )
                }
                delay(80)
            }
            if (morePending) break
            windowStart = windowEnd + 1
        }
        rebuildAllCycles()
        return HistorySyncResult(
            importedDays = importedDays,
            cycles = database.tripCycleDao().all().size,
            trainingCycles = database.tripCycleDao().trainingCount(),
            lastError = lastError,
            morePending = morePending
        )
    }

    private suspend fun importDay(
        simei: String,
        sid: String,
        day: TrackDayInfo,
        now: Long
    ) {
        val dayStart = TimeKeys.startOfDay(day.dateKey)
        val dayEnd = TimeKeys.endOfDay(day.dateKey)
        val points = api.queryTrackPoints(simei, dayStart, dayEnd, sid)
        val pauses = runCatching { api.queryPauses(simei, dayStart, dayEnd, sid) }
            .getOrDefault(emptyList())
        val trips = runCatching { api.queryPlatformTrips(simei, dayStart, dayEnd, sid) }
            .getOrDefault(emptyList())
        val importedAt = System.currentTimeMillis() / 1000
        val pointEntities = points
            .distinctBy { "${it.timeSec}:${it.lat}:${it.lon}" }
            .map {
                TrackPointEntity(
                    simei = simei,
                    timeSec = it.timeSec,
                    latE6 = (it.lat * 1_000_000.0).roundToInt(),
                    lonE6 = (it.lon * 1_000_000.0).roundToInt(),
                    wgs = it.wgs,
                    altitudeM = it.altitudeM,
                    altitudeUsable = it.altitudeUsable,
                    speedRaw = it.speedRaw,
                    speedKmh = it.speedKmh,
                    direction = it.direction,
                    segmentDistanceM = it.segmentDistanceM,
                    ptype = it.ptype,
                    externalVoltageV = it.externalVoltageV,
                    importedAt = importedAt
                )
            }
        if (pointEntities.isNotEmpty()) database.trackPointDao().insertAll(pointEntities)
        if (pauses.isNotEmpty()) {
            database.pauseIntervalDao().insertAll(
                pauses.map {
                    PauseIntervalEntity(
                        pauseKey = it.pauseKey,
                        simei = simei,
                        startTime = it.startTime,
                        endTime = it.endTime,
                        distanceM = it.distanceM,
                        source = "platform",
                        active = true,
                        cycleKey = null,
                        updatedAt = importedAt
                    )
                }
            )
        }
        if (trips.isNotEmpty()) {
            database.platformTripDao().insertAll(
                trips.map {
                    PlatformTripEntity(
                        tripKey = it.tripKey,
                        simei = simei,
                        startTime = it.startTime,
                        endTime = it.endTime,
                        distanceM = it.distanceM,
                        importedAt = importedAt
                    )
                }
            )
        }
        createHistoricalAnchors(simei, day, pauses, pointEntities)
        database.historyImportDayDao().upsert(
            HistoryImportDayEntity(
                dateKey = day.dateKey,
                status = "complete",
                pointCount = pointEntities.size,
                tripCount = trips.size,
                pauseCount = pauses.size,
                lastError = null,
                importedAt = importedAt
            )
        )
    }

    private suspend fun createHistoricalAnchors(
        simei: String,
        day: TrackDayInfo,
        pauses: List<PauseRecord>,
        points: List<TrackPointEntity>
    ) {
        val anchors = mutableListOf<RestAnchorEntity>()
        val calibration = estimatorAnchors()
        pauses.filter { it.endTime - it.startTime >= TRAINING_REST_SECONDS }
            .forEach { pause ->
                val windowStart = max(pause.startTime, pause.endTime - 30 * 60)
                val valid = points
                    .filter {
                        it.timeSec in windowStart..pause.endTime &&
                            (it.externalVoltageV ?: 0.0) > 0.0
                    }
                    .sortedBy { it.timeSec }
                if (valid.size < 2) return@forEach
                val first = valid.first()
                val last = valid.last()
                val span = last.timeSec - first.timeSec
                val delta = abs(last.externalVoltageV!! - first.externalVoltageV!!)
                if (span < 10 * 60 || delta > 0.05) return@forEach
                val voltage = last.externalVoltageV!!
                anchors += RestAnchorEntity(
                    anchorKey = "history:$simei:${pause.pauseKey}",
                    simei = simei,
                    observedAt = pause.endTime,
                    voltageV = voltage,
                    socPercent = BatteryEstimator.socFromVoltage(voltage, calibration) ?: 0,
                    restSeconds = pause.endTime - pause.startTime,
                    grade = "TRAINING",
                    source = "HISTORY",
                    dayKey = day.dateKey,
                    dayDistanceKm = null,
                    cycleStartedAt = pause.startTime,
                    createdAt = System.currentTimeMillis() / 1000
                )
            }
        if (anchors.isNotEmpty()) database.restAnchorDao().insertAll(anchors)
    }

    private suspend fun createLiveCycle(
        start: RestAnchorEntity,
        end: RestAnchorEntity,
        distanceKm: Double
    ) {
        if (distanceKm < 0.05) return
        val now = System.currentTimeMillis() / 1000
        // 与历史重建使用同一 key，后续同步轨迹时会覆盖并校正同一条周期。
        val cycleKey = "auto:${start.simei}:${start.anchorKey}:${end.anchorKey}"
        database.tripCycleDao().insert(
            TripCycleEntity(
                cycleKey = cycleKey,
                simei = start.simei,
                startAnchorKey = start.anchorKey,
                endAnchorKey = end.anchorKey,
                startTime = start.observedAt,
                endTime = end.observedAt,
                startVoltageV = start.voltageV,
                endVoltageV = end.voltageV,
                startSoc = start.socPercent,
                endSoc = end.socPercent,
                distanceKm = distanceKm,
                dateKey = start.dayKey,
                trainingEligible = false,
                trainingReason = "待轨迹与标注校验",
                chargingSuspected = false,
                createdAt = now,
                updatedAt = now
            )
        )
        rebuildCycle(cycleKey)
    }

    private suspend fun withSession(block: suspend (String) -> HistorySyncResult): HistorySyncResult {
        var sid = credentials.getSid()?.takeIf { it.isNotBlank() } ?: loginStored()
        return try {
            block(sid)
        } catch (error: BiotApiException) {
            if (error.code != BiotApi.SESSION_EXPIRED) throw error
            sid = loginStored()
            block(sid)
        }
    }

    private suspend fun loginStored(): String {
        val settings = loadSettings()
        val account = settings.account ?: throw BiotApiException(-1, "没有保存的账号")
        val password = credentials.getPassword() ?: throw BiotApiException(-1, "没有保存的密码")
        val result = api.login(account, password)
        credentials.saveSid(result.sid)
        return result.sid
    }

    private suspend fun rebuildAllCycles() {
        val simei = loadSettings().deviceSimei ?: return
        val anchors = dedupeAnchors(database.restAnchorDao().all(simei))
        for (index in 0 until anchors.lastIndex) {
            val start = anchors[index]
            val end = anchors[index + 1]
            if (end.observedAt <= start.observedAt) continue
            val trackDistance = distanceFromTracks(simei, start.observedAt, end.observedAt)
            val platformDistance = database.platformTripDao()
                .between(simei, start.observedAt, end.observedAt)
                .sumOf { it.distanceM } / 1000.0
            val distance = max(trackDistance, platformDistance)
            if (distance < 0.05) continue
            val now = System.currentTimeMillis() / 1000
            val cycleKey = "auto:$simei:${start.anchorKey}:${end.anchorKey}"
            val existing = database.tripCycleDao().byKey(cycleKey)
            val charging = database.chargeEventDao()
                .between(start.observedAt, end.observedAt)
                .any { it.choice != "not_charged" }
            database.tripCycleDao().insert(
                TripCycleEntity(
                    cycleKey = cycleKey,
                    simei = simei,
                    startAnchorKey = start.anchorKey,
                    endAnchorKey = end.anchorKey,
                    startTime = start.observedAt,
                    endTime = end.observedAt,
                    startVoltageV = start.voltageV,
                    endVoltageV = end.voltageV,
                    startSoc = start.socPercent,
                    endSoc = end.socPercent,
                    distanceKm = distance,
                    dateKey = start.dayKey,
                    trainingEligible = false,
                    trainingReason = "待校验",
                    chargingSuspected = charging,
                    createdAt = existing?.createdAt ?: now,
                    updatedAt = now
                )
            )
            rebuildCycle(cycleKey)
        }
    }

    private suspend fun rebuildCycle(cycleKey: String) {
        val cycle = database.tripCycleDao().byKey(cycleKey) ?: return
        val points = database.trackPointDao()
            .between(cycle.simei, cycle.startTime, cycle.endTime)
            .map { it.toDomain() }
        val pauses = database.pauseIntervalDao()
            .between(cycle.simei, cycle.startTime, cycle.endTime)
            .filter { it.active }
        val intervals = TripAnalytics.motionIntervals(
            cycle.startTime,
            cycle.endTime,
            pauses.map { it.toSpan() }
        ).ifEmpty {
            listOf(MotionInterval(cycle.startTime, cycle.endTime))
        }
        val trackDistance = TripAnalytics.totalDistanceKm(points)
        val totalDuration = intervals.sumOf { it.durationSeconds }.coerceAtLeast(1)
        val oldSegments = database.tripSegmentDao().forCycle(cycleKey)
        val now = System.currentTimeMillis() / 1000
        val segments = intervals.mapIndexed { index, interval ->
            val fallback = if (trackDistance < 0.01) {
                cycle.distanceKm * interval.durationSeconds.toDouble() / totalDuration
            } else {
                0.0
            }
            val metrics = TripAnalytics.metrics(
                points,
                interval.startTime,
                interval.endTime,
                fallback
            )
            val old = oldSegments.maxByOrNull {
                overlapSeconds(it.startTime, it.endTime, interval.startTime, interval.endTime)
            }?.takeIf {
                overlapSeconds(it.startTime, it.endTime, interval.startTime, interval.endTime) > 0
            }
            TripSegmentEntity(
                segmentKey = "$cycleKey:${interval.startTime}:${interval.endTime}",
                cycleKey = cycleKey,
                sequence = index,
                startTime = interval.startTime,
                endTime = interval.endTime,
                distanceKm = metrics.distanceKm,
                durationSeconds = metrics.durationSeconds,
                avgSpeedKmh = metrics.avgSpeedKmh,
                maxSpeedKmh = metrics.maxSpeedKmh,
                speedStdKmh = metrics.speedStdKmh,
                ascentM = metrics.ascentM,
                descentM = metrics.descentM,
                downhillRatio = metrics.downhillRatio,
                positiveAccel = metrics.positiveAccel,
                negativeAccel = metrics.negativeAccel,
                regenProxyPerKm = metrics.regenProxyPerKm,
                peopleCount = old?.peopleCount,
                updatedAt = now
            )
        }
        database.tripSegmentDao().deleteByCycle(cycleKey)
        database.tripSegmentDao().insertAll(segments)
        pauses.forEach { database.pauseIntervalDao().setCycle(it.pauseKey, cycleKey) }
        evaluateCycle(cycleKey)
    }

    private suspend fun evaluateCycle(cycleKey: String) {
        val cycle = database.tripCycleDao().byKey(cycleKey) ?: return
        val start = database.restAnchorDao().byKey(cycle.startAnchorKey)
        val end = database.restAnchorDao().byKey(cycle.endAnchorKey)
        val segments = database.tripSegmentDao().forCycle(cycleKey)
        val charging = database.chargeEventDao()
            .between(cycle.startTime, cycle.endTime)
            .any { it.choice != "not_charged" }
        val covered = segments.sumOf { it.distanceKm }
        val reason = TrainingRules.reason(
            TrainingRules.Input(
                startIsTrainingAnchor = start?.grade == "TRAINING",
                endIsTrainingAnchor = end?.grade == "TRAINING",
                chargingSuspected = charging || cycle.chargingSuspected,
                segmentCount = segments.size,
                unlabelledSegments = segments.count { it.peopleCount == null },
                coveredKm = covered,
                totalKm = cycle.distanceKm,
                startSoc = cycle.startSoc,
                endSoc = cycle.endSoc,
                durationSeconds = cycle.endTime - cycle.startTime
            )
        )
        database.tripCycleDao().setTraining(
            cycleKey,
            reason == TrainingRules.ELIGIBLE,
            reason,
            System.currentTimeMillis() / 1000
        )
    }

    private suspend fun trainingSamples(): List<RangeTrainingSample> {
        val result = mutableListOf<RangeTrainingSample>()
        val simei = loadSettings().deviceSimei
        database.tripCycleDao().all()
            .filter { it.trainingEligible && (simei == null || it.simei == simei) }
            .forEach { cycle ->
                val segments = database.tripSegmentDao().forCycle(cycle.cycleKey)
                if (segments.isEmpty() || segments.any { it.peopleCount == null }) return@forEach
                val totalKm = segments.sumOf { it.distanceKm }
                if (totalKm < 0.05 || cycle.startSoc <= cycle.endSoc) return@forEach
                val people = PeopleRules.clamp(
                    (
                        segments.sumOf { (it.peopleCount ?: PeopleRules.DEFAULT) * it.distanceKm } /
                            totalKm
                        ).roundToInt()
                )
                val profile = RangeProfile(
                    avgSpeedKmh = weighted(segments) { it.avgSpeedKmh },
                    ascentPerKm = weighted(segments) { it.ascentM } / totalKm,
                    descentPerKm = weighted(segments) { it.descentM } / totalKm,
                    downhillRatio = weighted(segments) { it.downhillRatio },
                    positiveAccel = weighted(segments) { it.positiveAccel },
                    negativeAccel = weighted(segments) { it.negativeAccel },
                    regenProxyPerKm = weighted(segments) { it.regenProxyPerKm }
                )
                result += RangeTrainingSample(
                    socDropPerKm = (cycle.startSoc - cycle.endSoc).toDouble() / totalKm,
                    peopleCount = people,
                    startSoc = cycle.startSoc.toDouble(),
                    profile = profile
                )
            }
        return result
    }

    private suspend fun buildLiveSnapshot(range: RangeStateEntity): LiveRangeSnapshot {
        val modelEntity = database.modelVersionDao().active()
        val model = RangeModel.decode(modelEntity?.coefficients)
        val anchor = range.activeAnchorKey?.let { database.restAnchorDao().byKey(it) }
        val modelState = when {
            model == null -> "物理基准"
            model.kind == "median" -> "本地中位数"
            model.kind == "regression" -> "本地回归"
            else -> "本地分区间回归"
        }
        if (anchor == null || range.anchorInvalidated) {
            return LiveRangeSnapshot(
                available = false,
                remainingKm = null,
                anchorRangeKm = null,
                distanceSinceAnchorKm = range.distanceSinceAnchorKm,
                anchorVoltageV = null,
                anchorObservedAt = null,
                source = "无有效锚点",
                currentPeople = range.currentPeopleCount,
                modelState = modelState
            )
        }
        val prediction = RangeModel.predictRange(
            model = model,
            anchorSoc = anchor.socPercent.toDouble(),
            peopleCount = range.currentPeopleCount,
            profile = recentProfile(),
            modelId = modelEntity?.id
        )
        val remaining = max(0.0, prediction.rangeKm - range.distanceSinceAnchorKm)
        return LiveRangeSnapshot(
            available = true,
            remainingKm = remaining.roundToInt(),
            anchorRangeKm = prediction.rangeKm.roundToInt(),
            distanceSinceAnchorKm = range.distanceSinceAnchorKm,
            anchorVoltageV = anchor.voltageV,
            anchorObservedAt = anchor.observedAt,
            source = prediction.source,
            currentPeople = range.currentPeopleCount,
            modelState = modelState
        )
    }

    private suspend fun recentProfile(): RangeProfile {
        val simei = loadSettings().deviceSimei
        val cycleKeys = database.tripCycleDao().all()
            .filter { simei == null || it.simei == simei }
            .map { it.cycleKey }
            .toSet()
        val segments = database.tripSegmentDao().all()
            .filter { it.cycleKey in cycleKeys }
            .filter { it.peopleCount != null && it.distanceKm > 0.01 }
            .takeLast(20)
        if (segments.isEmpty()) return RangeProfile()
        val total = segments.sumOf { it.distanceKm }.coerceAtLeast(0.001)
        return RangeProfile(
            avgSpeedKmh = weighted(segments) { it.avgSpeedKmh },
            ascentPerKm = weighted(segments) { it.ascentM } / total,
            descentPerKm = weighted(segments) { it.descentM } / total,
            downhillRatio = weighted(segments) { it.downhillRatio },
            positiveAccel = weighted(segments) { it.positiveAccel },
            negativeAccel = weighted(segments) { it.negativeAccel },
            regenProxyPerKm = weighted(segments) { it.regenProxyPerKm }
        )
    }

    private fun updateDistance(
        range: RangeStateEntity,
        sample: VoltageSampleEntity
    ): RangeStateEntity {
        val dayKm = sample.dayDistanceKm ?: return range
        val dayKey = TimeKeys.dateKey(sample.curTime ?: sample.observedAt)
        val lastDay = range.lastDayKey
        val lastKm = range.lastDayDistanceKm
        val added = DistanceAccounting.addedKm(lastDay, lastKm, dayKey, dayKm)
        return range.copy(
            distanceSinceAnchorKm = range.distanceSinceAnchorKm + added,
            lastDayKey = dayKey,
            lastDayDistanceKm = dayKm
        )
    }

    private suspend fun distanceFromTracks(
        simei: String,
        startTime: Long,
        endTime: Long
    ): Double = TripAnalytics.totalDistanceKm(
        database.trackPointDao().between(simei, startTime, endTime).map { it.toDomain() }
    )

    private fun dedupeAnchors(anchors: List<RestAnchorEntity>): List<RestAnchorEntity> {
        val result = mutableListOf<RestAnchorEntity>()
        anchors.sortedBy { it.observedAt }.forEach { anchor ->
            val last = result.lastOrNull()
            if (last != null && anchor.observedAt - last.observedAt <= 300) {
                if (anchor.grade == "TRAINING" && last.grade != "TRAINING") {
                    result[result.lastIndex] = anchor
                }
            } else {
                result += anchor
            }
        }
        return result
    }

    private suspend fun ensureEditable(cycleKey: String) {
        val cycle = database.tripCycleDao().byKey(cycleKey)
            ?: throw IllegalArgumentException("找不到行程周期")
        require(TimeKeys.isEditable(cycle.dateKey, System.currentTimeMillis() / 1000, 7)) {
            "超过 7 天的行程只读"
        }
    }

    private suspend fun updateMonthlySummaries() {
        val now = System.currentTimeMillis() / 1000
        val summaries = database.tripCycleDao().all()
            .groupBy { TimeKeys.monthKey(it.dateKey) }
            .map { (month, list) ->
                val distance = list.sumOf { it.distanceKm }
                val socDrop = list.sumOf { max(0, it.startSoc - it.endSoc).toDouble() }
                MonthlySummaryEntity(
                    monthKey = month,
                    cycleCount = list.size,
                    distanceKm = distance,
                    avgSocPerKm = if (distance > 0.1) socDrop / distance else null,
                    updatedAt = now
                )
            }
        if (summaries.isNotEmpty()) database.monthlySummaryDao().upsertAll(summaries)
    }

    private suspend fun cleanupOldData() {
        val cutoffSeconds = System.currentTimeMillis() / 1000 - RETENTION_DAYS * DAY_SECONDS
        database.trackPointDao().deleteOlderThan(cutoffSeconds)
        database.voltageSampleDao().deleteOlderThan(cutoffSeconds * 1000L)
        database.platformTripDao().deleteOlderThan(cutoffSeconds)
        database.pauseIntervalDao().deleteOrphanOlderThan(cutoffSeconds)
        database.restAnchorDao().deleteHistoryOlderThan(cutoffSeconds)
        database.tripCycleDao().deleteOlderThan(cutoffSeconds)
        database.tripSegmentDao().deleteOrphans()
    }

    private suspend fun estimatorAnchors(): EstimatorAnchors = EstimatorAnchors(
        fullVoltage = database.batteryAnchorDao().latest("full")?.voltageV,
        emptyVoltage = database.batteryAnchorDao().latest("empty")?.voltageV
    )

    private suspend fun loadSettings(): AppSettingsEntity =
        database.appSettingsDao().get() ?: AppSettingsEntity()

    private suspend fun saveSettings(settings: AppSettingsEntity) {
        database.appSettingsDao().upsert(settings)
    }

    private suspend fun loadRangeState(): RangeStateEntity =
        database.rangeStateDao().get() ?: RangeStateEntity()

    private suspend fun emptySnapshot(): LiveRangeSnapshot {
        val range = loadRangeState()
        return LiveRangeSnapshot(
            available = false,
            remainingKm = null,
            anchorRangeKm = null,
            distanceSinceAnchorKm = 0.0,
            anchorVoltageV = null,
            anchorObservedAt = null,
            source = "无锚点",
            currentPeople = range.currentPeopleCount,
            modelState = "物理基准"
        )
    }

    private fun TrackPointEntity.toDomain(): TripPoint = TripPoint(
        timeSec = timeSec,
        lat = latE6 / 1_000_000.0,
        lon = lonE6 / 1_000_000.0,
        altitudeM = altitudeM,
        altitudeUsable = altitudeUsable,
        speedKmh = speedKmh,
        segmentDistanceM = segmentDistanceM
    )

    private fun PauseIntervalEntity.toSpan(): PauseSpan = PauseSpan(
        pauseKey = pauseKey,
        startTime = startTime,
        endTime = endTime,
        distanceM = distanceM,
        source = source,
        active = active
    )

    private fun overlapSeconds(aStart: Long, aEnd: Long, bStart: Long, bEnd: Long): Long =
        (minOf(aEnd, bEnd) - max(aStart, bStart)).coerceAtLeast(0)

    private fun weighted(
        segments: List<TripSegmentEntity>,
        selector: (TripSegmentEntity) -> Double
    ): Double {
        val total = segments.sumOf { it.distanceKm }.coerceAtLeast(0.001)
        return segments.sumOf { selector(it) * it.distanceKm } / total
    }

    companion object {
        private const val DAY_SECONDS = 24 * 60 * 60L
        private const val RETENTION_DAYS = 730
        private const val TRAINING_REST_SECONDS = 150L * 60L
        private const val INITIAL_DAYS_PER_RUN = 60
    }
}
