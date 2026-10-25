package org.ba7lgj.yuanjinzaixian.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.text.TextAlign
import androidx.glance.unit.ColorProvider
import org.ba7lgj.yuanjinzaixian.R
import org.ba7lgj.yuanjinzaixian.YuanJinApplication
import org.ba7lgj.yuanjinzaixian.data.BatteryStateStore
import org.ba7lgj.yuanjinzaixian.domain.BatteryState
import org.ba7lgj.yuanjinzaixian.domain.BatteryStatus

class BatteryWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val state = BatteryStateStore.read(context)
        provideContent {
            BatteryContent(state)
        }
    }

    @Composable
    private fun BatteryContent(state: BatteryState) {
        GlanceTheme {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .clickable(actionRunCallback<RefreshBatteryAction>()),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(ColorProvider(Color(0xB3FFFFFF)))
                        .padding(horizontal = 6.dp, vertical = 12.dp)
                        .cornerRadius(12.dp),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                        Image(
                            provider = ImageProvider(R.drawable.ic_aima),
                            contentDescription = null,
                            modifier = GlanceModifier.width(14.dp).height(14.dp)
                        )
                        Spacer(GlanceModifier.width(4.dp))
                        Text(
                            text = displayName(state),
                            modifier = GlanceModifier.width(58.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        BarWithMarkers(state)
                        Spacer(GlanceModifier.width(4.dp))
                        Text(
                            text = rightText(state),
                            modifier = GlanceModifier.width(42.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun BarWithMarkers(state: BatteryState) {
        Column(horizontalAlignment = Alignment.Horizontal.CenterHorizontally) {
            BatteryBar(state)
            Box(modifier = GlanceModifier.width(136.dp).height(7.dp)) {
                Row(modifier = GlanceModifier.fillMaxSize()) {
                    Text(
                        text = "E",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurfaceVariant,
                            fontSize = 7.sp
                        )
                    )
                    Spacer(GlanceModifier.defaultWeight())
                    Text(
                        text = "F",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurfaceVariant,
                            fontSize = 7.sp
                        )
                    )
                }
                Row(modifier = GlanceModifier.fillMaxSize()) {
                    Spacer(GlanceModifier.width(25.dp))
                    Spacer(
                        GlanceModifier
                            .width(4.dp)
                            .height(4.dp)
                            .background(ColorProvider(Color(0xFF8B8B86)))
                            .cornerRadius(2.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun BatteryBar(state: BatteryState) {
        val soc = (state.displaySocPercent
            ?: state.socPercent
            ?: state.instantSocPercent
            ?: 0).coerceIn(0, 100)
        val fillWidth = soc / 100f * 136f
        val triangleLeft = (soc / 100f * 136f - 5f).coerceIn(0f, 126f)
        Column(modifier = GlanceModifier.width(136.dp)) {
            Box(modifier = GlanceModifier.width(136.dp).height(9.dp)) {
                Row(modifier = GlanceModifier.fillMaxSize()) {
                    Spacer(GlanceModifier.width(triangleLeft.dp))
                    Image(
                        provider = ImageProvider(pointerDrawable(state)),
                        contentDescription = null,
                        modifier = GlanceModifier.width(10.dp).height(7.dp)
                    )
                }
            }
            Box(
                modifier = GlanceModifier
                    .width(136.dp)
                    .height(27.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Spacer(
                    modifier = GlanceModifier
                        .width(136.dp)
                        .height(27.dp)
                        .background(ColorProvider(Color(0xFFF2F2EE)))
                        .cornerRadius(13.dp)
                )
                Spacer(
                    GlanceModifier
                        .width(fillWidth.dp)
                        .height(27.dp)
                        .background(ImageProvider(fillDrawable(soc)))
                )
                Row(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    Spacer(GlanceModifier.width(27.dp))
                    Spacer(
                        GlanceModifier
                            .width(1.dp)
                            .height(27.dp)
                            .background(ColorProvider(Color(0x66333333)))
                    )
                }
            }
        }
    }

    private fun displayName(state: BatteryState): String {
        val name = state.deviceLabel?.trim().orEmpty()
        if (name.isBlank()) return "我的车"
        return if (name.length > 5) name.take(4) + "…" else name
    }

    private fun rightText(state: BatteryState): String {
        val soc = state.displaySocPercent ?: state.socPercent ?: state.instantSocPercent
        // 只有“锚点以来的实时距离”可用时才显示公里，否则回到百分比。
        return state.liveRangeKm?.let { "$it km" } ?: soc?.let { "$it%" } ?: "--"
    }

    private fun fillDrawable(soc: Int): Int = when {
        soc < 20 -> R.drawable.widget_battery_fill_red
        soc < 80 -> R.drawable.widget_battery_fill_light_green
        else -> R.drawable.widget_battery_fill_dark_green
    }

    private fun pointerDrawable(state: BatteryState): Int =
        if (state.status == BatteryStatus.STABLE && !state.stale) {
            R.drawable.widget_pointer_green
        } else {
            R.drawable.widget_pointer_gray
        }
}

class RefreshBatteryAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val app = context.applicationContext as? YuanJinApplication ?: return
        runCatching { app.container.repository.refreshOnce() }
        BatteryWidget().updateAll(context)
    }
}

class BatteryWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = BatteryWidget()
}
