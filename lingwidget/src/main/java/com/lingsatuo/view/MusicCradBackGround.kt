package com.lingsatuo.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class MusicCradBackGround : View {
    private val paint = Paint()
    private val paint2 = Paint()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        paint.isAntiAlias = false
        paint.color = context.resources.getColor(R.color.colorAccent)
        paint.strokeWidth = 4f
        paint2.isAntiAlias = false
        paint2.color = context.resources.getColor(R.color.colorAccent)
        paint2.strokeWidth = 4f
        paint2.style = Paint.Style.FILL_AND_STROKE
        if (canvas == null) return
        val diameter = measuredHeight * (5f / 8f)
        val startX = paddingLeft + diameter / 2
        val startY = paddingTop + (measuredHeight / 2f - diameter/2)
        val endX = startX
        val endY = paddingTop + measuredHeight * 1f
        val rectF = RectF(paddingLeft * 1f + diameter / 6, startY, paddingLeft + diameter, startY + diameter)
        val rectF2 = RectF(paddingLeft * 1f + diameter / 6 - 5, startY, paddingLeft + diameter, startY + diameter + 5)
        paint2.setShadowLayer(10f, -1f, 0f, Color.BLACK)

        canvas.drawLine(startX+1f, paddingTop*1f, startX+1f, paddingTop*1f + measuredHeight, paint2)
        canvas.drawArc(rectF2, 90f, 180f, false, paint2)

        canvas.drawArc(rectF, 90f, 180f, false, paint)
        canvas.drawRect(startX, paddingTop * 1f, paddingLeft + measuredWidth * 1f, paddingTop + measuredHeight * 1f, paint)
        super.onDraw(canvas)
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