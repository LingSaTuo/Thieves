package com.lingsatuo.wxapi

import android.app.Activity
import android.graphics.BitmapFactory
import android.widget.Toast
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.utils.BitmapCompress
import com.lingsatuo.utils.NetWork
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

object Shar2WeiChat {
    fun getApi(activity: Activity):IWXAPI = WXAPIFactory.createWXAPI(activity,"wxb36b8d12d24fc278")
    fun share2friend(activity: Activity,musicItem: MusicItem){
        if (musicItem.isloca){
            Toast.makeText(activity,"你了解我的想法吗？不了解？（无法分享本地文件）",Toast.LENGTH_SHORT).show()
            return
        }
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
            try {
                val icon = NetWork().getBytes(musicItem.albumicon)
                var bmp = BitmapFactory.decodeByteArray(icon, 0, icon.size)
                if (icon.size/1024>32) bmp = BitmapCompress.WeChatBitmapToByteArray(bmp)
                msg.setThumbImage(bmp)
                val req = SendMessageToWX.Req()
                req.transaction = "music" + System.currentTimeMillis()
                req.message = msg
                req.scene = if (type == 0) SendMessageToWX.Req.WXSceneTimeline else SendMessageToWX.Req.WXSceneSession
                RunOnUiThread {
                    getApi(activity).sendReq(req)
                }
            }catch (e:Throwable){
                Toast.makeText(activity,e.toString(),Toast.LENGTH_SHORT).show()
            }
        }).start()
    }
    fun share2circle(activity: Activity,musicItem: MusicItem){
        har(activity,musicItem,0)
    }
}