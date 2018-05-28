package com.lingsatuo

import android.content.Context
import android.graphics.Typeface

/**
 * Created by Administrator on 2018/3/27.
 */
object Typeface {
    private var face:Typeface?=null
    private val CANARO_EXTRA_BOLD_PATH = "fonts/RobotoCondensed-Regular.ttf"
    fun getType(context: Context):Typeface{
        if (face==null){
            face = Typeface.createFromAsset(context.getAssets(), CANARO_EXTRA_BOLD_PATH)
        }
        return face!!
    }
}