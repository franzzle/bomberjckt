package com.pimpedpixel.games

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Disposable

//Original canyonbomber field is 320x200
//Each brick was 8x8 pixels
class Brick : Disposable {
    private var color: Color? = null
    private var textureBrick: Texture? = null
    private var brickSprite: Sprite? = null
    private var destroyed = false
    private var textureCache: MutableMap<Color?, Texture?>? = null
    @JvmField
    var isOuterWall = false
    @JvmField
    var x = 0
    @JvmField
    var y = 0
    @JvmField
    var row = 0
    @JvmField
    var column = 0
    @JvmField
    var width = 0
    @JvmField
    var height = 0
    @JvmField
    var brickColorTextureFileName: String? = null

    fun initGraphics(assetManager: AssetManager) {

        brickSprite = Sprite(assetManager.get(this.brickColorTextureFileName, BrickTexture::class.java).texture)
        brickSprite!!.setPosition(x.toFloat(), y.toFloat())
    }

    fun isDestroyed(): Boolean {
        return destroyed
    }

    fun setDestroyed(destroyed: Boolean) {
        if (isOuterWall) {
            this.destroyed = false
        } else {
            this.destroyed = destroyed
        }
    }

    fun draw(batch: Batch?) {
        if (!isDestroyed()) {
            brickSprite!!.draw(batch)
        }
    }

    fun updatePosition(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    override fun dispose() {
        textureBrick!!.dispose()
    }

    val boundingRectangle: Rectangle
        get() = Rectangle(brickSprite!!.x, brickSprite!!.y, brickSprite!!.width, brickSprite!!.height)

    fun setColor(color: Color?) {
        this.color = color
    }

    fun getColor(): Color? {
        return color
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val brick = o as Brick
        return row == brick.row &&
                column == brick.column
    }

    override fun hashCode(): Int {
        return row + column
    }

    companion object {
        @JvmStatic
        fun forColAndRow(colIndex: Int, rowIndex: Int): Brick {
            val brick = Brick()
            brick.column = colIndex
            brick.row = rowIndex
            return brick
        }
    }
}
