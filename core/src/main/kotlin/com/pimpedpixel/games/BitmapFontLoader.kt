package com.pimpedpixel.games

import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont

const val RETRO_FONT = "baskic28.fnt"

class BitmapFontLoader() {
    fun load() {
        val assetManager = AssetManagerHolder.assetManager
        val bitmapFontParameter = BitmapFontParameter()
        bitmapFontParameter.minFilter = Texture.TextureFilter.Nearest
        bitmapFontParameter.magFilter = Texture.TextureFilter.Nearest
        assetManager.setLoader(
            BitmapFont::class.java,
            BitmapFontLoader(InternalFileHandleResolver())
        )
        assetManager.load(RETRO_FONT, BitmapFont::class.java, bitmapFontParameter)
    }
}
