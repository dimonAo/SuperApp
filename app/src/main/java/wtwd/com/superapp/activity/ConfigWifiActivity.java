package wtwd.com.superapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.hiflying.smartlink.AbstractSmartLinkerActivity;
import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.R1;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v7.MulticastSmartLinker;
import com.hiflying.smartlink.v7.MulticastSmartLinkerActivity;

import java.util.Queue;

import cn.xlink.sdk.common.StringUtil;
import cn.xlink.sdk.v5.listener.XLinkScanDeviceListener;
import cn.xlink.sdk.v5.listener.XLinkTaskListener;
import cn.xlink.sdk.v5.model.XDevice;
import cn.xlink.sdk.v5.module.connection.XLinkScanDeviceTask;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import cn.xlink.sdk.v5.module.main.XLinkSDK;
import cn.xlink.sdk.v5.module.subscription.XLinkAddDeviceTask;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.entity.Device;
import wtwd.com.superapp.manager.DeviceManager;
import wtwd.com.superapp.util.Constant;
import wtwd.com.superapp.util.DialogUtils;
import wtwd.com.superapp.util.Utils;
import wtwd.com.superapp.widget.RingProgressView;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class ConfigWifiActivity extends BaseActivity implements OnSmartLinkListener {
    private ImageView img_invisible_pwd;
    private EditText editText_hiflying_smartlinker_ssid;
    private EditText editText_hiflying_smartlinker_password;
    private Button button_hiflying_smartlinker_start;

    private Dialog mConnectingDialog;
    private RingProgressView mRingProgressView;

    private boolean mIsConncting = false;
    private boolean isInvisiblePassword = true;

    private ISmartLinker mISmartLinker;
    private BroadcastReceiver mWifiChangedReceiver;

    @Override
    public void initToolBar(Toolbar toolbar) {
        text_tool_bar_title.setText("选择设备工作Wifi");
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_hiflying_sniffer_smart_linker;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        mConnectingDialog = new Dialog(this, R.style.MyCommonDialog);
        mISmartLinker = setupSmartLinker();

        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);

        initView();
        addListener();

        this.mWifiChangedReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
//                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
                if (networkInfo != null && networkInfo.isConnected()) {
                    editText_hiflying_smartlinker_ssid.setText(getSSid());
                    editText_hiflying_smartlinker_password.requestFocus();
                    button_hiflying_smartlinker_start.setEnabled(true);
                } else {
                    if (mConnectingDialog.isShowing()) {
                        mRingProgressView.setProgress(100);
                        mConnectingDialog.dismiss();
                        readyGo(JoinWifiFailedActivity.class);
                    } else {
                        showNoWifiConnect();
                    }
                    mIsConncting = false;
                }

            }
        };
        this.registerReceiver(this.mWifiChangedReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));


    }


    private void showNoWifiConnect() {
        Dialog mDialog = new Dialog(this, R.style.MyCommonDialog);
        DialogUtils.showUnconnectedWifiDialog(ConfigWifiActivity.this, mDialog);
    }

    private String getSSid() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wm != null) {
            WifiInfo wi = wm.getConnectionInfo();
            if (wi != null) {
                String ssid = wi.getSSID();
                if (ssid.length() > 2 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    return ssid.substring(1, ssid.length() - 1);
                }
                return ssid;
            }
        }
        return "";
    }

    @Override
    public View getSnackView() {
        return tool_bar;
    }

    private String getSsidString() {
        return editText_hiflying_smartlinker_ssid.getText().toString().trim();
    }

    private String getPasswordString() {
        return editText_hiflying_smartlinker_password.getText().toString().trim();
    }

    private void initView() {

        editText_hiflying_smartlinker_password = (EditText) findViewById(R.id.editText_hiflying_smartlinker_password);
        editText_hiflying_smartlinker_ssid = (EditText) findViewById(R.id.editText_hiflying_smartlinker_ssid);
        button_hiflying_smartlinker_start = (Button) findViewById(R.id.button_hiflying_smartlinker_start);
        img_invisible_pwd = (ImageView) findViewById(R.id.img_invisible_pwd);
        img_invisible_pwd.setBackgroundResource(R.mipmap.wifi_invisible_pwd);

        editText_hiflying_smartlinker_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);


        img_invisible_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInvisiblePassword) {
                    img_invisible_pwd.setBackgroundResource(R.mipmap.wifi_visible_pwd);
                    editText_hiflying_smartlinker_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    isInvisiblePassword = false;
                } else {
                    img_invisible_pwd.setBackgroundResource(R.mipmap.wifi_invisible_pwd);
                    editText_hiflying_smartlinker_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isInvisiblePassword = true;

                }

                editText_hiflying_smartlinker_password.setSelection(editText_hiflying_smartlinker_password.getText().length());
            }
        });

    }


    private void addListener() {

        button_hiflying_smartlinker_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsConncting) {
                    mISmartLinker.setOnSmartLinkListener(ConfigWifiActivity.this);
//                    mISmartLinker.setTimeoutPeriod(10*1000);
                    try {
                        mISmartLinker.start(ConfigWifiActivity.this.getApplicationContext()
                                , getPasswordString()
                                , new String[]{getSsidString()});
                        showConnectingWifiDialog(ConfigWifiActivity.this, mConnectingDialog);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mIsConncting = true;
                }

            }
        });
    }

    private void showConnectingWifiDialog(Activity mActivity, final Dialog mDialog) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_connecting_wifi, null, false);

        mRingProgressView = (RingProgressView) view.findViewById(R.id.ring_circle);
        mRingProgressView.setCurrentNumAndTargetNum(100, 100);

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(view);


        mDialog.show();
        DialogUtils.setDialogMaxheightLayoutParams(mActivity, mDialog);

    }

    protected void onDestroy() {
        super.onDestroy();
        this.mISmartLinker.setOnSmartLinkListener(null);

        try {
            this.unregisterReceiver(this.mWifiChangedReceiver);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private XLinkScanDeviceTask mScanTask;
    private volatile boolean mScanning = false;
    private static final String TAG = "AddDevicePresenter";
    private boolean mSubscribing = false;
    private XLinkAddDeviceTask mTask;

    //扫描超时  SDK默认90秒
    public static final int SEARCH_NEW_DEVICE_TIMEOUT = 30000;
    //扫描间隔
    public static final int SEARCH_NEW_DEVICE_INTERVAL = 1000;

    /**
     * 执行扫描设备, 在异步线程上执行。
     */
    void scan(String pid, final String mac) {
        if (mScanning || StringUtil.isEmpty(pid)) {
//            if (getView() != null) {
//                getView().showScanningUncompleted();
//            }
            return;
        }

        mScanTask = XLinkScanDeviceTask.newBuilder()
                .setTotalTimeout(SEARCH_NEW_DEVICE_TIMEOUT)// 设置超时，单位毫秒，默认90秒
                .setProductIds(pid)
//                .setRetryInterval(SEARCH_NEW_DEVICE_INTERVAL)// scan per 1 sec
                .setScanDeviceListener(new XLinkScanDeviceListener() {// 设置搜索回调, **回调在主线程上执行**
                    @Override
                    public void onScanResult(XDevice xDevice) {
                        // 同一设备仅会回调一次
                        Log.e(TAG, "onGotDeviceByScan() called with: " + "xDevice = [" + xDevice + "]");
                        if (xDevice != null) {
                            if (mac.equals(xDevice.getMacAddress())) {

                                //搜索完成，停止搜索
//                                XLinkSDK.stopTask(mScanTask);

                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = xDevice;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }

                    @Override
                    public void onError(XLinkErrorCode xLinkErrorCode) {

                        Log.e(TAG, "xLinkErrorCode.name : " + xLinkErrorCode.name());
                        Log.e(TAG, "xLinkErrorCode.getValue : " + xLinkErrorCode.getValue());

                        if (xLinkErrorCode != XLinkErrorCode.ERROR_TASK_TIMEOUT) {

                        } else {

                        }
                        mScanning = false;
                    }

                    @Override
                    public void onStart() {
                        //开始扫描
                        mScanning = true;


                    }

                    @Override
                    public void onComplete(Void aVoid) {
                        mScanning = false;
//                        getView().showCompleteScanning();


                    }
                }).build();
        XLinkSDK.startTask(mScanTask);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (1 == msg.what) {
                Log.e(TAG, "add device handler");
                XDevice xDevice = (XDevice) msg.obj;
                addDevices(xDevice);
            }
        }
    };

    //添加设备
    private void addDevices(final XDevice device) {
        mTask = XLinkAddDeviceTask.newBuilder()
                .setXDevice(device)
                .setListener(new XLinkTaskListener<XDevice>() {// 设置本次订阅任务的回调

                    @Override
                    public void onError(XLinkErrorCode xLinkErrorCode) {
                        Log.d(TAG, "subscribe device fail: " + device.getMacAddress() + " -> " + xLinkErrorCode);
                        mSubscribing = false;
                    }

                    @Override
                    public void onStart() {
                        Log.d(TAG, "start subscribe device: " + device.getMacAddress());
                        mSubscribing = true;
                    }

                    @Override
                    public void onComplete(XDevice xDevice) {
                        // 订阅成功
                        Log.e(TAG, "subscribe device successfully: " + xDevice.getMacAddress());
                        Device device = new Device(xDevice);
                        DeviceManager.getInstance().addDevice(device);
                        showSnackBarLong("绑定设备成功");
                        // 从添加队列里拿下一个设备进行添加操作
                        mSubscribing = false;
                        readyGo(MainActivity.class);


                    }
                })
                .build();
        XLinkSDK.startTask(mTask);
    }


    /**
     * 连接成功
     *
     * @param var1
     */
    @Override
    public void onLinked(SmartLinkedModule var1) {
        Log.e("TAG", "onLinked");
        Log.e("TAG", "var1.getMac() : " + var1.getMac());
        showSnackBarLong("配网成功，开始绑定设备");
        scan(Constant.PRODUCTID, var1.getMac());

    }


    @Override
    public void onCompleted() {

        //连接成功完成
        if (mConnectingDialog.isShowing()) {
            mRingProgressView.setProgress(100);
            mConnectingDialog.dismiss();
        }

        final Dialog mSuccessDialog = new Dialog(this, R.style.MyCommonDialog);
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                DialogUtils.showConnectedWifiDialog(ConfigWifiActivity.this, mSuccessDialog);
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSuccessDialog.dismiss();
            }
        }, 500);

        mIsConncting = false;

//        readyGo(MainActivity.class);

    }

    @Override
    public void onTimeOut() {
        //连接失败，超时
        Log.e("TAG", "配网失败");
        showSnackBarLong("配网失败");
        if (mConnectingDialog.isShowing()) {
            mRingProgressView.setProgress(100);
            mConnectingDialog.dismiss();
            readyGo(JoinWifiFailedActivity.class);
            mIsConncting = false;
        }

    }

    public ISmartLinker setupSmartLinker() {
        return MulticastSmartLinker.getInstance();
    }


}
