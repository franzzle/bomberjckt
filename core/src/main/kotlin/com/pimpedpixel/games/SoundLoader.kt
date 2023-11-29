package com.pimpedpixel.games

import com.badlogic.gdx.audio.Sound

class SoundLoader {
    fun load() {
        val assetManager = AssetManagerHolder.assetManager
        assetManager.load("sounds/bigexplode.wav", Sound::class.java)
        assetManager.load("sounds/bombfall.wav", Sound::class.java)
        assetManager.load("sounds/start.wav", Sound::class.java)
    }
}