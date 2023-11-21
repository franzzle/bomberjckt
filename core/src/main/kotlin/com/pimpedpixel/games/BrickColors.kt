package com.pimpedpixel.games

import com.badlogic.gdx.graphics.Color

internal class BrickColors {
    private val colorMap: MutableMap<Int, Color>;

    init {
        colorMap = HashMap()
        val canyonProperties = GameProperties("canyon")
        val keys = canyonProperties.getKeyValuePairs().keys()
        for (key in keys) {
            val colorHex = canyonProperties.getPropAsString(key)
            val color = Color.valueOf(colorHex)
            val toIntOrNull = key.split("brickcolor")[1].toInt()
            colorMap[toIntOrNull] = color
        }
    }

    fun getColor(colorNumber: Int): Color? {
        return colorMap.get(colorNumber)
    }

    fun getPossibleColors(): Set<String> {
        return colorMap.keys.map { "brickcolor$it.png" }.toSet()
    }
}
