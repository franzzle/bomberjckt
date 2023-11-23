package com.pimpedpixel.games.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class BlinkingSimpleMessage(private val message: String) : Actor() {
    private val messageFont: BitmapFont
    private val duration: Float
    private val startSequenceTextWidth: Float
    private var elapsed = 0f
    private var visible = false

    // TODO Assetmanager to load font
    init {
        val fontFile = Gdx.files.internal("graphicpixel@1x.fnt")
        val pngFile = Gdx.files.internal("graphicpixel@1x.png")
        messageFont = BitmapFont(fontFile, pngFile, false)
        messageFont.region.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        val glyphLayout = GlyphLayout(messageFont, message)
        startSequenceTextWidth = glyphLayout.width
        duration = 0.7f
    }

    override fun act(delta: Float) {
        if (elapsed < duration) {
            // Increase the elapsed time by the delta provided.
            elapsed += delta
        } else {
            elapsed = 0f
            visible = !visible
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (visible) {
            val yThreeQuartersAboveBottom = Gdx.graphics.height * 0.75f
            messageFont.draw(
                batch,
                message,
                Gdx.graphics.width * 0.5f - startSequenceTextWidth * 0.5f,
                yThreeQuartersAboveBottom
            )
        }
    }
}
