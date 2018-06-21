package com.lingsatuo.view;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.lingsatuo.Typeface;
import com.lingsatuo.musiclrc.LrcFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/11/26.
 */

public class KoolLrcView extends View {
    private Context mContext;
    private Paint mPaint;
    private List<LrcLineBean> mLrcAllLineBeanList=new ArrayList<>();
    private int mNowPlayLinePos;
    private float mTextHeight;
    private float mAnimateChangeY;
    private int mViewWidth, mViewHeight;
    private float mTextCenterY;//baseline和文字中线的偏移  0,0时  就是y
    private float mTextLineDis = 20;//行距
    private float mHighLightTextCenterY;//画高亮文字的起始Y
    private float mTextSize = 30;
    private int mNormalTextColor = Color.GRAY;
    private int mHighLightTextColor = Color.GREEN;
    private int mUpOrDownViewCanDrawLines;//

    public KoolLrcView(Context context) {
        this(context, null);
    }

    public KoolLrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KoolLrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mHighLightTextColor = getResources().getColor(R.color.colorAccent);
        mNormalTextColor = getResources().getColor(R.color.button_textColor_Dark);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTypeface(Typeface.INSTANCE.getType(getContext()));


        //获取文字的高度  向上取整
        mTextHeight = (float) Math.ceil(mPaint.descent() - mPaint.ascent());
               /* Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
                float fontSpacing =mPaint.getFontSpacing();
                float textHeight2 = (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);*/

        /**
         * baseline和文字中线的偏移  0,0时  就是y
         */
        mTextCenterY = mTextHeight / 2 - mPaint.descent();


        // handler.post(runnable);//启动歌词滚动
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = resolveSize(widthSize, widthMeasureSpec);
        int height = resolveSize(heightSize, heightMeasureSpec);

        mViewWidth = width;
        mViewHeight = height;


        mHighLightTextCenterY = mViewHeight / 2;

        float temp = mViewHeight - mTextHeight;//减去中间一行
        mUpOrDownViewCanDrawLines = (int) Math.ceil(temp / 2 / (mTextHeight + mTextLineDis));
        mUpOrDownViewCanDrawLines = mUpOrDownViewCanDrawLines + 1;//多画一行
        //
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       /* int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;*/


        canvas.translate(mViewWidth / 2, mHighLightTextCenterY);


        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        /**
         * 参考中线
         */
        //#### canvas.drawLine(-mViewWidth / 2, 0, mViewHeight / 2, 0, mPaint);

        /**
         *
         */
        String testText = "暂无歌词";
        //
        Rect rectOut = new Rect();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.button_textColor_Dark));
        mPaint.getTextBounds(testText, 0, testText.length(), rectOut);
        //
        @SuppressLint("DrawAllocation") Rect rectNew = new Rect(-mViewWidth / 2, (int) (rectOut.top + mTextCenterY),
                mViewWidth / 2, (int) (rectOut.bottom + mTextCenterY));

        /**
         * 参考框
         */
        //### canvas.drawRect(rectNew, mPaint);



        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);

        if (mLrcAllLineBeanList.size()<=0){
            mPaint.setColor(getResources().getColor(R.color.colorAccent));
            canvas.drawText(testText,0, 0 + mTextCenterY,mPaint);
            return;
        }
        //KLog.d("mNowPlayLinePos:"+mNowPlayLinePos);

        for (int i = 0; i < mLrcAllLineBeanList.size(); i++) {
            //
            String textContent = mLrcAllLineBeanList.get(i).getLrc();

            //画  正在播放的行
            if (i == mNowPlayLinePos) {
                mPaint.setColor(mHighLightTextColor);
                canvas.drawText(textContent, 0, 0 + mTextCenterY - mAnimateChangeY, mPaint);

            } else if (i < mNowPlayLinePos) {
                //上面的行
              /*  if ((mNowPlayLinePos - i) > mUpOrDownViewCanDrawLines) {
                    continue;
                }*/

                mPaint.setColor(mNormalTextColor);
                canvas.drawText(textContent, 0,
                        0 - (mTextHeight + mTextLineDis) * (mNowPlayLinePos - i) + mTextCenterY - mAnimateChangeY, mPaint);
            } else {
                //下面的行
              /*  if ((i - mNowPlayLinePos) > mUpOrDownViewCanDrawLines) {
                    continue;
                }*/
                mPaint.setColor(mNormalTextColor);
                canvas.drawText(textContent, 0,
                        0 + (mTextHeight + mTextLineDis) * (i - mNowPlayLinePos) + mTextCenterY - mAnimateChangeY, mPaint);
            }


        }


    }

    //millisecond是定时器传过来的  会调用很多次 值一直在变
    private int mLastSaveTempI = -1;

    public void setTimeMillisecond(int millisecond) {
        //KLog.d("zfq millisecond:" + millisecond);
        long LeftTime;
        long rightTime;
        long duation;
        for (int i = 0; i < mLrcAllLineBeanList.size(); i++) {
            if (i == 0) {
                LeftTime = 0;
                rightTime = mLrcAllLineBeanList.get(i).getEnd();
            } else {
                LeftTime = mLrcAllLineBeanList.get(i).getStart();
                rightTime = mLrcAllLineBeanList.get(i).getEnd();
            }
            //确定是第几句
            if (millisecond >= LeftTime && millisecond < rightTime) {
                // millisecond是定时器传过来的  会调用很多次  所以要处理
                if (mLastSaveTempI != i) {
                    duation = rightTime - LeftTime;
                    nextLine(i+1, duation);
                    ///
                    mLastSaveTempI = i;
                }
                break;
            }
        }
    }

    private ValueAnimator valueAnimator;

    public void nextLine(final int nextLinePos, long duration) {
        if (nextLinePos == mNowPlayLinePos) {
            return;
        }


        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
        }
        /**
         * 播放到最后一行歌词 固定 不需要动画了
         */
        if (mNowPlayLinePos < mLrcAllLineBeanList.size() - 1) {
            /**
             * 向上切换一行动画
             */
            valueAnimator = ValueAnimator.ofFloat(0.0f, mTextHeight + mTextLineDis).setDuration(duration);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 0~mTextHeight + mTextLineDis   0~一行的高度
                    mAnimateChangeY = (float) animation.getAnimatedValue();
                    // KLog.d("mAnimateChangeY：" + mAnimateChangeY);
                    if (mAnimateChangeY == 0) {
                        /* do nothing */
                    } else if (mAnimateChangeY < (mTextHeight + mTextLineDis)) {//逐渐位移1行
                        invalidate();
                    } else if (mAnimateChangeY == (mTextHeight + mTextLineDis)) { //位移1行结束时候
                        mAnimateChangeY = 0;// 当前行停顿 切换下一行 不改变上下Y   重新绘制
                        //下一行
                        mNowPlayLinePos = nextLinePos;
                        //  mNowPlayLinePos++;
                        invalidate();//只改变下一行颜色
                    }
                }
            });
            valueAnimator.start();
        }

    }

    public void setLrcStr(String lrcStr) {
        mLrcAllLineBeanList.clear();
        mNowPlayLinePos = 0;
        mAnimateChangeY = 0;
        LrcFactory.Builder lb = new LrcFactory.Builder("");
        lb.readLine(lrcStr);
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.end();
        }
        mLrcAllLineBeanList.addAll(lb.getRoot());
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float startX, startY;
        int action = event.getAction();
//        KLog.d("action:" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // mHighLightTextCenterY = event.getY();
                // invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(event);
    }
}