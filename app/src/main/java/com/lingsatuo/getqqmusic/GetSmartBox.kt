package com.lingsatuo.getqqmusic

import com.lingsatuo.thieves.SearchActivity
import com.lingsatuo.utils.NetWork
import org.json.JSONObject
import java.util.regex.Pattern

class GetSmartBox(private var keywords: String,private var listener:(ArrayList<SearchActivity.HotKey>,Throwable?)->Unit) : Thread() {
    private var hotkeys = ArrayList<SearchActivity.HotKey>()
    companion object {
        private var time = System.currentTimeMillis()
    }
    override fun run() {
        if (System.currentTimeMillis() - time>200){
            time = System.currentTimeMillis()
        }else{
            return
        }
        try {
            val mkeywords = keywords.replace(" ", "%20")
            val href = "https://c.y.qq.com/splcloud/fcgi-bin/smartbox_new.fcg?is_xml=0&format=jsonp&key=$mkeywords&g_tk=5381&jsonpCallback=SmartboxKeysCallbackmod_search9941&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"
            val str = NetWork().getBytes(href)
            get(String(str))
            RunOnUiThread{
                listener.invoke(hotkeys,null)
            }
        }catch (e:Throwable){
            RunOnUiThread{
                listener.invoke(hotkeys,e)
            }
        }
        super.run()
    }

    private fun get(json: String) {
        val patterns = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(json)
        if (matcher.find()) {
            val root = JSONObject(matcher.group())
            var data = root.getJSONObject("data")
            val song = data.getJSONObject("song")
            val songs = song.getJSONArray("itemlist")
            for (index in 0 until songs.length()){
                val hotkey = SearchActivity.HotKey().setBody(songs.getJSONObject(index).getString("name"))
                hotkeys.add(hotkey)
            }
        }
    }
}