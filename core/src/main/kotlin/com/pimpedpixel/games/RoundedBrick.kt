package com.pimpedpixel.games

import com.badlogic.gdx.graphics.Pixmap

object RoundedBrick {
    fun getPixmapRoundedRectangle(width: Int, height: Int, radius: Int, color: Int): Pixmap {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        // Pink rectangle
        pixmap.fillRectangle(0, radius, pixmap.width, pixmap.height - 2 * radius)

// Green rectangle
        pixmap.fillRectangle(radius, 0, pixmap.width - 2 * radius, pixmap.height)


// Bottom-left circle
        pixmap.fillCircle(radius, radius, radius)

// Top-left circle
        pixmap.fillCircle(radius, pixmap.height - radius, radius)

// Bottom-right circle
        pixmap.fillCircle(pixmap.width - radius, radius, radius)

// Top-right circle
        pixmap.fillCircle(pixmap.width - radius, pixmap.height - radius, radius)
        return pixmap
    }
}
