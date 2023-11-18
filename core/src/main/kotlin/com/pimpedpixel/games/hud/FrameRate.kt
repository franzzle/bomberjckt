package com.pimpedpixel.games.hud

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.TimeUtils
import com.pimpedpixel.games.common.ScreenManager.Companion.getInstance

class FrameRate : ApplicationAdapter(), Disposable {
    private val messageFont: BitmapFont
    private val batch: SpriteBatch
    private val precalculatedWidthFontMessage: Float
    private val precalculatedHeightFontMessage: Float
    private var cam: OrthographicCamera
    private var lastTimeCounted: Long
    private var sinceChange = 0f
    private var frameRate: Float

    init {
        lastTimeCounted = TimeUtils.millis()
        frameRate = Gdx.graphics.framesPerSecond.toFloat()
        val fontNameWithoutExtension = "graphicpixel@" + getInstance()!!.scale + "x"
        val handleFntFile = Gdx.files.internal("$fontNameWithoutExtension.fnt")
        val handlePngFile = Gdx.files.internal("$fontNameWithoutExtension.png")
        val FLIPPED = false
        messageFont = BitmapFont(handleFntFile, handlePngFile, FLIPPED)
        val glyphLayout = GlyphLayout(messageFont, "00 FPS")
        precalculatedWidthFontMessage = glyphLayout.width
        precalculatedHeightFontMessage = glyphLayout.height
        batch = SpriteBatch()
        cam = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }

    override fun resize(screenWidth: Int, screenHeight: Int) {
        cam = OrthographicCamera(screenWidth.toFloat(), screenHeight.toFloat())
        cam.translate(screenWidth / 2f, screenHeight / 2f)
        cam.update()
        batch.projectionMatrix = cam.combined
    }

    fun update() {
        val delta = TimeUtils.timeSinceMillis(lastTimeCounted)
        lastTimeCounted = TimeUtils.millis()
        sinceChange += delta.toFloat()
        if (sinceChange >= 1000) {
            sinceChange = 0f
            frameRate = Gdx.graphics.framesPerSecond.toFloat()
        }
    }

    override fun render() {
        batch.begin()
        messageFont.draw(
            batch,
            frameRate.toInt().toString() + " fps",
            Gdx.graphics.width - precalculatedWidthFontMessage,
            precalculatedHeightFontMessage
        )
        batch.end()
    }

    override fun dispose() {
        messageFont.dispose()
        batch.dispose()
    }
}
