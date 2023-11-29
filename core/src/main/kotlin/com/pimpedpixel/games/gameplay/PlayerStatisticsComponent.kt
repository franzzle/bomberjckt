package com.pimpedpixel.games.gameplay

import com.badlogic.ashley.core.Component

class PlayerStatisticsComponent(var playerChoice: PlayerChoice): Component  {
    var score: Int = 0
    var misses: Int = 0
    var hits: Int = 0
    val isOut: Boolean
        get() = misses >= 3

    val isStillInTheGame: Boolean
        get() = misses < 3

    fun reset() {
        score = 0
        misses = 0
        hits = 0
    }
}