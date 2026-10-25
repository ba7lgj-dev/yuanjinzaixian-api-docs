package org.ba7lgj.yuanjinzaixian.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        VoltageSampleEntity::class,
        BatteryAnchorEntity::class,
        AnchorRequestEntity::class,
        ChargeEventEntity::class,
        AppSettingsEntity::class,
        TrackPointEntity::class,
        RestAnchorEntity::class,
        PauseIntervalEntity::class,
        PlatformTripEntity::class,
        TripCycleEntity::class,
        TripSegmentEntity::class,
        HistoryImportDayEntity::class,
        ModelVersionEntity::class,
        MonthlySummaryEntity::class,
        RangeStateEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun voltageSampleDao(): VoltageSampleDao
    abstract fun batteryAnchorDao(): BatteryAnchorDao
    abstract fun anchorRequestDao(): AnchorRequestDao
    abstract fun chargeEventDao(): ChargeEventDao
    abstract fun appSettingsDao(): AppSettingsDao
    abstract fun trackPointDao(): TrackPointDao
    abstract fun restAnchorDao(): RestAnchorDao
    abstract fun pauseIntervalDao(): PauseIntervalDao
    abstract fun platformTripDao(): PlatformTripDao
    abstract fun tripCycleDao(): TripCycleDao
    abstract fun tripSegmentDao(): TripSegmentDao
    abstract fun historyImportDayDao(): HistoryImportDayDao
    abstract fun modelVersionDao(): ModelVersionDao
    abstract fun monthlySummaryDao(): MonthlySummaryDao
    abstract fun rangeStateDao(): RangeStateDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "yuanjin_battery.db"
                ).addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                    MIGRATION_4_5,
                    MIGRATION_5_6,
                    MIGRATION_6_7,
                    MIGRATION_7_8
                )
                    .build()
                    .also { instance = it }
            }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE app_settings ADD COLUMN nickname TEXT")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS anchor_requests (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "type TEXT NOT NULL, " +
                        "requestedAt INTEGER NOT NULL, " +
                        "expiresAt INTEGER NOT NULL, " +
                        "status TEXT NOT NULL)"
                )
                db.execSQL("DELETE FROM battery_anchors")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM battery_anchors")
                db.execSQL("DELETE FROM anchor_requests")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE app_settings ADD COLUMN stableLockedVoltageV REAL")
                db.execSQL("ALTER TABLE app_settings ADD COLUMN stableLockedPosTime INTEGER")
                db.execSQL("ALTER TABLE app_settings ADD COLUMN stableMarkedAt INTEGER")
                db.execSQL("ALTER TABLE app_settings ADD COLUMN stableCycleStartedAt INTEGER")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE app_settings ADD COLUMN stableLockedComTime INTEGER")
                db.execSQL(
                    "UPDATE app_settings SET " +
                        "stableLockedVoltageV = NULL, " +
                        "stableLockedPosTime = NULL, " +
                        "stableLockedComTime = NULL, " +
                        "stableMarkedAt = NULL, " +
                        "stableCycleStartedAt = NULL"
                )
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS track_points (" +
                        "simei TEXT NOT NULL, " +
                        "timeSec INTEGER NOT NULL, " +
                        "latE6 INTEGER NOT NULL, " +
                        "lonE6 INTEGER NOT NULL, " +
                        "wgs TEXT, " +
                        "altitudeM REAL, " +
                        "altitudeUsable INTEGER NOT NULL, " +
                        "speedRaw REAL, " +
                        "speedKmh REAL, " +
                        "direction REAL, " +
                        "segmentDistanceM REAL, " +
                        "ptype TEXT, " +
                        "externalVoltageV REAL, " +
                        "importedAt INTEGER NOT NULL, " +
                        "PRIMARY KEY(simei, timeSec, latE6, lonE6))"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS rest_anchors (" +
                        "anchorKey TEXT NOT NULL PRIMARY KEY, " +
                        "simei TEXT NOT NULL, " +
                        "observedAt INTEGER NOT NULL, " +
                        "voltageV REAL NOT NULL, " +
                        "socPercent INTEGER NOT NULL, " +
                        "restSeconds INTEGER NOT NULL, " +
                        "grade TEXT NOT NULL, " +
                        "source TEXT NOT NULL, " +
                        "dayKey TEXT NOT NULL, " +
                        "dayDistanceKm REAL, " +
                        "cycleStartedAt INTEGER, " +
                        "createdAt INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS pause_intervals (" +
                        "pauseKey TEXT NOT NULL PRIMARY KEY, " +
                        "simei TEXT NOT NULL, " +
                        "startTime INTEGER NOT NULL, " +
                        "endTime INTEGER NOT NULL, " +
                        "distanceM REAL NOT NULL, " +
                        "source TEXT NOT NULL, " +
                        "active INTEGER NOT NULL, " +
                        "cycleKey TEXT, " +
                        "updatedAt INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS platform_trips (" +
                        "tripKey TEXT NOT NULL PRIMARY KEY, " +
                        "simei TEXT NOT NULL, " +
                        "startTime INTEGER NOT NULL, " +
                        "endTime INTEGER NOT NULL, " +
                        "distanceM REAL NOT NULL, " +
                        "importedAt INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS trip_cycles (" +
                        "cycleKey TEXT NOT NULL PRIMARY KEY, " +
                        "simei TEXT NOT NULL, " +
                        "startAnchorKey TEXT NOT NULL, " +
                        "endAnchorKey TEXT NOT NULL, " +
                        "startTime INTEGER NOT NULL, " +
                        "endTime INTEGER NOT NULL, " +
                        "startVoltageV REAL NOT NULL, " +
                        "endVoltageV REAL NOT NULL, " +
                        "startSoc INTEGER NOT NULL, " +
                        "endSoc INTEGER NOT NULL, " +
                        "distanceKm REAL NOT NULL, " +
                        "dateKey TEXT NOT NULL, " +
                        "trainingEligible INTEGER NOT NULL, " +
                        "trainingReason TEXT NOT NULL, " +
                        "chargingSuspected INTEGER NOT NULL, " +
                        "createdAt INTEGER NOT NULL, " +
                        "updatedAt INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS trip_segments (" +
                        "segmentKey TEXT NOT NULL PRIMARY KEY, " +
                        "cycleKey TEXT NOT NULL, " +
                        "sequence INTEGER NOT NULL, " +
                        "startTime INTEGER NOT NULL, " +
                        "endTime INTEGER NOT NULL, " +
                        "distanceKm REAL NOT NULL, " +
                        "durationSeconds INTEGER NOT NULL, " +
                        "avgSpeedKmh REAL NOT NULL, " +
                        "maxSpeedKmh REAL NOT NULL, " +
                        "speedStdKmh REAL NOT NULL, " +
                        "ascentM REAL NOT NULL, " +
                        "descentM REAL NOT NULL, " +
                        "downhillRatio REAL NOT NULL, " +
                        "positiveAccel REAL NOT NULL, " +
                        "negativeAccel REAL NOT NULL, " +
                        "regenProxyPerKm REAL NOT NULL, " +
                        "peopleCount INTEGER, " +
                        "updatedAt INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS history_import_days (" +
                        "dateKey TEXT NOT NULL PRIMARY KEY, " +
                        "status TEXT NOT NULL, " +
                        "pointCount INTEGER NOT NULL, " +
                        "tripCount INTEGER NOT NULL, " +
                        "pauseCount INTEGER NOT NULL, " +
                        "lastError TEXT, " +
                        "importedAt INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS model_versions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "kind TEXT NOT NULL, " +
                        "sampleCount INTEGER NOT NULL, " +
                        "maeSocPerKm REAL NOT NULL, " +
                        "coefficients TEXT NOT NULL, " +
                        "trainedAt INTEGER NOT NULL, " +
                        "active INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS monthly_summaries (" +
                        "monthKey TEXT NOT NULL PRIMARY KEY, " +
                        "cycleCount INTEGER NOT NULL, " +
                        "distanceKm REAL NOT NULL, " +
                        "avgSocPerKm REAL, " +
                        "updatedAt INTEGER NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS range_state (" +
                        "id INTEGER NOT NULL, " +
                        "activeAnchorKey TEXT, " +
                        "anchorInvalidated INTEGER NOT NULL, " +
                        "distanceSinceAnchorKm REAL NOT NULL, " +
                        "lastDayKey TEXT, " +
                        "lastDayDistanceKm REAL, " +
                        "currentPeopleCount INTEGER NOT NULL, " +
                        "modelVersionId INTEGER, " +
                        "historyInitialDone INTEGER NOT NULL, " +
                        "lastHistorySyncAt INTEGER, " +
                        "PRIMARY KEY(id))"
                )
            }
        }

        /**
         * v8：人数上限从 4 人收窄到 2 人（含骑行者）。
         * 只裁剪人数并停用旧模型，不删除轨迹、电压历史、周期和标定。
         */
        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE app_settings " +
                        "ADD COLUMN modelNeedsRetrain INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL("UPDATE app_settings SET modelNeedsRetrain = 1")
                db.execSQL(
                    "UPDATE trip_segments SET peopleCount = 2 " +
                        "WHERE peopleCount IS NOT NULL AND peopleCount > 2"
                )
                db.execSQL(
                    "UPDATE trip_segments SET peopleCount = 1 " +
                        "WHERE peopleCount IS NOT NULL AND peopleCount < 1"
                )
                db.execSQL(
                    "UPDATE range_state SET currentPeopleCount = " +
                        "CASE WHEN currentPeopleCount < 1 THEN 1 " +
                        "WHEN currentPeopleCount > 2 THEN 2 " +
                        "ELSE currentPeopleCount END"
                )
                db.execSQL("UPDATE model_versions SET active = 0")
            }
        }
    }
}
