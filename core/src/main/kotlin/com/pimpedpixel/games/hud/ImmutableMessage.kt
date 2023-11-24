package com.pimpedpixel.games.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.pimpedpixel.games.AssetManagerHolder
import com.pimpedpixel.games.RETRO_FONT

class ImmutableMessage(private var message: String): Actor() {
    private val messageFont: BitmapFont
    private val textWidth: Float
    private val textHeight: Float
    var active = false

    init{
        messageFont = AssetManagerHolder.assetManager.get(RETRO_FONT, BitmapFont::class.java)
        val glyphLayout = GlyphLayout(messageFont, message)
        textWidth = glyphLayout.width
        textHeight = glyphLayout.height
        x = Gdx.graphics.width.toFloat() * 0.5f - textWidth * 0.5f
        y = Gdx.graphics.height.toFloat() * 0.5f - textHeight * 0.5f
    }

    override fun act(delta: Float) {
        super.act(delta)
        isVisible = active
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (isVisible) {
            messageFont.draw(batch, message, x, y)
        }
    }
}