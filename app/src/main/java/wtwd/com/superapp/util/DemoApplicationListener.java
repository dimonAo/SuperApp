package wtwd.com.superapp.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.xlink.sdk.common.XLog;
import cn.xlink.sdk.v5.listener.XLinkCloudListener;
import cn.xlink.sdk.v5.listener.XLinkDataListener;
import cn.xlink.sdk.v5.listener.XLinkDeviceStateListener;
import cn.xlink.sdk.v5.listener.XLinkTaskListener;
import cn.xlink.sdk.v5.listener.XLinkUserListener;
import cn.xlink.sdk.v5.manager.CloudConnectionState;
import cn.xlink.sdk.v5.model.EventNotify;
import cn.xlink.sdk.v5.model.XDevice;
import cn.xlink.sdk.v5.model.XLinkDataPoint;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import cn.xlink.sdk.v5.module.main.XLinkSDK;
import cn.xlink.sdk.v5.module.notify.EventNotifyHelper;
import cn.xlink.sdk.v5.module.share.XLinkHandleShareDeviceTask;
import wtwd.com.superapp.R;
import wtwd.com.superapp.activity.MainActivity;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.entity.Device;
import wtwd.com.superapp.manager.DeviceManager;
import wtwd.com.superapp.manager.UserManager;

/**
 * Created by CHENJIAHUI on 2017/2/25.
 * 统一实现XLINKSDK的监听器
 */

public class DemoApplicationListener implements XLinkDataListener, XLinkUserListener, XLinkCloudListener, XLinkDeviceStateListener {
    private static final String TAG = "MyXLinkListener";

    WeakReference<Application> mContext;
    WeakReference<BaseActivity> mCurrentBaseActivity;

    public DemoApplicationListener(Application app) {
        mContext = new WeakReference<>(app);
        initLifeObservable(app);
    }

    private void initLifeObservable(Application app) {
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (activity instanceof BaseActivity) {
                    mCurrentBaseActivity = new WeakReference<>((BaseActivity) activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
//                if (activity.equals(mCurrentBaseActivity.get())) {
//                    mCurrentBaseActivity = new WeakReference<>(null);
//                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public void onCloudStateChanged(CloudConnectionState state) {
        // 当SDK与云平台连上/断开的时候，会回调这个方法。
        Log.d(TAG, "onCloudStateChanged() called with: cloudConnectionState = [" + state + "]");
        EventBus.getDefault().post(state);
    }

    @Override
    public void onEventNotify(EventNotify eventNotify) {
        // 当SDK连接上云端，并接收到云端推送时，会回掉这个方法。
        Log.d(TAG, "onEventNotify() called with: " + "eventNotify.messageType = [" + eventNotify.messageType + "]");
        switch (eventNotify.messageType) {
            case EventNotify.MSG_TYPE_DEVICE_SHARE:
                handleDeviceShareNotify(eventNotify);
                break;
            case EventNotify.MSG_TYPE_SUBSCRIPTION_CHANGE:
                break;
            case EventNotify.MSG_TYPE_DEVICE_PROP_CHANGE:
                break;
            case EventNotify.MSG_TYPE_ONLINE_STATE_CHANGE:
                break;
        }

        try {
            byte[] src = eventNotify.payload;
            int len = (src[0] << 2) | src[1];
            String srcString = new String(src, 2, len);

            Toast.makeText(mContext.get(), srcString, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "onEventNotify: " + eventNotify, ex);
            ex.printStackTrace();
        }
    }

    private void handleDeviceShareNotify(EventNotify eventNotify) {
//        final EventNotifyHelper.DeviceShareNotify notify = EventNotifyHelper.parseDeviceShareNotify(eventNotify.payload);
//        Log.d(TAG, "handleDeviceShareNotify: " + notify);
//
//        Context context = mCurrentBaseActivity.get();
//        if (mCurrentBaseActivity.get() == null) {
//            return;
//        }
//
//        switch (notify.type) {
//            case EventNotifyHelper.DeviceShareNotify.TYPE_RECV_SHARE: //收到新的分享
//                mCurrentBaseActivity.get().showConfirmDialog(
//                        context.getString(R.string.prompt_title),
//                        context.getString(R.string.prompt_handle_share) + " ：" + notify.invite_code,
//                        context.getString(R.string.share_btn_deny),
//                        context.getString(R.string.share_btn_accept),
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                doDenyShare(notify);
//                            }
//                        },
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                doAcceptShare(notify);
//                            }
//                        }
//                );
//                break;
//            case EventNotifyHelper.DeviceShareNotify.TYPE_ACCEPT_SHARE: // 发出去的分享被接受
//                mCurrentBaseActivity.get().showPromptDialog(
//                        context.getString(R.string.prompt_title),
//                        context.getString(R.string.share_notify_accept) + " ：" + notify.device_id
//                );
//                break;
//            case EventNotifyHelper.DeviceShareNotify.TYPE_CANCEL_SHARE: // 原有的分享被取消
//                mCurrentBaseActivity.get().showPromptDialog(
//                        context.getString(R.string.prompt_title),
//                        context.getString(R.string.share_notify_cancel) + " ：" + notify.device_id
//                );
//                break;
//            case EventNotifyHelper.DeviceShareNotify.TYPE_DENY_SHARE: // 发出去的分享被拒绝
//                mCurrentBaseActivity.get().showPromptDialog(
//                        context.getString(R.string.prompt_title),
//                        context.getString(R.string.share_notify_deny) + " ：" + notify.device_id
//                );
//                break;
//        }


    }

    private void doDenyShare(EventNotifyHelper.DeviceShareNotify notify) {
//        XLinkHandleShareDeviceTask task = XLinkHandleShareDeviceTask.newBuilder()
//                .setAction(XLinkHandleShareDeviceTask.Action.DENY)
//                .setInviteCode(notify.invite_code)
//                .setUid(UserManager.getInstance().getUid())
//                .setListener(new XLinkTaskListener<String>() {
//                    @Override
//                    public void onError(XLinkErrorCode xLinkErrorCode) {
//                        XLog.d(TAG, "XLinkHandleShareDeviceTask onError() called with: " + "xLinkErrorCode = [" + xLinkErrorCode + "]");
//                        Context context = mCurrentBaseActivity.get();
//                        if (mCurrentBaseActivity.get() != null) {
//                            mCurrentBaseActivity.get().showPromptDialog(
//                                    context.getString(R.string.prompt_title),
//                                    context.getString(R.string.share_device_deny_fail) + "\n" + xLinkErrorCode
//
//                            );
//                        }
//                    }
//
//                    @Override
//                    public void onStart() {
//                        XLog.d(TAG, "XLinkHandleShareDeviceTask onStart()");
//                    }
//
//                    @Override
//                    public void onComplete(String s) {
//                        XLog.d(TAG, "XLinkHandleShareDeviceTask onComplete() called with: " + "s = [" + s + "]");
//                        Context context = mCurrentBaseActivity.get();
//                        if (mCurrentBaseActivity.get() != null) {
//                            mCurrentBaseActivity.get().showPromptDialog(
//                                    context.getString(R.string.prompt_title),
//                                    context.getString(R.string.share_device_deny_success) + "\n" + s
//                            );
//                        }
//                    }
//                })
//                .build();
//        XLinkSDK.startTask(task);
    }

    private void doAcceptShare(EventNotifyHelper.DeviceShareNotify notify) {
//        XLinkHandleShareDeviceTask task = XLinkHandleShareDeviceTask.newBuilder()
//                .setAction(XLinkHandleShareDeviceTask.Action.ACCEPT)
//                .setInviteCode(notify.invite_code)
//                .setUid(UserManager.getInstance().getUid())
//                .setListener(new XLinkTaskListener<String>() {
//                    @Override
//                    public void onError(XLinkErrorCode xLinkErrorCode) {
//                        XLog.d(TAG, "XLinkHandleShareDeviceTask onError() called with: " + "xLinkErrorCode = [" + xLinkErrorCode + "]");
//                        Context context = mCurrentBaseActivity.get();
//                        if (mCurrentBaseActivity.get() != null) {
//                            mCurrentBaseActivity.get().showPromptDialog(
//                                    context.getString(R.string.prompt_title),
//                                    context.getString(R.string.share_device_accept_fail) + "\n" + xLinkErrorCode
//
//                            );
//                        }
//                    }
//
//                    @Override
//                    public void onStart() {
//                        XLog.d(TAG, "XLinkHandleShareDeviceTask onStart()");
//                    }
//
//                    @Override
//                    public void onComplete(String s) {
//                        XLog.d(TAG, "XLinkHandleShareDeviceTask onComplete() called with: " + "s = [" + s + "]");
//
//                        refreshDevice();
//
////                        Context context = mCurrentBaseActivity.get();
////                        if (mCurrentBaseActivity.get() != null) {
////                            mCurrentBaseActivity.get().showPromptDialog(
////                                    context.getString(R.string.prompt_title),
////                                    context.getString(R.string.share_device_accept_success) + "\n" + s,
////                                    new View.OnClickListener() {
////                                        @Override
////                                        public void onClick(View v) {
////                                            refreshDevice();
////                                        }
////                                    }
////                            ).show();
////                        }
//                    }
//                })
//                .build();
//        XLinkSDK.startTask(task);
    }

    private void refreshDevice() {
//        DeviceManager.getInstance().refreshDeviceList(new XLinkTaskListener<List<XDevice>>() {
//            @Override
//            public void onError(XLinkErrorCode xLinkErrorCode) {
//                Toast.makeText(mContext.get(), "刷新失败：" + xLinkErrorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStart() {
//            }
//
//            @Override
//            public void onComplete(List<XDevice> xDevices) {
//                // 通知添加完成
//                EventBus.getDefault().post(new UpdateListEvent());
//            }
//        });
    }

    @Override
    public void onDataPointUpdate(XDevice xDevice, List<XLinkDataPoint> list) {
        // 当SDK收到设备上报的数据端点时，会回调这个方法。
        Log.d(TAG, "onDataPointUpdate() called with: " + "xDevice = [" + xDevice.getMacAddress() + "], list = [" + list + "]");
        Device device = DeviceManager.getInstance().getDevice(xDevice.getMacAddress());
        if (device != null) {
            for (XLinkDataPoint dataPoint : list) {
                XLinkDataPoint data = device.getDataPointByIndex(dataPoint.getIndex(), dataPoint.getType());
                if (data != null) {
                    data.setValue(dataPoint.getValue());
                }
            }
        }
        //通知界面进行UI变更
//        EventBus.getDefault().post(new DataPointUpdateEvent());
    }

    @Override
    public void onDeviceStateChanged(XDevice xDevice, XDevice.State state) {
        // 受SDK管理的设备（用户未调用`XinkSDK.getDeviceManager().removeDevice(xDevice)`）状态发生改变时
        Log.d(TAG, "onDeviceStateChanged() called with: " + "xDevice = [" + xDevice.getMacAddress() + "], status = [" + state + "]");
        Device device = DeviceManager.getInstance().getDevice(xDevice.getMacAddress());
        if (device == null) {
            Log.w(TAG, "null device state changed : " + xDevice);
            return;
        }
        device.setXDevice(xDevice);
        switch (state) {
            case DETACHED:
                Log.w(TAG, xDevice.getMacAddress() + " : DETACHED ");
                break;
            case DISCONNECTED:
                Log.w(TAG, xDevice.getMacAddress() + " : DISCONNECTED ");
//                EventBus.getDefault().post(new UpdateListEvent());
                break;
            case CONNECTING:
                Log.w(TAG, xDevice.getMacAddress() + " : CONNECTING ");
                break;
            case CONNECTED:
                Log.w(TAG, xDevice.getMacAddress() + " : CONNECTED ");
//                EventBus.getDefault().post(new UpdateListEvent());
                break;
        }
    }

    @Override
    public void onDeviceChanged(XDevice xDevice, XDevice.Event event) {
        Log.d(TAG, "onDeviceChanged() called with: xDevice = [" + xDevice + "], event = [" + event + "]");
        switch (event) {
            case SUBSCRIBE:
                break;
            case UNSUBSCRIBE: {
                Toast.makeText(mContext.get(), xDevice.getMacAddress() + " 已被取消订阅，请刷新列表", Toast.LENGTH_SHORT).show();
                DeviceManager.getInstance().removeDevice(xDevice.getMacAddress());
//                EventBus.getDefault().post(new UpdateListEvent());
            }
            break;
            case INFO:
                break;
            case PROPERTY:
                break;
        }
    }

    /**
     * 用户主动退出
     * USER_LOGOUT,
     * 单点登录。当前用户被踢出
     * SINGLE_SIGN_KICK_OFF,
     *
     * @param reason
     */
    @Override
    public void onUserLogout(LogoutReason reason) {
        // 当 setPassword/setPhone/setEmail 被正确设置，而且用户被下线的时候，会回调这个方法。
        switch (reason) {
            case SINGLE_SIGN_KICK_OFF:
                Log.w(TAG, "SINGLE_SIGN_KICK_OFF");
            case TOKEN_EXPIRED:
                Log.w(TAG, "TOKEN_EXPIRED");

                // 退出登录
                XLinkSDK.logoutAndStop();
                // 清楚demo的用户信息
                UserManager.getInstance().logout();
                DeviceManager.getInstance().clear();
                //跳转登录页面
                Intent intent = new Intent(mContext.get(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.get().startActivity(intent);

//                Toast.makeText(mContext.get(), "别人登录了这个帐号或者Token过期", Toast.LENGTH_SHORT).show();

            case USER_LOGOUT:
                Log.w(TAG, "USER_LOGOUT");
                break;
        }
    }
}
