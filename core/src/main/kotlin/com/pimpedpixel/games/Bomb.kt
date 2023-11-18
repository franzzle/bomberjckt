package com.pimpedpixel.games

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable
import com.pimpedpixel.games.common.ScreenManager
import com.pimpedpixel.games.scoring.PreferenceService

class Bomb(private val preferenceService: PreferenceService) : Actor(), Disposable {
    private val bombSprite: Sprite
    private val textureBomb: Texture
    private val gravity: Vector2
    private var visible = false
    private var hitSomething = false
    private var velocityY = 0f
    private var velocityX = 0f
    var numberOfBricksDestroyed = 0
        private set

    init {
        val instance = ScreenManager.getInstance()
        val scale = instance!!.scale
        textureBomb = getTexture(scale)
        bombSprite = Sprite(getTexture(scale))
        gravity = Vector2(0f, -1f * scale)
        reset()
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

    fun hasHitSomething(): Boolean {
        return hitSomething
    }

    fun setHitSomething(hitSomething: Boolean) {
        this.hitSomething = hitSomething
    }

    fun reset() {
        bombSprite.setPosition(Gdx.graphics.width * 0.5f, Gdx.graphics.height - bombSprite.height)
        visible = false
        velocityY = STARTING_Y_VELOCITY
        velocityX = STARTING_X_VELOCITY
        hitSomething = false
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (visible) {
            bombSprite.draw(batch)
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (visible) {
            hitSomething = numberOfBricksDestroyed > 0
            val deltaTime = Gdx.graphics.deltaTime
            velocityY += gravity.y
            bombSprite.setPosition(
                bombSprite.x + velocityX * deltaTime,
                bombSprite.y + velocityY * deltaTime)
        }
    }

    override fun dispose() {
        textureBomb.dispose()
    }

    val boundingRectangle: Rectangle
        get() = Rectangle(bombSprite.x, bombSprite.y, bombSprite.width, bombSprite.height)

    fun bombThrown(position: Vector2, direction: Vector2) {
        if (preferenceService.forKey("sounds.active", java.lang.Boolean.TRUE)) {
            Gdx.audio.newSound(Gdx.files.internal("sounds/bombfall.wav")).play()
        }
        numberOfBricksDestroyed = 0
        velocityY = STARTING_Y_VELOCITY
        velocityX = direction.x * STARTING_X_VELOCITY
        bombSprite.setPosition(position.x, position.y)
        Gdx.app.log(this.javaClass.getSimpleName(), "Bomb thrown")
        visible = true
    }

    fun addOneToBrickAnnihilationCount() {
        if (preferenceService.forKey("sounds.active", java.lang.Boolean.TRUE)) {
            Gdx.audio.newSound(Gdx.files.internal("sounds/bigexplode.wav")).play()
        }
        numberOfBricksDestroyed++
    }

    fun areEnoughBricksDestroyed(): Boolean {
        return numberOfBricksDestroyed > 3
    }

    fun bombCollidedWithCanyon() {
//        Gdx.app.log(this.getClass().getSimpleName(),"Bomb collided with outer wall or bottom of the canyon");
        visible = false
    }

    override fun toString(): String {
        return String.format("Bomb with gravity %s pulling on it", gravity.toString())
    }

    companion object {
        const val STARTING_Y_VELOCITY = -5f
        const val STARTING_X_VELOCITY = 35f
    }
}
