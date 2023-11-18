package com.pimpedpixel.games.fx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils

class ScreenShake {
    private val duration = 0.5f
    private var elapsed = 0f
    private val intensity = 2f
    private var stopped = true

    fun update(camera: OrthographicCamera) {
        // Only shake when required.
        if (elapsed < duration && !stopped) {
            // Calculate the amount of shake based on how long it has been shaking already
            val amountOfShakeAmplitudeInterpolation = intensity * camera.zoom * ((duration - elapsed) / duration)
            val x = (MathUtils.random.nextFloat() - 0.5f) * amountOfShakeAmplitudeInterpolation
            val y = (MathUtils.random.nextFloat() - 0.5f) * amountOfShakeAmplitudeInterpolation

            camera.translate(-x, -y)

            // Increase the elapsed time by the delta provided.
            elapsed += Gdx.graphics.deltaTime
        } else {
            stopped = true
            resetCamInOriginalPosition(camera)
        }
    }

    private fun resetCamInOriginalPosition(camera: OrthographicCamera) {
        camera.position[Gdx.graphics.width * 0.5f, Gdx.graphics.height * 0.5f] = 0f
    }

    fun startShaking() {
        elapsed = 0f
        stopped = false
    }
}
