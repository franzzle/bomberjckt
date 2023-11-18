package com.pimpedpixel.games.scoring

interface PreferenceService {
    fun setForKey(key: String?, value: Boolean)
    fun forKey(key: String?, initialValue: Boolean?): Boolean
}
