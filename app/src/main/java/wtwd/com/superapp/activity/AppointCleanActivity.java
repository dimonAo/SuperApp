package wtwd.com.superapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;

public class AppointCleanActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recycler_scheduled_task;
    private TextView text_no_task;


    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_appoint_clean;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        setTitleToolbarStyle(SOLID_COLOR_TITLE,R.color.colorWhite);
        text_tool_bar_title.setText("定时列表");
        img_tool_bar_right.setBackgroundResource(R.mipmap.sweep_add_alarm);

        recycler_scheduled_task = (RecyclerView) findViewById(R.id.recycler_scheduled_task);
        text_no_task = (TextView) findViewById(R.id.text_no_task);

        addListener();

    }

    private void addListener() {
        img_tool_bar_right.setOnClickListener(this);
    }


    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (R.id.img_tool_bar_right == v.getId()) {
            readyGo(AppointCleanSetActivity.class);
        }


    }
}
