package wtwd.com.superapp.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;

public class RegisterSuccessActivity extends BaseActivity {
    private ImageView img_register_type;
    private TextView text_congratulation;
    private TextView text_time_count;


    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            text_time_count.setText(String.format(getResources().getString(R.string.register_success), (millisUntilFinished / 1000)));


        }

        @Override
        public void onFinish() {
            readyGo(LoginActivity.class);

        }
    }


    @Override
    public void initToolBar(Toolbar toolbar) {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_register_success;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        text_tool_bar_title.setText("注册");
        TimeCount timeCount = new TimeCount(3000, 1000L);

        img_register_type = (ImageView) findViewById(R.id.img_register_type);
        text_congratulation = (TextView) findViewById(R.id.text_congratulation);
        text_time_count = (TextView) findViewById(R.id.text_time_count);

        if (checkRegisterType()) {
            img_register_type.setBackgroundResource(R.mipmap.phone_register_success);
            text_congratulation.setText("恭喜你,注册成功");
        } else {
            img_register_type.setBackgroundResource(R.mipmap.email_register_success);
            text_congratulation.setText("请到注册邮箱激活账号");
        }

        timeCount.start();


    }

    private boolean checkRegisterType() {
        if (null == getIntent()) {
            return false;
        }

        Bundle bundle = getIntent().getExtras();

        if (null != bundle) {
            return bundle.getBoolean("isPhone");
        }
        return false;
    }


    @Override
    public View getSnackView() {
        return null;
    }
}
