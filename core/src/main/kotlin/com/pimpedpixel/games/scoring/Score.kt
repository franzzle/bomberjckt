package com.pimpedpixel.games.scoring

class Score(@JvmField val score: Int) {
    companion object {
        @JvmStatic
        fun forInteger(score: Int): Score {
            return Score(score)
        }
    }
}
