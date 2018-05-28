package com.lingsatuo.getqqmusic

class MusicItemGet(private var listener:(Throwable?,ArrayList<MusicItem>)-> Unit,private var html:String) :Thread() {
    override fun run() {
        try{
            val list = ItemGet(html).get()
            RunOnUiThread{
                listener.invoke(null,list)
            }
        }catch (e:Throwable){
            RunOnUiThread{
                listener.invoke(e,ArrayList())
            }
        }
        super.run()
    }
}