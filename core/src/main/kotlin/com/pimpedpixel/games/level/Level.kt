package com.pimpedpixel.games.level

class Level (val levelNumber: Int,
             private val description: String) {
    private var allowedBombs = 0
    private var allowedMisses = 0
    private var music: String? = null
    private var bricksDestroyedWithOneBomb = 0
    private var timeLimits = false
    private var timeLimitInSeconds = -1
    private var horizontalVelocity = 0f
    private var canyonLayoutName: String = "canyon"

    fun withAllowedBombs(allowedBombs: Int): Level {
        this.allowedBombs = allowedBombs
        return this
    }

    fun withAllowedMisses(allowedMisses: Int): Level {
        this.allowedMisses = allowedMisses
        return this
    }

    fun withMusic(music: String?): Level {
        this.music = music
        return this
    }

    fun withBricksDestroyedWithOneBomb(bricksDestroyedWithOneBomb: Int): Level {
        this.bricksDestroyedWithOneBomb = bricksDestroyedWithOneBomb
        return this
    }

    fun withTimeLimits(timeLimits: Boolean): Level {
        this.timeLimits = timeLimits
        return this
    }

    fun withTimeLimitInSeconds(timeLimitInSeconds: Int): Level {
        this.timeLimitInSeconds = timeLimitInSeconds
        return this
    }

    fun withHorizontalVelocity(horizontalVelocity: Float): Level {
        this.horizontalVelocity = horizontalVelocity
        return this
    }

    fun withCanyonLayoutName(canyonLayoutName: String): Level {
        this.canyonLayoutName = canyonLayoutName
        return this
    }

    override fun toString(): String {
        return description
    }
}
