package com.lingsatuo.getqqmusic

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class ItemGet(private var html:String) {
    private val list = ArrayList<MusicItem>()
    init {
        list.clear()
        val ht = Jsoup.parse(html)
        val root = ht.select("ul.songlist__list").select("li")
        for (ele in root){
            getItem(ele)
        }
    }
    private fun getItem(element: Element){
        val item = MusicItem()
        item.mid = element.attr("mid")
        item.href = element.select("a.js_song").attr("href")
        item.title = element.select("a.js_song").attr("title")
        item.singer = element.select("a.singer_name").text()
        item.album = element.select("div.songlist__album").select("a").text()
        item.albumid = element.select("div.songlist__album").select("a").attr("data-albummid")
        val strings = item.href.split("/")
        item.singmid = strings[strings.size-1].split(".")[0]
        list.add(item)
    }
    fun get() = list
}