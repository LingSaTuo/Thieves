package com.lingsatuo.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.lingsatuo.musiclrc.LrcFactory


class MusicLrcView : View {
    private val lrclist = ArrayList<LrcLineBean>()//歌词集合
    private var time = 0L//当前时间
    private var lrcLineBean: LrcLineBean? = null//当前应该高亮的
    private var indexline: LrcLineBean? = null//当前应该高亮的
    private val nowLinePaint = Paint()
    private val defPaint = Paint()
    private var lineHeight = 0
    private var textsize = 40f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {
        nowLinePaint.color = context.resources.getColor(R.color.colorAccent)
        defPaint.color = context.resources.getColor(R.color.button_textColor_Dark)
        nowLinePaint.strokeWidth = 15f
        nowLinePaint.textSize = textsize
        nowLinePaint.isAntiAlias = true
        defPaint.strokeWidth = 15f
        defPaint.textSize = textsize
        defPaint.isAntiAlias = true

        defPaint.textAlign = Paint.Align.CENTER
        defPaint.style = Paint.Style.FILL

        nowLinePaint.textAlign = Paint.Align.CENTER
        nowLinePaint.style = Paint.Style.FILL
        val rect = Rect()
        nowLinePaint.getTextBounds("S", 0, 1, rect)
        lineHeight = rect.height()
    }
    fun setTextSize(size:Float){
        this.textsize = size
        defPaint.textSize = textsize
        nowLinePaint.textSize = textsize
    }
    fun setColor(colorDef:Int,heigh:Int){
        nowLinePaint.color = heigh
        defPaint.color = colorDef
    }
    fun setCurrTime(time: Long) {
        this.time = time
        for (lrc in lrclist) {
            if (lrc.start <= time && time < lrc.end) {
                lrcLineBean = lrc
                invalidate()
                break
            }
            if (lrc == lrclist[lrclist.size-1]){
                lrcLineBean = lrc
                invalidate()
                break
            }
            lrcLineBean = null
        }
    }

    fun setLrc(string: String) {
        lrclist.clear()
        val lrc = LrcFactory.Builder("")
        lrc.readLine(string)
        lrclist.addAll(lrc.getRoot())
        lrcLineBean = null
        indexline = null
        scrollY = 0
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        var height = paddingTop * 1f
        for (lrc in lrclist) {
            height += lineHeight*2
            if (lrc == lrcLineBean) {
                if (lrc != indexline) {
                    val y = height.toInt() - lineHeight
                    scroll(y - measuredHeight / 2)
                    indexline = lrc
                }
                canvas.drawText(lrc.lrc, 0f + paddingLeft + measuredWidth / 2, height, nowLinePaint)
            } else {
                canvas.drawText(lrc.lrc, 0f + paddingLeft + measuredWidth / 2, height, defPaint)
            }
        }
    }

    private var ani: ValueAnimator? = null
    private fun scroll(y: Int) {
        if (ani?.isRunning == true) ani?.end()
        val now = y - lineHeight
        ani = ValueAnimator.ofFloat(lineHeight*2f).setDuration(500)
        ani!!.addUpdateListener { an ->
            if (an.animatedValue as Float != 0f)
                scrollY = (now + (an.animatedValue as Float)).toInt()
        }
        ani!!.start()
    }
}