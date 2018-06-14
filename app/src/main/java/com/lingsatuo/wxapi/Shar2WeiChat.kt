package com.lingsatuo.wxapi

import android.app.Activity
import android.graphics.BitmapFactory
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.utils.NetWork
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

object Shar2WeiChat {
    fun getApi(activity: Activity):IWXAPI = WXAPIFactory.createWXAPI(activity,"wxb36b8d12d24fc278")
    fun share2friend(activity: Activity,musicItem: MusicItem){
        har(activity,musicItem,1)
    }
    private fun har(activity: Activity,musicItem: MusicItem,type:Int){
        val music = WXWebpageObject()
        music.webpageUrl  = musicItem.href
        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = musicItem.title
        msg.description = musicItem.getSingers()
        Thread({
            val icon = NetWork().getBytes(musicItem.albumicon)
            val bmp = BitmapFactory.decodeByteArray(icon,0,icon.size)
            msg.setThumbImage(bmp)
            val req = SendMessageToWX.Req()
            req.transaction =  "music" + System.currentTimeMillis()
            req.message = msg
            req.scene = if (type==0) SendMessageToWX.Req.WXSceneTimeline else SendMessageToWX.Req.WXSceneSession
                    RunOnUiThread{
                getApi(activity).sendReq(req)
            }
        }).start()
    }
    fun share2circle(activity: Activity,musicItem: MusicItem){
        har(activity,musicItem,0)
    }
}