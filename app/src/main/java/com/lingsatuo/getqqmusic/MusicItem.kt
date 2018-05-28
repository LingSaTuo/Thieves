package com.lingsatuo.getqqmusic

import java.io.Serializable

class MusicItem : Serializable {
    var title =""//标题
    var href=""//链接
    var album = ""//专辑
    var albumid = ""//专辑
    var albumicon = ""//专辑
    var singer = ""//歌手
    var mid = ""//mid
    var singmid = ""//singmig
    var filename = ""//文件名
    var vkey = ""//Vkey
    override fun toString() = "$title   $href   $album  $singer  $mid  $singmid"
}