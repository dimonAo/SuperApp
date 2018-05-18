package wtwd.com.superapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import wtwd.com.superapp.R;
import wtwd.com.superapp.entity.SweepMapEntity;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class SweeperMap extends View {
    /**
     * 背景格子画笔
     */
    private Paint mBgpaint;


    private Paint mSweeppaint;
    private Paint mSweepDevicepaint;

    ArrayList<SweepMapEntity> list = new ArrayList<>();

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


        mSweeppaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSweeppaint.setStyle(Paint.Style.FILL);

        mSweepDevicepaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSweepDevicepaint.setStyle(Paint.Style.FILL);
        mSweepDevicepaint.setColor(ContextCompat.getColor(getContext(), R.color.blue_sweep_device));
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

        canvas.translate(0, mHeight);

        float lenght = mMin / 110;

        drawSweepRange(canvas, lenght);


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

    private int max;

    private void drawSweepRange(Canvas canvas, float lenght) {
        Log.e("Draw Sweep Range", "drawSweepRange list size : " + list.size());
//        Log.e("Draw Sweep Range", "list size : " + list.toString());

        if (list.size() <= 0) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            SweepMapEntity mEn = list.get(i);
            //判断你是否是障碍碰撞
            if (mEn.isBumper()) {
                mSweeppaint.setColor(ContextCompat.getColor(getContext(), R.color.red_obstacle));
            } else {
                max = i;
                mSweeppaint.setColor(ContextCompat.getColor(getContext(), R.color.sweep_clear));

            }

            canvas.drawRect(mEn.getX() * lenght, -mEn.getY() * lenght, (mEn.getX() + 1) * lenght, (-mEn.getY() + 1) * lenght, mSweeppaint);

//            if (!mEn.isBumper()) {
//                canvas.drawCircle(mEn.getX()*lenght + lenght / 2, -mEn.getY()*lenght + lenght / 2, 2 * lenght, mSweepDevicepaint);
//            }

//            Log.e("Draw Sweep Range", "list size : [" + i + "] : " + mEn.toString());
        }
        canvas.drawCircle(list.get(max).getX() * lenght + lenght / 2, -list.get(max).getY() * lenght + lenght / 2, 2 * lenght, mSweepDevicepaint);


    }

    private void drawSweeper(Canvas canvas) {

    }


    public void setSweepList(ArrayList<SweepMapEntity> lists) {
        Log.e("TAG", "setSweepList : ==> " + lists.size());
        if (lists.isEmpty()) {
            this.list.clear();
        } else {
            this.list.clear();
            this.list.addAll(lists);
            invalidate();
        }
//        postInvalidate();
    }


    public void addSweepList(ArrayList<SweepMapEntity> lists) {
        Log.e("TAG", "addSweepList : " + lists.size());
        if (lists.isEmpty()) {
            return;
        }
        this.list.addAll(lists);
        invalidate();
    }

}
