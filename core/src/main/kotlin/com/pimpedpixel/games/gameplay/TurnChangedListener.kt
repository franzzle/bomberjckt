package com.pimpedpixel.games.gameplay

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_A
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_B
import com.pimpedpixel.games.world.FlyingMachine

class TurnChangedListener(
    private val engine: PooledEngine,
    private val flyingMachinePlayerA: FlyingMachine,
    private val flyingMachinePlayerB: FlyingMachine): Listener<PlayerChoice>{

    private val turnFamily: Family = Family.all(TurnComponent::class.java).get()

    override fun receive(signal: Signal<PlayerChoice>?, playerChoice: PlayerChoice?) {
        Gdx.app.log(this.javaClass.simpleName,"Turn changed to $playerChoice")

        val turnEntities = engine.getEntitiesFor(turnFamily)
        if (turnEntities.size() == 1) {
            val entityWithTurnComponent: Entity = turnEntities.first()
            val turnComponent: TurnComponent = entityWithTurnComponent.getComponent(TurnComponent::class.java)
            if(turnComponent.playerChoice === PLAYER_A){
                turnComponent.playerChoice = PLAYER_B
                flyingMachinePlayerB.startMovement()
            } else {
                turnComponent.playerChoice = PLAYER_A
                flyingMachinePlayerA.startMovement()
            }
        } else {
            Gdx.app.log(this.javaClass.simpleName,"No single entity with TurnComponent found.")
        }
    }
}