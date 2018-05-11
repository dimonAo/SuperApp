package wtwd.com.superapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import wtwd.com.superapp.R;


public class RingProgressView extends View {

    private static final String TAG = "RingProgressView";
    public static int[] SWEEP_GRADIENT_COLORS;
    /**
     * 起始实心圆画笔
     */
    private Paint mStartCirclePaint;

    /**
     * 结束圆环画笔
     */
    private Paint mEndCirclePaint;

    /**
     * 细圆环画笔
     */
    private Paint mRingCirclePaint;

    /**
     * 进度条画笔
     */
    private Paint mProgressPaint;

    /**
     * 文字画笔
     */
    private Paint mTextPaint;

    /**
     * 起始实心圆颜色
     */
    private int mStartCircleColor;

    /**
     * 结束圆环颜色
     */
    private int mEndCircleColor;

    /**
     * 细圆环颜色
     */
    private int mRingCircleColor;

    /**
     * 进度条颜色
     */
    private int mProgressColor;

    /**
     * 进度条宽度
     */
    private int mProgressWidth;

    /**
     * 顶部文字
     */
    private String mTopText;

    /***
     * 底部文字
     */
    private String mBottomUnitText;

    /**
     * 文字颜色
     */
    private int mTextColor;

    /**
     * 圆环圆心X轴坐标
     */
    private int mCenterX;

    /**
     * 圆环圆心Y轴坐标
     */
    private int mCenterY;

    /**
     * 圆环半径
     */
    private int mRadius;

    /**
     * 目标数量
     */
    private int mTargetNum;

    /**
     * 已完成数量
     */
    private int mCurrentNum;

    /**
     * 已经绘制到达的数
     */
    private int mProgress;

    private boolean mDisplayPercentageSymbol = false;

    @SuppressLint("HandlerLeak")
    private Handler circleHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int temp = (Integer) msg.obj;
                Log.e(TAG, "temp : " + temp);
                setProgress(temp);
            }
        }
    };

    public RingProgressView(Context context) {
        this(context, null);
    }

    public RingProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initArray(context, attrs, defStyleAttr);
    }

    private void initArray(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RingProgressView, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.RingProgressView_start_circle_color:
                    mStartCircleColor = a.getColor(attr, ContextCompat.getColor(context, R.color.colorWhite));
                    break;

                case R.styleable.RingProgressView_end_circle_color:
                    mEndCircleColor = a.getColor(attr, ContextCompat.getColor(context, R.color.colorWhite));
                    break;

                case R.styleable.RingProgressView_ring_color:
                    mRingCircleColor = a.getColor(attr, ContextCompat.getColor(context, R.color.color_66));
                    break;

                case R.styleable.RingProgressView_progress_color:
                    mProgressColor = a.getColor(attr, ContextCompat.getColor(context, R.color.colorWhite));
                    break;

                case R.styleable.RingProgressView_text_color:
                    mTextColor = a.getColor(attr, ContextCompat.getColor(context, R.color.colorWhite));
                    break;

                case R.styleable.RingProgressView_progress_width:
                    mProgressWidth = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RingProgressView_top_text:
                    mTopText = a.getString(attr);
                    break;
                case R.styleable.RingProgressView_bottom_unit_text:
                    mBottomUnitText = a.getString(attr);
                    break;
            }
        }
        initPaint();

    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //将圆心移到canvas中心
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        canvas.translate(mCenterX, mCenterY);

        //计算圆环半径
        mRadius = (int) (Math.min(getWidth() / 2, getHeight() / 2) * 0.9);
        if (20 < (mRadius * 0.1)) {
            mProgressWidth = (int) (mRadius * 0.1);
        } else {
            mProgressWidth = 20;
        }

        drawRingCircle(canvas);

        drawProgressArc(canvas);

        drawText(canvas);

    }

    /**
     * 画细圆环
     *
     * @param canvas
     */
    private void drawRingCircle(Canvas canvas) {
        mRingCirclePaint.setStrokeWidth(mProgressWidth);
        canvas.drawCircle(0, 0, mRadius, mRingCirclePaint);
    }


    /**
     * 画起始点圆
     *
     * @param canvas
     */
    private void drawStartCircle(Canvas canvas) {
        canvas.drawCircle(0, -mRadius, mProgressWidth / 2, mStartCirclePaint);
    }

    /**
     * 画进度圆弧
     */
    private void drawProgressArc(Canvas canvas) {
        mProgressPaint.setStrokeWidth(mProgressWidth);

        Shader mColorShader = new SweepGradient(0, 0, SWEEP_GRADIENT_COLORS, null);
//        Matrix matrix = new Matrix();
//        matrix.postRotate(0.5f);
//        mColorShader.setLocalMatrix(matrix);
        mProgressPaint.setShader(mColorShader);
        RectF mArc = new RectF();
        mArc.top = -mRadius;
        mArc.bottom = mRadius;
        mArc.left = -mRadius;
        mArc.right = mRadius;
        canvas.drawArc(mArc, -90, (((mProgress * 1f) / (mTargetNum * 1f)) * 360), false, mProgressPaint);

    }

    /***
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        //头部文字
        RectF mTopTextF = new RectF();
        mTopTextF.top = (float) (-mRadius * (0.67));
        mTopTextF.bottom = (float) (-mRadius * (0.45));
        mTextPaint.setTextSize((float) ((-mRadius * (0.45)) - (-mRadius * (0.67))));
        canvas.drawText(mTopText, -(mTextPaint.measureText(mTopText) / 2), mTopTextF.bottom, mTextPaint);

        //中间数字
        RectF mMiddleTextF = new RectF();
        mMiddleTextF.bottom = (float) (mRadius * (0.3));
        mTextPaint.setTextSize((float) (mRadius * (0.3) - (-(mRadius * (0.3)))));
        canvas.drawText(mProgress + "%", -(mTextPaint.measureText(mProgress + "%") / 2), mMiddleTextF.bottom, mTextPaint);

        //中间数字后面的单位
        if (mDisplayPercentageSymbol) {
            mTextPaint.setTextSize((float) (mRadius * (0.2)));
            canvas.drawText("%", (mTextPaint.measureText(mTopText) / 2) + 5, mMiddleTextF.bottom, mTextPaint);
        }


        //底部单位文字
        RectF mBottomTextF = new RectF();
//        mBottomTextF.top = (float) (-mRadius * (0.67));
        mBottomTextF.bottom = (float) (mRadius * (0.6));
        mTextPaint.setTextSize((float) ((mRadius * (0.67)) - (mRadius * (0.45))));
        canvas.drawText(mBottomUnitText, -(mTextPaint.measureText(mBottomUnitText) / 2), mBottomTextF.bottom, mTextPaint);

    }


    /**
     * 测量
     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//
//    }


    /**
     * 初始化画笔
     */
    private void initPaint() {

        SWEEP_GRADIENT_COLORS = new int[]{ContextCompat.getColor(getContext(), R.color.blue_btn_start_color),
                ContextCompat.getColor(getContext(), R.color.blue_btn_end_color)};

        mStartCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStartCirclePaint.setColor(mStartCircleColor);
        mStartCirclePaint.setStyle(Paint.Style.FILL);

        mEndCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEndCirclePaint.setColor(mEndCircleColor);
        mEndCirclePaint.setStyle(Paint.Style.STROKE);
        mEndCirclePaint.setStrokeWidth(mProgressWidth / 2);

        mRingCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingCirclePaint.setColor(mRingCircleColor);
        mRingCirclePaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);


    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
//        invalidate();
        Log.e(TAG, "this.mProgress : " + this.mProgress);
        postInvalidate();
    }

    public void setCurrentNumAndTargetNum(int mCurrentNum, int mTargetNum) {
        this.mCurrentNum = mCurrentNum;
        this.mTargetNum = mTargetNum;
        new Thread(new CircleThread()).start();
    }

    public void setCurrentNumAndTargetNum() {
        new Thread(new CircleThread()).start();
    }


    public void setDisplayPercentageSymbol(boolean mDisplayPercentageSymbol) {
        this.mDisplayPercentageSymbol = mDisplayPercentageSymbol;
        invalidate();
    }

    public void setBottomUnitText(String mBottomUnitText) {
        this.mBottomUnitText = mBottomUnitText;
        invalidate();
    }

    private class CircleThread implements Runnable {

        int m = 0;
        int i = 0;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
//                    m++;
                    Message msg = new Message();
                    msg.what = 1;
                    i++;

                    if (mProgress >= 100) {
                        return;
                    }


                    if (i < (((mCurrentNum * 1f) / (mTargetNum * 1f)) * 100f)) {
                        mProgress = (int) (i * mTargetNum / 100f);
                    } else {
                        mProgress = mCurrentNum;

                    }
                    msg.obj = mProgress;
                    circleHandler.sendMessage(msg);

//                    if ((i >= (((mCurrentNum * 1f) / (mTargetNum * 1f)) * 100f)) || (mProgress == mCurrentNum)) {
////                        Log.e(TAG, "return i : " + i);
////                        Log.e(TAG, "return mProgress : " + mProgress);
//                        return;
//                    }
//                    mProgress++;

//                    msg.obj = mProgress;
//                    circleHandler.sendMessage(msg);
//                    if (mProgress >= 100) {
//                        return;
//                    }


                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    Thread.currentThread().interrupt();
                }
            }
        }

    }


}
