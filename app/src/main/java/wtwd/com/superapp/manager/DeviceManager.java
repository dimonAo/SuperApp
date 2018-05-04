package wtwd.com.superapp.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.xlink.sdk.v5.listener.XLinkTaskListener;
import cn.xlink.sdk.v5.model.XDevice;
import cn.xlink.sdk.v5.model.XLinkDataPoint;
import cn.xlink.sdk.v5.module.connection.XLinkConnectDeviceTask;
import cn.xlink.sdk.v5.module.datapoint.XLinkGetDataPointMetaInfoTask;
import cn.xlink.sdk.v5.module.http.XLinkSyncDeviceListTask;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import cn.xlink.sdk.v5.module.main.XLinkSDK;
import wtwd.com.superapp.entity.Device;

/**
 * 设备管理
 */
public class DeviceManager {
    private static final String TAG = "DeviceManager";

    private WeakReference<Context> mContext;
    private ConcurrentMap<String, Device> mDevices;

    private static class LazyHolder {
        private static final DeviceManager INSTANCE = new DeviceManager();
    }

    public static DeviceManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 初始化设备管理类
     */
    public void init(Context context) {
        mContext = new WeakReference<>(context);
        // 初始化设备
        mDevices = new ConcurrentHashMap<>();
    }

    public Context getContext() {
        if (mContext == null || mContext.get() == null) {
            throw new NullPointerException("context is null");
        }
        return mContext.get();
    }

    public void addDevice(String key, Device value) {
        if (mDevices != null)
            mDevices.put(key, value);
    }

    public void addDevice(Device value) {
        if (mDevices != null)
            mDevices.put(value.getXDevice().getMacAddress(), value);
    }

    public void removeDevice(String key) {
        if (mDevices != null)
            mDevices.remove(key);
    }

    public void removeDevice(Device device) {
        if (mDevices != null)
            mDevices.remove(device.getXDevice().getMacAddress());
    }

    public void clear() {
        if (mDevices != null)
            mDevices.clear();
    }


    /* -------------------- 对外方法 --------------------- */

    /**
     * 得到所有设备列表
     */
    public List<Device> getAllDevices() {
        return new ArrayList<>(mDevices.values());
    }

    public int getSize() {
        return mDevices.size();
    }

    /**
     * 传入mac地址 得到保存的设备实例
     */
    public Device getDevice(@NonNull String mac) {
        return mDevices.get(mac);
    }

    /**
     * 传入XDevice地址 得到保存的设备实例
     */
    public Device getDevice(@NonNull XDevice xDevice) {
        return mDevices.get(xDevice.getMacAddress());
    }

    /**
     * 保存设备硬件信息（如果设备对象不存在则新建一个）
     */
    @Nullable
    public Device updateXDevice(@NonNull XDevice xDevice) {
        // 得到保存的设备
        Device device = getDevice(xDevice.getMacAddress());
        if (device == null) {
            // 为空则新建一个
            device = new Device(xDevice);
        } else {
            device.setXDevice(xDevice);
        }
        addDevice(device);

        return device;
    }

    /**
     * 连接设备 XlinkListener回调
     */
    public static void connectDevice(final Device device) {
        if (device == null) {
            Log.d(TAG, "connect null device");
            return;
        }
        if (!device.isConnected()) {
            Log.d(TAG, "connect device ：" + device.getXDevice().getMacAddress());
            // 新建连接任务
            XLinkConnectDeviceTask connectDeviceTask = XLinkConnectDeviceTask.newBuilder()
                    .setXDevice(device.getXDevice())
                    .setListener(new XLinkTaskListener<XDevice>() {
                        @Override
                        public void onError(XLinkErrorCode errorCode) {
                            Log.d(TAG, "XLinkConnectDeviceTask onError() called with: errorCode = [" + errorCode + "]");
                        }

                        @Override
                        public void onStart() {
                            Log.d(TAG, "XLinkConnectDeviceTask onStart() called");
                        }

                        @Override
                        public void onComplete(XDevice result) {
                            Log.d(TAG, "XLinkConnectDeviceTask onComplete() called with: result = [" + result + "]");
                        }
                    })
                    .build();
            // 执行连接设备
            XLinkSDK.startTask(connectDeviceTask);
            // 在XLinkSDK的XLinkDeviceStateListener中的onDeviceStateChanged()也会被调用。
        }
    }

    /**
     * 新建获取设备列表任务。要求设备和当前用户之间有订阅关系。
     */
    public void refreshDeviceList(final XLinkTaskListener<List<XDevice>> listener) {
        // 获取设备后自动连接设备，无需主动connectDevice
        XLinkSyncDeviceListTask task = XLinkSyncDeviceListTask.newBuilder()
                .setListener(new XLinkTaskListener<List<XDevice>>() {
                    @Override
                    public void onError(XLinkErrorCode xLinkErrorCode) {
                        Log.d(TAG, "onError() called with: xLinkErrorCode = [" + xLinkErrorCode + "]");
                        if (listener != null) {
                            listener.onError(xLinkErrorCode);
                        }
                    }

                    @Override
                    public void onStart() {
                        if (listener != null) {
                            listener.onStart();
                        }
                    }

                    @Override
                    public void onComplete(List<XDevice> xDevices) {
                        Log.d(TAG, "onComplete() called with: xDevices = [" + xDevices + "]");
                        if (xDevices.size() <= 0) {
                            DeviceManager.getInstance().clear();
                        } else {

                            // 已得到的订阅设备
                            ArrayList<String> addMacs = new ArrayList<>();
                            // 解析订阅设备列表
                            for (XDevice xDev : xDevices) {
                                String addMac = parseDevice(xDev);
                                if (!TextUtils.isEmpty(addMac)) {
                                    addMacs.add(addMac);
                                }
                            }
                            // 删除不在订阅设备列表的设备
                            for (Device device : DeviceManager.getInstance().getAllDevices()) {
                                if (!isInSubscribeDevice(device.getXDevice().getMacAddress(), addMacs)) {
                                    DeviceManager.getInstance().removeDevice(device);
                                }
                            }

                            // 正式开发环境无需调用下面这一行
//                            syncDataPointMetaInfo(xDevices);
                        }

                        if (listener != null) {
                            listener.onComplete(xDevices);
                        }
                    }
                })
                .build();
        XLinkSDK.startTask(task);
    }


    /**
     * 获取设备元信息
     *
     * ！！！demo专用代码，正式开发app时可忽略这一步！！！
     * ！！！demo专用代码，正式开发app时可忽略这一步！！！
     * ！！！demo专用代码，正式开发app时可忽略这一步！！！
     *
     * @param xDevices
     */
    public void syncDataPointMetaInfo(final List<XDevice> xDevices) {
        Set<String> pids = new HashSet<>();
        for (final XDevice device :
                xDevices) {
            // 相同pid不重复拿
            if (pids.contains(device.getProductId())) {
                break;
            }
            pids.add(device.getProductId());

            XLinkGetDataPointMetaInfoTask task = XLinkGetDataPointMetaInfoTask.newBuilder()
                    .setXDevice(device)
                    .setListener(new XLinkTaskListener<List<XLinkDataPoint>>() {
                        @Override
                        public void onError(XLinkErrorCode xLinkErrorCode) {
                            Log.e(TAG, "XLinkGetDataPointMetaInfoTask onError: " + xLinkErrorCode);
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete(List<XLinkDataPoint> dataPoints) {
                            DeviceManager.getInstance().getDevice(device)
                                    .setDataPoints(dataPoints);
                        }
                    })
                    .build();
            XLinkSDK.startTask(task);
        }
    }

    private boolean isInSubscribeDevice(String save, ArrayList<String> subMacs) {
        for (String subMac : subMacs) {
            if (TextUtils.equals(save, subMac)) {
                return true;
            }
        }
        return false;
    }

    private String parseDevice(XDevice xDevice) {
        if (xDevice != null) {
            Device device = DeviceManager.getInstance().updateXDevice(xDevice);
            if (device != null) {
                DeviceManager.getInstance().addDevice(device);
                return xDevice.getMacAddress();
            }
        }
        return null;
    }
}
