package com.pimpedpixel.games.world

import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.pimpedpixel.games.AssetManagerHolder
import com.pimpedpixel.games.SoundPlayer
import com.pimpedpixel.games.fx.ScreenShakeStarter
import com.pimpedpixel.games.gameplay.PlayerStatisticsComponent

class BombBrickContactListener(private var engine: PooledEngine) : ContactListener {
    private val playerStatsComponentFamily: Family = Family.all(PlayerStatisticsComponent::class.java).get()

    // Add a HashMap to keep track of previous collisions
    private val previousCollisions = HashMap<Pair<BombUserData, Brick>, Boolean>()



    override fun beginContact(contact: Contact) {
        // This method is called when two fixtures start to overlap (collide).
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB

        if ((fixtureA.body.userData is BombUserData  && fixtureB.body.userData is Brick) ||
            (fixtureA.body.userData is Brick && fixtureB.body.userData is BombUserData)) {

            val bombUserData: BombUserData
            val brick: Brick

            if(fixtureA.body.userData is BombUserData && fixtureB.body.userData is Brick){
                bombUserData = fixtureA.body.userData as BombUserData
                brick = fixtureB.body.userData as Brick
            }else{
                bombUserData = fixtureB.body.userData as BombUserData
                brick = fixtureA.body.userData as Brick
            }

            // Check if this is the first contact for this Bomb-Brick pair
            val pair = Pair(bombUserData, brick)
            val isFirstContact = previousCollisions[pair] != true

            if (isFirstContact && brick.isDestroyed().not()) {
//                Gdx.app.log("", "Bomb collided with a brick ${brick.row} - ${brick.column}}")
                handleBombBrickCollision(bombUserData, brick)
                previousCollisions[pair] = true
            }
        }
    }

    private fun handleBombBrickCollision(bombUserData: BombUserData, brick: Brick) {
        val playerStatisticsComponent = getPlayerStatisticsComponent(bombUserData)
        if(playerStatisticsComponent != null){
            if(bombUserData.bricksHit == 0 && brick.isOuterWall && bombUserData.outerWallHit == 0){
                bombUserData.outerWallHit++
                playerStatisticsComponent.misses++
                bombUserData.destroyed = true
                return
            }
            if (bombUserData.bricksHit >= 3) {
                bombUserData.destroyed = true
            }else{
                brick.setDestroyed(true)
                bombUserData.bricksHit++
                recordMissesAndScore(playerStatisticsComponent, bombUserData, brick)
            }
            if(bombUserData.bricksHit == 1 && brick.isOuterWall.not()){
                SoundPlayer.playBombSound()
                engine.addEntity(engine.createEntity().apply { add(ScreenShakeStarter()) })
            }
        }
//        Gdx.app.log(this.javaClass.simpleName, "Bomb ${bombUserData.index} collided ${bombUserData.bricksHit} times with a brick")
    }

    private fun getPlayerStatisticsComponent(bombUserData: BombUserData) : PlayerStatisticsComponent? {
        val entitiesFor = engine.getEntitiesFor(playerStatsComponentFamily)
        if (entitiesFor.size() > 0) {
            val iterator = entitiesFor.iterator()
            while (iterator.hasNext()) {
                val playerStatisticsComponent = iterator.next().getComponent(PlayerStatisticsComponent::class.java)
                if (bombUserData.thrownBy === playerStatisticsComponent.playerChoice) {
                    return playerStatisticsComponent
                }
            }
        }
        return null
    }


    private fun recordMissesAndScore(
        playerStatisticsComponent: PlayerStatisticsComponent,
        bombUserData: BombUserData,
        brick: Brick
    ) {
        if (bombUserData.thrownBy === playerStatisticsComponent.playerChoice) {
            if (brick.isOuterWall && bombUserData.bricksHit == 0) {
                playerStatisticsComponent.misses++
            } else {
                playerStatisticsComponent.hits++
                playerStatisticsComponent.score += brick.score
            }
        }
        Gdx.app.log(
            "",
            "Player has ${playerStatisticsComponent.score} points"
        )
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
