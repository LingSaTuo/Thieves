package com.lingsatuo.utils

import android.content.Context
import android.widget.Toast
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.mv.GetMvAbsPath

object StartMV {
    fun start(context: Context,item:MusicItem){
        val tost = Toast.makeText(context,"获取中。。。", Toast.LENGTH_SHORT)
        tost.show()
        GetMvAbsPath(item.mvItem!!, { path, e ->
            if (e == null) {
                FileUtils.startMvVideo(context, path)
            }else{
                tost.setText("当前歌曲MV没有第一通道资源")
                tost.show()
                val mv = item.mvItem!!
                val paths = "${mv.startHref}${mv.lfilename}?sdtfrom=v1070&type=mp4&vkey=${mv.fvkey}${mv.other}"
                FileUtils.startMvVideo(context, paths)
            }
        }).start()
    }
}