package com.lingsatuo.getqqmusic

class GetMusicFileName {
    enum class Quality{
        M4AL,
        M4AH,
        MP3,
        MP3H,
        OGG,
        FLAC,
        APE
    }
    companion object {
        fun getName(quality: Quality,item: MusicItem) = when(quality){
            Quality.M4AL->"C200${item.strMediaMid}.m4a"
            Quality.M4AH->"C400${item.strMediaMid}.m4a"
            Quality.OGG->"O600${item.strMediaMid}.ogg"
            Quality.MP3->"M500${item.strMediaMid}.mp3"
            Quality.MP3H->"M800${item.strMediaMid}.mp3"
            Quality.FLAC->"F000${item.strMediaMid}.flac"
            Quality.APE->"A000${item.strMediaMid}.ape"
        }
    }
}