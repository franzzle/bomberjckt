package com.pimpedpixel.games

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.pimpedpixel.games.gameplay.ComponentHelper
import com.pimpedpixel.games.gameplay.GamePhaseState
import com.pimpedpixel.games.gameplay.PlayerChoice
import com.pimpedpixel.games.hud.Alignment
import com.pimpedpixel.games.hud.BlinkingMessage
import com.pimpedpixel.games.hud.FrameRateActor
import com.pimpedpixel.games.hud.UpdatableMessage
import com.pimpedpixel.games.scoring.ScoreFormatter.intToStringWithLeadingZeroes

class HudStage(): Stage() {
    private val playerAMissesMessage: UpdatableMessage
    private val playerAScoreMessage: UpdatableMessage
    private val playerBMissesMessage: UpdatableMessage
    private val playerBScoreMessage: UpdatableMessage
    private val startGameBlinkingMessage: BlinkingMessage
    private val gameOverBlinkingMessage: BlinkingMessage
    private val whichPlayerWonMessage: BlinkingMessage
    init {
        playerAMissesMessage = UpdatableMessage(alignment = Alignment.LEFT_BOTTOM, color = Color.WHITE)
        playerAScoreMessage = UpdatableMessage(alignment = Alignment.LEFT_TOP, color = Color.WHITE)
        playerAScoreMessage.setMessage(intToStringWithLeadingZeroes(0))

        playerBMissesMessage = UpdatableMessage(alignment = Alignment.RIGHT_BOTTOM, color = Color.BLACK)
        playerBScoreMessage = UpdatableMessage(alignment = Alignment.RIGHT_TOP, color = Color.BLACK)
        playerBScoreMessage.setMessage(intToStringWithLeadingZeroes(0))

        whichPlayerWonMessage = BlinkingMessage(gamePhaseStateToBeActive = GamePhaseState.WHO_WON)
        startGameBlinkingMessage = BlinkingMessage("Press space to start", GamePhaseState.ATTRACT_SCREEN)
        gameOverBlinkingMessage = BlinkingMessage("Game Over", GamePhaseState.GAME_OVER)

        addActor(playerAMissesMessage)
        addActor(playerAScoreMessage)
        addActor(playerBMissesMessage)
        addActor(playerBScoreMessage)
        addActor(startGameBlinkingMessage)
        addActor(gameOverBlinkingMessage)
        addActor(whichPlayerWonMessage)

        addActor(FrameRateActor())
    }

    override fun act() {
        super.act()

        val playerAStats = ComponentHelper.retrievePlayerStatisticsComponent(PlayerChoice.PLAYER_A)
        playerAMissesMessage.setMessage("X".repeat(playerAStats.misses))
        playerAScoreMessage.setMessage(intToStringWithLeadingZeroes(playerAStats.score))

        val playerBStats = ComponentHelper.retrievePlayerStatisticsComponent(PlayerChoice.PLAYER_B)
        playerBMissesMessage.setMessage("X".repeat(playerBStats.misses))
        playerBScoreMessage.setMessage(intToStringWithLeadingZeroes(playerBStats.score))

        val gamePhaseStateComponent = ComponentHelper.retrieveGamePhaseStateComponent()

        startGameBlinkingMessage.updateActive(gamePhaseStateComponent)
        gameOverBlinkingMessage.updateActive(gamePhaseStateComponent)

        val playerAStatsComp = ComponentHelper.retrievePlayerStatisticsComponent(PlayerChoice.PLAYER_A)
        val playerBStatsComp = ComponentHelper.retrievePlayerStatisticsComponent(PlayerChoice.PLAYER_B)


        when {
            playerAStatsComp.score > playerBStatsComp.score -> {
                whichPlayerWonMessage.setMessage("White won!")
                whichPlayerWonMessage.color = Color.WHITE
            }
            playerAStatsComp.score < playerBStatsComp.score -> {
                whichPlayerWonMessage.setMessage("Black won!")
                whichPlayerWonMessage.color = Color.BLACK
            }
            else -> whichPlayerWonMessage.setMessage("It's a tie!")
        }
        whichPlayerWonMessage.updateActive(gamePhaseStateComponent)

    }
}