package com.pimpedpixel.games.world

import com.pimpedpixel.games.gameplay.PlayerChoice

class BombUserData(
    val index: Int,
    var thrownBy: PlayerChoice = PlayerChoice.UNDEFINED,
    var bricksHit: Int = 0,
    var outerWallHit: Int = 0,
    var destroyed: Boolean = false
) {
    fun reset() {
        thrownBy = PlayerChoice.UNDEFINED
        bricksHit = 0
        outerWallHit = 0
        destroyed = false
    }
}
