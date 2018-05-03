package wtwd.com.superapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;

public class JoinWifiFailedActivity extends BaseActivity {
    private Button btn_add_fail;

    @Override
    public void initToolBar(Toolbar toolbar) {
        text_tool_bar_title.setText("添加失败");
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_join_wifi_failed;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);

        btn_add_fail = (Button) findViewById(R.id.btn_add_fail);
        btn_add_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public View getSnackView() {
        return null;
    }
}
