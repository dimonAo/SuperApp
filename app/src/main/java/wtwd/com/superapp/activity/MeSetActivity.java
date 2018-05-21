package wtwd.com.superapp.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import cn.xlink.sdk.v5.module.main.XLinkSDK;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;

public class MeSetActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_login_out;

    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_me_set;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        setTitleToolbarStyle(PURE_PICTURE_TITLE, R.color.colorWhite);
        text_tool_bar_title.setText("设置");

        btn_login_out = (Button) findViewById(R.id.btn_login_out);

        addListener();
    }

    private void addListener() {
        btn_login_out.setOnClickListener(this);
    }

    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_login_out == v.getId()) {
//            XLinkSDK.logoutAndStop();
//            readyGo(LoginActivity.class);

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}
