package com.lingsatuo.app

import android.graphics.Bitmap

class ThemeUtils private constructor(){
    private var PrimaryColor = 0xff353853
    private var AccentColor = 0xffFF4081
    private var background:Bitmap? = null
    private var textColor = 0xffededed

    companion object {
        private var inthis = ThemeUtils()
        fun getInstance():ThemeUtils = inthis
    }
    fun setPrimaryColor(color:Long){
        this.PrimaryColor = color
    }
    fun setAccentColor(color:Long){
        this.AccentColor = color
    }
    fun setBackground(bit:Bitmap){
        this.background = bit
    }
    fun setTextColor(color:Long){
        this.textColor = color
    }
    fun getTextColor() = this.textColor
    fun getBackgroud() = this.background
    fun getPrimaryColor() = this.PrimaryColor
    fun getAccentColor() = this.AccentColor
}