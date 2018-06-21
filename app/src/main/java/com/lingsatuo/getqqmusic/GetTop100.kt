package com.lingsatuo.getqqmusic

import com.lingsatuo.utils.NetWork
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class GetTop100(private var lis:(Throwable?, ArrayList<MusicItem>)->Unit):Thread() {
    val list = ArrayList<MusicItem>()
    override fun run() {
        super.run()
        list.clear()
        try{
            val cadlender = Calendar.getInstance()
            cadlender.add(Calendar.DAY_OF_MONTH,-1)
            val year = cadlender.get(Calendar.YEAR)
            val month = cadlender.get(Calendar.MONTH)+1
            val smonth = if (month<10) "0$month" else "$month"
            val day = cadlender.get(Calendar.DAY_OF_MONTH)
            val href = "https://c.y.qq.com/v8/fcg-bin/" +
                    "fcg_v8_toplist_cp.fcg?" +
                    "tpl=3&page=1&date=$year-$smonth-$day&topid=4&type=top&song_begin=0&song_num=100&g_tk=1293189984&jsonpCallback=MusicJsonCallbacktoplist&loginUin=1617685714&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"
            val str = String(NetWork().getBytes(href))
            read(str)
            RunOnUiThread{
                lis.invoke(null,list)
            }
        }catch (e:Throwable){
            RunOnUiThread{
                lis.invoke(e,list)
            }
        }
    }
    private fun read(string:String){
        val jobj = GetJsonObj.get(string)
        val songlist = jobj.getJSONArray("songlist")
        for (index in 0 until songlist.length()){
            val song = songlist.getJSONObject(index)
            val item = MusicItem()
            val data = song.getJSONObject("data")
            item.albumid = data.getInt("albumid")
            item.albummid = data.getString("albummid")
            item.album = data.getString("albumname")
            addSinger(item,data.getJSONArray("singer"))
            item.singid = data.getInt("songid")
            item.singmid = data.getString("songmid")
            item.title = data.getString("songname")
            item.strMediaMid = data.getString("strMediaMid")
            getFileSize(item,data)
            item.icon = item.albummid
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
}