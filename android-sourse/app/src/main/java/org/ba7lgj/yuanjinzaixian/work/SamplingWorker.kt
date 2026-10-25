package org.ba7lgj.yuanjinzaixian.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.ba7lgj.yuanjinzaixian.YuanJinApplication
import org.ba7lgj.yuanjinzaixian.data.BiotApi
import org.ba7lgj.yuanjinzaixian.data.BiotApiException

class SamplingWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as YuanJinApplication
        return try {
            app.container.repository.sample()
            Result.success()
        } catch (error: BiotApiException) {
            if (error.code == BiotApi.RATE_LIMITED) Result.retry() else Result.success()
        } catch (_: Exception) {
            Result.success()
        }
    }
}

class SamplingScheduleSyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val app = applicationContext as YuanJinApplication
        val settings = app.container.repository.getSettings()
        SamplingScheduler.sync(
            context = app,
            enabled = app.container.repository.hasCredentials() &&
                settings.deviceSimei != null,
            stableMarkedAt = settings.stableMarkedAt
        )
        return Result.success()
    }
}
