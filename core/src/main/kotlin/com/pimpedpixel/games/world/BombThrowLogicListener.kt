package com.pimpedpixel.games.world

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.pimpedpixel.games.gameplay.GamePhaseState

class BombThrowLogicListener(private val engine: PooledEngine) : Listener<GamePhaseState> {
    override fun receive(signal: Signal<GamePhaseState>?,
                         gamePhaseState: GamePhaseState?) {
        if (gamePhaseState == GamePhaseState.GAME_RUNNING) {
            engine!!.addEntity(Entity().add(BombThrow()))
        }
    }
}
