package com.pimpedpixel.games

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable

class Blimp : Actor(), Disposable {
    private val blimpTexture: Texture
    private val blimpSprite: Sprite
    @JvmField
    val direction: Vector2

    init {
        val format = "blimp@1x.png"
        blimpTexture = Texture(Gdx.files.internal(format))
        blimpSprite = Sprite(blimpTexture)
        width = blimpSprite.width
        height = blimpSprite.height
        direction = Vector2(1f, 0f)
    }

    @Override
    override fun draw(batch: Batch?, parentAlpha: Float) {
        blimpSprite.draw(batch)
    }
    override fun act(delta: Float) {
        super.act(delta)
        blimpSprite.setPosition(x, y)
    }

    override fun dispose() {
        blimpTexture.dispose()
    }

    fun flip() {
        direction[-1.0f * direction.x] = direction.y
        blimpSprite.flip(true, false)
    }
}
