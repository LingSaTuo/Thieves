package com.lingsatuo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
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
    fun startMvVideo(context: Context,path: String){
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse(path)
        intent.setDataAndType(uri,"video/*")
        context.startActivity(intent)
    }
}