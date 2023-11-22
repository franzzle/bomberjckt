package com.pimpedpixel.games.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.pimpedpixel.games.world.BrickTextureLoader.BrickTextureParameter

class BrickTextureLoader(resolver: InternalFileHandleResolver?) :
    SynchronousAssetLoader<BrickTexture?, BrickTextureParameter?>(resolver) {
    lateinit var brickTexture : BrickTexture


    override fun load(
        manager: AssetManager,
        fileName: String,
        file: FileHandle,
        parameter: BrickTextureParameter?
    ): BrickTexture? {
        val canyon = CanyonLayoutParserImpl().parse("canyon")
        val sizeOfSideOfOneBrick = Gdx.graphics.width / canyon.width

        val colorIndex = fileName.split("brickcolor")[1].split(".")[0].toInt()

        val brickPixMap = Pixmap(sizeOfSideOfOneBrick, sizeOfSideOfOneBrick, Pixmap.Format.RGBA8888)
        brickPixMap.setColor(BrickColors().getColor(colorIndex));
        brickPixMap.fill()
        val texture = Texture(brickPixMap)
        brickPixMap.dispose()
        return BrickTexture(texture, fileName)
    }

    class BrickTextureParameter : AssetLoaderParameters<BrickTexture?>()

    override fun getDependencies(
        fileName: String?,
        file: FileHandle?,
        parameter: BrickTextureParameter?
    ): Array<AssetDescriptor<Any>> {
        return Array();
    }
}
