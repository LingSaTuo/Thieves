package com.lingsatuo.getqqmusic

import java.io.Serializable

class MusicItem : Serializable {
    var title = ""//标题
    var isloca = false//本地？
    var href = ""//链接
    var strMediaMid = ""//文件mid

    var icon: String = ""
        set(value) {
            field = if (isloca) value
            else {
                if (value != "001ZaCQY2OxVMg")
                    "https://y.gtimg.cn/music/photo_new/T002R90x90M000$value.jpg?max_age=2592000"
                else "http://musicone-1253269015.coscd.myqcloud.com/9AB05C66154C4C2D975C53B3BFC43E76.png"
            }
        }

    var album = ""//专辑
    var albumid = 0//专辑 id
    var albummid = ""//专辑 mid
    var albumicon = ""//专辑
    var singer = ArrayList<Singer>()//歌手
    var mid = ""//mid
    var singmid = ""//singmig
    var singid = 0// id
    var filename = ""//文件名
    var filesize = HashMap<GetMusicFileName.Quality,String>()
    var vkey = ""//Vkey
    override fun toString() = "$title   $href   $album  $singer  $mid  $singmid   $albumicon   $icon"
    class Singer : Serializable {
        var singerid = 0//歌手id
        var singer = ""//歌手名字
        var singermid = ""//歌手mid
        var singerhref = ""//歌手详情链接
        fun getSingerIcon() = "https://y.gtimg.cn/music/photo_new/T001R300x300M000$singermid.jpg?max_age=2592000"
    }

    fun getSingers(): String {
        val str = StringBuilder()
        for (index in 0 until singer.size) {
            str.append(singer[index].singer)
            if (index != singer.size - 1)
                str.append(" / ")
        }
        return str.toString()
    }
}