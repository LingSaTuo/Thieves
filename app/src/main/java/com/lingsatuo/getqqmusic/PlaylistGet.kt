package com.lingsatuo.getqqmusic

import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.regex.Pattern

class PlaylistGet() {
    private val list = ArrayList<MusicGroup>()

    private fun getUrl() = "https://u.y.qq.com/cgi-bin/musicu.fcg?callback=recom6278047711117863&g_tk=5381&jsonpCallback=recom6278047711117863&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&data={\"recomPlaylist\":{\"method\":\"get_hot_recommend\",\"param\":{\"async\":1,\"cmd\":2},\"module\":\"playlist.HotRecommendServer\"}}"

    init {
        list.clear()
        getItem()
    }

    private fun getItem(){
        val source = Jsoup.connect(getUrl()).get()
        val patterns  = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(source.toString())
        if (matcher.find()){
            val string = matcher.group()
            val json = JSONObject(string)
            val rec = json.getJSONObject("recomPlaylist")
            val data = rec.getJSONObject("data")
            val list = data.getJSONArray("v_hot")
            for (obj in 0 until list.length()){
                createItem(list.getJSONObject(obj))
            }
        }
    }

    private fun createItem(jsonObject: JSONObject){
        val group = MusicGroup()
        group.disstid = jsonObject.getString("content_id")
        group.icon = jsonObject.getString("cover")
        group.href = "https://y.qq.com/n/yqq/playlist/${group.disstid}.html#stat=y_new.index.playlist.pic"
        group.title = jsonObject.getString("title").replace("&amp;","&")
        group.amount = jsonObject.getInt("listen_num").toString()
        list.add(group)
    }







//    private fun getItem(element: Element) {
//        val mGroup = MusicGroup()
//        val sec = element.select("a.js_playlist")
//        mGroup.href = sec.attr("href")
//        if (mGroup.href.indexOf("https:") == -1){
//            mGroup.href = "https:${mGroup.href}"
//        }
//        mGroup.disstid = sec.attr("data-disstid")
//        mGroup.icon = sec.select("img").attr("src")
//        mGroup.title = sec.select("img").attr("alt")
//        mGroup.amount = element.select("div.playlist__other").text().split("：")[1]
//        list.add(mGroup)
//    }

    fun get() = list

//    init {
//        list.clear()
//        val doc = Jsoup.parse("")
//        var index = 0
//        val first = doc.select("ul.playlist__list.slide__list").select("li.playlist__item.slide__item")
//        for (item in first) {
//            if (index++==10)break
//            getItem(item)
//        }
//    }
}