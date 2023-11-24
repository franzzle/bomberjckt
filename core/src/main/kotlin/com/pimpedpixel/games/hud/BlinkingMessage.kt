package com.pimpedpixel.games.hud

import com.pimpedpixel.games.gameplay.GamePhaseState
import com.pimpedpixel.games.gameplay.GamePhaseStateComponent

class BlinkingMessage(message: String = "",
                      private val gamePhaseStateToBeActive: GamePhaseState) : BaseUpdatableMessage(Alignment.OFF_CENTER) {
    private val duration: Float
    private var elapsed = 0f
    private var active = false

    init {
        setMessage(message)
        duration = 0.7f
        isVisible = false
    }

    override fun act(delta: Float) {
        if(active){
            if (elapsed < duration) {
                // Increase the elapsed time by the delta provided.
                elapsed += delta
            } else {
                elapsed = 0f
                isVisible = !isVisible
            }
        }else{
            elapsed = 0f
            isVisible = false
        }
    }

    fun updateActive(gamePhaseStateComponent : GamePhaseStateComponent) {
        active = gamePhaseStateComponent.gamePhaseState === gamePhaseStateToBeActive
    }
}
