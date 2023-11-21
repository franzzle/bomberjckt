package com.pimpedpixel.games

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable
import com.pimpedpixel.games.common.ScreenManager
import com.pimpedpixel.games.scoring.PreferenceService

class Bomb(world: World, initalPos: Vector2) : Actor(), Disposable {
    private val bombSprite: Sprite
    var bombBody: Body
    init {
        bombSprite = Sprite(getTexture(1))

        val bombBodyDef = BodyDef()
        bombBodyDef.position.x = initalPos.x
        bombBodyDef.position.y = initalPos.y
        bombBodyDef.type = BodyDef.BodyType.DynamicBody
        bombBody = world!!.createBody(bombBodyDef)
        bombBody.userData = "bomb"
        bombBody.isActive = false

        // Create a skull shape (replace with your own shape)
        val bombCircularShape = CircleShape()
        bombCircularShape.radius = BOMB_SIZE_PER_METER / 2
        val fixtureDef = FixtureDef()
        fixtureDef.shape = bombCircularShape

        fixtureDef.density = 1.0f
        fixtureDef.friction = 0.95f
        fixtureDef.restitution = 0.05f // Adjust for bounciness

        bombBody.createFixture(fixtureDef)
    }

    private fun getTexture(scale: Int): Texture {
        val textureBomb: Texture
        val brickPixMap = Pixmap(4 * scale, 4 * scale, Pixmap.Format.RGBA8888)
        brickPixMap.setColor(Color.WHITE)
        brickPixMap.fill()
        textureBomb = Texture(brickPixMap)
        brickPixMap.dispose()
        return textureBomb
    }

    override fun act(delta: Float) {
        super.act(delta)
        Gdx.app.log("", "Bomb position: ${bombBody.position.x} ${bombBody.position.y}")
        Gdx.app.log("", "Bomb active: ${bombBody.isActive}")

        val verticalPosition = bombBody.position.y * PIXELS_PER_METER
        bombSprite.setPosition(bombBody.position.x * PIXELS_PER_METER, verticalPosition)
        if(verticalPosition < 0){
            bombBody?.setLinearVelocity(Vector2.Zero)
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if(isVisible){
            bombSprite.draw(batch)
        }
    }

    override fun dispose() {
        bombSprite.texture.dispose()
    }

    val boundingRectangle: Rectangle
        get() = Rectangle(bombSprite.x, bombSprite.y, bombSprite.width, bombSprite.height)



    override fun toString(): String {
        return "Bomb";
    }

    fun isOffScreen(): Boolean {
        return bombSprite.y + bombSprite.height < 0
    }
}
