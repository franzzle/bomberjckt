package com.pimpedpixel.games

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import java.nio.charset.StandardCharsets.UTF_8

class CanyonLayoutParserImpl : CanyonLayoutParser {
    override fun parse(canyonLayout: String): CanyonLayout {
        val brickLines = getBrickLines(canyonLayout)
        return CanyonLayout(layoutLines = brickLines, width = brickLines[0].length, height = brickLines.size)
    }

    private fun getBrickLines(canyonLayoutPattern: String): List<String> {
        val brickLines: MutableList<String> = ArrayList()
        val properties = Gdx.files.internal("$canyonLayoutPattern.txt")
        parseLines(properties, brickLines)
        brickLines.reverse()
        return brickLines
    }

    private fun parseLines(properties: FileHandle, brickLines: MutableList<String>) {
        properties.reader(UTF_8.name()).use { reader ->
            reader.forEachLine { line ->
                brickLines.add(line)
            }
        }
    }
}
