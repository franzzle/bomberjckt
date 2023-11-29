package com.pimpedpixel.games.scoring

import kotlin.math.ln

private const val LEADING_ZEROES = 6

object ScoreFormatter {
    private val stringBuilder = StringBuilder(LEADING_ZEROES)

    @JvmStatic
    fun intToStringWithLeadingZeroes(num: Int): String {
        stringBuilder.clear() // Clear the StringBuilder for reuse

        return if (num == 0) {
            // Handle the special case when num is 0
            "0".padStart(LEADING_ZEROES, '0')
        } else {
            val zeroes = LEADING_ZEROES - (ln(num.toDouble()) / ln(10.0)).toInt() - 1
            for (i in 0 until zeroes) {
                stringBuilder.append('0')
            }
            stringBuilder.append(num)
            stringBuilder.toString()
        }
    }
}

