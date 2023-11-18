package com.pimpedpixel.games.scoring

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

open class ScoringServiceImpl : ScoringService {
    private var preferences: Preferences? = null
        get() {
            if (field == null) {
                field = Gdx.app.getPreferences("valleybomber")
            }
            return field
        }

    override fun add(score: Score) {
        if (preferences!!.get().containsKey(HIGHSCORE_PREF_KEY)) {
            val highscore = preferences!!.getInteger(HIGHSCORE_PREF_KEY)
            if (score.score > highscore) {
                preferences!!.putInteger(HIGHSCORE_PREF_KEY, score.score)
            }
        } else {
            preferences!!.putInteger(HIGHSCORE_PREF_KEY, score.score)
        }
        storePermanently()
    }

    private fun storePermanently() {
        preferences!!.flush()
    }

    override val currentHighScore: Int?
        get() = if (preferences!!.get().containsKey(HIGHSCORE_PREF_KEY)) {
            preferences!!.getInteger(HIGHSCORE_PREF_KEY)
        } else null

    companion object {
        private const val HIGHSCORE_PREF_KEY = "highscore"
    }
}
