package wtwd.com.superapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.xlink.sdk.v5.listener.XLinkTaskListener;
import cn.xlink.sdk.v5.module.http.XLinkRemoveDeviceTask;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import cn.xlink.sdk.v5.module.main.XLinkSDK;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.entity.Device;
import wtwd.com.superapp.manager.DeviceManager;

public class SweeperSetActivity extends BaseActivity implements View.OnClickListener {

    private TextView text_device_id;
    private Button btn_delete_device;
    private Device mDevice;

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

        text_device_id = (TextView) findViewById(R.id.text_device_id);
        btn_delete_device = (Button) findViewById(R.id.btn_delete_device);

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
    }


    @Override
    public View getSnackView() {
        return tool_bar;
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_delete_device == v.getId()) {
            doRemoveDevice();
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
//                        if (getView() != null) {
//                            getView().dismissLoading();
//                            getView().showRemoveDeviceError(errorCode);
//                        }
                        showSnackBarLong("解绑失败");
                    }

                    @Override
                    public void onStart() {
//                        getView().showLoading();
                    }

                    @Override
                    public void onComplete(String result) {
//                        if (getView() != null) {
//                            getView().dismissLoading();
//                            getView().showSuccessRemoveDevice(mDevice);
//                        }

                        showSnackBarLong("解绑成功");
                        readyGo(MainActivity.class);
                        finish();
                    }
                })
                .build();
        XLinkSDK.startTask(removeDeviceTask);
    }


}
