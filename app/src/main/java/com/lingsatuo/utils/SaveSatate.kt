package com.lingsatuo.utils

import android.content.Context
import com.lingsatuo.adapter.UserRvAdapter

object SaveSatate {
    fun setUserInfo(context: Context,user:UserRvAdapter.User){
        val settings = context.getSharedPreferences("userinfo", 0)
        val editor = settings.edit()
        editor.putString("qqnum",user.qqnum)
        editor.apply()
    }
    fun readUserInfo(context: Context):UserRvAdapter.User{
        val settings = context.getSharedPreferences("userinfo", 0)
        val qqnum = settings.getString("qqnum","")
        val user = UserRvAdapter.User()
        user.qqnum = qqnum
        return user
    }
}