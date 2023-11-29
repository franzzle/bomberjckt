package com.pimpedpixel.games.world

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color.BLACK
import com.badlogic.gdx.graphics.Color.WHITE
import com.badlogic.gdx.math.Vector2
import com.pimpedpixel.games.gameplay.ComponentHelper
import com.pimpedpixel.games.gameplay.PlayerChoice
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_A
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_B
import com.pimpedpixel.games.gameplay.TurnComponent
import com.pimpedpixel.games.world.FlyingMachineDirection.LEFT_TO_RIGHT

private const val HOR_VELOCITY = 20f

class BombThrowingSystem(
    private val bombPool: BombPool,
    private val flyingMachinePlayerA: FlyingMachine?,
    private val flyingMachinePlayerB: FlyingMachine?,
    private val activeBombs: MutableList<Bomb>
) : IteratingSystem(Family.all(BombThrow::class.java).get()) {

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        engine.removeEntity(entity)

        val retrieveTurnComponent = ComponentHelper.retrieveTurnComponent()
        if (ComponentHelper.retrievePlayerStatisticsComponent(PLAYER_A).isStillInTheGame && retrieveTurnComponent.playerChoice == PLAYER_A) {
            tryAddBomb(PLAYER_A, turnComponent = retrieveTurnComponent)
        }
        if (ComponentHelper.retrievePlayerStatisticsComponent(PLAYER_B).isStillInTheGame && retrieveTurnComponent.playerChoice == PLAYER_B) {
            tryAddBomb(PLAYER_B, turnComponent = retrieveTurnComponent)
        }
    }

    private fun tryAddBomb(playerChoice: PlayerChoice, turnComponent: TurnComponent) {
        val bomb = bombPool?.obtainBomb()
        if (bomb != null) {
            bomb.bombBody.isActive = true
            val sign = if (turnComponent.currentDirection == LEFT_TO_RIGHT) 1 else -1
            bomb.bombBody.linearVelocity = Vector2(sign * HOR_VELOCITY, bomb.bombBody.linearVelocity.y)
            (bomb.bombBody.userData as BombUserData).thrownBy = playerChoice
            if (playerChoice == PLAYER_A) {
                bomb.setInitialPosition(flyingMachinePlayerA!!.x, flyingMachinePlayerA!!.y)
                bomb.color = WHITE
            } else {
                bomb.setInitialPosition(flyingMachinePlayerB!!.x, flyingMachinePlayerB!!.y)
                bomb.color = BLACK
            }
        }
        bomb?.let { activeBombs.add(it) }
    }
}