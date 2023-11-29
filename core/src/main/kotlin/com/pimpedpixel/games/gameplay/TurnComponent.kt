package com.pimpedpixel.games.gameplay

import com.badlogic.ashley.core.Component
import com.pimpedpixel.games.world.FlyingMachineDirection

class TurnComponent : Component {
    var playerChoice: PlayerChoice = PlayerChoice.UNDEFINED
    var currentDirection: FlyingMachineDirection = FlyingMachineDirection.UNDEFINED

    fun swapPlayerAndDirection() {
        swapDirection()
        when(playerChoice) {
            PlayerChoice.PLAYER_A -> playerChoice = PlayerChoice.PLAYER_B
            PlayerChoice.PLAYER_B -> playerChoice = PlayerChoice.PLAYER_A
            PlayerChoice.UNDEFINED -> playerChoice = PlayerChoice.PLAYER_A
        }
    }

    fun swapDirection() {
        when (currentDirection) {
            FlyingMachineDirection.LEFT_TO_RIGHT -> currentDirection = FlyingMachineDirection.RIGHT_TO_LEFT
            FlyingMachineDirection.RIGHT_TO_LEFT -> currentDirection = FlyingMachineDirection.LEFT_TO_RIGHT
            FlyingMachineDirection.UNDEFINED -> currentDirection = FlyingMachineDirection.LEFT_TO_RIGHT
        }
    }

    fun reset() {
        playerChoice = PlayerChoice.UNDEFINED
        currentDirection = FlyingMachineDirection.UNDEFINED
    }
}