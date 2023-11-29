package com.pimpedpixel.games.hud

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.TimeUtils
import com.pimpedpixel.games.AssetManagerHolder
import com.pimpedpixel.games.RETRO_FONT

class FrameRateActor : Actor() {
    private var currentMessage = "60 fps"
    private var lastTimeCounted: Long
    private var sinceChange = 0f
    private var frameRateInt: Int
    private val messageFont: BitmapFont

    init {
        lastTimeCounted = TimeUtils.millis()
        frameRateInt = 60
        messageFont = AssetManagerHolder.assetManager.get(RETRO_FONT, BitmapFont::class.java)

        val glyphLayout = com.badlogic.gdx.graphics.g2d.GlyphLayout(messageFont, currentMessage)
        x = Gdx.graphics.width.toFloat() * 0.5f - glyphLayout.width * 0.5f
        y = 40f
    }

    override fun act(delta: Float) {
        val currentTime = TimeUtils.millis()
        val deltaMillis = currentTime - lastTimeCounted
        lastTimeCounted = currentTime
        sinceChange += deltaMillis.toFloat()

        if (sinceChange >= 1000) {
            sinceChange = 0f
            frameRateInt = (1000f / deltaMillis).toInt()
        }
        currentMessage = "$frameRateInt fps"
        val glyphLayout = GlyphLayout(messageFont, currentMessage)
        x = Gdx.graphics.width.toFloat() * 0.5f - glyphLayout.width * 0.5f
        y = 40f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        messageFont.draw(batch, currentMessage, x, y)
    }
}
