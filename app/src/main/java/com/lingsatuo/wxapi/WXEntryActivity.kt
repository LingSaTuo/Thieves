package com.lingsatuo.wxapi

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WXEntryActivity : Activity(), IWXAPIEventHandler {
    lateinit var api: IWXAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        api = WXAPIFactory.createWXAPI(this, "wxb36b8d12d24fc278", false)
        api.handleIntent(intent, this)
        super.onCreate(savedInstanceState)
    }

    override fun onResp(p0: BaseResp?) {
        when (p0?.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                Toast.makeText(this,"分享成功",Toast.LENGTH_SHORT).show()
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                Toast.makeText(this,"分享取消",Toast.LENGTH_SHORT).show()
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                Toast.makeText(this,"微信拒绝",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onReq(p0: BaseReq?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}