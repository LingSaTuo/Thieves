package com.lingsatuo.getqqmusic

class GetMusicFileName {
    enum class Quality{
        M4AL,M4AH,MP3,MP3H,OGG,FLAC,APE
    }
    companion object {
        fun getName(quality: Quality,item: MusicItem) = when(quality){
            Quality.M4AL->"C200${item.singmid}.m4a"
            Quality.M4AH->"C400${item.singmid}.m4a"
            Quality.OGG->"O600${item.singmid}.ogg"
            Quality.MP3->"M500${item.singmid}.mp3"
            Quality.MP3H->"M800${item.singmid}.mp3"
            Quality.FLAC->"F000${item.singmid}.flac"
            Quality.APE->"A000${item.singmid}.ape"
        }
    }
}