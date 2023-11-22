package com.pimpedpixel.games.world

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import java.util.LinkedList

class BombPool(private val world: World, stage: Stage) {
    private val bombList: LinkedList<Bomb> = LinkedList()

    init {
        // Initialize the bomb pool with 20 bombs
        for (i in 0 until 10) {
            val bomb = createBomb(i)
            stage.addActor(bomb);
            bombList.add(bomb)
        }
    }

    private fun createBomb(index: Int): Bomb {
        val x = -100f // Set the initial position off-screen
        val y = -100f
        return Bomb(world = world, index = index, initialPos = Vector2(x, y))
    }

    fun obtainBomb(): Bomb? {
        if (!bombList.isEmpty()) {
            val bomb = bombList.pop()
            resetBomb(bomb)
            return bomb
        }
        return null;
    }

    fun freeBomb(bomb: Bomb) {
        bombList.add(bomb)
    }

    private fun resetBomb(bomb: Bomb) {
        // Reset the bomb's properties
        bomb.isVisible = true
        val bombBody = bomb.bombBody
        bombBody.isActive = false
        bombBody.linearVelocity = Vector2.Zero
        val bombUserData = bombBody.userData as BombUserData
        bombUserData.destroyed = false
        bombUserData.bricksHit = 0

        bomb.setPosition(-100f, -100f) // Set the position off-screen
    }
}

