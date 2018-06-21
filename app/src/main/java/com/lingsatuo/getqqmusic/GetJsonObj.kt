package com.lingsatuo.getqqmusic

import org.json.JSONObject
import java.util.regex.Pattern

object GetJsonObj {
    fun get(json:String):JSONObject{
        val patterns = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(json)
        if (matcher.find()) {
            return JSONObject(matcher.group())
        }
        return JSONObject()
    }
}