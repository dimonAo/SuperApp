package wtwd.com.superapp.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.xlink.sdk.v5.module.main.XLinkSDK;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseFragment;
import wtwd.com.superapp.fragment.MainFamilyFragment;
import wtwd.com.superapp.fragment.MainHomeFragment;
import wtwd.com.superapp.fragment.MainMeFragment;
import wtwd.com.superapp.manager.DeviceManager;
import wtwd.com.superapp.manager.UserManager;
import wtwd.com.superapp.util.Utils;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final int MAIN_FAMILY = 0;
    private static final int MAIN_HOME = 1;
    private static final int MAIN_ME = 2;

    private Button btn_main_family;
    private Button btn_main_home;
    private Button btn_main_me;

    private List<Button> buttons = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_main_family = (Button) findViewById(R.id.btn_main_family);
        btn_main_home = (Button) findViewById(R.id.btn_main_home);
        btn_main_me = (Button) findViewById(R.id.btn_main_me);

        addListener();
        initPage();

    }

    private void addListener() {
        buttons.add(btn_main_family);
        buttons.add(btn_main_home);
        buttons.add(btn_main_me);

        btn_main_family.setOnClickListener(this);
//        btn_main_home.setOnClickListener(this);
        btn_main_me.setOnClickListener(this);
    }


    private void initPage() {
        prepareFragment();
        changePage(MAIN_FAMILY);
    }

    public void changePage(int page) {
        updateFragment(page);
    }

    private void prepareFragment() {
        // TODO: 2018/a1/26 0026 添加fragment实例到mFragments集合中
        fragments.add(MainFamilyFragment.getMainFamilyFragment());
        fragments.add(MainHomeFragment.getMainHomeFragment());
        fragments.add(MainMeFragment.getMainMeFragment());
        for (BaseFragment fragment : fragments) {
            getSupportFragmentManager().beginTransaction().add(R.id.relative_content, fragment).hide(fragment).commit();
        }
    }

    /**
     * 切换fragment,且设置button selector状态
     */
    private void updateFragment(int position) {
        if (position > fragments.size() - 1) {
            return;
        }
        for (int i = 0; i < fragments.size(); i++) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BaseFragment fragment = fragments.get(i);
            if (i == position) {
                buttons.get(i).setSelected(true);
                buttons.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorMainBottomTextSelect));

                transaction.show(fragment);
            } else {
                buttons.get(i).setSelected(false);
                buttons.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorMainAtyBottomTextDefault));
                transaction.hide(fragment);
            }
            transaction.commit();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.btn_main_family == id) {
            changePage(MAIN_FAMILY);
            Utils.setStatusBarColor(this, R.color.transparent);
        }
//        else if (R.id.btn_main_home == id) {
//            changePage(MAIN_HOME);
//            Utils.setStatusBarColor(this, R.color.transparent);
////            Utils.setStatusBarColor(this,R.color.transparent);
//        }
        else if (R.id.btn_main_me == id) {
            changePage(MAIN_ME);
            Utils.setStatusBarColor(this, R.color.colorWhite);
            if (0 == Utils.StatusBarLightMode(this)) {
                Utils.setStatusBarColor(this, R.color.alpha_black_5);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doSignOut();
        exitApp();
    }

    public void doSignOut() {
        UserManager.getInstance().logout();
        DeviceManager.getInstance().clear();
        // 停止SDK, 断开云端连接，清除授权信息
        XLinkSDK.logoutAndStop();
//        XLinkSDK.logout();
    }

    public void exitApp() {
        // 停止SDK, 断开云端连接
        XLinkSDK.stop();
    }


}
