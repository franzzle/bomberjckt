package com.pimpedpixel.games.world

import com.pimpedpixel.games.GameProperties

class CanyonGrid(
    private var layout: CanyonLayout,
    private var colorProperties: GameProperties
) {
    val grid: Array<Array<Char?>> = Array(layout.width) { Array(layout.height) { null } }

    init {
        layout.layoutLines.forEachIndexed { columnIndex, line ->
            line.forEachIndexed { rowIndex, char ->
                grid[rowIndex][columnIndex] = char
            }
        }
    }
}
