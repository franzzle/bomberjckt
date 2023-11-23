package com.pimpedpixel.games.gameplay

import com.badlogic.ashley.core.Component

class GamePhaseStateComponent : Component {
    var gamePhaseState: GamePhaseState = GamePhaseState.ATTRACT_SCREEN

    // Helper method to transition to the next state
    fun transitionToNextState() {
        when (gamePhaseState) {
            GamePhaseState.ATTRACT_SCREEN -> gamePhaseState = GamePhaseState.GAME_RUNNING
            GamePhaseState.GAME_RUNNING -> gamePhaseState = GamePhaseState.GAME_OVER
            GamePhaseState.GAME_OVER -> gamePhaseState = GamePhaseState.ATTRACT_SCREEN
        }
    }
}