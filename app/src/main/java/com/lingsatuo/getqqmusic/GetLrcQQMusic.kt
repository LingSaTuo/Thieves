package com.lingsatuo.getqqmusic

import android.os.Environment
import com.lingsatuo.utils.FileUtils
import com.lingsatuo.utils.NetWork
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class GetLrcQQMusic(private var item: MusicItem, private var lis: (String, Throwable?) -> Unit) : Thread() {
    private val href = "https://u.y.qq.com/cgi-bin/musicu.fcg?_=1529342495335"
    private val songid = "https://c.y.qq.com/v8/fcg-bin/fcg_play_single_song.fcg?songmid=${item.singmid}&tpl=yqq_song_detail&format=jsonp&callback=getOneSongInfoCallback&g_tk=5381&jsonpCallback=getOneSongInfoCallback&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"
    override fun run() {
        super.run()
        try {
            val file = File(Environment.getExternalStorageDirectory(), "MusicOne/lrc")
            val df = File(file, item.title + "_" + item.getSingers() + "." + "lrc")
            val lrc = if (!item.isloca && !df.exists()) readJson() else if (item.isloca && !df.exists()) {
                var keywords = item.title+" "
                for (singer in item.singer){
                    keywords+="${singer.singer}  "
                }
                val mkeywords = keywords.replace(" ", "%20").replace("&","%26")
                val href = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=txt.yqq.top&searchid=0&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&p=0&n=20&w=$mkeywords&g_tk=5381&jsonpCallback=MusicJsonCallback09304789982202721&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"
                val str = String(NetWork().getBytes(href))
                item.singid = getSongid(str)
                readLrcT()
            } else {
                String(FileUtils.read(df.path))
            }
            RunOnUiThread {
                lis.invoke(lrc, null)
            }
        } catch (e: Throwable) {
            RunOnUiThread {
                lis.invoke("", e)
            }
        }
    }

    private fun getid(jsonSource: String): Int {
        val patterns = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(jsonSource)
        if (matcher.find()) {
            val root = matcher.group()
            val json = JSONObject(root)
            val data = json.getJSONArray("data")
            val id = data.getJSONObject(0).getInt("id")
            return id
        }
        return 0
    }

    private fun getSongid(str:String):Int{
        val patterns = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(str)
        if (matcher.find()) {
            val root = JSONObject(matcher.group())
            val data = root.getJSONObject("data")
            val song = data.getJSONObject("song")
            val listi = song.getJSONArray("list")
            if (listi.length()<1)return -1
            val obj = listi.getJSONObject(0)
            item.albummid = obj.getJSONObject("album").getString("mid")
            item.albumid = obj.getJSONObject("album").getInt("id")
            item.albumicon = "https://y.gtimg.cn/music/photo_new/T002R300x300M000${item.albummid}.jpg?max_age=2592000"
            item.album = obj.getJSONObject("album").getString("name")
            item.href = "https://y.qq.com/n/yqq/song/${item.singmid}.html"
            return obj.getInt("id")
        }
        return -1
    }

    private fun getLrc(jsonSource: String): String {
        val root = JSONObject(jsonSource)
        val song_detail = root.getJSONObject("song_detail")
        val data = song_detail.getJSONObject("data")
        val info = data.getJSONArray("info")
        for (index in 0 until info.length()) {
            if (info.getJSONObject(index).getString("type") == "lyric") {
                val content = info.getJSONObject(index).getJSONArray("content")
                val lrc = content.getJSONObject(0).getString("value").replace("\\n", "\n")
                return lrc
            }
        }
        return ""
    }

    private fun readJson(): String {
        val jsonSource = String(NetWork().getBytes(songid))
        val id = getid(jsonSource)
        item.singid = id
        return readLrcT()
    }

    private fun readLrcT(): String {
        val json = "{\"comm\":{\"g_tk\":5381,\"uin\":0,\"format\":\"json\",\"inCharset\":" +
                "\"utf-8\",\"outCharset\":\"utf-8\",\"notice\":0," +
                "\"platform\":\"h5\",\"needNewCode\":1},\"song_detail\"" +
                ":{\"module\":\"music.pf_song_detail_svr\"," +
                "\"method\":\"get_song_detail\"," +
                "\"param\":{\"song_id\":${item.singid}}}}"
        val url = URL(href)
        val huc = url.openConnection() as HttpURLConnection
        huc.doOutput = true
        huc.doInput = true
        huc.useCaches = false
        huc.requestMethod = "GET"
        huc.setRequestProperty("Charset", "UTF-8")
        huc.setRequestProperty("contentType", "application/json")
        huc.connect()
        val os = huc.outputStream
        os.write(json.toByteArray())
        os.flush()
        os.close()
        val iis = huc.inputStream
        val buffered = BufferedReader(InputStreamReader(iis, "UTF-8"))
        val s = StringBuffer()
        while (true) {
            val line = buffered.readLine() ?: break
            s.append(line)
        }
        buffered.close()
        iis.close()
        val lrc = getLrc(s.toString())
        huc.disconnect()
        return lrc
    }
}