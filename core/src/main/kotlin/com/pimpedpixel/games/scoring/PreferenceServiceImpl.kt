package com.pimpedpixel.games.scoring

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

class PreferenceServiceImpl : PreferenceService {
    protected var preferences: Preferences? = null
        protected get() {
            if (field == null) {
                field = Gdx.app.getPreferences("valleybomber")
            }
            return field
        }
        private set

    override fun setForKey(key: String?, value: Boolean) {
        if (key != null) {
            preferences!!.putBoolean(key, value)
        }
    }

    override fun forKey(key: String?, initialValue: Boolean?): Boolean {
        if (preferences!!.contains(key)) {
            return preferences!!.getBoolean(key)
        } else {
            preferences!!.putBoolean(key, initialValue!!)
        }
        return false
    }
}
