package wtwd.com.superapp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import wtwd.com.superapp.R;

/**
 * Created by Administrator on 2018/4/3 0003.
 */

public class CircleWhiteView extends View{

    private Paint mOutCirclePaint;
    private Paint mInnerCiclePaint;

    public CircleWhiteView(Context context) {
        this(context,null);
    }

    public CircleWhiteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleWhiteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mOutCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutCirclePaint.setColor(ContextCompat.getColor(context, R.color.colorWhite));

        mInnerCiclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCiclePaint.setColor(ContextCompat.getColor(context,R.color.index_bg));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int mHeight = getHeight();
        int mWidth = getWidth();

        canvas.translate(mWidth/2,mHeight/2);




    }
}
