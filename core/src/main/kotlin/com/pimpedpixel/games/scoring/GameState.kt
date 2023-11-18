package com.pimpedpixel.games.scoring

class GameState {
    var isStarted = false
    var isFinished = false
    var isGameOverShownLongEnough = false
    var isPlayerAllowedToDropAnotherBomb = true
    @JvmField
    var numberOfMisses = 0
    @JvmField
    var score = 0

}
