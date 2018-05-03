package wtwd.com.superapp.fragment;

import android.bluetooth.BluetoothClass;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import wtwd.com.superapp.R;
import wtwd.com.superapp.activity.ChooseDeviceTypeActivity;
import wtwd.com.superapp.activity.ConfigWifiActivity;
import wtwd.com.superapp.activity.SweeperActivity;
import wtwd.com.superapp.adapter.MainFamilyAdapter;
import wtwd.com.superapp.base.BaseFragment;
import wtwd.com.superapp.entity.DeviceEntity;
import wtwd.com.superapp.util.Utils;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class MainFamilyFragment extends BaseFragment implements View.OnClickListener {

    private static MainFamilyFragment mInstance;
    private RecyclerView recycler_device;
    private LinearLayout lin_add;

    private MainFamilyAdapter mAdapter;
    private List<DeviceEntity> mDeviceEntirys = new ArrayList<>();


    public static MainFamilyFragment getMainFamilyFragment() {
        if (null == mInstance) {
            mInstance = new MainFamilyFragment();
        }
        return mInstance;
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_main_family;
    }


    /**
     * type:
     * 1.灯
     * 2.空调
     * 3.电饭锅
     * 4.扫地机
     * 5.净化器
     * 6.插座
     * 7.冰箱
     * <p>
     * online:
     * 0.离线
     * 1.在线
     */

    private void displayDeviceList() {
        mDeviceEntirys.clear();

        DeviceEntity en = new DeviceEntity();
        en.setDevice_name("扫地boy");
        en.setDevice_type(4);
        en.setOn_line(1);
        en.setPosition_name("客厅");
        mDeviceEntirys.add(en);

//        for (int i = 1; i < 11; i++) {
//            DeviceEntity en = new DeviceEntity();
//            if (8 > i) {
//                en.setDevice_type(i);
//            } else {
//                en.setDevice_type(i - 7);
//            }
//
//            switch (i) {
//                case 1:
//                    en.setDevice_name("小夜灯");
//                    en.setOn_line(0);
//                    en.setPosition_name("卧室");
//                    break;
//
//                case 2:
//                    en.setDevice_name("空调1.5");
//                    en.setOn_line(1);
//                    en.setPosition_name("主卧室");
//                    break;
//
//                case 3:
//                    en.setDevice_name("厨房");
//                    en.setOn_line(0);
//                    en.setPosition_name("九阳电饭锅");
//                    break;
//
//                case 4:
//                    en.setDevice_name("扫地boy");
//                    en.setOn_line(1);
//                    en.setPosition_name("客厅");
//                    break;
//
//                case 5:
//                    en.setDevice_name("净化器");
//                    en.setOn_line(1);
//                    en.setPosition_name("客厅");
//                    break;
//
//                case 6:
//                    en.setDevice_name("三孔插座");
//                    en.setOn_line(0);
//                    en.setPosition_name("书房");
//                    break;
//
//                case 7:
//                    en.setDevice_name("三菱冰箱");
//                    en.setOn_line(1);
//                    en.setPosition_name("厨房");
//                    break;
//
//                case 8:
//                    en.setDevice_name("吊灯");
//                    en.setOn_line(1);
//                    en.setPosition_name("阳台");
//                    break;
//
//                case 9:
//                    en.setDevice_name("空调2.0");
//                    en.setOn_line(0);
//                    en.setPosition_name("客厅");
//                    break;
//
//                case 10:
//                    en.setDevice_name("厨房");
//                    en.setOn_line(1);
//                    en.setPosition_name("美的电饭锅");
//                    break;
//            }
//
//            mDeviceEntirys.add(en);
//        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initFragmentView(View mView) {
//        Utils.setStatusBarColor(getActivity(),R.color.transparent);
//        Utils.setStatusBarColor(getActivity(),R.color.colorWhite);
        Utils.setStatusBarColor(getActivity(), R.color.transparent);
        lin_add = (LinearLayout) mView.findViewById(R.id.lin_add);

        recycler_device = (RecyclerView) mView.findViewById(R.id.recycler_device);
        mAdapter = new MainFamilyAdapter(R.layout.item_main_family_device_display, mDeviceEntirys);

        recycler_device.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        DividerItemDecoration mItem = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        mItem.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.transparent_devider));
        recycler_device.addItemDecoration(mItem);
        recycler_device.setAdapter(mAdapter);

        displayDeviceList();

        addListener();
    }

    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                readyGo(SweeperActivity.class);
            }
        });

        lin_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (R.id.lin_add == v.getId()) {
            readyGo(ChooseDeviceTypeActivity.class);
        }
    }
}
