package com.lingsatuo.fragment

import android.app.Activity
import java.io.Serializable


class MBundle(private var title:String,private var type:Int) : Serializable {
    fun getTitle() = title
    fun getType() = type
}