package com.pimpedpixel.games.level

class LevelProgression(val levels: List<Level>) {
    fun getLevel(levelNumber: Int): Level {
        for (level in levels) {
            if (level.levelNumber == levelNumber) {
                return level
            }
        }
        return levels[levels.size - 1]
    }
}
