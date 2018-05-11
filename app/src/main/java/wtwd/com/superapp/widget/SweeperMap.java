package wtwd.com.superapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import wtwd.com.superapp.R;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class SweeperMap extends View {
    /**
     * 背景格子画笔
     */
    private Paint mBgpaint;

    /**
     * 视图宽高
     */
    private float mWidth, mHeight;


    public SweeperMap(Context context) {
        this(context, null);
    }

    public SweeperMap(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweeperMap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mBgpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgpaint.setStyle(Paint.Style.STROKE);
//        mBgpaint.setDither(true);
        mBgpaint.setStrokeWidth(0);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = getHeight();
        //获取绘制区域
        float mMin = Math.min(mWidth, mHeight);
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.color_f3));
        drawBg(canvas, mMin);


    }

    private void drawBg(Canvas canvas, float mMin) {
        float mWidthPerRect = mMin / 110;
        mBgpaint.setColor(ContextCompat.getColor(getContext(), R.color.color_ee));
//        Rect mRect = new Rect()

        for (int i = 0; i < 110; i++) {
            for (int i1 = 0; i1 < 110; i1++) {
                canvas.drawRect(i * mWidthPerRect, i * mWidthPerRect, (i1 + 1) * mWidthPerRect, (i1 + 1) * mWidthPerRect, mBgpaint);
            }
        }


    }


}
