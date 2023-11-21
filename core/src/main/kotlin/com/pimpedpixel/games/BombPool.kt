package com.pimpedpixel.games

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.Vector2
import java.util.LinkedList

class BombPool(private val world: World) {
    private val bombList: LinkedList<Bomb> = LinkedList()

    init {
        // Initialize the bomb pool with 20 bombs
        for (i in 0 until 20) {
            val bomb = createBomb()
            bombList.add(bomb)
        }
    }

    private fun createBomb(): Bomb {
        val x = -100f // Set the initial position off-screen
        val y = -100f
        return Bomb(world, Vector2(x, y))
    }

    fun obtainBomb(): Bomb? {
        if (bombList.isEmpty()) {
            // If the pool is empty, create a new bomb
            return createBomb()
        }
        // Otherwise, retrieve a bomb from the pool and reset its properties
        val bomb = bombList.pop()
        resetBomb(bomb)
        return bomb
    }

    fun freeBomb(bomb: Bomb) {
        // Return the bomb to the pool
        bombList.add(bomb)
    }

    private fun resetBomb(bomb: Bomb) {
        // Reset the bomb's properties
        bomb.bombBody.isActive = false
        bomb.setPosition(-100f, -100f) // Set the position off-screen
    }
}

