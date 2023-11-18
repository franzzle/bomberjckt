package com.pimpedpixel.games.scoring

import java.util.*

interface GameStateListener : EventListener {
    fun gameStateChanged(gameState: GameState?)
}
