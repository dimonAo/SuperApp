package wtwd.com.superapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;

public class AddSweeperAlarmActivity extends BaseActivity {


    @Override
    public void initToolBar(Toolbar toolbar) {
        text_tool_bar_title.setText("添加设备");
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_add_sweeper_alarm;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {

        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(ConfigWifiActivity.class);
            }
        });

    }

    @Override
    public View getSnackView() {
        return null;
    }
}