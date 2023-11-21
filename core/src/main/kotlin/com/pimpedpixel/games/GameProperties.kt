package com.pimpedpixel.games

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.PropertiesUtils
import java.io.IOException

class GameProperties(type: String) {
    private val objectMap: ObjectMap<String, String>

    init {
        val path = "$type.properties"
        val properties = Gdx.files.internal(path)
        val reader = properties.reader("UTF-8")
        objectMap = ObjectMap()
        try {
            PropertiesUtils.load(objectMap, reader)
        } catch (e: IOException) {
            Gdx.app.error("", e.message)
        }
        try {
            reader?.close()
        } catch (e: IOException) {
            Gdx.app.error("", e.message)
        }
    }

    fun getPropAsFloat(key: String): Float {
        return objectMap.get(key).toFloat()
    }

    fun getPropAsInt(key: String): Int {
        return objectMap.get(key).toInt()
    }

    fun getPropAsBoolean(key: String): Boolean {
        return objectMap.get(key).toBoolean()
    }
    fun getPropAsString(key: String): String {
        return objectMap.get(key);
    }

    fun getPropAsColor(key: String): Color {
        return Color.valueOf(objectMap.get(key))
    }

    fun getKeyValuePairs(): ObjectMap<String, String> {
        return objectMap
    }
}
