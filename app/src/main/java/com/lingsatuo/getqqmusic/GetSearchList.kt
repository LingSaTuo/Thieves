package com.lingsatuo.getqqmusic

import com.lingsatuo.getqqmusic.mv.MvItem
import com.lingsatuo.utils.NetWork
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern

class GetSearchList(private var keywords: String, private var page: Int, private var lis: (Throwable?, ArrayList<MusicItem>) -> Unit) : Thread() {
    val list = ArrayList<MusicItem>()
    override fun run() {
        val mkeywords = keywords.replace(" ", "%20").replace("&","%26")
        val href = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=txt.yqq.top&searchid=0&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&p=$page&n=20&w=$mkeywords&g_tk=5381&jsonpCallback=MusicJsonCallback09304789982202721&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"
        try {
            Thread.sleep(300)
            val str = String(NetWork().getBytes(href))
            getlist(str)
            RunOnUiThread {
                lis.invoke(null, list)
            }
        } catch (e: Throwable) {
            RunOnUiThread {
                lis.invoke(e, list)
            }
        }
        super.run()
    }

    private fun getlist(jsonSource: String) {
        val patterns = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(jsonSource)
        if (matcher.find()) {
            val root = JSONObject(matcher.group())
            val data = root.getJSONObject("data")
            val song = data.getJSONObject("song")
            val listi = song.getJSONArray("list")
            for (index in 0 until listi.length()) {
                val obj = listi.getJSONObject(index)
                val item = MusicItem()
                item.singmid = obj.getString("mid")
                if (obj.getJSONObject("mv").getInt("id")!=0){
                    val mv = MvItem()
                    mv.mvid = obj.getJSONObject("mv").getString("vid")
                    item.mvItem = mv
                }
                item.strMediaMid = obj.getJSONObject("file").getString("strMediaMid")
                getFileSize(item,obj.getJSONObject("file"))
                item.singid = obj.getInt("id")
                item.title = obj.getString("title")
                item.mid = obj.getInt("id").toString()
                val singers = obj.getJSONArray("singer")
                getSingers(item,singers)
                item.albummid = obj.getJSONObject("album").getString("mid")
                item.albumid = obj.getJSONObject("album").getInt("id")
                item.icon = item.albummid
                item.album = obj.getJSONObject("album").getString("name")
                item.href = "https://y.qq.com/n/yqq/song/${item.singmid}.html"
                list.add(item)
            }
        }
    }

    private fun getFileSize(item: MusicItem,file:JSONObject){
        val m4al = file.getInt("size_aac")
        if (m4al!=0)
            item.filesize.put(GetMusicFileName.Quality.M4AH,"${String.format("%.2f",(m4al/1024f/1024))}MB")
        val mp3h = file.getInt("size_320")
        if (mp3h!=0)
            item.filesize.put(GetMusicFileName.Quality.MP3H,"${String.format("%.2f",(mp3h/1024f/1024))}MB")
        val mp3 = file.getInt("size_128")
        if (mp3!=0)
            item.filesize.put(GetMusicFileName.Quality.MP3,"${String.format("%.2f",(mp3/1024f/1024))}MB")
        val ape = file.getInt("size_ape")
        if (ape!=0)
            item.filesize.put(GetMusicFileName.Quality.APE,"${String.format("%.2f",(ape/1024f/1024))}MB")
        val flac = file.getInt("size_flac")
        if (flac!=0)
            item.filesize.put(GetMusicFileName.Quality.FLAC,"${String.format("%.2f",(flac/1024f/1024))}MB")
        val ogg = file.getInt("size_ogg")
        if (ogg!=0)
            item.filesize.put(GetMusicFileName.Quality.OGG,"${String.format("%.2f",(ogg/1024f/1024))}MB")
    }

    private fun getSingers(item:MusicItem,singers: JSONArray){
        for (a in 0 until singers.length()) {
            val sings = MusicItem.Singer()
            sings.singer = singers.getJSONObject(a).getString("name")
            sings.singerid = singers.getJSONObject(a).getInt("id")
            sings.singermid = singers.getJSONObject(a).getString("mid")
            sings.singerhref = "https://y.qq.com/n/yqq/singer/${sings.singermid}.html"
            item.singer.add(sings)
        }
    }
}