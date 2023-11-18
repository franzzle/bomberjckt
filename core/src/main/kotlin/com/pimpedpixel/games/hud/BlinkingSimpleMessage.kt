package com.pimpedpixel.games.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.pimpedpixel.games.common.ScreenManager.Companion.getInstance

class BlinkingSimpleMessage(private val messageProvider: MessageProvider) {
    private val messageFont: BitmapFont
    private val duration: Float
    private val startSequenceTextWidth: Float
    private var elapsed = 0f
    private var visible = false

    init {
        val fontNameWithoutExtension = "graphicpixel@" + getInstance()!!.scale + "x"
        val fontFile = Gdx.files.internal("$fontNameWithoutExtension.fnt")
        val pngFile = Gdx.files.internal("$fontNameWithoutExtension.png")
        messageFont = BitmapFont(fontFile, pngFile, false)
        messageFont.region.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        val glyphLayout = GlyphLayout(messageFont, messageProvider.message())
        startSequenceTextWidth = glyphLayout.width
        duration = 0.7f
    }

    fun update() {
        if (elapsed < duration) {
            // Increase the elapsed time by the delta provided.
            elapsed += Gdx.graphics.deltaTime
        } else {
            elapsed = 0f
            visible = !visible
        }
    }

    fun draw(batch: Batch?) {
        if (visible) {
            val yThreeQuartersAboveBottom = Gdx.graphics.height * 0.75f
            messageFont.draw(
                batch,
                messageProvider.message(),
                positionCenteredWithSentenceSizeAccountedFor,
                yThreeQuartersAboveBottom
            )
        }
    }

    val positionCenteredWithSentenceSizeAccountedFor: Float
        get() = Gdx.graphics.width * 0.5f - startSequenceTextWidth * 0.5f
}
