package com.pimpedpixel.games

import java.util.*

interface CanyonStateListener : EventListener {
    fun canyonStateChanged(canyonState: CanyonState?, brick: Brick?, bomb: Bomb?)
    fun canyonStateChanged(canyonState: CanyonState?)
    fun scoringStateChanged(canyonState: ScoringStateEnum?)
}
