package com.pimpedpixel.games.world

class CanyonGrid(private var layout: CanyonLayout) {
    val grid: Array<Array<Char?>> = Array(layout.width) { Array(layout.height) { null } }

    init {
        layout.layoutLines.forEachIndexed { columnIndex, line ->
            line.forEachIndexed { rowIndex, char ->
                grid[rowIndex][columnIndex] = char
            }
        }
    }
}
