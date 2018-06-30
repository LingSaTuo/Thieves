package com.lingsatuo.utils

import android.content.Context
import com.lingsatuo.adapter.UserRvAdapter
import com.lingsatuo.app.BaseActivity

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
    fun save(activity: BaseActivity,tagString: String,value:String){
        val settings = activity.getSharedPreferences("tagString", 0)
        val editor = settings.edit()
        editor.putString(tagString,value)
        editor.apply()
    }
    fun read(activity: BaseActivity,tagString: String):String{
        val settings = activity.getSharedPreferences("tagString", 0)
        val value = settings.getString(tagString,"")
        return value
    }
}