package org.ba7lgj.yuanjinzaixian.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.ba7lgj.yuanjinzaixian.R

object NotificationHelper {
    const val CHANNEL_ALERTS = "battery_alerts"
    const val NOTIFICATION_ID_CHARGE = 1001
    const val NOTIFICATION_ID_PROTECTION = 1002
    private const val ACTION_CHARGE = "org.ba7lgj.yuanjinzaixian.CHARGE_CHOICE"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            CHANNEL_ALERTS,
            "电量提醒",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "疑似充电/反弹与保护线提醒"
        }
        manager.createNotificationChannel(channel)
    }

    fun showChargeAmbiguity(
        context: Context,
        eventId: Long,
        deltaV: Double
    ) {
        val charged = actionIntent(context, eventId, "charged", 1)
        val notCharged = actionIntent(context, eventId, "not_charged", 2)
        val unknown = actionIntent(context, eventId, "unknown", 3)
        val notification = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("检测到疑似充电/电压反弹")
            .setContentText(
                "电压上升约 ${"%.2f".format(deltaV)}V，车辆是否刚刚充过电？"
            )
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "无法仅凭电压判断是车辆充电还是铅酸反弹。请选择一次，后续不再重复询问。"
            ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .addAction(0, "充过电", charged)
            .addAction(0, "没充电", notCharged)
            .addAction(0, "不确定", unknown)
            .build()
        notifySafely(context, NOTIFICATION_ID_CHARGE, notification)
    }

    fun showProtection(context: Context, voltage: Double?) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("已到电量保护线")
            .setContentText("当前外电电压 ${voltage ?: "--"}V，请尽快充电。")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        notifySafely(context, NOTIFICATION_ID_PROTECTION, notification)
    }

    fun cancelCharge(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID_CHARGE)
    }

    private fun actionIntent(
        context: Context,
        eventId: Long,
        choice: String,
        requestOffset: Int
    ): PendingIntent {
        val intent = Intent(context, ChargeActionReceiver::class.java)
            .setAction(ACTION_CHARGE)
            .putExtra("event_id", eventId)
            .putExtra("choice", choice)
        return PendingIntent.getBroadcast(
            context,
            (eventId.hashCode() * 10 + requestOffset),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun notifySafely(
        context: Context,
        id: Int,
        notification: android.app.Notification
    ) {
        runCatching { NotificationManagerCompat.from(context).notify(id, notification) }
    }
}
