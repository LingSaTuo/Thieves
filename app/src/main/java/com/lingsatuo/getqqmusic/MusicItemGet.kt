package com.lingsatuo.getqqmusic

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MusicItemGet(private var listener:(Throwable?,ArrayList<MusicItem>)-> Unit,private var musicGroup: MusicGroup) :Thread() {
    private val href = "https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&" +
            "json=1&utf8=1&onlysong=0&" +
            "disstid=${musicGroup.disstid}&format=jsonp" +
            "&g_tk=1293731818&" +
            "jsonpCallback=playlistinfoCallback&" +
            "loginUin=0&hostUin=0&format=jsonp" +
            "&inCharset=utf8" +
            "&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0"
    override fun run() {
        try{
            val url = URL(href)
            val huc = url.openConnection() as HttpURLConnection
            huc.doOutput = false
            huc.doInput = true
            huc.useCaches = false
            huc.requestMethod = "GET"
            huc.setRequestProperty("Charset", "UTF-8")
            huc.setRequestProperty("contentType", "application/json")
            huc.setRequestProperty("referer", "y.qq.com")
            huc.connect()
            val iis = huc.inputStream
            val buffered = BufferedReader(InputStreamReader(iis, "UTF-8"))
            var s = StringBuffer()
            while (true) {
                val line = buffered.readLine() ?: break
                s.append(line)
            }
            buffered.close()
            iis.close()
            huc.disconnect()

            val list = ItemGet(s.toString(),musicGroup).get()
            RunOnUiThread{
                listener.invoke(null,list)
            }
        }catch (e:Throwable){
            RunOnUiThread{
                listener.invoke(e,ArrayList())
            }
        }
        super.run()
    }
}