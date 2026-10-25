package org.ba7lgj.yuanjinzaixian.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.ba7lgj.yuanjinzaixian.YuanJinApplication
import org.ba7lgj.yuanjinzaixian.data.BiotApi
import org.ba7lgj.yuanjinzaixian.data.BiotApiException
import java.util.concurrent.TimeUnit

/**
 * 历史轨迹同步。首次登录后回填最近两年，之后每天增量同步最近两天。
 * 同步失败只影响历史页，不会阻塞实时电量采样。
 */
class HistorySyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as? YuanJinApplication ?: return Result.success()
        val initial = inputData.getBoolean(KEY_INITIAL, false)
        val settings = app.container.repository.getSettings()
        if (settings.deviceSimei.isNullOrBlank()) return Result.success()
        return try {
            app.container.trips.syncHistory(initialBackfill = initial)
            app.container.repository.refreshFromDatabase()
            // 首次回填按批次执行，未完成时让 WorkManager 继续下一批。
            if (!app.container.trips.historyInitialDone()) Result.retry() else Result.success()
        } catch (error: BiotApiException) {
            if (error.code == BiotApi.RATE_LIMITED) Result.retry() else Result.success()
        } catch (_: Exception) {
            Result.success()
        }
    }

    companion object {
        const val KEY_INITIAL = "initial_backfill"
    }
}

object HistorySyncScheduler {
    private const val INITIAL_WORK_NAME = "yuanjin_history_initial"
    private const val DAILY_WORK_NAME = "yuanjin_history_daily"

    fun sync(context: Context, enabled: Boolean, initialBackfill: Boolean) {
        val workManager = WorkManager.getInstance(context.applicationContext)
        if (!enabled) {
            workManager.cancelUniqueWork(INITIAL_WORK_NAME)
            workManager.cancelUniqueWork(DAILY_WORK_NAME)
            return
        }
        if (initialBackfill) {
            val request = OneTimeWorkRequestBuilder<HistorySyncWorker>()
                .setConstraints(networkConstraints())
                .setInputData(workDataOf(HistorySyncWorker.KEY_INITIAL to true))
                .build()
            workManager.enqueueUniqueWork(
                INITIAL_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                request
            )
        }
        val daily = PeriodicWorkRequestBuilder<HistorySyncWorker>(24, TimeUnit.HOURS)
            .setConstraints(networkConstraints())
            .build()
        workManager.enqueueUniquePeriodicWork(
            DAILY_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            daily
        )
    }

    private fun networkConstraints(): Constraints =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
}
