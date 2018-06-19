package com.lingsatuo.songscan

import com.lingsatuo.getqqmusic.MusicItem

class Song2MusicItem(private var song:Song) {
    fun turn():MusicItem {
        val item = MusicItem()
        item.title = song.getTitle()
        item.isloca = true
        item.albumicon = "http://musicone-1253269015.coscd.myqcloud.com/9AB05C66154C4C2D975C53B3BFC43E76.png"
        item.mid = song.getFileUrl()
        val singer = MusicItem.Singer()
        singer.singer = song.getSinger()
        item.singer.add(singer)
        item.singmid = "${Math.random()*100}"
        return item
    }
}