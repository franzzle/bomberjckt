package com.pimpedpixel.games.world

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.pimpedpixel.games.AssetManagerHolder

class BrickAssetLoader() {
    fun load(){
        val assetManager = AssetManagerHolder.assetManager
        assetManager.setLoader(BrickTexture::class.java, BrickTextureLoader(InternalFileHandleResolver()))
        BrickColors().getPossibleColors().forEach { brickColorFilename ->
            assetManager.load(
                brickColorFilename, BrickTexture::class.java,
                BrickTextureLoader.BrickTextureParameter()
            )
        }
    }
}