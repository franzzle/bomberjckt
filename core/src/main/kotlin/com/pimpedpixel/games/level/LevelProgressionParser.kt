package com.pimpedpixel.games.level

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

object LevelProgressionParser {
    fun parse(): LevelProgression {
        val pathLevelProgressionJson = "levelProgression.json"
        val pathLevelProgressionJsonFileHandle = Gdx.files.internal(pathLevelProgressionJson)
        val root = JsonReader().parse(pathLevelProgressionJsonFileHandle)
        val levels = root["levels"]
        val levelList: MutableList<Level> = ArrayList()
        for (level in levels) {
            val levelNumber = level.getInt("level")
            val description = level.getString("description")
            val allowedBombs = level.getInt("allowedBombs")
            val music = level.getString("music")
            val allowedMisses = level.getInt("allowedMisses")
            val horizontalVelocity = level.getFloat("horizontalVelocity")
            val bricksDestroyedWithOneBomb = level.getInt("bricksDestroyedWithOneBomb")
            val timeLimits = level.getBoolean("timeLimits")
            val timeLimitInSeconds = level.getInt("timeLimitInSeconds")
            val newLevel = Level(levelNumber, description)
                .withAllowedBombs(allowedBombs)
                .withMusic(music)
                .withAllowedMisses(allowedMisses)
                .withHorizontalVelocity(horizontalVelocity)
                .withBricksDestroyedWithOneBomb(bricksDestroyedWithOneBomb)
                .withTimeLimits(timeLimits)
                .withTimeLimitInSeconds(timeLimitInSeconds)
            levelList.add(newLevel)
        }
        return LevelProgression(levelList)
    }
}
