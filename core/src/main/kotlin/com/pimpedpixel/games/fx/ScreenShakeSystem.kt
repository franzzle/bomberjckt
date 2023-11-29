package com.pimpedpixel.games.fx

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage

class ScreenShakeSystem(private val stage: Stage) : IteratingSystem(Family.all(ScreenShakeStarter::class.java).get()) {
    private var elapsed = 0f
    private val originalPosition: Vector3 = Vector3(stage.camera.position)

    // TODO Fill from properties
    private val duration = 0.5f
    private val intensity = 2f

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val orthographicCamera = stage.camera as OrthographicCamera

        if (elapsed < duration) {
            val amountOfShakeAmplitudeInterpolation =
                intensity * ((duration - elapsed) / duration)
            val x = (MathUtils.random.nextFloat() - 0.5f) * amountOfShakeAmplitudeInterpolation
            val y = (MathUtils.random.nextFloat() - 0.5f) * amountOfShakeAmplitudeInterpolation

            orthographicCamera.translate(-x, -y)

            elapsed += deltaTime
        } else {
            elapsed = 0f
            resetCamInOriginalPosition(orthographicCamera)
            entity.remove(ScreenShakeStarter::class.java)
        }
    }

    private fun resetCamInOriginalPosition(camera: OrthographicCamera) {
        camera.position.set(originalPosition.x, originalPosition.y, originalPosition.z)
    }
}
