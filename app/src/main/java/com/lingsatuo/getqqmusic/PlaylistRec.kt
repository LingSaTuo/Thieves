package com.lingsatuo.getqqmusic

class PlaylistRec(private var listenr: (Throwable?, ArrayList<MusicGroup>) -> Unit) : Thread() {
    override fun run() {
        try{
            val list = PlaylistGet().get()
            RunOnUiThread{
                listenr.invoke(null,list)
            }
        }catch (e:Throwable){
            RunOnUiThread{
                listenr.invoke(e,ArrayList())
            }
        }
        super.run()
    }
}