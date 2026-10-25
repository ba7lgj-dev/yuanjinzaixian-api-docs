package org.ba7lgj.yuanjinzaixian

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.ba7lgj.yuanjinzaixian.data.AppContainer
import org.ba7lgj.yuanjinzaixian.data.BatteryStateStore
import org.ba7lgj.yuanjinzaixian.notifications.NotificationHelper
import org.ba7lgj.yuanjinzaixian.work.SamplingScheduler
import org.ba7lgj.yuanjinzaixian.work.HistorySyncScheduler

class YuanJinApplication : Application() {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        BatteryStateStore.load(this)
        NotificationHelper.createChannels(this)
        container = AppContainer(this)
        appScope.launch {
            val settings = container.repository.getSettings()
            SamplingScheduler.sync(
                context = this@YuanJinApplication,
                enabled = container.repository.hasCredentials() &&
                    settings.deviceSimei != null,
                stableMarkedAt = settings.stableMarkedAt
            )
            val historyEnabled = container.repository.hasCredentials() &&
                settings.deviceSimei != null
            HistorySyncScheduler.sync(
                context = this@YuanJinApplication,
                enabled = historyEnabled,
                initialBackfill = historyEnabled && !container.trips.historyInitialDone()
            )
        }
    }
}
