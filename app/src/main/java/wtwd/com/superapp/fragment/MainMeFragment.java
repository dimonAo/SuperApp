package wtwd.com.superapp.fragment;

import android.support.v4.content.ContextCompat;
import android.view.View;

import wtwd.com.superapp.R;
import wtwd.com.superapp.activity.MeSetActivity;
import wtwd.com.superapp.base.BaseFragment;
import wtwd.com.superapp.util.Utils;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class MainMeFragment extends BaseFragment implements View.OnClickListener {
    private static MainMeFragment mInstance;

    public static MainMeFragment getMainMeFragment() {
        if (null == mInstance) {
            mInstance = new MainMeFragment();
        }
        return mInstance;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_main_me;
    }


    @Override
    public void initFragmentView(View mView) {
//        tool_bar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
//        Utils.setWindowStatusBarColor(getActivity(), R.color.colorWhite);
//        Utils.setStatusBarColor(getActivity(),R.color.colorWhite);
        text_tool_bar_title.setText("");
        img_tool_bar_right.setImageResource(R.mipmap.set);
        img_tool_bar_right.setVisibility(View.VISIBLE);

        addListener();
    }

    private void addListener() {
        img_tool_bar_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.img_tool_bar_right == v.getId()) {
            readyGo(MeSetActivity.class);
        }
    }
}
