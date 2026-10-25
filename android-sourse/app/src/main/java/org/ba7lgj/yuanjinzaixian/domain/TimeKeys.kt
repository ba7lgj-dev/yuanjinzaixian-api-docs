package org.ba7lgj.yuanjinzaixian.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeKeys {
    private const val DAY_SECONDS = 24 * 60 * 60L

    fun dateKey(epochSeconds: Long): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date(epochSeconds * 1000L))

    fun monthKey(epochSeconds: Long): String =
        SimpleDateFormat("yyyy-MM", Locale.CHINA).format(Date(epochSeconds * 1000L))

    fun monthKey(dateKey: String): String = dateKey.take(7)

    fun startOfDay(dateKey: String): Long {
        val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(dateKey)
            ?: return 0L
        return parsed.time / 1000L
    }

    fun endOfDay(dateKey: String): Long = startOfDay(dateKey) + DAY_SECONDS - 1L

    fun daysAgoKey(nowSeconds: Long, days: Int): String =
        dateKey(nowSeconds - days * DAY_SECONDS)

    fun isEditable(dateKey: String, nowSeconds: Long, editableDays: Int = 7): Boolean =
        dateKey >= daysAgoKey(nowSeconds, editableDays)

    fun isOlderThan(dateKey: String, nowSeconds: Long, days: Int): Boolean =
        dateKey < daysAgoKey(nowSeconds, days)

    fun dayLabel(dateKey: String, nowSeconds: Long): String {
        val today = dateKey(nowSeconds)
        if (dateKey == today) return "今天"
        val yesterday = dateKey(nowSeconds - DAY_SECONDS)
        if (dateKey == yesterday) return "昨天"
        return dateKey.substringAfter('-')
    }

    fun sameDay(a: Long, b: Long): Boolean = dateKey(a) == dateKey(b)

    fun addSeconds(epochSeconds: Long, seconds: Long): Long {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.timeInMillis = epochSeconds * 1000L
        calendar.add(Calendar.SECOND, seconds.toInt())
        return calendar.timeInMillis / 1000L
    }
}
