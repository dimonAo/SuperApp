package wtwd.com.superapp.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;

public class AppointCleanSetActivity extends BaseActivity {

    private String[] weekday;

    private RecyclerView recycler_repeat_weekday;


    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_appoint_clean_set;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        weekday = getResources().getStringArray(R.array.weekday);
        text_tool_bar_title.setText("定时");

        recycler_repeat_weekday = (RecyclerView) findViewById(R.id.recycler_repeat_weekday);

    }

    @Override
    public View getSnackView() {
        return null;
    }
}
