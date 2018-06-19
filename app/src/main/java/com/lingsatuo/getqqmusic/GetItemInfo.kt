package com.lingsatuo.getqqmusic


class GetItemInfo(private var item:MusicItem,private var listener:(Throwable?)->Unit) :Thread(){
    override fun run() {
        try{
            if (item.albumid == "001ZaCQY2OxVMg" || item.isloca)
                item.albumicon = "http://musicone-1253269015.coscd.myqcloud.com/9AB05C66154C4C2D975C53B3BFC43E76.png"
            else
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