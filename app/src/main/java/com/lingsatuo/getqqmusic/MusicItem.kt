package com.lingsatuo.getqqmusic

import java.io.Serializable

class MusicItem : Serializable {
    var title =""//标题
    var href=""//链接
    var album = ""//专辑
    var albumid = ""//专辑
    var albumicon = ""//专辑
    var singer = ArrayList<Singer>()//歌手
    var mid = ""//mid
    var singmid = ""//singmig
    var filename = ""//文件名
    var vkey = ""//Vkey
    override fun toString() = "$title   $href   $album  $singer  $mid  $singmid"
    class Singer : Serializable{
        var singerid=""//歌手id
        var singer=""//歌手名字
        var singermid=""//歌手mid
        var singerhref=""//歌手详情链接
        fun getSingerIcon() = "https://y.gtimg.cn/music/photo_new/T001R300x300M000$singermid.jpg?max_age=2592000"
    }
    fun getSingers():String{
        val str = StringBuilder()
        for (index in 0 until singer.size){
            str.append(singer[index].singer)
            if (index!=singer.size-1)
                str.append(" 、 ")
        }
        return str.toString()
    }
}