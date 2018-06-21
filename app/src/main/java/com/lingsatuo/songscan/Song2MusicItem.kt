package com.lingsatuo.songscan

import com.lingsatuo.getqqmusic.MusicItem

class Song2MusicItem(private var song:Song) {
    fun turn():MusicItem {
        val item = MusicItem()
        item.title = song.getTitle()
        item.isloca = true
        item.albumicon = song.getAlbumIcon()
        item.icon = song.getAlbumIcon()
        item.mid = song.getFileUrl()
        val singer = MusicItem.Singer()
        singer.singer = song.getSinger()
        item.singer.add(singer)
        item.singmid = "${Math.random()*100}"
        return item
    }
}