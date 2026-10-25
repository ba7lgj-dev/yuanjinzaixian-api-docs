package org.ba7lgj.yuanjinzaixian.domain

/**
 * 载人人数规则：总人数包含骑行者，最多两人（骑行者 + 1 名乘客）。
 * 所有标注、缓存读取和模型特征都必须经过这里收敛，避免出现 3、4 人的历史脏数据。
 */
object PeopleRules {
    const val MIN = 1
    const val MAX = 2
    const val DEFAULT = 1

    fun clamp(value: Int): Int = value.coerceIn(MIN, MAX)

    fun clampOrNull(value: Int?): Int? = value?.let(::clamp)

    fun isSupported(value: Int?): Boolean = value != null && value in MIN..MAX

    fun label(value: Int): String = when (clamp(value)) {
        MIN -> "1 人（仅骑行者）"
        else -> "2 人（骑行者 + 1 名乘客）"
    }

    fun shortLabel(value: Int?): String = value?.let { "${clamp(it)} 人" } ?: "未标注"

    fun options(): List<Int> = (MIN..MAX).toList()
}
