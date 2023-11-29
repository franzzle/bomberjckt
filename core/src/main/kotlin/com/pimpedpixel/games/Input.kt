package com.pimpedpixel.games

import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.pimpedpixel.games.gameplay.GamePhaseState
import com.pimpedpixel.games.gameplay.GamePhaseStateComponent
import com.pimpedpixel.games.gameplay.PlayerChoice

class Input(private val engine: PooledEngine,
            private val signalGamePhaseState: Signal<GamePhaseState>,
            private val playerChoiceSignal : Signal<PlayerChoice>,
            ) : InputProcessor {
    private val gamePhaseStateComponentFamily: Family = Family.all(GamePhaseStateComponent::class.java).get()
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.SPACE) {
            val entitiesFor = engine.getEntitiesFor(gamePhaseStateComponentFamily)
            if(entitiesFor.size() > 0){
                val gamePhaseStateComponent: GamePhaseStateComponent = entitiesFor
                    .first()
                    .getComponent(GamePhaseStateComponent::class.java)

                when (gamePhaseStateComponent.gamePhaseState) {
                    GamePhaseState.ATTRACT_SCREEN -> {
                        SoundPlayer.playStartSound()
                        playerChoiceSignal.dispatch(PlayerChoice.PLAYER_A)
                        gamePhaseStateComponent.transitionToNextState()
                    }
                    GamePhaseState.GAME_RUNNING -> {
                        signalGamePhaseState.dispatch(gamePhaseStateComponent.gamePhaseState)
                    }
                    else -> {
                        // It's OK
                    }
                }
                return true
            }
        }
        return false
    }


    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}
