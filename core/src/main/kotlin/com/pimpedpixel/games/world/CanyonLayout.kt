package com.pimpedpixel.games.world

class CanyonLayout(
    @JvmField var layoutLines: List<String> = emptyList(),
    @JvmField var width: Int = 0,
    @JvmField var height: Int = 0
) {
    override fun toString(): String {
        return "CanyonLayout($width x $height)"
    }
}


