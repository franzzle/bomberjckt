package com.pimpedpixel.games.gameplay

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_A
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_B
import com.pimpedpixel.games.world.FlyingMachine

class TurnChangedListener(
    private val flyingMachinePlayerA: FlyingMachine,
    private val flyingMachinePlayerB: FlyingMachine): Listener<PlayerChoice>{

    override fun receive(signal: Signal<PlayerChoice>?, playerChoice: PlayerChoice?) {
        Gdx.app.log(this.javaClass.simpleName,"Turn changed to $playerChoice")

        val turnComponent = ComponentHelper.retrieveTurnComponent()

        val playerAStatsComp = ComponentHelper.retrievePlayerStatisticsComponent(PLAYER_A)
        val playerBStatsComp = ComponentHelper.retrievePlayerStatisticsComponent(PLAYER_B)

        when {
            playerAStatsComp.isStillInTheGame && playerBStatsComp.isStillInTheGame -> {
                turnComponent.swapPlayerAndDirection()
                if(turnComponent.playerChoice === PLAYER_A){
                    flyingMachinePlayerA.startMovement(turnComponent.currentDirection)
                } else {
                    flyingMachinePlayerB.startMovement(turnComponent.currentDirection)
                }
            }
            playerAStatsComp.isStillInTheGame && playerBStatsComp.isOut -> {
                turnComponent.swapDirection()
                turnComponent.playerChoice = PLAYER_A
                flyingMachinePlayerA.startMovement(turnComponent.currentDirection)
            }
            playerAStatsComp.isOut && playerBStatsComp.isStillInTheGame -> {
                turnComponent.swapDirection()
                turnComponent.playerChoice = PLAYER_B
                flyingMachinePlayerB.startMovement(turnComponent.currentDirection)
            }
        }
    }
}