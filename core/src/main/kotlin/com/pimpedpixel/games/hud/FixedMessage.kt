package com.pimpedpixel.games.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor

class FixedMessage(var message: String): Actor() {
    private val messageFont: BitmapFont
    private val textWidth: Float
    private val textHeight: Float

    init{
        messageFont = BitmapFont(
            Gdx.files.internal("graphicpixel@1x.fnt"),
            Gdx.files.internal("graphicpixel@1x.png"), false)
        messageFont.region.texture.setFilter(
            com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest,
            com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest)

        val glyphLayout = GlyphLayout(messageFont, message)
        textWidth = glyphLayout.width
        textHeight = glyphLayout.height
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (isVisible) {
            messageFont.draw(batch, message, x, y)
        }
    }
}