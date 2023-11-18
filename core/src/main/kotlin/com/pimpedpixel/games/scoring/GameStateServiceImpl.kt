package com.pimpedpixel.games.scoring

import com.badlogic.gdx.Gdx
import com.pimpedpixel.games.GameProperties
import com.pimpedpixel.games.GamePropertiesEnum.GAME_STATE

class GameStateServiceImpl(gameStateListener: GameStateListener) : GameStateService {
    override val gameState: GameState = GameState()
    private val gameStateListener: GameStateListener
    private val gameProperties: GameProperties
    private var elapsedTimeSinceGameFinished: Float

    init {
        this.gameStateListener = gameStateListener
        this.elapsedTimeSinceGameFinished = 0f
        this.gameProperties = GameProperties(GAME_STATE.prefixName)
    }

    override val numberOfMisses: String
        get() = "Missed ${gameState.numberOfMisses} times"

    override val score: String
        get() = ScoreFormatter.toScoreWithLeadingZeroes("Score", gameState.score)

    override fun addMiss() {
        this.gameState.numberOfMisses += 1
        if (gameState.numberOfMisses > gameProperties.getPropAsInt("number.allowed.misses")) {
            this.gameState.isFinished = true
            this.elapsedTimeSinceGameFinished = 0f
            this.gameStateListener.gameStateChanged(gameState)
        }
    }

    override fun addScore(scoreToAdd: Int) {
        this.gameState.score += scoreToAdd
    }

    override fun resetGameState() {
        this.gameState.isFinished = false
        this.gameState.isGameOverShownLongEnough = false
        this.elapsedTimeSinceGameFinished = 0f
        this.gameState.score = 0
        this.gameState.numberOfMisses = 0
    }

    fun update() {
        if (gameState.isFinished) {
            elapsedTimeSinceGameFinished += Gdx.graphics.deltaTime
            if (elapsedTimeSinceGameFinished > 4.0f) {
                gameState.isGameOverShownLongEnough = true
                gameStateListener.gameStateChanged(gameState)
            }
        }
    }
}
