package wtwd.com.superapp.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.internal.Util;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.util.Utils;
import wtwd.com.superapp.widget.wheelpicker.WheelPicker;

public class AppointCleanSetActivity extends BaseActivity {

    private String[] weekday;
    private List<String> mHourLists = new ArrayList<>();
    private List<String> mMinunteLists = new ArrayList<>();
    private List<CleanRepeatEntity> mRepeatWeekday = new ArrayList<>();

    private RecyclerView recycler_repeat_weekday;
    private WheelPicker wheel_picker_hour;
    private WheelPicker wheel_picker_minute;
    private CleanRepeatAdapter mCleanRepeatAdapter;

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
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);
        recycler_repeat_weekday = (RecyclerView) findViewById(R.id.recycler_repeat_weekday);
        wheel_picker_hour = (WheelPicker) findViewById(R.id.wheel_picker_hour);
        wheel_picker_minute = (WheelPicker) findViewById(R.id.wheel_picker_minute);

        mCleanRepeatAdapter = new CleanRepeatAdapter(R.layout.item_repeat);
        recycler_repeat_weekday.setLayoutManager(new GridLayoutManager(this, 4));
        DividerItemDecoration mItem = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        mItem.setDrawable(ContextCompat.getDrawable(this, R.drawable.transparent_devider));
        recycler_repeat_weekday.addItemDecoration(mItem);

        recycler_repeat_weekday.setAdapter(mCleanRepeatAdapter);

        getTimeData();
        addListener();
    }

    private void addListener() {
        mCleanRepeatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean isCheck = mCleanRepeatAdapter.getData().get(position).isChecked();


                mCleanRepeatAdapter.getData().get(position).setChecked(!isCheck);


                mCleanRepeatAdapter.notifyItemChanged(position);


            }
        });
    }


    @Override
    public View getSnackView() {
        return null;
    }


    private void getTimeData() {
        mRepeatWeekday.clear();

        for (int i = 0; i < 24; i++) {
            mHourLists.add(Utils.addZeroBeforSingleString(i + ""));
        }

        for (int i = 0; i < 60; i++) {
            mMinunteLists.add(Utils.addZeroBeforSingleString(i + ""));
        }

        for (String aWeekday : weekday) {
            CleanRepeatEntity mEn = new CleanRepeatEntity();
            mEn.setChecked(false);
            mEn.setRepeatWeek(aWeekday);
            mRepeatWeekday.add(mEn);
        }

        wheel_picker_hour.setData(mHourLists);
        wheel_picker_minute.setData(mMinunteLists);

        mCleanRepeatAdapter.getData().addAll(mRepeatWeekday);
        mCleanRepeatAdapter.notifyDataSetChanged();

    }


    private class CleanRepeatAdapter extends BaseQuickAdapter<CleanRepeatEntity, BaseViewHolder> {

        public CleanRepeatAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, CleanRepeatEntity item) {
            if (item.isChecked()) {
                helper.getView(R.id.lin_week_bg).setSelected(true);
                helper.setText(R.id.text_repeat_week, item.getRepeatWeek())
                        .setTextColor(R.id.text_repeat_week, ContextCompat.getColor(AppointCleanSetActivity.this, R.color.colorWhite));
            } else {
                helper.getView(R.id.lin_week_bg).setSelected(false);
                helper.setText(R.id.text_repeat_week, item.getRepeatWeek())
                        .setTextColor(R.id.text_repeat_week, ContextCompat.getColor(AppointCleanSetActivity.this, R.color.color_33));
            }


        }
    }


    private class CleanRepeatEntity {
        private String repeatWeek;
        private boolean checked;

        public String getRepeatWeek() {
            return repeatWeek;
        }

        public void setRepeatWeek(String repeatWeek) {
            this.repeatWeek = repeatWeek;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }


}
