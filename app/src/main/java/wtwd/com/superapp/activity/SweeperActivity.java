package wtwd.com.superapp.activity;

import android.content.Context;
import android.media.MediaDrm;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.util.Utils;

public class SweeperActivity extends BaseActivity implements View.OnClickListener {

    private static final int BTN_AUTO = 0;
    private static final int BTN_FIX_POINT = 1;
    private static final int BTN_SIDE = 2;
    private static final int BTN_PLANNED = 3;
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

    private List<Button> mModeButton = new ArrayList<>();
    private List<Button> mStrongButton = new ArrayList<>();
    private List<Button> mDirectionButton = new ArrayList<>();

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


        addData();
    }

    private void addData() {
        mModeButton.clear();
        mStrongButton.clear();
        mDirectionButton.clear();

        mModeButton.add(btn_auto);
        mModeButton.add(btn_fix_point);
        mModeButton.add(btn_side);
        mModeButton.add(btn_planned);
        mModeButton.add(btn_mop);
        mModeButton.add(btn_recharge);


        mStrongButton.add(btn_close);
        mStrongButton.add(btn_standard);
        mStrongButton.add(btn_strong);

        mDirectionButton.add(btn_top);
        mDirectionButton.add(btn_right);
        mDirectionButton.add(btn_down);
        mDirectionButton.add(btn_left);

        updateText(BTN_AUTO);
        updateStrongMode(BTN_CLOSE);
        setClearState(false);

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

    private void updateText(int position) {
        if (position > mModeButton.size() - 1) {
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
        int id = v.getId();

        switch (id) {
            case R.id.btn_auto:
                updateText(BTN_AUTO);
                break;
            case R.id.btn_fix_point:
                updateText(BTN_FIX_POINT);
                break;

            case R.id.btn_side:
                updateText(BTN_SIDE);
                break;

            case R.id.btn_planned:
                updateText(BTN_PLANNED);
                break;
            case R.id.btn_mop:
                updateText(BTN_MOP);
                break;
            case R.id.btn_recharge:
                updateText(BTN_RECHARGE);
                break;

            case R.id.btn_close:
                updateStrongMode(BTN_CLOSE);
                break;
            case R.id.btn_standard:
                updateStrongMode(BTN_STANDARD);
                break;
            case R.id.btn_strong:
                updateStrongMode(BTN_STRONG);
                break;


            case R.id.btn_top:
                updateDirection(BTN_TOP);
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
                } else {
//                    img_btn_center.setSelected(true);
                    setClearState(true);
                }
                break;
        }
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
}
