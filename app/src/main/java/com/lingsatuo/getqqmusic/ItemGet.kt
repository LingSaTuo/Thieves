package com.lingsatuo.getqqmusic

import com.lingsatuo.getqqmusic.mv.MvItem
import org.json.JSONArray
import org.json.JSONObject

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
            if (song.getString("vid")!=""){
                item.mvItem = MvItem()
                item.mvItem!!.mvid = song.getString("vid")
            }
            getFileSize(item,song)
            item.icon = item.albummid
            addSinger(item,song.getJSONArray("singer"))
            item.singid = song.getInt("songid")
            item.singmid = song.getString("songmid")
            item.title = song.getString("songname")
            item.href = "https://y.qq.com/n/yqq/song/${item.singmid}.html"
            list.add(item)
        }
    }



    private fun getFileSize(item: MusicItem,file: JSONObject){
        val mp3h = file.getInt("size320")
        if (mp3h!=0)
            item.filesize.put(GetMusicFileName.Quality.MP3H,"${String.format("%.2f",(mp3h/1024f/1024))}MB")
        val mp3 = file.getInt("size128")
        if (mp3!=0)
            item.filesize.put(GetMusicFileName.Quality.MP3,"${String.format("%.2f",(mp3/1024f/1024))}MB")
        val ape = file.getInt("sizeape")
        if (ape!=0)
            item.filesize.put(GetMusicFileName.Quality.APE,"${String.format("%.2f",(ape/1024f/1024))}MB")
        val flac = file.getInt("sizeflac")
        if (flac!=0)
            item.filesize.put(GetMusicFileName.Quality.FLAC,"${String.format("%.2f",(flac/1024f/1024))}MB")
        val ogg = file.getInt("sizeogg")
        if (ogg!=0)
            item.filesize.put(GetMusicFileName.Quality.OGG,"${String.format("%.2f",(ogg/1024f/1024))}MB")
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