package wtwd.com.superapp.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseFragment;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class MainHomeFragment extends BaseFragment {
    private static MainHomeFragment mInstance;

    public static MainHomeFragment getMainHomeFragment() {
        if (null == mInstance) {
            mInstance = new MainHomeFragment();
        }
        return mInstance;
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_main_home;
    }

    @Override
    public void initFragmentView(View mView) {

    }
}
