package com.pimpedpixel.games.scoring

const val BOMBER_JCK_PREF_KEY = "bomberjckt"
interface PreferenceService {
    fun setForKey(key: String?, value: Boolean)
    fun forKey(key: String?, initialValue: Boolean?): Boolean
}
