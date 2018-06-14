package com.lingsatuo

import com.lingsatuo.lingapplication.LingBaseTop
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class Top : LingBaseTop() {
    override fun onCreate() {
        val key = "wxb36b8d12d24fc278"
        WXAPIFactory.createWXAPI(this,key,true).registerApp(key)
        super.onCreate()
    }
}