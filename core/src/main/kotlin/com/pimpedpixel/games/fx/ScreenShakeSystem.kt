package com.pimpedpixel.games.fx

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.pimpedpixel.games.CameraComponent

class ScreenShakeSystem : EntitySystem() {
    private var elapsed = 0f
    private var stopped = false
    private var entities: ImmutableArray<Entity>? = null

    //TODO Fill from properties
    private val duration = 0.5f
    private val intensity = 2f

    override fun addedToEngine(engine: Engine?) {
        entities = engine?.getEntitiesFor(Family.all(CameraComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        // Get the camera component from your Ashley entities.
        val cameraComponent: CameraComponent = entities?.first()?.getComponent(CameraComponent::class.java) ?: return

        if (elapsed < duration && !stopped) {
            val amountOfShakeAmplitudeInterpolation =
                intensity * ((duration - elapsed) / duration)
            val x = (MathUtils.random.nextFloat() - 0.5f) * amountOfShakeAmplitudeInterpolation
            val y = (MathUtils.random.nextFloat() - 0.5f) * amountOfShakeAmplitudeInterpolation

            cameraComponent.camera.translate(-x, -y)

            elapsed += deltaTime
        } else {
            stopped = true
            resetCamInOriginalPosition(cameraComponent.camera)
        }
    }

    private fun resetCamInOriginalPosition(camera: OrthographicCamera) {
        camera.position.set(Gdx.graphics.width * 0.5f, Gdx.graphics.height * 0.5f, 0f)
    }


}