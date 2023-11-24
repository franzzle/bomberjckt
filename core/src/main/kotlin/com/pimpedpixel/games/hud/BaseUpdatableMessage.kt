package com.pimpedpixel.games.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable
import com.pimpedpixel.games.AssetManagerHolder
import com.pimpedpixel.games.RETRO_FONT

abstract class BaseUpdatableMessage(
    private var alignment: Alignment,
    assignedColor: Color = Color.WHITE) : Actor(), Disposable {
    private var currentMessage = ""
    private var textWidth = 0f
    private var textHeight = 0f

    private val messageFont: BitmapFont

    init {
        val bitmapFont = AssetManagerHolder.assetManager.get(RETRO_FONT, BitmapFont::class.java)
        this.messageFont = BitmapFont(bitmapFont.data, bitmapFont.regions, false)
        this.color = assignedColor
    }

    fun setMessage(newMessage: String) {
        currentMessage = newMessage
        recalculateTextSize()
    }
    private fun recalculateTextSize() {
        val glyphLayout = GlyphLayout(messageFont, currentMessage)
        textWidth = glyphLayout.width
        textHeight = glyphLayout.height

        when (alignment) {
            Alignment.LEFT_BOTTOM -> {
                x = 20f
                y = 40f
            }
            Alignment.RIGHT_BOTTOM -> {
                x = Gdx.graphics.width.toFloat() - textWidth - 20f
                y = 40f
            }
            Alignment.LEFT_TOP -> {
                x = 20f
                y = Gdx.graphics.height.toFloat() - 20f
            }
            Alignment.RIGHT_TOP -> {
                x = Gdx.graphics.width.toFloat() - textWidth - 20f
                y = Gdx.graphics.height.toFloat() - 20f
            }

            Alignment.MID_BOTTOM -> {
                x = Gdx.graphics.width.toFloat() * 0.5f - textWidth * 0.5f
                y = 40f
            }
            Alignment.MID_TOP -> {
                x = Gdx.graphics.width.toFloat() * 0.5f - textWidth * 0.5f
                y = Gdx.graphics.height.toFloat() - 20f
            }
            Alignment.OFF_CENTER -> {
                x = Gdx.graphics.width.toFloat() * 0.5f - textWidth * 0.5f
                y = Gdx.graphics.height.toFloat() * 0.7f - textHeight * 0.5f
            }
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (isVisible) {
            messageFont.setColor(color.r, color.g, color.b, 1f)
            messageFont.draw(batch, currentMessage, x, y)
        }
    }

    override fun dispose() {
        messageFont.dispose()
    }
}

