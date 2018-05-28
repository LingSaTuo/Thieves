package com.lingsatuo.getqqmusic

import android.util.Log

class GetItemInfo(private var item:MusicItem,private var listener:(Throwable?)->Unit) :Thread(){
    override fun run() {
        try{
            item.albumicon = "https://y.gtimg.cn/music/photo_new/T002R300x300M000${item.albumid}.jpg?max_age=2592000"
            RunOnUiThread {
                listener.invoke(null)
            }
        }catch (e:Throwable){
            RunOnUiThread{
                listener.invoke(e)
            }
        }
        super.run()
    }
}