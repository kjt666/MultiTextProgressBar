package com.wowo.kjt.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

public class MultiTextProgressBar extends ProgressBar {

    private final int PROGRESSBAR_DEFAULT_HEIGHT = dp2px(10);//px
    private final int PROGRESSBAR_DEFAULT_TEXT_SIZE = dp2px(14);//px
    private final int PROGRESSBAR_DEFAULT_REACH_COLOR = 0xfff456c5;
    private final int PROGRESSBAR_DEFAULT_UNREACH_COLOR = 0xFFF2F2F2;
    private final int PROGRESSBAR_DEFAULT_REACH_UNREACH_TEXT_COLOR = Color.GRAY;
    private final int PROGRESSBAR_DEFAULT_PROGRESS_TEXT_COLOR = Color.WHITE;
    private final int PROGRESSBAR_DEFAULT_PROGRESS_LEFT_TEXT_COLOR = Color.GRAY;

    private int mProgressbarHeight;
    private int mReachColor;
    private int mUnReachColor;
    private int mRealWidth;
    private int mTextSize;
    private int mReachUnreachTextColor;
    private int mProgressTextColor;
    private int mLeftTextColor;

    private String mLeftText = "";
    private String mProgressText = "";
    private String mReachText = "";
    private String mUnReachText = "";
    private boolean mHasReachOrUnReachText = false;
    private float mLeftTextWidth;

    private Paint mPaint;
    private Bitmap mDstBmp, mSrcBmp;
    private Canvas mSrcCanvas;
    float progressX;

    public MultiTextProgressBar(Context context) {
        this(context, null);
    }

    public MultiTextProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultiTextProgressBar);
        mProgressbarHeight = (int) ta.getDimension(R.styleable.MultiTextProgressBar_progressbar_height, PROGRESSBAR_DEFAULT_HEIGHT);
        mReachColor = ta.getColor(R.styleable.MultiTextProgressBar_reach_color, PROGRESSBAR_DEFAULT_REACH_COLOR);
        mUnReachColor = ta.getColor(R.styleable.MultiTextProgressBar_unreach_color, PROGRESSBAR_DEFAULT_UNREACH_COLOR);
        mTextSize = (int) ta.getDimension(R.styleable.MultiTextProgressBar_text_size, PROGRESSBAR_DEFAULT_TEXT_SIZE);
        mProgressText = ta.getString(R.styleable.MultiTextProgressBar_progress_text);
        mLeftText = ta.getString(R.styleable.MultiTextProgressBar_left_text);
        mReachText = ta.getString(R.styleable.MultiTextProgressBar_reach_text);
        mUnReachText = ta.getString(R.styleable.MultiTextProgressBar_unreach_text);
        mReachUnreachTextColor = ta.getColor(R.styleable.MultiTextProgressBar_reach_unreach_text_color, PROGRESSBAR_DEFAULT_REACH_UNREACH_TEXT_COLOR);
        mProgressTextColor = ta.getColor(R.styleable.MultiTextProgressBar_progress_text_color,PROGRESSBAR_DEFAULT_PROGRESS_TEXT_COLOR);
        mLeftTextColor = ta.getColor(R.styleable.MultiTextProgressBar_left_text_color,PROGRESSBAR_DEFAULT_PROGRESS_LEFT_TEXT_COLOR);
        if (!TextUtils.isEmpty(mReachText) || !TextUtils.isEmpty(mUnReachText))
            mHasReachOrUnReachText = true;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mProgressbarHeight);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int heightVal = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal, heightVal);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mDstBmp = getDstPic();//完成测量后再创建图片\
        mSrcBmp = Bitmap.createBitmap(getMeasuredWidth(), mProgressbarHeight, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!TextUtils.isEmpty(mLeftText)) {//左侧文字改变时，目标图像也要实时生成
            mPaint.setTextSize(mTextSize);
            mLeftTextWidth = mPaint.measureText(mLeftText) + mProgressbarHeight / 2;//加半个progressBar高度
            mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - (int) mLeftTextWidth;//如果progresbar左侧有文字，progressbar的长度也要减去
            mDstBmp = getDstPic();
        }
        progressX = mRealWidth * getProgress() / 100;
        mSrcBmp = getSrcPic(progressX);
        int layer = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        if (mHasReachOrUnReachText)
            canvas.translate(getPaddingLeft(), (getMeasuredHeight() - mProgressbarHeight) / 3); //这个高度上的位移让我想了很久。画图和普通的画线不一样。画图是从图片的（0,0）点开始画的，画线是从（0，线高度的一半）开始画的。
        else
            canvas.translate(getPaddingLeft(), getPaddingTop());
        if (!TextUtils.isEmpty(mLeftText)) {
            mPaint.setColor(mLeftTextColor);
            float y = -(mPaint.descent() + mPaint.ascent()) / 2;
            canvas.drawText(mLeftText, 0, (mProgressbarHeight / 2) + y, mPaint);
        }
        canvas.drawBitmap(mDstBmp, mLeftTextWidth, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(mSrcBmp, mLeftTextWidth, 0, mPaint);
        mPaint.setXfermode(null);
        if (!TextUtils.isEmpty(mProgressText)) {
            mPaint.setColor(mProgressTextColor);
            mPaint.setTextSize(mTextSize);
            float y = -(mPaint.descent() + mPaint.ascent()) / 2;
            canvas.drawText(mProgressText, mProgressbarHeight / 2 + mLeftTextWidth, (mProgressbarHeight / 2) + y, mPaint);
        }
        if (!TextUtils.isEmpty(mReachText)) {
            mPaint.setTextSize(sp2px(10));
            mPaint.setColor(mReachUnreachTextColor);
            canvas.drawText(mReachText, mProgressbarHeight / 2 + mLeftTextWidth, mProgressbarHeight / 2 * 3, mPaint);
        }
        if (!TextUtils.isEmpty(mUnReachText)) {
            mPaint.setTextSize(sp2px(10));
            mPaint.setColor(mReachUnreachTextColor);
            float measureText = mPaint.measureText(mUnReachText);
            canvas.drawText(mUnReachText, getMeasuredWidth() - measureText, mProgressbarHeight / 2 * 3, mPaint);
        }
        canvas.restoreToCount(layer);
    }

    /**
     * 测量控件高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = height;
        } else {
            if (mHasReachOrUnReachText)
                result = getPaddingTop() + getPaddingBottom() + mProgressbarHeight * 2;
            else
                result = getPaddingTop() + getPaddingBottom() + mProgressbarHeight;
        }
        return result;
    }

    /**
     * 获取源图像，因为使用次数较多，所以将一些资源抽了出来，避免频繁gc操作，耗费内存
     *
     * @param progress
     * @return
     */
    private Bitmap getSrcPic(float progress) {
        mSrcCanvas = new Canvas(mSrcBmp);
        mPaint.setColor(mReachColor);
        //canvas.drawLine(mProgressbarHeight, mProgressbarHeight / 2, progress - mProgressbarHeight, mProgressbarHeight / 2, paint);
        if (getProgress() == 0)
            mPaint.setStrokeCap(Paint.Cap.BUTT);
        else
            mPaint.setStrokeCap(Paint.Cap.ROUND);
        if (getProgress() <= 50) {
            mSrcCanvas.drawLine(0, mProgressbarHeight / 2, progress, mProgressbarHeight / 2, mPaint);
        } else {
            mSrcCanvas.drawLine(0, mProgressbarHeight / 2, progress - mProgressbarHeight / 2, mProgressbarHeight / 2, mPaint);
        }
//        mSrcCanvas.drawRect(new RectF(0, 0, mProgressbarHeight/2, mProgressbarHeight), mPaint);
        return mSrcBmp;
    }

    /**
     * 获取目标图层图像，初始一次就行了。用临时变量
     *
     * @return
     */
    private Bitmap getDstPic() {
        //创建画布，不用mRealWidth的原因是加入给控件设定10dp宽度，在设定10dp的pading，那么这个mReaalWidth则为负数，创建bitmap会报错，同理，高度我们也是限定了的
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth() - (int) mLeftTextWidth, mProgressbarHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mPaint.setColor(mUnReachColor);
        //因为线帽是线段两头的样式，是多出来的一部分，如果不设置线帽从0开始就可以，线帽长度为线的高度的一半。
        canvas.drawLine(mProgressbarHeight / 2, mProgressbarHeight / 2, mRealWidth - mProgressbarHeight / 2, mProgressbarHeight / 2, mPaint);
        canvas.drawRect(new RectF(0, 0, mProgressbarHeight / 2, mProgressbarHeight), mPaint);
        return bitmap;
    }

    public void setProgressBarText(String progressBarText) {
        mProgressText = progressBarText;
        invalidate();
    }

    public void setReachText(String reachText) {
        mReachText = reachText;
        invalidate();
    }

    public void setUnReachText(String unReachText) {
        mUnReachText = unReachText;
        invalidate();
    }

    public void setLeftText(String leftText) {
        mLeftText = leftText;
        invalidate();
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
