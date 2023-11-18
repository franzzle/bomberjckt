package com.pimpedpixel.games.scoring

import kotlin.math.ln

private const val LEADING_ZEROES = 6

object ScoreFormatter {
    @JvmStatic
    fun toScoreWithLeadingZeroes(prefix: String?, score: Int): String {
        val prefixOrDefault = prefix ?: "Score"
        val intToStringWithLeadingZeroes = intToStringWithLeadingZeroes(score)
        return "$prefixOrDefault : $intToStringWithLeadingZeroes"
    }

    private fun intToStringWithLeadingZeroes(num: Int): String {
        val resultingPrefixedScore = StringBuffer(LEADING_ZEROES)
        val zeroes = LEADING_ZEROES - (ln(num.toDouble()) / ln(10.0)).toInt() - 1
        for (i in 0 until zeroes) {
            resultingPrefixedScore.append(0)
        }
        return resultingPrefixedScore.append(num).toString()
    }
}
