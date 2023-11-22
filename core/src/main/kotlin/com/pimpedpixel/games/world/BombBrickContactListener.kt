package com.pimpedpixel.games.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.physics.box2d.*

class BombBrickContactListener : ContactListener {
    var brickDestroyedSound: Sound? = null
    init {
        brickDestroyedSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bigexplode.wav"))
    }

    override fun beginContact(contact: Contact) {
        // This method is called when two fixtures start to overlap (collide).
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB

        if ((fixtureA.body.userData is BombUserData  && fixtureB.body.userData is Brick) ||
            (fixtureA.body.userData is Brick && fixtureB.body.userData is BombUserData)) {

            if(fixtureA.body.userData is BombUserData && fixtureB.body.userData is Brick){
                handleBombBrickCollision(fixtureA.body.userData as BombUserData, fixtureB.body.userData as Brick)
            }else{
                handleBombBrickCollision(fixtureB.body.userData as BombUserData, fixtureA.body.userData as Brick)
            }
        }
    }

    private fun handleBombBrickCollision(bombUserData: BombUserData, brick: Brick) {
        if(!brick.isDestroyed()) {
            bombUserData.bricksHit++
        }

        if(bombUserData.bricksHit > 0 && bombUserData.bricksHit < 2){
            brickDestroyedSound?.play()
        }
        if (bombUserData.bricksHit >= 4) {
            bombUserData.destroyed = true
        }else{
            brick.setDestroyed(true)
        }
//        Gdx.app.log(this.javaClass.simpleName, "Bomb ${bombUserData.index} collided ${bombUserData.bricksHit} times with a brick")
    }

    override fun endContact(contact: Contact) {
        // This method is called when two fixtures stop overlapping (no longer collide).
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        // This method is called before the collision is resolved.
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
        // This method is called after the collision is resolved.
    }
}
