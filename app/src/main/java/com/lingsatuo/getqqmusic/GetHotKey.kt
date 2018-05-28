package com.lingsatuo.getqqmusic

import com.lingsatuo.utils.NetWork
import org.json.JSONObject
import java.util.regex.Pattern

class GetHotKey(private var lis:(String)->Unit) : Thread() {
    override fun run() {
        val str = String(NetWork().getBytes("https://c.y.qq.com/splcloud/fcgi-bin/gethotkey.fcg?g_tk=5381&jsonpCallback=hotSearchKeysmod_search&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"))

        val patterns = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(str)
        if (matcher.find()) {
            val key = JSONObject(matcher.group()).getJSONObject("data").getJSONArray("hotkey").getJSONObject(0).getString("k")
            RunOnUiThread{
                lis.invoke(key)
            }
        }else{
            RunOnUiThread{
                lis.invoke("中华人民共和国国歌")
            }
        }
        super.run()
    }
}