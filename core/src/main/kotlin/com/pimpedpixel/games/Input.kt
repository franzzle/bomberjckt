package com.pimpedpixel.games

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.pimpedpixel.games.scoring.GameStateService
import com.pimpedpixel.games.scoring.GameStateServiceImpl

class Input(private val gameStateService: GameStateServiceImpl) : InputProcessor {
    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.SPACE) {
            if (gameStateService.gameState.isStarted) {
                Gdx.app.log("", "Drop bomb")
                dropBombIfAllowedTo()
            } else {
                startGame()
            }
        }
        return false
    }

    private fun startGame() {
        Gdx.app.log("", "Start game")

        if (!gameStateService.gameState.isStarted) {
            gameStateService.resetGameState()
        }
        gameStateService.gameState.isStarted = true
    }

    private fun dropBombIfAllowedTo() {
        if (gameStateService!!.gameState.isPlayerAllowedToDropAnotherBomb) {
            gameStateService!!.activateBomb()
        }
    }

    private fun throwBomb() {

    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
//        if (!game.paused) {
//            dropBombIfAllowedTo()
//            return true
//        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
//        if (!game.paused) {
//            startGame()
//            return true
//        }
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
