package com.pimpedpixel.games

import com.badlogic.gdx.scenes.scene2d.Action

class FlipAction(private val blimp: Blimp): Action() {
    override fun act(delta: Float): Boolean {
        blimp.flip()
        return true
    }
}
