package com.lingsatuo.getqqmusic

import com.lingsatuo.utils.NetWork
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.regex.Pattern

class GetMusicAbsPath(private var item :MusicItem,private var quality:GetMusicFileName.Quality,private var listener:(String)->Unit):Thread() {
    override fun run() {
        try {
            if (item.isloca) {
                RunOnUiThread {
                    listener.invoke(item.mid)
                }
                return
            }
            item.filename = GetMusicFileName.getName(quality,item)
            val vkg = VKeyGeter(item)
            val fcgfile = vkg.toString()
            val bytes = NetWork().getBytes(fcgfile)
            val json = String(bytes)
            val key = getVkey(json)
            item.vkey = key
            val path = "http://dl.stream.qqmusic.qq.com/" + item.filename + "?vkey=" + key + "&guid=" + vkg.getGuid()+"&uin="+vkg.getuin() + "&fromtag=" + vkg.getfromtag()
            RunOnUiThread{
                println(path)
                listener.invoke(path)
            }
            super.run()
        }catch (e:Throwable){
            e.printStackTrace()
        }
    }
    private fun getVkey(jsonSource:String):String{
        println(jsonSource)
        val patterns  = Pattern.compile("\\{[.*\\S\\s]+\\}")
        val matcher = patterns.matcher(jsonSource)
        if (matcher.find()){
            val root = matcher.group()
            val json = JSONObject(root)
            val data = json.getJSONObject("data")
            val array = data.getJSONArray("items")
            var key = array.getJSONObject(0).getString("vkey")
            if (key == ""){
                val jsp = Jsoup.connect(VKeyGeter.getKeyUrl()).get()
                key = jsp.select("info").attr("key")
            }
            return key
        }
        return ""
    }
}