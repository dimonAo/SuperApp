package wtwd.com.superapp.eventbus;


import cn.xlink.sdk.v5.model.XDevice;

/**
 * 搜索到设备的通知
 */
public class ScanDeviceEvent {
    private XDevice xDevice;

    public ScanDeviceEvent(XDevice xDevice) {
        setxDevice(xDevice);
    }

    public XDevice getxDevice() {
        return xDevice;
    }

    public void setxDevice(XDevice xDevice) {
        this.xDevice = xDevice;
    }
}
