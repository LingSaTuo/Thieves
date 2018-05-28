package com.lingsatuo.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.lingsatuo.Typeface

class MusicWaveForm :View{
    private val paint:Paint = Paint()
    constructor(context: Context):this(context,null)
    constructor(context: Context, attributeSet: AttributeSet?):this(context,attributeSet,0)
    constructor(context: Context, attributeSet: AttributeSet?, def:Int):super(context,attributeSet,def){

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var dw = 0
        var dh = 0
        dw += paddingLeft + paddingRight
        dh += paddingTop + paddingBottom
        val measuredWidth = View.resolveSizeAndState(dw, widthMeasureSpec, 0)
        val measuredHeight = View.resolveSizeAndState(dh, heightMeasureSpec, 0)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

}