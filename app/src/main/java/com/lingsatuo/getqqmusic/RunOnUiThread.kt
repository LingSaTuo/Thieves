package com.lingsatuo.getqqmusic

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * Created by Administrator on 2018/4/18.
 */
class RunOnUiThread(runWhat: () -> Unit) {
    private var runOnUi:()->Unit= runWhat
    private val handler = @SuppressLint("HandlerLeak")
    object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg==null)return
            runOnUi.invoke()
        }
    }

    init {
        val msg = Message()
        msg.what = 0
        msg.obj = null
        handler.sendMessage(msg)
    }
}