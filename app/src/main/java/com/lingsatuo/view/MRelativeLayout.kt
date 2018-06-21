package com.lingsatuo.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.RelativeLayout

class MRelativeLayout : RelativeLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        val windowsmanageer = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val height = windowsmanageer.defaultDisplay.height
        when(event.action){
            MotionEvent.ACTION_OUTSIDE->{
                layoutParams.height = (height - event.rawY).toInt()
            }
            MotionEvent.ACTION_MOVE->{
                layoutParams.height = (height - event.rawY).toInt()
            }
        }
        requestLayout()
        return true
    }
}