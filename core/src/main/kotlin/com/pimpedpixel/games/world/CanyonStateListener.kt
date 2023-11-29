package com.pimpedpixel.games.world

import com.pimpedpixel.games.ScoringStateEnum
import java.util.*

interface CanyonStateListener : EventListener {
    fun canyonStateChanged(canyonState: CanyonState?, brick: Brick?, bomb: Bomb?)
    fun canyonStateChanged(canyonState: CanyonState?)
    fun scoringStateChanged(canyonState: ScoringStateEnum?)
}
