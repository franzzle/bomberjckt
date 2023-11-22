package com.pimpedpixel.games.world

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.physics.box2d.World

class BrickBuilder(private val world: World) {
    private var color: Color? = null
    private var widthOfOneBrick = 0
    private var heightOfOneBrick = 0
    private var x = 0
    private var y = 0
    private var isOuterWall = false

    fun color(color: Color): BrickBuilder {
        this.color = color
        return this
    }

    fun width(width: Int): BrickBuilder {
        this.widthOfOneBrick = width
        return this
    }

    fun height(height: Int): BrickBuilder {
        this.heightOfOneBrick = height
        return this
    }

    fun position(x: Int, y: Int): BrickBuilder {
        this.x = x
        this.y = y
        return this
    }

    fun isOuterWall(isOuterWall: Boolean): BrickBuilder {
        this.isOuterWall = isOuterWall
        return this
    }

    fun build(): Brick {
        val brick = Brick(world)
        brick.width = widthOfOneBrick
        brick.height = heightOfOneBrick
        brick.isOuterWall = isOuterWall
        brick.setColor(color)
        brick.updatePosition(x, y)
        return brick
    }
}

