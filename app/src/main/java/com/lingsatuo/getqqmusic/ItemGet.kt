package com.lingsatuo.getqqmusic

import org.json.JSONArray

class ItemGet(private var json: String,private var musicGroup: MusicGroup) {
    private val list = ArrayList<MusicItem>()

    init {
        list.clear()
        getListAndMusicGroup()
    }

    private fun getListAndMusicGroup() {
        val jobj = GetJsonObj.get(json)
        val cdlist = jobj.getJSONArray("cdlist").getJSONObject(0)
        musicGroup.describe = cdlist.getString("desc")
        musicGroup.icon = cdlist.getString("logo")
        musicGroup.num = cdlist.getInt("songnum")
        val songlist = cdlist.getJSONArray("songlist")
        for (index in 0 until songlist.length()){
            val song = songlist.getJSONObject(index)
            val item = MusicItem()
            item.album = song.getString("albumname")
            item.albumid = song.getInt("albumid")
            item.albummid = song.getString("albummid")
            item.strMediaMid = song.getString("strMediaMid")
            item.icon = item.albummid
            addSinger(item,song.getJSONArray("singer"))
            item.singid = song.getInt("songid")
            item.singmid = song.getString("songmid")
            item.title = song.getString("songname")
            item.href = "https://y.qq.com/n/yqq/song/${item.singmid}.html"
            list.add(item)
        }
    }

    private fun addSinger(musicItem: MusicItem,jsonArray: JSONArray){
        for (index in 0 until jsonArray.length()){
            val singer = MusicItem.Singer()
            val s = jsonArray.getJSONObject(index)
            singer.singer = s.getString("name")
            singer.singermid = s.getString("mid")
            singer.singerid = s.getInt("id")
            musicItem.singer.add(singer)
        }
    }

    fun get() = list
}