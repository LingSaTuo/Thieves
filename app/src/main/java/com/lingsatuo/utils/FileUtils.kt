package com.lingsatuo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.lingsatuo.getqqmusic.MusicItem
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun musicFileExists(item:MusicItem): String {
        val file = File(Environment.getExternalStorageDirectory(), "MusicOne/songs")
        val df = file.path+"/"+item.title + "_" + item.getSingers() + "."
        return myExists(df)
    }
    private fun myExists(path:String):String{
        val ends = arrayListOf(
                "flac","mp3","ogg","ape","m4a"
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
    fun write(path:String,str:String){
        val f = File(path)
        if (!f.parentFile.exists())f.parentFile.mkdirs()
        if (!f.exists())f.createNewFile()
        val out = FileOutputStream(f)
        out.write(str.toByteArray())
        out.close()
    }
    fun read(path:String):ByteArray =  File(path).readBytes()
}