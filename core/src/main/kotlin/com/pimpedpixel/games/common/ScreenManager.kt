package com.pimpedpixel.games.common

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Rectangle
import com.pimpedpixel.games.GameProperties
import com.pimpedpixel.games.GamePropertiesEnum

class ScreenManager private constructor() {
    var gameProperties: GameProperties? = null
        private set
    val rectScale: Rectangle
        get() {
            val scaleWidth = Gdx.graphics.width / instance!!.gameProperties!!.getPropAsFloat("width")
            val scaleHeight = Gdx.graphics.width / instance!!.gameProperties!!.getPropAsFloat("height")
            val rectangle = Rectangle()
            rectangle.setWidth(scaleWidth)
            rectangle.setHeight(scaleHeight)
            return rectangle
        }
    val scale: Int
        get() {
            val rectScale = instance!!.rectScale
            var scaleWidth = rectScale.getWidth().toInt()
            if (scaleWidth == 0) {
                Gdx.app.log(this.javaClass.simpleName, "WARN , scale = 0 and blimp might not run OK.")
                scaleWidth = 1
            }
            return scaleWidth
        }

    companion object {
        private var instance: ScreenManager? = null
        @JvmStatic
        fun getInstance(): ScreenManager? {
            if (instance == null) {
                instance = ScreenManager()
                instance!!.gameProperties = GameProperties(GamePropertiesEnum.SCREEN_MANAGER.prefixName)
            }
            return instance
        }

        @JvmStatic
        fun getWidthForRatio(ratioForWidthScreen: Float): Float {
            return Gdx.app.graphics.width * ratioForWidthScreen
        }

        @JvmStatic
        fun getHeightForRatio(ratioForHeightScreen: Float): Float {
            val aspectRatio = Gdx.app.graphics.width.toFloat() / Gdx.app.graphics.height.toFloat()
            return Gdx.app.graphics.width * ratioForHeightScreen * aspectRatio
        }
    }
}
