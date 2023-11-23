package com.pimpedpixel.games.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor

class SimpleMessage(private val alignment: Alignment) : Actor() {
    private var currentMessage: String = ""
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private val messageFont: BitmapFont

    init {
        messageFont = BitmapFont(
            Gdx.files.internal("graphicpixel@1x.fnt"),
            Gdx.files.internal("graphicpixel@1x.png"), false)
        messageFont.region.texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
        messageFont.setColor(Color.RED.r, Color.RED.g, Color.RED.b, 1f)
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
                y = Gdx.graphics.height.toFloat() - 40f
            }
            Alignment.RIGHT_TOP -> {
                x = Gdx.graphics.width.toFloat() - textWidth - 20f
                y = Gdx.graphics.height.toFloat() - 40f
            }
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (isVisible) {
            messageFont.draw(batch, currentMessage, x, y)
        }
    }

    fun getBounds(): Rectangle {
        return Rectangle(x, y, textWidth, textHeight)
    }

}
