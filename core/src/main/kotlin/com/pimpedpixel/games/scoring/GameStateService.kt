package com.pimpedpixel.games.scoring

interface GameStateService {
    /**
     * Adds a miss to the current game state
     */
    fun addMiss()

    /**
     * Adds a score to the current game state
     */
    fun addScore(scoreToAdd: Int)

    /**
     * Resets the current game state
     * Score is set to 0
     * Misses is set to 0
     */
    fun resetGameState()

    fun activateBomb()

    val score: String?
    val gameState: GameState?

    /**
     * Returns the number of misses
     * @return the number of misses
     */
    val numberOfMisses: String?
}
