package wtwd.com.superapp.activity;

import android.content.Context;
import android.media.MediaDrm;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.xlink.sdk.core.model.DataPointValueType;
import cn.xlink.sdk.task.RetryUntilTimeoutTask;
import cn.xlink.sdk.v5.listener.XLinkTaskListener;
import cn.xlink.sdk.v5.model.XDevice;
import cn.xlink.sdk.v5.model.XLinkDataPoint;
import cn.xlink.sdk.v5.module.connection.XLinkConnectDeviceTask;
import cn.xlink.sdk.v5.module.datapoint.XLinkGetDataPointTask;
import cn.xlink.sdk.v5.module.datapoint.XLinkSetDataPointTask;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import cn.xlink.sdk.v5.module.main.XLinkSDK;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.entity.Device;
import wtwd.com.superapp.eventbus.DataPointUpdateEvent;
import wtwd.com.superapp.manager.DeviceManager;
import wtwd.com.superapp.util.Utils;

public class SweeperActivity extends BaseActivity implements View.OnClickListener {

    private static final int BTN_PLANNED = 0;
    private static final int BTN_FIX_POINT = 1;
    private static final int BTN_SIDE = 2;
    private static final int BTN_AUTO = 3;
    private static final int BTN_MOP = 4;
    private static final int BTN_RECHARGE = 5;

    private static final int BTN_CLOSE = 0;
    private static final int BTN_STANDARD = 1;
    private static final int BTN_STRONG = 2;

    private Button btn_auto;
    private Button btn_fix_point;
    private Button btn_side;
    private Button btn_planned;
    private Button btn_mop;
    private Button btn_recharge;


    private Button btn_close;
    private Button btn_standard;
    private Button btn_strong;

    private Button btn_top;
    private Button btn_left;
    private Button btn_right;
    private Button btn_down;

    private Button img_btn_center;

    private TextView text_mode;
    private TextView clear_state;
    private TextView text_battery;

    private List<Button> mModeButton = new ArrayList<>();
    private List<Button> mStrongButton = new ArrayList<>();
    private List<Button> mDirectionButton = new ArrayList<>();

    private Device mDevice;


    @Override
    public void initToolBar(Toolbar toolbar) {
//        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSweeperText));
//        Utils.setWindowStatusBarColor(this, R.color.colorSweeperText);
//        Utils.setStatusBarColor(this,R.color.colorSweeperText);
//        Utils.setMargins(toolbar, 0, Utils.getStatusBarHeight(this), 0, 0);
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorSweeperText);

        text_tool_bar_title.setText("扫地机器人");
        text_tool_bar_title.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));


        img_tool_bar_right.setImageResource(R.mipmap.set);
        img_tool_bar_right.setVisibility(View.VISIBLE);


    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_sweeper;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        EventBus.getDefault().register(this);
        initView();

    }

    private void initView() {
        btn_auto = (Button) findViewById(R.id.btn_auto);
        btn_fix_point = (Button) findViewById(R.id.btn_fix_point);
        btn_side = (Button) findViewById(R.id.btn_side);
        btn_planned = (Button) findViewById(R.id.btn_planned);
        btn_mop = (Button) findViewById(R.id.btn_mop);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);


        btn_close = (Button) findViewById(R.id.btn_close);
        btn_standard = (Button) findViewById(R.id.btn_standard);
        btn_strong = (Button) findViewById(R.id.btn_strong);

        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_top = (Button) findViewById(R.id.btn_top);
        btn_down = (Button) findViewById(R.id.btn_down);

        img_btn_center = (Button) findViewById(R.id.img_btn_center);
        text_mode = (TextView) findViewById(R.id.text_mode);
        clear_state = (TextView) findViewById(R.id.clear_state);
        text_battery = (TextView) findViewById(R.id.text_battery);


        addData();

        String mac = getTargetDeviceMacAddress();
        if (null != mac) {
            mDevice = DeviceManager.getInstance().getDevice(mac);
        }

        connectDevice(mDevice);


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


    private void addData() {
        mModeButton.clear();
        mStrongButton.clear();
        mDirectionButton.clear();

        mModeButton.add(btn_planned);
        mModeButton.add(btn_fix_point);
        mModeButton.add(btn_side);
        mModeButton.add(btn_auto);
        mModeButton.add(btn_mop);
        mModeButton.add(btn_recharge);


        mStrongButton.add(btn_close);
        mStrongButton.add(btn_standard);
        mStrongButton.add(btn_strong);

        mDirectionButton.add(btn_top);
        mDirectionButton.add(btn_right);
        mDirectionButton.add(btn_down);
        mDirectionButton.add(btn_left);

//        updateText(BTN_AUTO);
//        updateStrongMode(BTN_CLOSE);
//        setClearState(false);

        addListener();
    }


    private void addListener() {
        btn_auto.setOnClickListener(this);
        btn_fix_point.setOnClickListener(this);
        btn_side.setOnClickListener(this);
        btn_planned.setOnClickListener(this);
        btn_mop.setOnClickListener(this);
        btn_recharge.setOnClickListener(this);

        btn_close.setOnClickListener(this);
        btn_standard.setOnClickListener(this);
        btn_strong.setOnClickListener(this);

        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_top.setOnClickListener(this);

        img_btn_center.setOnClickListener(this);


    }

    private void displaySweeperStatus(List<XLinkDataPoint> dataPoints) {
        for (int i = 0; i < dataPoints.size(); i++) {
            resolveProtocol(dataPoints.get(i));
        }
    }


    private void resolveProtocol(XLinkDataPoint dataPoint) {
        switch (dataPoint.getIndex()) {

            case 0:
                /**
                 * 工作状态
                 *1: 工作中
                 *2: 待机
                 *3: 故障
                 *4: 休眠
                 *5: 工作完成
                 *6: 充电
                 *7: 充电完成
                 */
                int status = ((Byte) dataPoint.getValue()) & 0xff;
                String mClearState = "";
                switch (status) {
                    case 0:
                        mClearState = "待机";
                        break;
                    case 1:
                        mClearState = "工作中";
                        break;

                    case 2:
                        mClearState = "待机";
                        break;

                    case 3:
                        mClearState = "故障";
                        break;

                    case 4:
                        mClearState = "休眠";
                        break;

                    case 5:
                        mClearState = "工作完成";
                        break;

                    case 6:
                        mClearState = "充电";
                        break;

                    case 7:
                        mClearState = "充电完成";
                        break;
//
//                    case 8:
//                        mClearState = "";
//                        break;
                }
                clear_state.setText(mClearState);

                break;

            case 1:
                /**
                 *工作模式
                 *1: 空闲模式
                 *2: 规划清扫
                 *3: 沿边清扫
                 *4: 定点清扫
                 *5: 拖地模式
                 *6: 回充模式
                 *7: 手动控制
                 *8: 单间规划
                 */

                int workMode = ((Byte) dataPoint.getValue()) & 0xff;
                switch (workMode) {
                    case 0:
                        updateText(0);
                        break;

                    case 1:

                        /**
                         * 空闲模式
                         */
                        updateText(7);
                        break;

                    case 2:
                        updateText(0);
                        break;
                    case 3:
                        updateText(2);
                        break;

                    case 4:
                        updateText(1);
                        break;
                    case 5:
                        updateText(4);
                        break;
                    case 6:
                        updateText(5);
                        break;
                    case 7:
                        /**
                         * 手动模式
                         */
                        updateText(-1);
                        break;

                    case 8:
                        updateText(3);
                        break;
                }


                break;

            case 2:

                /**
                 * 电池电量
                 *
                 *
                 *
                 */
                int battery = ((Byte) dataPoint.getValue()) & 0xff;
                Log.e(TAG, "battery : " + battery);
                text_battery.setText(String.format("%s%%", battery + ""));
                break;

            case 3:

                /**
                 * 清扫方向
                 *0: 停止
                 *1: 前进
                 *2: 后退
                 *3: 左转
                 *4: 右转
                 */


                break;

            case 4:

                /**
                 * 风机状态
                 *1: 正常2: 强力3: 关闭
                 *
                 */
                int fanState = ((Byte) dataPoint.getValue()) & 0xff;
                if (3 == fanState) {
                    updateStrongMode(0);
                } else if (0 == fanState) {
                    updateStrongMode(0);
                } else {
                    updateStrongMode(fanState + 1);
                }

                break;

            case 5:
                /**
                 * 工作指令
                 * 1:启动 2:暂停
                 */

                int workCmd = ((Byte) dataPoint.getValue()) & 0xff;
                if (1 == workCmd) {
                    setClearState(true);
                } else if (2 == workCmd) {
                    setClearState(false);
                }


                break;
            case 6:

                /**
                 * 地图频率
                 * 地图数据请求频率：
                 *1: 2S
                 *2: 5S
                 *3: 25S
                 */

                break;

            case 7:

                /**
                 * 渗水速度
                 *1: 低 2:中(默认) 3: 高
                 */

                break;

            case 8:
                /**
                 * 勿扰模式
                 *0:关闭(默认) 1:开启
                 */
                break;
            case 9:
                /**
                 * 寻找机器人
                 * 0: 无 1:触发
                 */

                break;
            case 10:

                /**
                 * 报警状态
                 *0:无报警
                 *1:主动轮过载
                 *2:前跌落触发
                 *3:左跌落触发
                 *4:右跌落触发
                 *5:机器离地
                 *6:左碰撞卡住
                 *7:右碰撞卡住
                 *8:边刷缠绕
                 *9:电量低于20%
                 *10:尘盒未安装
                 *11:风扇异常
                 *12:滚刷异常
                 *13:电池故障
                 *14:水箱未安装
                 */


                break;
        }
    }


    /**
     * 模式
     *
     * @param position
     */
    private void updateText(int position) {
        if (position > mModeButton.size() - 1) {
            for (int i = 0; i < mModeButton.size(); i++) {
                mModeButton.get(i).setSelected(false);
                mModeButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperTextUnselected));
            }
            text_mode.setText("空闲");
            return;
        }

        if (position < 0) {
            for (int i = 0; i < mModeButton.size(); i++) {
                mModeButton.get(i).setSelected(false);
                mModeButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperTextUnselected));
            }
            text_mode.setText("手动");
            return;
        }


        for (int i = 0; i < mModeButton.size(); i++) {
            if (i == position) {
                mModeButton.get(i).setSelected(true);
                mModeButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperText));
//                text_mode.setText(mModeButton.get(i).getText() + "模式");
                text_mode.setText(String.format("%s", mModeButton.get(i).getText()));
            } else {
                mModeButton.get(i).setSelected(false);
                mModeButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperTextUnselected));
            }
        }
    }

    /**
     * 风机状态
     *
     * @param position
     */
    private void updateStrongMode(int position) {
        if (position > mStrongButton.size() - 1) {
            return;
        }

        for (int i = 0; i < mStrongButton.size(); i++) {
            if (i == position) {
                mStrongButton.get(i).setSelected(true);
                mStrongButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperText));
            } else {
                mStrongButton.get(i).setSelected(false);
                mStrongButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperTextUnselected));
            }
        }

    }

    private void updateDirection(int position) {
//        if (position > mDirectionButton.size() - 1) {
//            return;
//        }
//
//        for (int i = 0; i < mDirectionButton.size(); i++) {
//            if (i == position) {
//                mDirectionButton.get(i).setSelected(true);
//                mDirectionButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperText));
//            } else {
//                mDirectionButton.get(i).setSelected(false);
//                mDirectionButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperTextUnselected));
//            }
//        }


    }

    @Override
    public View getSnackView() {
        return null;
    }

//trdl

    private static final int BTN_TOP = 0;
    private static final int BTN_RIGHT = 1;
    private static final int BTN_DOWN = 2;
    private static final int BTN_LEFT = 3;


    @Override
    public void onClick(View v) {
        if (XDevice.State.CONNECTED != mDevice.getXDevice().getCloudConnectionState()) {
            showSnackBarLong("设备未连接");
            return;
        }

        int id = v.getId();

        int index = 1;
        byte value = 2;


        switch (id) {
            case R.id.btn_planned:
                updateText(BTN_PLANNED);
                index = 1;
                value = (byte) 2;

//                setDataPoint(1, DataPointValueType.BYTE, (byte) 2);
                break;
            case R.id.btn_fix_point:
                updateText(BTN_FIX_POINT);
                index = 1;
                value = (byte) 4;
//                setDataPoint(1, DataPointValueType.BYTE, (byte) 4);
                break;

            case R.id.btn_side:
                updateText(BTN_SIDE);
                index = 1;
                value = (byte) 3;
//                setDataPoint(1, DataPointValueType.BYTE, (byte) 3);
                break;


            case R.id.btn_auto:
                updateText(BTN_AUTO);
                index = 1;
                value = (byte) 8;
//                setDataPoint(1, DataPointValueType.BYTE, (byte) 8);
                break;
            case R.id.btn_mop:
                updateText(BTN_MOP);
                index = 1;
                value = (byte) 5;
//                setDataPoint(1, DataPointValueType.BYTE, (byte) 5);
                break;
            case R.id.btn_recharge:
                updateText(BTN_RECHARGE);
                index = 1;
                value = (byte) 6;
//                setDataPoint(1, DataPointValueType.BYTE, (byte) 6);
                break;

            case R.id.btn_close:
                updateStrongMode(BTN_CLOSE);
                index = 4;
                value = (byte) 3;
//                setDataPoint(4, DataPointValueType.BYTE, (byte) 3);
                break;
            case R.id.btn_standard:
                updateStrongMode(BTN_STANDARD);
                index = 4;
                value = (byte) 1;
//                setDataPoint(4, DataPointValueType.BYTE, (byte) 1);
                break;
            case R.id.btn_strong:
                updateStrongMode(BTN_STRONG);
                index = 4;
                value = (byte) 2;
//                setDataPoint(4, DataPointValueType.BYTE, (byte) 2);
                break;


            case R.id.btn_top:
                updateDirection(BTN_TOP);
                updateText(-1);
                index = 3;
                value = (byte) 1;

                break;
            case R.id.btn_right:
                updateDirection(BTN_RIGHT);
                break;
            case R.id.btn_down:
                updateDirection(BTN_DOWN);
                break;
            case R.id.btn_left:
                updateDirection(BTN_LEFT);
                break;

            case R.id.img_btn_center:
                if (img_btn_center.isSelected()) {
//                    img_btn_center.setSelected(false);
                    setClearState(false);
                    index = 5;
                    value = (byte) 1;
//                    setDataPoint(5, DataPointValueType.BYTE, (byte) 1);
                } else {
//                    img_btn_center.setSelected(true);
                    setClearState(true);
                    index = 5;
                    value = (byte) 2;
//                    setDataPoint(5, DataPointValueType.BYTE, (byte) 2);
                }
                break;
        }

        setDataPoint(index, DataPointValueType.BYTE, value);

    }

    private void setClearState(boolean isSelected) {
        img_btn_center.setSelected(isSelected);
        if (isSelected) {
            clear_state.setText("清扫中");
        } else {
            clear_state.setText("暂停");
            for (int i = 0; i < mDirectionButton.size(); i++) {
                mDirectionButton.get(i).setSelected(false);
                mDirectionButton.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorSweeperTextUnselected));
            }
        }
    }

    private static final String TAG = "SweeperActivity";

    /**
     * 连接设备 XlinkListener回调
     */
    private void connectDevice(final Device device) {
        if (device == null) {
            Log.e(TAG, "connect null device");
            return;
        }
        if (!device.isConnected()) {
            Log.e(TAG, "connect device ：" + device.getXDevice().getMacAddress());
            // 新建连接任务
            XLinkConnectDeviceTask connectDeviceTask = XLinkConnectDeviceTask.newBuilder()
                    .setXDevice(device.getXDevice())
                    .setListener(new XLinkTaskListener<XDevice>() {
                        @Override
                        public void onError(XLinkErrorCode errorCode) {
                            Log.e(TAG, "XLinkConnectDeviceTask onError() called with: errorCode = [" + errorCode + "]");
                        }

                        @Override
                        public void onStart() {
                            Log.e(TAG, "XLinkConnectDeviceTask onStart() called");
                        }

                        @Override
                        public void onComplete(XDevice result) {
                            Log.e(TAG, "XLinkConnectDeviceTask onComplete() called with: result = [" + result + "]");

                            getDataPoint();

                        }
                    })
                    .build();
            // 执行连接设备
            XLinkSDK.startTask(connectDeviceTask);
            // 在XLinkSDK的XLinkDeviceStateListener中的onDeviceStateChanged()也会被调用。
        } else {
            Log.e(TAG, "device is connect");
            getDataPoint();
        }
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
//                        if (getContext() != null) {
//                            Toast.makeText(DemoApplication.getAppInstance(), index + " | " + type + " | " + xLinkErrorCode.toString(), Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(XDevice xDevice) {
                        Log.d(TAG, "onComplete() called with: = [" + dp + "]");
                        mDevice.setDataPointByIndex(dp);

//                        EventBus.getDefault().post(new DataPointUpdateEvent());
                    }
                })
                .build();
        XLinkSDK.startTask(task);
    }

    /**
     * 调用XLINKSDK获取设备数据端点(从本地或者云端)
     */
    public void getDataPoint() {
        // 2. 再用probe取DataPoint的值
        XLinkGetDataPointTask task = XLinkGetDataPointTask.newBuilder()
                .setXDevice(mDevice.getXDevice())
                .setListener(new XLinkTaskListener<List<XLinkDataPoint>>() {
                    @Override
                    public void onError(XLinkErrorCode errorCode) {
                        Log.d(TAG, "XLinkProbeTask onError() called with: errorCode = [" + errorCode + "]");
//                        if (getView() != null) {
//                            getView().dismissLoading();
//                            Toast.makeText(DemoApplication.getAppInstance(), "拉取失败：" + errorCode.toString(), Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public void onStart() {
                        Log.d(TAG, "XLinkProbeTask onStart() called");
//                        if (getView() != null) {
//                            getView().showLoading();
//                        }
                    }

                    @Override
                    public void onComplete(List<XLinkDataPoint> dataPoints) {
//                        if (getView() != null) {
//                            getView().dismissLoading();
//                        }
                        Log.d(TAG, "XLinkProbeTask onComplete() called with: result = [" + dataPoints + "]");

                        mDevice.setDataPoints(dataPoints);

                        Collections.sort(mDevice.getDataPoints(), new Comparator<XLinkDataPoint>() {
                            @Override
                            public int compare(XLinkDataPoint o1, XLinkDataPoint o2) {
                                return o1.getIndex() - o2.getIndex();
                            }
                        });

                        displaySweeperStatus(dataPoints);
//                        if (getView() != null) {
//                            getView().refreshAdapter();
//                        }
                    }
                })
                .build();
        XLinkSDK.startTask(task);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataPointUpdateEvent(DataPointUpdateEvent event) {
        Log.e(TAG, "event bus get data : " + mDevice.getDataPoints().toString());
        displaySweeperStatus(mDevice.getDataPoints());
    }


}
