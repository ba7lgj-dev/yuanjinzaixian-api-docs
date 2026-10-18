package org.ba7lgj.yuanjinzaixian

import android.app.Application
import org.ba7lgj.yuanjinzaixian.data.AppContainer
import org.ba7lgj.yuanjinzaixian.data.BatteryStateStore
import org.ba7lgj.yuanjinzaixian.notifications.NotificationHelper
import org.ba7lgj.yuanjinzaixian.work.SamplingScheduler

class YuanJinApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        BatteryStateStore.load(this)
        NotificationHelper.createChannels(this)
        container = AppContainer(this)
        SamplingScheduler.schedule(this)
    }
}
