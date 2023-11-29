package com.pimpedpixel.games.world

import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.utils.Disposable
import com.pimpedpixel.games.gameplay.PlayerChoice
import com.pimpedpixel.games.gameplay.PlayerChoice.PLAYER_B
import com.pimpedpixel.games.world.FlyingMachineDirection.LEFT_TO_RIGHT
import com.pimpedpixel.games.world.FlyingMachineDirection.RIGHT_TO_LEFT

class FlyingMachine(val flyingMachineDirection: FlyingMachineDirection,
                    private val changePlayersTurnSignal: Signal<PlayerChoice>,
                    private val playerChoice: PlayerChoice
) : Actor(), Disposable {
    private val flyingMachineSprite: Sprite

    init {
        flyingMachineSprite = Sprite(Texture(Gdx.files.internal("plane.png")))

        width = flyingMachineSprite.width
        height = flyingMachineSprite.height
    }

    fun startMovement(newFlyingMachineDirection: FlyingMachineDirection) {
        val flyingMachineWidth = this.width
        val screenHeight = Gdx.graphics.height.toFloat()

        // Randomly generate the vertical position within the specified range
        val startY = screenHeight * 0.7f
        val endY = screenHeight * 0.8f
        var randomY = startY + MathUtils.random() * (endY - startY)

        var startX = -flyingMachineWidth
        var endX = Gdx.graphics.width.toFloat()

        if (newFlyingMachineDirection == RIGHT_TO_LEFT) {
            startX = Gdx.graphics.width.toFloat()
            endX = -flyingMachineWidth
        }

        this.setPosition(startX, randomY)

        val flipIfComingFromRightToLeft: RunnableAction = Actions.run {
            if(flyingMachineDirection === RIGHT_TO_LEFT){
                flyingMachineSprite.flip(!flyingMachineSprite.isFlipX, false)
            }
            if(newFlyingMachineDirection === LEFT_TO_RIGHT){
                flyingMachineSprite.flip(flyingMachineSprite.isFlipX, false)
            }
        }
        val changeColor: RunnableAction = Actions.run {
            when(playerChoice){
                PLAYER_B -> flyingMachineSprite.color = Color.BLACK
                else -> flyingMachineSprite.color = Color.WHITE
            }
        }

        val moveToAction: MoveToAction = Actions.moveTo(endX, randomY, 7f)


        // When the blimp reaches the right or left side, reset its position accordingly
        val reset: RunnableAction = Actions.run {
            randomY = startY + MathUtils.random() * (endY - startY)
            Gdx.app.log("", "Moving to $randomY")

            if (newFlyingMachineDirection == RIGHT_TO_LEFT) {
                this.setPosition(Gdx.graphics.width.toFloat(), randomY)
            } else {
                this.setPosition(-flyingMachineWidth, randomY)
            }
        }
        val signalOtherPlayersTurn = Actions.run { this.changePlayersTurnSignal.dispatch(PLAYER_B) }
        val makeVisible: RunnableAction = Actions.run { this.isVisible = true }
        val makeInvisible: RunnableAction = Actions.run { this.isVisible = false }
        this.addAction(Actions.sequence(
            flipIfComingFromRightToLeft,
            changeColor,
            makeVisible,
            moveToAction,
            reset,
            signalOtherPlayersTurn,
            makeInvisible)
        )
    }

    @Override
    override fun draw(batch: Batch?, parentAlpha: Float) {
        flyingMachineSprite.draw(batch)
    }
    override fun act(delta: Float) {
        super.act(delta)
        flyingMachineSprite.setPosition(x, y)
    }

    override fun dispose() {
        flyingMachineSprite.texture.dispose()
    }
}
