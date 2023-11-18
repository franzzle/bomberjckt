package com.pimpedpixel.games.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.pimpedpixel.games.common.ScreenManager.Companion.getInstance

class SimpleMessage(private val messageProvider: MessageProvider) {
    private val previousMessage: String
    private val messageFont: BitmapFont
    var startSequenceTextWidth = 0f
    var startSequenceTextHeight = 0f
    var isVisible: Boolean
    var position: Vector2? = null

    init {
        val fontNameWithoutExtension = "graphicpixel@" + getInstance()!!.scale + "x"
        val fontFile = Gdx.files.internal("$fontNameWithoutExtension.fnt")
        val pngFile = Gdx.files.internal("$fontNameWithoutExtension.png")
        messageFont = BitmapFont(fontFile, pngFile, false)
        messageFont.region.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        previousMessage = messageProvider.message()
        recalculateFontSize()
        isVisible = true
    }

    fun update() {
        if (previousMessage != messageProvider.message()) {
            recalculateFontSize()
        }
    }

    private fun recalculateFontSize() {
        val glyphLayout = GlyphLayout(messageFont, messageProvider.message())
        startSequenceTextWidth = glyphLayout.width
        startSequenceTextHeight = glyphLayout.height
    }

    val message: String
        get() = messageProvider.message()

    fun draw(batch: Batch?) {
        if (isVisible) {
            messageFont.draw(batch, messageProvider.message(), position!!.x, position!!.y)
        }
    }

    val size: Rectangle
        get() = Rectangle(0f, 0f, startSequenceTextWidth, startSequenceTextHeight)
}
