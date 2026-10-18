package org.ba7lgj.yuanjinzaixian.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
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
                        .background(ColorProvider(Color(0xB3FFFFFF)))
                        .padding(4.dp)
                        .cornerRadius(12.dp),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.Vertical.CenterVertically
                    ) {
                        Text(
                            text = displayName(state),
                            modifier = GlanceModifier.width(58.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Column(
                            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                        ) {
                            BatteryBar(state)
                            if (
                                state.status == BatteryStatus.STABLE &&
                                !state.stale &&
                                state.rangeKm != null
                            ) {
                                Text(
                                    text = "预计 ${state.rangeKm} km",
                                    modifier = GlanceModifier.width(136.dp),
                                    style = TextStyle(
                                        color = GlanceTheme.colors.onSurfaceVariant,
                                        fontSize = 9.sp,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                        Spacer(GlanceModifier.width(6.dp))
                        Text(
                            text = state.voltageV?.let { "%.2fV".format(it) } ?: "--",
                            modifier = GlanceModifier.width(42.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
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
        val pointerOffset = soc / 100f * 131f
        Box(
            modifier = GlanceModifier
                .width(136.dp)
                .height(22.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Spacer(
                GlanceModifier
                    .width(136.dp)
                    .height(18.dp)
                    .background(ColorProvider(Color(0xFFF2F2EE)))
                    .cornerRadius(9.dp)
            )
            Spacer(
                GlanceModifier
                    .width(fillWidth.dp)
                    .height(18.dp)
                    .background(ColorProvider(fillColor(soc)))
                    .cornerRadius(9.dp)
            )
            Row(modifier = GlanceModifier.fillMaxSize()) {
                Spacer(GlanceModifier.width(pointerOffset.dp))
                Box(
                    modifier = GlanceModifier
                        .width(5.dp)
                        .height(22.dp)
                        .background(ColorProvider(Color(0xFFFFFFFF)))
                        .cornerRadius(3.dp)
                ) {
                    Spacer(
                        GlanceModifier
                            .fillMaxSize()
                            .padding(1.5.dp)
                            .background(pointerColor(state))
                            .cornerRadius(1.dp)
                    )
                }
            }
            Row(modifier = GlanceModifier.fillMaxSize()) {
                Spacer(GlanceModifier.width(27.dp))
                Spacer(
                    GlanceModifier
                        .width(1.dp)
                        .height(18.dp)
                        .background(ColorProvider(Color(0x66333333)))
                )
            }
        }
    }

    private fun displayName(state: BatteryState): String {
        val name = state.deviceLabel?.trim().orEmpty()
        if (name.isBlank()) return "我的车"
        return if (name.length > 6) name.take(5) + "…" else name
    }

    private fun fillColor(soc: Int): Color = when {
        soc < 20 -> Color(0xFFE57373)
        soc < 80 -> Color(0xFFA5D6A7)
        else -> Color(0xFF2E7D32)
    }

    private fun pointerColor(state: BatteryState): ColorProvider =
        if (state.status == BatteryStatus.STABLE && !state.stale) {
            ColorProvider(Color(0xFF1B5E20))
        } else {
            ColorProvider(Color(0xFF8B8B86))
        }
}

class RefreshBatteryAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val app = context.applicationContext as? YuanJinApplication ?: return
        runCatching { app.container.repository.immediateRefresh() }
        BatteryWidget().updateAll(context)
    }
}

class BatteryWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = BatteryWidget()
}
