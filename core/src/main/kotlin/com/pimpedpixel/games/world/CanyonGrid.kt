package com.pimpedpixel.games.world

import com.pimpedpixel.games.GameProperties

class CanyonGrid(
    private var layout: CanyonLayout,
    private var colorProperties: GameProperties
) {
    val grid: Array<Array<String?>> = Array(layout.width) { Array(layout.height) { null } }

    init {
        layout.layoutLines.forEachIndexed { columnIndex, line ->
            line.forEachIndexed { rowIndex, char ->
                grid[rowIndex][columnIndex] = "brickcolor$char.png"
            }
        }
    }

    fun isValidIndex(rowIndex: Int, columnIndex: Int): Boolean {
        return columnIndex in 0 until layout.width && rowIndex in 0 until layout.height
    }

    fun getColor(rowIndex: Int, columnIndex: Int): String {
        if (isValidIndex(rowIndex, columnIndex)) {
            return grid[rowIndex][columnIndex]!!
        }
        return "#000000";
    }
}
