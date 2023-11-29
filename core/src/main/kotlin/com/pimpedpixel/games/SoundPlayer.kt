package com.pimpedpixel.games

import com.badlogic.gdx.audio.Sound

object SoundPlayer {
    @JvmStatic
    fun playStartSound() {
        val sound = AssetManagerHolder.assetManager.get("sounds/start.wav", Sound::class.java)
        sound.play()
    }

    @JvmStatic
    fun playBombSound() {
        val sound = AssetManagerHolder.assetManager.get("sounds/bigexplode.wav", Sound::class.java)
        sound.play()
    }
}