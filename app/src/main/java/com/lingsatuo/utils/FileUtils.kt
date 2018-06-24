package com.lingsatuo.utils

import android.os.Environment
import com.lingsatuo.getqqmusic.MusicItem
import java.io.File

object FileUtils {
    fun musicFileExists(item:MusicItem): String {
        val file = File(Environment.getExternalStorageDirectory(), "MusicOne/songs")
        val df = file.path+"/"+item.title + "_" + item.getSingers() + "."
        return myExists(df)
    }
    private fun myExists(path:String):String{
        val ends = arrayListOf(
                "mp3","ogg","flac","ape"
        )
        for (end in ends){
            if(File(path+end).exists()) return path+end
        }
        return ""
    }
}