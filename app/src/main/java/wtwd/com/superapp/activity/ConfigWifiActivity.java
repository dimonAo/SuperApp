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

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
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

    private Handler mHander = new Handler();
    private Dialog mConnectingDialog;
    private RingProgressView mRingProgressView;

    private boolean mIsConncting = false;
    private boolean isInvisiblePassword = true;

    private ISmartLinker mISmartLinker;
    private BroadcastReceiver mWifiChangedReceiver;

    @Override
    public void initToolBar(Toolbar toolbar) {
        text_tool_bar_title.setText("选择设备工作Wifi");
        initToolbar();

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
        return null;
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


    private void initToolbar() {


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


    @Override
    public void onLinked(SmartLinkedModule var1) {
        Log.e("TAG", "onLinked");
    }

    @Override
    public void onCompleted() {

        //连接成功完成
        if (mConnectingDialog.isShowing()) {
            mRingProgressView.setProgress(100);
            mConnectingDialog.dismiss();
        }

        final Dialog mSuccessDialog = new Dialog(this, R.style.MyCommonDialog);
        mHander.post(new Runnable() {
            @Override
            public void run() {

                DialogUtils.showConnectedWifiDialog(ConfigWifiActivity.this, mSuccessDialog);
            }
        });

        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSuccessDialog.dismiss();
            }
        }, 500);

        mIsConncting = false;

        readyGo(MainActivity.class);

    }

    @Override
    public void onTimeOut() {
        //连接失败，超时
        Log.e("TAG", "配网失败");
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
