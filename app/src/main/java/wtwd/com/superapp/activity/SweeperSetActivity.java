package wtwd.com.superapp.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.xlink.sdk.core.model.DataPointValueType;
import cn.xlink.sdk.v5.listener.XLinkTaskListener;
import cn.xlink.sdk.v5.model.XDevice;
import cn.xlink.sdk.v5.model.XLinkDataPoint;
import cn.xlink.sdk.v5.module.datapoint.XLinkSetDataPointTask;
import cn.xlink.sdk.v5.module.http.XLinkRemoveDeviceTask;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import cn.xlink.sdk.v5.module.main.XLinkSDK;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.entity.Device;
import wtwd.com.superapp.manager.DeviceManager;
import wtwd.com.superapp.util.DialogUtils;

public class SweeperSetActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SweeperSetActivity";

    private TextView text_seepage;
    private TextView text_device_id;
    private Button btn_delete_device;
    private Device mDevice;
    private RelativeLayout relative_position_sweeper;
    private RelativeLayout relative_appointment_clean;
    private RelativeLayout relative_seepage;

    private String[] sweepSeepageLevel;
    private Dialog mDialog;

    @Override
    public void initToolBar(Toolbar toolbar) {
        text_tool_bar_title.setText("设备管理");
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_sweeper_set;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        sweepSeepageLevel = getResources().getStringArray(R.array.seepage_level);
        mDialog = new Dialog(this, R.style.TextDialog);
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);
        text_device_id = (TextView) findViewById(R.id.text_device_id);
        btn_delete_device = (Button) findViewById(R.id.btn_delete_device);
        text_seepage = (TextView) findViewById(R.id.text_seepage);
        relative_position_sweeper = (RelativeLayout) findViewById(R.id.relative_position_sweeper);
        relative_appointment_clean = (RelativeLayout) findViewById(R.id.relative_appointment_clean);
        relative_seepage = (RelativeLayout) findViewById(R.id.relative_seepage);
        addListener();

        String mac = getTargetDeviceMacAddress();
        if (null != mac) {
            mDevice = DeviceManager.getInstance().getDevice(mac);
            text_device_id.setText(mDevice.getXDevice().getDeviceId() + "");
        }


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

    private void addListener() {
        btn_delete_device.setOnClickListener(this);
        relative_position_sweeper.setOnClickListener(this);
        relative_appointment_clean.setOnClickListener(this);
        relative_seepage.setOnClickListener(this);
    }


    @Override
    public View getSnackView() {
        return tool_bar;
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_delete_device == v.getId()) {
            doRemoveDevice();
        } else if (R.id.relative_position_sweeper == v.getId()) {
            //寻找扫地机
            setDataPoint(9, DataPointValueType.BYTE, (byte) 1);
        } else if (R.id.relative_appointment_clean == v.getId()) {
            readyGo(AppointCleanActivity.class);
        } else if (R.id.relative_seepage == v.getId()) {

            final List<String> mList = new ArrayList<>();
            mList.addAll(Arrays.asList(sweepSeepageLevel));

            DialogUtils.showSeepageDialog(SweeperSetActivity.this, mDialog, mList, new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    text_seepage.setText(mList.get(position));

                    int index = 7;
                    byte value = (byte) (position + 1);
                    setDataPoint(index, DataPointValueType.BYTE, value);
                    mDialog.dismiss();
                }
            });

        }
    }

    /**
     * 删除设备
     */
    public void doRemoveDevice() {
        XLinkRemoveDeviceTask removeDeviceTask = XLinkRemoveDeviceTask.newBuilder()
                .setXDevice(mDevice.getXDevice())
                .setListener(new XLinkTaskListener<String>() {
                    @Override
                    public void onError(XLinkErrorCode errorCode) {

                        showSnackBarLong("解绑失败");
                    }

                    @Override
                    public void onStart() {
                        //开始移除订阅设备
                    }

                    @Override
                    public void onComplete(String result) {

                        showSnackBarLong("解绑成功");
                        readyGo(MainActivity.class);
                        finish();
                    }
                })
                .build();
        XLinkSDK.startTask(removeDeviceTask);
    }


    /**
     * 调用XLINKSDK修改数据端点
     * 注意DataPoint的type和value要对应上
     * <p>
     * //  public enum ValueType {
     * //                  // 对应的Java基本数据类型
     * //      BOOL,       // Boolean
     * //      BYTE,       // Byte
     * //      SHORT,      // Short
     * //      USHORT,     // Short
     * //      INT,        // Integer
     * //      UINT,       // Integer
     * //      LONG,       // Long
     * //      ULONG,      // Long
     * //      FLOAT,      // Float
     * //      DOUBLE,     // Double
     * //      STRING,     // String
     * //      BYTE_ARRAY, // byte[]
     * //  }
     *
     * @param index DataPoint的index
     * @param type  DataPoint的数据类型，见上表
     * @param value 要设置的值
     */
    void setDataPoint(final int index, final DataPointValueType type, Object value) {
        final XLinkDataPoint dp = new XLinkDataPoint(index, type);
        dp.setValue(value);
        Log.d(TAG, "setDataPoint: " + dp);

        XLinkSetDataPointTask task = XLinkSetDataPointTask.newBuilder()
                .setXDevice(mDevice.getXDevice())
                .setDataPoints(Collections.singletonList(dp))
                .setListener(new XLinkTaskListener<XDevice>() {
                    @Override
                    public void onError(XLinkErrorCode xLinkErrorCode) {
                        Log.d(TAG, "onError() called with: xLinkErrorCode = [" + xLinkErrorCode + "]" + " + " + dp);

                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(XDevice xDevice) {
                        Log.d(TAG, "onComplete() called with: = [" + dp + "]");
//                        mDevice.setDataPointByIndex(dp);
//                        EventBus.getDefault().post(new DataPointUpdateEvent());
                    }
                })
                .build();
        XLinkSDK.startTask(task);
    }


}
