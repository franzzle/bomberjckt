package com.pimpedpixel.games

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2

class Input(private val game: BomberJcktGame) : InputProcessor {
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.SPACE) {
            if (game.gameStateService!!.gameState.isStarted) {
                dropBombIfAllowedTo()
            } else {
                startGame()
            }
        }
        return false
    }

    private fun startGame() {
        if (!game.gameStateService!!.gameState.isStarted) {
            game.gameStateService!!.resetGameState()
        }
        game.gameStateService!!.gameState.isStarted = true
    }

    private fun dropBombIfAllowedTo() {
        if (game.gameStateService!!.gameState.isPlayerAllowedToDropAnotherBomb) {
            throwBomb()
            game.gameStateService!!.gameState.isPlayerAllowedToDropAnotherBomb = false
        }
    }

    private fun throwBomb() {
        val bombSpawnPosition = Vector2(
            game.blimp!!.x + game.blimp!!.width * 0.5f,
            game.blimp!!.y + game.blimp!!.height * 0.5f
        )
        game.bomb!!.bombThrown(bombSpawnPosition, game.blimp!!.direction)
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (!game.paused) {
            dropBombIfAllowedTo()
            return true
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (!game.paused) {
            startGame()
            return true
        }
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
