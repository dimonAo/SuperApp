package wtwd.com.superapp.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import wtwd.com.superapp.R;
import wtwd.com.superapp.adapter.AddDeviceAdapter;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.entity.DeviceEntity;

public class ChooseDeviceTypeActivity extends BaseActivity {
    private RecyclerView recycler_view_devices;
    private AddDeviceAdapter mAddAdapter;

    @Override
    public void initToolBar(Toolbar toolbar) {
        text_tool_bar_title.setText("选择设备类型");
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_choose_device_type;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);
        initView();

    }

    private void initView() {
        recycler_view_devices = (RecyclerView) findViewById(R.id.recycler_view_devices);

        mAddAdapter = new AddDeviceAdapter(R.layout.item_local_device, null);
        recycler_view_devices.setLayoutManager(new GridLayoutManager(this, 3));
        DividerItemDecoration mItem = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        mItem.setDrawable(ContextCompat.getDrawable(this, R.drawable.transparent_devider));
        recycler_view_devices.addItemDecoration(mItem);
        recycler_view_devices.setAdapter(mAddAdapter);

        mAddAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (0 == position) {
                    readyGo(ConfigWifiActivity.class);
                }
            }
        });

        //获取设备类型数据
        getData();
    }

    private void getData() {
        DeviceEntity mEn = new DeviceEntity();
        mEn.setDevice_type(1);
        mEn.setDevice_name("扫地机");

        mAddAdapter.getData().add(mEn);
        mAddAdapter.notifyDataSetChanged();
    }


    @Override
    public View getSnackView() {
        return null;
    }
}
