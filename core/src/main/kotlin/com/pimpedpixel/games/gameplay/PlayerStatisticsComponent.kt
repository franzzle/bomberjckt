package com.pimpedpixel.games.gameplay

import com.badlogic.ashley.core.Component

class PlayerStatisticsComponent(var playerChoice: PlayerChoice): Component  {
    var score: Int = 0
    var misses: Int = 0
    var hits: Int = 0
}