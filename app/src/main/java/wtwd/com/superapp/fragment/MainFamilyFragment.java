package wtwd.com.superapp.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xlink.restful.api.app.UserAuthApi;
import cn.xlink.sdk.v5.listener.XLinkTaskListener;
import cn.xlink.sdk.v5.model.XDevice;
import cn.xlink.sdk.v5.module.http.XLinkSyncDeviceListTask;
import cn.xlink.sdk.v5.module.http.XLinkUserAuthorizeTask;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import cn.xlink.sdk.v5.module.main.XLinkSDK;
import io.xlink.wifi.sdk.XlinkAgent;
import wtwd.com.superapp.R;
import wtwd.com.superapp.activity.ChooseDeviceTypeActivity;
import wtwd.com.superapp.activity.SweeperActivity;
import wtwd.com.superapp.adapter.MainFamilyAdapter;
import wtwd.com.superapp.base.BaseFragment;
import wtwd.com.superapp.entity.Device;
import wtwd.com.superapp.entity.DeviceEntity;
import wtwd.com.superapp.manager.DeviceManager;
import wtwd.com.superapp.manager.UserManager;
import wtwd.com.superapp.util.Constant;
import wtwd.com.superapp.util.Utils;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class MainFamilyFragment extends BaseFragment implements View.OnClickListener {

    private static MainFamilyFragment mInstance;
    private RecyclerView recycler_device;
    private LinearLayout lin_add;

    private MainFamilyAdapter mAdapter;
    private List<Device> mDeviceEntirys = new ArrayList<>();


    public static MainFamilyFragment getMainFamilyFragment() {
        if (null == mInstance) {
            mInstance = new MainFamilyFragment();
        }
        return mInstance;
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_main_family;
    }


    /**
     * type:
     * 1.灯
     * 2.空调
     * 3.电饭锅
     * 4.扫地机
     * 5.净化器
     * 6.插座
     * 7.冰箱
     * <p>
     * online:
     * 0.离线
     * 1.在线
     */

    private void displayDeviceList() {
//        mDeviceEntirys.clear();
//
//        DeviceEntity en = new DeviceEntity();
//        en.setDevice_name("扫地boy");
//        en.setDevice_type(4);
//        en.setOn_line(1);
//        en.setPosition_name("客厅");
//        mDeviceEntirys.add(en);

//        for (int i = 1; i < 11; i++) {
//            DeviceEntity en = new DeviceEntity();
//            if (8 > i) {
//                en.setDevice_type(i);
//            } else {
//                en.setDevice_type(i - 7);
//            }
//
//            switch (i) {
//                case 1:
//                    en.setDevice_name("小夜灯");
//                    en.setOn_line(0);
//                    en.setPosition_name("卧室");
//                    break;
//
//                case 2:
//                    en.setDevice_name("空调1.5");
//                    en.setOn_line(1);
//                    en.setPosition_name("主卧室");
//                    break;
//
//                case 3:
//                    en.setDevice_name("厨房");
//                    en.setOn_line(0);
//                    en.setPosition_name("九阳电饭锅");
//                    break;
//
//                case 4:
//                    en.setDevice_name("扫地boy");
//                    en.setOn_line(1);
//                    en.setPosition_name("客厅");
//                    break;
//
//                case 5:
//                    en.setDevice_name("净化器");
//                    en.setOn_line(1);
//                    en.setPosition_name("客厅");
//                    break;
//
//                case 6:
//                    en.setDevice_name("三孔插座");
//                    en.setOn_line(0);
//                    en.setPosition_name("书房");
//                    break;
//
//                case 7:
//                    en.setDevice_name("三菱冰箱");
//                    en.setOn_line(1);
//                    en.setPosition_name("厨房");
//                    break;
//
//                case 8:
//                    en.setDevice_name("吊灯");
//                    en.setOn_line(1);
//                    en.setPosition_name("阳台");
//                    break;
//
//                case 9:
//                    en.setDevice_name("空调2.0");
//                    en.setOn_line(0);
//                    en.setPosition_name("客厅");
//                    break;
//
//                case 10:
//                    en.setDevice_name("厨房");
//                    en.setOn_line(1);
//                    en.setPosition_name("美的电饭锅");
//                    break;
//            }
//
//            mDeviceEntirys.add(en);
//        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initFragmentView(View mView) {
//        Utils.setStatusBarColor(getActivity(),R.color.transparent);
//        Utils.setStatusBarColor(getActivity(),R.color.colorWhite);
        XLinkSDK.start();

        Utils.setStatusBarColor(getActivity(), R.color.transparent);
        lin_add = (LinearLayout) mView.findViewById(R.id.lin_add);

        recycler_device = (RecyclerView) mView.findViewById(R.id.recycler_device);
        mAdapter = new MainFamilyAdapter(R.layout.item_main_family_device_display, null);

        recycler_device.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        DividerItemDecoration mItem = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        mItem.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.transparent_devider));
        recycler_device.addItemDecoration(mItem);
        recycler_device.setAdapter(mAdapter);

        displayDeviceList();

        addListener();
    }

    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                readyGo(SweeperActivity.class);
            }
        });

        lin_add.setOnClickListener(this);

//        loginUser("aowending@waterworld.com.cn", "Wtwd123456");
        login(Constant.PREF_KEY_COM_ID, "", "aowending@waterworld.com.cn", "Wtwd123456");

    }

    /**
     * 执行用户验证
     */
    private void login(final String corpId, String phone, String email, final String password) {
        final String account = TextUtils.isEmpty(phone) ? email : phone;

        UserManager.getInstance().setCorpId(corpId);
        UserManager.getInstance().setAccount(account);

        XLinkUserAuthorizeTask task = XLinkUserAuthorizeTask.newBuilder()
                .setPhone(account.toLowerCase(), password) // 这里没有判断帐号类型，都设置上也行
                .setEmail(account.toLowerCase(), password) // 这里没有判断帐号类型，都设置上也行
                .setCorpId(corpId)
                .setListener(new XLinkTaskListener<UserAuthApi.UserAuthResponse>() {
                    @Override
                    public void onError(XLinkErrorCode errorCode) {

                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(UserAuthApi.UserAuthResponse result) {
                        Log.e("TAG", "login success");

                        // app要保存好授权信息，下次打开app跳过登陆的步骤
                        UserManager.getInstance().setUid(result.userId);
                        UserManager.getInstance().setAccessToken(result.accessToken);
                        UserManager.getInstance().setAuthString(result.authorize);
                        UserManager.getInstance().setRefreshToken(result.refreshToken);

                    }
                })
                .build();
        XLinkSDK.startTask(task);
    }


    @Override
    public void onClick(View v) {
        if (R.id.lin_add == v.getId()) {
            readyGo(ChooseDeviceTypeActivity.class);
        }
    }

    private void syncDeviceListTask() {

        DeviceManager.getInstance().refreshDeviceList(new XLinkTaskListener<List<XDevice>>() {
            @Override
            public void onError(XLinkErrorCode xLinkErrorCode) {
                //刷新失败
                Log.e("TAG", "刷新失败 value ：" + xLinkErrorCode.getValue());
                Log.e("TAG", "刷新失败 name ：" + xLinkErrorCode.name());
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(List<XDevice> xDevices) {
                //刷新成功
//                refreshAdapter();

                Log.e("TAG", "xDevices size : " + xDevices.size());
                mAdapter.getData().addAll(DeviceManager.getInstance().getAllDevices());


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        syncDeviceListTask();
    }
}
