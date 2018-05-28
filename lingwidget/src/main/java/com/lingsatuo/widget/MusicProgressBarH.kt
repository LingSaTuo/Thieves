package com.lingsatuo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.View
import com.lingsatuo.view.R

class MusicProgressBarH : View {
    private val backgroudPaint = Paint()
    private val sw = 10f
    private val secondaryProgressPaint = Paint()
    private val progressPaint = Paint()
    private var max = 1
    private var sec = 0
    private var smooth = true
    private var progress = 0
    private var mediaPlayer: MediaPlayer? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {
        backgroudPaint.strokeWidth = sw
        secondaryProgressPaint.strokeWidth = sw
        progressPaint.strokeWidth = sw
        backgroudPaint.color = context.resources.getColor(R.color.progressbar_back)
        secondaryProgressPaint.color = context.resources.getColor(R.color.progressbar_sec)
        progressPaint.color = context.resources.getColor(R.color.progressbar_color)
        backgroudPaint.isAntiAlias = true
        progressPaint.isAntiAlias = true
        secondaryProgressPaint.isAntiAlias = true
    }

    fun setMediaPlayer(mediaPlayer: MediaPlayer?) {
        this.mediaPlayer = mediaPlayer
    }

    fun setMax(max: Int) {
        this.max = max
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

    private var sd = 0
    private var pd = 0
    private var sd2 = 0
    private var pd2 = 0
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        if (mediaPlayer != null&&mediaPlayer!!.isPlaying) {
            setMax(mediaPlayer!!.duration)
            setProgress(mediaPlayer!!.currentPosition)
        }
        if (smooth) {
            if (sd - sec>500){
                if (sd2==0){
                    sd2 = ((sd - sec)/100f/sd*max).toInt()
                }
                sd -= sd2
                if (sd<sec)sd=sec
            }else{
                sd = sec
            }
            if (pd - progress > 500){
                if (pd2==0){
                    pd2 = ((pd - progress)/100f/pd*max).toInt()
                }
                pd -= pd2
                if (pd<progress)pd=progress
            }else{
                pd = progress
            }
        }else{
            sd = sec
            pd2 = 0
            sd2 = 0
            pd = progress
        }
        canvas.drawRect(paddingLeft * 1f, paddingTop * 1f, paddingLeft + measuredWidth * 1f, paddingTop + measuredHeight * 1f, backgroudPaint)
        canvas.drawRect(paddingLeft * 1f, paddingTop * 1f, paddingLeft + (measuredWidth * 1f * sd / max), paddingTop + measuredHeight * 1f, secondaryProgressPaint)
        canvas.drawRect(paddingLeft * 1f, paddingTop * 1f, paddingLeft + (measuredWidth * 1f * pd / max), paddingTop + measuredHeight * 1f, progressPaint)
        invalidate()
        super.onDraw(canvas)
    }
}