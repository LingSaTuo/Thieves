package com.lingsatuo.utils

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.lingsatuo.service.MusicService
import com.lingsatuo.thieves.R
import com.lingsatuo.wxapi.Shar2WeiChat

class SharePop (private var activity : Activity,private var v:View){

    private var pop: PopupWindow
    private val view: View = LayoutInflater.from(activity).inflate(R.layout.share_layout_pop,null,false)
    init {
        pop = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true)
        pop.setBackgroundDrawable(ColorDrawable(0x00ffffff))
    }
    fun show(){
        val item = MusicService.instance?.item ?: return
        if (item.isloca)return
        if (item.singmid == "")return
        pop.showAsDropDown(v)
        view.findViewById<LinearLayout>(R.id.share2wechat).setOnClickListener {
            Shar2WeiChat.share2friend(activity,item)
            pop.dismiss()
        }
        view.findViewById<LinearLayout>(R.id.share2circle).setOnClickListener {
            Shar2WeiChat.share2circle(activity,item)
            pop.dismiss()
        }
    }

}