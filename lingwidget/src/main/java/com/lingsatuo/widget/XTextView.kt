package com.lingsatuo.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.lingsatuo.Typeface

/**
 * Created by Administrator on 2018/3/27.
 */
class XTextView:AppCompatTextView {
    constructor(context:Context):this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0)
    constructor(context: Context,attributeSet: AttributeSet?,def:Int):super(context,attributeSet,def){
        typeface = Typeface.getType(context)
    }
}