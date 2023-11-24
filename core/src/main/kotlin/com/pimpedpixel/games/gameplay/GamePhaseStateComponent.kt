package com.pimpedpixel.games.gameplay

import com.badlogic.ashley.core.Component

class GamePhaseStateComponent : Component {
    var gamePhaseState: GamePhaseState = GamePhaseState.ATTRACT_SCREEN

    // Helper method to transition to the next state
    fun transitionToNextState() {
        gamePhaseState = when (gamePhaseState) {
            GamePhaseState.ATTRACT_SCREEN -> GamePhaseState.GAME_RUNNING
            GamePhaseState.GAME_RUNNING -> GamePhaseState.GAME_OVER
            GamePhaseState.GAME_OVER -> GamePhaseState.WHO_WON
            GamePhaseState.WHO_WON -> GamePhaseState.ATTRACT_SCREEN
        }
    }
}