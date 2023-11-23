package com.pimpedpixel.games.world

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.pimpedpixel.games.gameplay.PlayerChoice
import java.util.*

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
        bomb.bombBody.isActive = false
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
        bombUserData.outerWallHit = 0
        bombUserData.bricksHit = 0
        bombUserData.thrownBy = PlayerChoice.UNDEFINED
        bomb.color = Color.WHITE
        bomb.setPosition(-100f, -100f) // Set the position off-screen
    }
}

