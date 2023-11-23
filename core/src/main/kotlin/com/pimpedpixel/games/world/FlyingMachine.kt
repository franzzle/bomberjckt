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

class FlyingMachine(val flyingMachineDirection: FlyingMachineDirection, private val signal: Signal<PlayerChoice>
) : Actor(), Disposable {
    private val flyingMachineTexture: Texture
    private val flyingMachineSprite: Sprite

    init {
        flyingMachineTexture = Texture(Gdx.files.internal("plane.png"))
        flyingMachineSprite = Sprite(flyingMachineTexture)
        if(flyingMachineDirection === FlyingMachineDirection.RIGHT_TO_LEFT){
            flyingMachineSprite.flip(true, false)
            flyingMachineSprite.setColor(Color.BLACK)
        }
        width = flyingMachineSprite.width
        height = flyingMachineSprite.height
    }

    fun startMovement() {
        val blimpWidth = this.width
        val screenHeight = Gdx.graphics.height.toFloat()

        // Randomly generate the vertical position within the specified range
        val startY = screenHeight * 0.7f
        val endY = screenHeight * 0.8f
        var randomY = startY + MathUtils.random() * (endY - startY)

        var startX = -blimpWidth
        var endX = Gdx.graphics.width.toFloat()

        if (flyingMachineDirection == FlyingMachineDirection.RIGHT_TO_LEFT) {
            startX = Gdx.graphics.width.toFloat()
            endX = -blimpWidth
        }

        this.setPosition(startX, randomY)

        val moveToAction: MoveToAction = Actions.moveTo(endX, randomY, 7f)

        // When the blimp reaches the right or left side, reset its position accordingly
        val reset: RunnableAction = Actions.run {
            randomY = startY + MathUtils.random() * (endY - startY)
            Gdx.app.log("", "Moving to $randomY")

            if (flyingMachineDirection == FlyingMachineDirection.RIGHT_TO_LEFT) {
                this.setPosition(Gdx.graphics.width.toFloat(), randomY)
            } else {
                this.setPosition(-blimpWidth, randomY)
            }
        }
        val signalOtherPlayersTurn: RunnableAction = Actions.run {
            this.signal.dispatch(PlayerChoice.PLAYER_B)
        }

        this.addAction(Actions.sequence(moveToAction, reset, signalOtherPlayersTurn))
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
        flyingMachineTexture.dispose()
    }
}
