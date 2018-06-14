package com.lingsatuo.getqqmusic

import com.lingsatuo.utils.NetWork
import org.json.JSONObject
import java.util.regex.Pattern

class GetHotKey(private var lis: (ArrayList<String>) -> Unit) : Thread() {
    val keys = ArrayList<String>()
    override fun run() {
        val str = String(NetWork().getBytes("https://c.y.qq.com/splcloud/fcgi-bin/gethotkey.fcg?g_tk=5381&jsonpCallback=hotSearchKeysmod_search&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"))

        val patterns = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(str)
        if (matcher.find()) {
            val keyi = JSONObject(matcher.group()).getJSONObject("data").getJSONArray("hotkey")
            for (a in 0 until 5) {
                keys.add(keyi.getJSONObject(a).getString("k"))
            }
            RunOnUiThread {
                lis.invoke(keys)
            }
        } else {
            RunOnUiThread {
                keys.add("中华人民共和国国歌")
                lis.invoke(keys)
            }
        }
        super.run()
    }
}