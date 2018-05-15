package wtwd.com.superapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cn.xlink.sdk.v5.model.XLinkDataPoint;
import wtwd.com.superapp.R;
import wtwd.com.superapp.application.SuperApplication;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.entity.Device;
import wtwd.com.superapp.entity.SweepMapEntity;
import wtwd.com.superapp.eventbus.DataPointUpdateEvent;
import wtwd.com.superapp.manager.DeviceManager;
import wtwd.com.superapp.sweepmap.SweepMap;
import wtwd.com.superapp.util.Utils;
import wtwd.com.superapp.widget.SweeperMap;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class SweeperDetailMapActivity extends BaseActivity {

    private SweeperMap sweep_map;
    private SuperApplication mApplicaton;
    private ArrayList<SweepMapEntity> mSweepMapList = new ArrayList<>();
    private Device mDevice;

    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.layout_sweeper_map;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        EventBus.getDefault().register(this);
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);
        mApplicaton = SuperApplication.getAppInstance();

        String mac = getTargetDeviceMacAddress();
        if (null != mac) {
            mDevice = DeviceManager.getInstance().getDevice(mac);
        }

        text_tool_bar_title.setText("工作详情");
        sweep_map = (SweeperMap) findViewById(R.id.sweep_map);

        mSweepMapList.clear();
        mSweepMapList.addAll(mApplicaton.getSweepList());
        sweep_map.setSweepList(mSweepMapList);
//        sweep_map.setSweepList(getData());


    }

    private ArrayList<SweepMapEntity> getData() {
        ArrayList<SweepMapEntity> mListEn = new ArrayList<>();
        for (int i = 0; i < 43; i++) {
            SweepMapEntity mEn = new SweepMapEntity();
            mEn.setX(i + 1);
            mEn.setY(20);
            mEn.setBumper(true);
            mListEn.add(mEn);
        }

        for (int i = 0; i < 42; i++) {
            SweepMapEntity mEn = new SweepMapEntity();
            mEn.setX(20);
            mEn.setY(i);
            mEn.setBumper(false);
            mListEn.add(mEn);
        }
        return mListEn;
    }


    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();


    }

    private String getTargetDeviceMacAddress() {
        if (null == getIntent()) {
            return null;
        }
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                return bundle.getString("device");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataPointUpdateEvent(DataPointUpdateEvent event) {
        for (int i = 0; i < mDevice.getDataPoints().size(); i++) {
            if (11 == mDevice.getDataPoints().get(i).getIndex()) {
                //解析协议

                setMapData(mDevice.getDataPoints().get(i));
//                sweep_map.addSweepList();
            }
        }
    }

    private int maxId;

    private void setMapData(XLinkDataPoint data) {

        byte[] a = (byte[]) data.getValue();

        String hexs = Utils.bytesToHexString(a);

        int hexId = Integer.parseInt(hexs.substring(0, 4), 16);
        if (maxId > hexId) {
            return;
        }
        maxId = hexId;

        int coordinateCount = Integer.parseInt(hexs.substring(4, 6), 16);

        SweepMap mSweepMap = new SweepMap();

        for (int i = 0; i < coordinateCount; i += 14) {
            String mDeviceCoordinate = hexs.substring(6 + i, 6 + ((i + 1) * 14));

            String mDeviceCollision = mDeviceCoordinate.substring(0, 2);
            String mDeviceX = mDeviceCoordinate.substring(2, 6);
            String mDeviceY = mDeviceCoordinate.substring(6, 10);
            String mDeviceDirecton = mDeviceCoordinate.substring(10, 14);

            int collision = Integer.parseInt(mDeviceCollision, 16);
            float x = (float) Integer.parseInt(mDeviceX, 16);
            float y = (float) Integer.parseInt(mDeviceY, 16);
            float direction = (float) Integer.parseInt(mDeviceDirecton, 16);
            ArrayList<SweepMapEntity> list = mSweepMap.getSweepArray(x, y, direction, collision);
            mApplicaton.setSweepList(list);
            sweep_map.addSweepList(list);
        }
    }

}
