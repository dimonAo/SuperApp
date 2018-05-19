package wtwd.com.superapp.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
import wtwd.com.superapp.eventbus.DataPointUpdateEvent;
import wtwd.com.superapp.eventbus.UpdateListEvent;
import wtwd.com.superapp.manager.DeviceManager;
import wtwd.com.superapp.manager.UserManager;
import wtwd.com.superapp.sweepmap.SweepMap;
import wtwd.com.superapp.util.Constant;
import wtwd.com.superapp.util.Utils;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class MainFamilyFragment extends BaseFragment implements View.OnClickListener, OnTabSelectListener {

    private static MainFamilyFragment mInstance;
    private RecyclerView recycler_device;
    private LinearLayout lin_add;
    private TabLayout tab_layout_room;
    private ViewPager viewpager_room;
    private SlidingTabLayout tl_2;

    private ArrayList<DeviceListFragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"默认房间"};

    private MainFamilyAdapter mAdapter;
    private ArrayList<Device> mDeviceEntirys = new ArrayList<>();


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

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initFragmentView(View mView) {
//        Utils.setStatusBarColor(getActivity(),R.color.transparent);
//        Utils.setStatusBarColor(getActivity(),R.color.colorWhite);
        XLinkSDK.start();
//        Utils.setStatusBarColor(getActivity(), R.color.transparent);

        for (String mTitle : mTitles) {
            mFragments.add(DeviceListFragment.getInstance(mTitle));
        }

        lin_add = (LinearLayout) mView.findViewById(R.id.lin_add);
        tl_2 = (SlidingTabLayout) mView.findViewById(R.id.tl_2);
        viewpager_room = (ViewPager) mView.findViewById(R.id.viewpager_room);
        mViewPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewpager_room.setAdapter(mViewPagerAdapter);
        tl_2.setViewPager(viewpager_room);
        tl_2.setOnTabSelectListener(this);

        recycler_device = (RecyclerView) mView.findViewById(R.id.recycler_device);
        mAdapter = new MainFamilyAdapter(R.layout.item_main_family_device_display, mDeviceEntirys);

        recycler_device.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        DividerItemDecoration mItem = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        mItem.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.transparent_devider));
        recycler_device.addItemDecoration(mItem);
        recycler_device.setAdapter(mAdapter);

//        displayDeviceList();

        addListener();
    }

    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putString("device", mDeviceEntirys.get(position).getXDevice().getMacAddress());
                readyGo(SweeperActivity.class, bundle);
            }
        });

        lin_add.setOnClickListener(this);

//        loginUser("aowending@waterworld.com.cn", "Wtwd123456");


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
//                        syncDeviceListTask();
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
                mDeviceEntirys.clear();
                Log.e("TAG", "xDevices size : " + xDevices.size());
                mDeviceEntirys.addAll(DeviceManager.getInstance().getAllDevices());
//                mAdapter.notifyDataSetChanged();
                if (mFragments.size() > 0) {
                    mFragments.get(0).setData(mDeviceEntirys);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        syncDeviceListTask();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG", "MainFamily fragment onstart");
        EventBus.getDefault().register(this);
//        login(Constant.PREF_KEY_COM_ID, "", "cminyan@waterworld.com.cn", "wtwd123456");
//        login(Constant.PREF_KEY_COM_ID, "", "zxiaobin@waterworld.com.cn", "Wtwd123456");
        syncDeviceListTask();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("TAG", "MainFamily fragment onstop");
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateListEvent(UpdateListEvent event) {
        Log.e("TAG", "UpdateListEvent");
        mDeviceEntirys.clear();
        mDeviceEntirys.addAll(DeviceManager.getInstance().getAllDevices());
        mAdapter.notifyDataSetChanged();
    }

    private MyPagerAdapter mViewPagerAdapter;


    /**
     * 切换标签的回调方法
     *
     * @param position
     */
    @Override
    public void onTabSelect(int position) {

        Log.e("TAG", "on tab select : " + mTitles[position]);
//        mFragments.add(DeviceListFragment.getInstance(mTitles[position]));
//        mFragments.get(position).setData(mDeviceEntirys);
    }

    @Override
    public void onTabReselect(int position) {
        Log.e("TAG", "onTabReselect : " + mTitles[position]);
//        mFragments.get(position).setData(mDeviceEntirys);
    }

    @Override
    public void onPageSelect(int position) {
        Log.e("SlidingTab", "onPageSelected : " + position);
        mFragments.get(position).setData(mDeviceEntirys);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
