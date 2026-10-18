package org.ba7lgj.yuanjinzaixian.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ba7lgj.yuanjinzaixian.YuanJinApplication

class ChargeActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getLongExtra("event_id", -1L)
        val choice = intent.getStringExtra("choice") ?: return
        if (eventId <= 0) return
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val app = context.applicationContext as YuanJinApplication
                app.container.repository.resolveChargeEvent(eventId, choice)
                NotificationHelper.cancelCharge(context)
            } finally {
                pending.finish()
            }
        }
    }
}
