package com.lingsatuo.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.View
import java.text.DecimalFormat

class MusicProgressBar : View {
    private val paint = Paint()
    private val textPaint = Paint()
    private val paint2 = Paint()
    private val sw = 10f
    private var max = 1000
    private var progress = 0
    private var player:MediaPlayer?=null
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        paint.color = context.resources.getColor(R.color.colorPrimary)
        paint.isAntiAlias = true
        paint.strokeWidth = 3f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE

        paint2.color = context.resources.getColor(R.color.colorPrimary)
        paint2.isAntiAlias = true
        paint2.strokeWidth = sw
        paint2.isAntiAlias = true
        paint2.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true
        textPaint.color = context.resources.getColor(R.color.button_textColor)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        if (player!=null){
            progress = player!!.currentPosition
            max = player!!.duration
        }
        val df = DecimalFormat("####00")
        val r = max - progress
        val m = (r/1000/60)
        val time = "- $m : ${df.format((r-(m*1000*60))/1000)}"
        val trueh = measuredHeight - paddingTop - paddingBottom
        val truew = measuredWidth - paddingLeft - paddingRight
        var dia = if ( trueh > truew) truew
        else trueh
        if (dia>dip2px(45)){
            dia = dip2px(45)
        }
        textPaint.textSize =dia/8f
        val px = (truew - dia) / 2f
        val py = (trueh - dia) / 2f
        val rectf = RectF(paddingLeft + sw + px, paddingTop + sw + py, paddingLeft + dia - sw + px, paddingTop + dia - sw + py)
        canvas.drawArc(rectf, 0f, 360f, false, paint)
        canvas.drawArc(rectf,-90f,360*(progress*1f/max*1f),false,paint2)
        val tx = textPaint.measureText(time)
        val rect = Rect()
        textPaint.getTextBounds(time,0,time.length,rect)
        val ty = rect.height()
        canvas.drawText(time,measuredWidth/2f - tx/2,(measuredHeight)/2f+ty/3,textPaint)
        invalidate()
        super.onDraw(canvas)
    }

    fun setMax(max: Int){
        this.max = max
    }
    fun setProgress(progress:Int){
        this.progress = progress
    }
   private fun dip2px(int: Int) :Int{
        val scale = resources.displayMetrics.density
        return (int * scale+0.5f).toInt()
    }
    fun setMediaPlayer(player:MediaPlayer){
        this.player = player
    }
}