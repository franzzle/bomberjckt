package com.pimpedpixel.games.world

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver

class BrickAssetloader(val assetManager: AssetManager) {
    fun load(){
        assetManager.setLoader(BrickTexture::class.java, BrickTextureLoader(InternalFileHandleResolver()))
        BrickColors().getPossibleColors().forEach { brickColorFilename ->
            assetManager.load(
                brickColorFilename, BrickTexture::class.java,
                BrickTextureLoader.BrickTextureParameter()
            )
        }

    }
}