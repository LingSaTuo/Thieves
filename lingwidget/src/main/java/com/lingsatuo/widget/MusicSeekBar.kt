package com.lingsatuo.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.lingsatuo.view.R

class MusicSeekBar : View {
    private val backgroudPaint = Paint()
    private val sw = 3f
    private val secondaryProgressPaint = Paint()
    private val progressPaint = Paint()
    private val sliderPaint = Paint()
    private var max = 1
    private var sec = 0
    private var smooth = true
    private var progress = 0
    private var listener:OnSeekChangeListener = object : OnSeekChangeListener{
        override fun Befor(musicSeekBar: MusicSeekBar) {
        }

        override fun ChangIng(musicSeekBar: MusicSeekBar) {
        }

        override fun Done(musicSeekBar: MusicSeekBar) {
        }

    }
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {
        backgroudPaint.strokeWidth = sw
        secondaryProgressPaint.strokeWidth = sw
        progressPaint.strokeWidth = sw
        sliderPaint.strokeWidth = sw
        backgroudPaint.color = 0x30ffffff
        secondaryProgressPaint.color = context.resources.getColor(R.color.subbutton_textColor)
        progressPaint.color = context.resources.getColor(R.color.button_textColor_Dark)
        sliderPaint.color = context.resources.getColor(R.color.button_textColor_Dark)
        backgroudPaint.isAntiAlias = true
        progressPaint.isAntiAlias = true
        sliderPaint.isAntiAlias = true
        secondaryProgressPaint.isAntiAlias = true
    }
    fun setColor(background:Int,secondary:Int,progress:Int,slider:Int){
        backgroudPaint.color = background
        secondaryProgressPaint.color = secondary
        progressPaint.color = progress
        sliderPaint.color = slider
    }
    fun setMax(max: Int) {
        this.max = max
    }
    fun setOnSeekBarChangeListener(listener:OnSeekChangeListener){
        this.listener = listener
    }
    fun setSecondaryProgress(sec: Int) {
        this.sec = sec
    }

    fun setProgress(progress: Int) {
        this.progress = progress
    }

    fun getMax() = max
    fun getSecondaryProgress() = sec
    fun getProgress() = progress
    fun setSmooth(sm: Boolean) {
        this.smooth = sm
    }
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        canvas.drawLine(paddingLeft*1f,measuredHeight/2f,paddingLeft+measuredWidth*1f,measuredHeight/2f,backgroudPaint)
        val secprogressWidth = (sec*1f/max*1f)*measuredWidth
        canvas.drawLine(paddingLeft*1f,measuredHeight/2f,paddingLeft+secprogressWidth,measuredHeight/2f,secondaryProgressPaint)
        val progressWidth = (progress*1f/max*1f)*measuredWidth
        canvas.drawLine(paddingLeft*1f,measuredHeight/2f,paddingLeft+progressWidth,measuredHeight/2f,progressPaint)
        val sliderHeight = 24
        canvas.drawRect(paddingLeft+progressWidth,measuredHeight/2f-sliderHeight/2,paddingLeft+progressWidth+sliderHeight/3,measuredHeight/2f+sliderHeight/2,sliderPaint)
        invalidate()
        super.onDraw(canvas)
    }
    interface OnSeekChangeListener{
        fun Befor(musicSeekBar: MusicSeekBar)
        fun ChangIng(musicSeekBar: MusicSeekBar)
        fun Done(musicSeekBar: MusicSeekBar)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)return super.onTouchEvent(event)
        when(event.action){
            MotionEvent.ACTION_DOWN->{
                progress = ((event.x/measuredWidth)*max).toInt()
                listener.Befor(this@MusicSeekBar)
            }
            MotionEvent.ACTION_MOVE->{
                progress = ((event.x/measuredWidth)*max).toInt()
                listener.ChangIng(this@MusicSeekBar)
            }
            MotionEvent.ACTION_UP->{
                progress = ((event.x/measuredWidth)*max).toInt()
                listener.Done(this@MusicSeekBar)
            }
            MotionEvent.ACTION_OUTSIDE->{
                progress = ((event.x/measuredWidth)*max).toInt()
                listener.ChangIng(this@MusicSeekBar)
            }
        }
        return true
    }
}