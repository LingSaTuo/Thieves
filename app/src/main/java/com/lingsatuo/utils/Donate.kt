package com.lingsatuo.utils

import android.app.Activity
import android.didikee.donate.AlipayDonate
import android.widget.Toast

class Donate(private var activity: Activity){

     fun pay(key:String){
        if (AlipayDonate.hasInstalledAlipayClient(activity)){
            AlipayDonate.startAlipayClient(activity,key)
        }else{
            Toast.makeText(activity,"支付宝宝宝宝宝宝呢。。。",Toast.LENGTH_LONG).show()
        }
    }
}