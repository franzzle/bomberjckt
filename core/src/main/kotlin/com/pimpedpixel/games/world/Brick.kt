package com.pimpedpixel.games.world

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Disposable

//Original canyonbomber field is 320x200, the web version is designed for 640x480
//Each brick was 8x8 pixels
class Brick(val world: World) : Disposable {
    private var color: Color? = null
    private var textureBrick: Texture? = null
    private var brickSprite: Sprite? = null
    private var destroyed = false
    private var destroyedFromTheStart = false

    @JvmField
    var score: Int = 0
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
        updatePosition(row * width, column * height)
        brickSprite = Sprite(assetManager.get(this.brickColorTextureFileName, BrickTexture::class.java).texture)
        brickSprite!!.setPosition(x.toFloat(), y.toFloat())

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody // Make it a static body
        bodyDef.position.set(
            x.toFloat() + brickSprite!!.width / 2,
            y.toFloat() + brickSprite!!.height / 2
        )

        val shape = PolygonShape()
        shape.setAsBox(
            brickSprite!!.width / 2,
            brickSprite!!.height / 2)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.friction = 0f // Set friction to 0
        fixtureDef.restitution = 0f // Set restitution to 0
        // Create the Box2D body and attach the fixture
        val body: Body = this.world.createBody(bodyDef)
        body.createFixture(fixtureDef)
        body.userData = this
        // Dispose the shape
        shape.dispose()
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

    fun setDestroyedFromTheStart(destroyedFromTheStart: Boolean) {
        this.destroyedFromTheStart = destroyedFromTheStart
        this.destroyed = destroyedFromTheStart
    }

    fun isDestroyedFromTheStart(): Boolean {
        return destroyedFromTheStart
    }

    fun draw(batch: Batch?) {
        if (!isDestroyed()) {
            brickSprite!!.draw(batch)
        }
    }

    private fun updatePosition(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    override fun dispose() {
        textureBrick!!.dispose()
    }

    fun setColor(color: Color?) {
        this.color = color
    }

    fun getColor(): Color? {
        return color
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val brick = other as Brick
        return row == brick.row &&
                column == brick.column
    }

    override fun hashCode(): Int {
        return row + column
    }
}
