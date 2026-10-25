package org.ba7lgj.yuanjinzaixian.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object SamplingScheduler {
    private const val CALIBRATION_WORK_NAME = "yuanjin_battery_calibration"
    private const val MONITOR_WORK_NAME = "yuanjin_battery_monitor"
    private const val LEGACY_WORK_NAME = "yuanjin_battery_sampling"
    private const val SYNC_WORK_NAME = "yuanjin_battery_schedule_sync"
    private const val MONITOR_INTERVAL_MS = 60 * 60 * 1000L

    fun sync(
        context: Context,
        enabled: Boolean,
        stableMarkedAt: Long?
    ) {
        val workManager = WorkManager.getInstance(context.applicationContext)
        workManager.cancelUniqueWork(LEGACY_WORK_NAME)
        if (!enabled) {
            workManager.cancelUniqueWork(CALIBRATION_WORK_NAME)
            workManager.cancelUniqueWork(MONITOR_WORK_NAME)
            return
        }
        if (stableMarkedAt == null) {
            workManager.cancelUniqueWork(MONITOR_WORK_NAME)
            enqueueCalibration(workManager)
        } else {
            workManager.cancelUniqueWork(CALIBRATION_WORK_NAME)
            enqueueMonitor(workManager, stableMarkedAt)
        }
    }

    fun requestSync(context: Context) {
        val request = OneTimeWorkRequestBuilder<SamplingScheduleSyncWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
            SYNC_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    internal fun monitorInitialDelayMs(nowMs: Long, stableMarkedAtSec: Long): Long {
        val elapsed = (nowMs - stableMarkedAtSec * 1000L).coerceAtLeast(0)
        val remainder = elapsed % MONITOR_INTERVAL_MS
        return when {
            elapsed == 0L -> MONITOR_INTERVAL_MS
            remainder == 0L -> 0L
            else -> MONITOR_INTERVAL_MS - remainder
        }
    }

    private fun enqueueCalibration(workManager: WorkManager) {
        val request = PeriodicWorkRequestBuilder<SamplingWorker>(15, TimeUnit.MINUTES)
            .setConstraints(networkConstraints())
            .build()
        workManager.enqueueUniquePeriodicWork(
            CALIBRATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun enqueueMonitor(workManager: WorkManager, stableMarkedAtSec: Long) {
        val delay = monitorInitialDelayMs(System.currentTimeMillis(), stableMarkedAtSec)
        val request = PeriodicWorkRequestBuilder<SamplingWorker>(60, TimeUnit.MINUTES)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(networkConstraints())
            .build()
        workManager.enqueueUniquePeriodicWork(
            MONITOR_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun networkConstraints(): Constraints =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
}
