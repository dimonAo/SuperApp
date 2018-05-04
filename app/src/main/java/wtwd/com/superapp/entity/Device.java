package wtwd.com.superapp.entity;


import java.util.ArrayList;
import java.util.List;

import cn.xlink.sdk.core.model.DataPointValueType;
import cn.xlink.sdk.v5.model.XDevice;
import cn.xlink.sdk.v5.model.XLinkDataPoint;

/**
 * 设备对象
 */
public final class Device {
    private final String TAG = "Device";

    public Device(XDevice xDevice) {
        setXDevice(xDevice);
    }

    //数据端点
    private List<XLinkDataPoint> mDataPoints = new ArrayList<>();

    private XDevice mXDevice;

    public XLinkDataPoint getDataPointByIndex(int index, DataPointValueType type) {
        for (XLinkDataPoint dataPoint : getDataPoints()) {
            //匹配index和数据类型
            if (dataPoint.getIndex() == index && dataPoint.getType() == type) {
                return dataPoint;
            }
        }
        return null;
    }

    public void setDataPointByIndex(XLinkDataPoint dp) {
        for (XLinkDataPoint dataPoint : getDataPoints()) {
            //匹配index和数据类型
            if (dataPoint.getIndex() == dp.getIndex() && dataPoint.getType() == dp.getType()) {
                dataPoint.setValue(dp.getValue());
            }
        }
    }

    public List<XLinkDataPoint> getDataPoints() {
        return mDataPoints;
    }

    public void setDataPoints(List<XLinkDataPoint> dataPoints) {
        mDataPoints = dataPoints;
//        if (!CommonUtil.isEmpty(dataPoints)) {
            // 求并集
//            mDataPoints.removeAll(dataPoints);
//            mDataPoints.addAll(dataPoints);
//        }
    }

    public XDevice getXDevice() {
        return mXDevice;
    }

    public void setXDevice(XDevice XDevice) {
        mXDevice = XDevice;
    }

    public boolean isConnected() {
        return getXDevice().getConnectionState() == XDevice.State.CONNECTED;
    }
}
