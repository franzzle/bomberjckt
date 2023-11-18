package com.pimpedpixel.games.controls

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.pimpedpixel.games.common.ScreenManager.Companion.getHeightForRatio
import com.pimpedpixel.games.common.ScreenManager.Companion.getWidthForRatio

class PauseButton : BaseDrawable(), InputProcessor {
    private val spaceBetweenPauseRects: Float
    private val shapeRenderer: ShapeRenderer
    private val rectangle: Rectangle
    private val rectTouchableArea: Rectangle
    private val totalWidth: Float
    private var pauseDelegate: PauseDelegate? = null

    init {
        val widthPauseRect = getWidthForRatio(0.014f)
        val heightPauseRects = getHeightForRatio(0.0325f)
        shapeRenderer = ShapeRenderer()
        name = "pause"
        spaceBetweenPauseRects = widthPauseRect * 2
        totalWidth = 2 * widthPauseRect + spaceBetweenPauseRects
        val x = Gdx.graphics.width - totalWidth * 0.5f
        val y = Gdx.graphics.height - heightPauseRects * 0.5f
        rectangle = Rectangle(x, y, widthPauseRect, heightPauseRects)
        rectTouchableArea = Rectangle()
        rectTouchableArea.x = x - totalWidth
        rectTouchableArea.y = y - heightPauseRects
        rectTouchableArea.width = totalWidth
        rectTouchableArea.height = heightPauseRects
    }

    fun setPauseDelegate(pauseDelegate: PauseDelegate?) {
        this.pauseDelegate = pauseDelegate
    }

    fun draw(batch: Batch) {
        draw(batch, -1f, -1f, -1f, -1f)
    }

    override fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float) {
        batch.end()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.WHITE
        shapeRenderer.rect(rectangle.x - totalWidth, rectangle.y - rectangle.height, rectangle.width, rectangle.height)
        shapeRenderer.rect(
            rectangle.x - totalWidth + spaceBetweenPauseRects,
            rectangle.y - rectangle.height,
            rectangle.width,
            rectangle.height
        )
        shapeRenderer.end()
        batch.begin()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (rectTouchableArea.contains(screenX.toFloat(), (Gdx.app.graphics.height - screenY).toFloat())) {
            Gdx.app.log(this.javaClass.simpleName, "Touched no the node")
            if (pauseDelegate != null) {
                pauseDelegate!!.pause()
            }
            return true
        }
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}
