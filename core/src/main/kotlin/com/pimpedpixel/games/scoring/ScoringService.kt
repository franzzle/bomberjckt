package com.pimpedpixel.games.scoring

interface ScoringService {
    /**
     * Add the score in the preferences stored on the device or pc
     * @param score
     */
    fun add(score: Score)

    /**
     * Returns the current highscore, null if no score is there yet
     * @return the current highscore
     */
    val currentHighScore: Int?
}
