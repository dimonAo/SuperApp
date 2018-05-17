package wtwd.com.superapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;

public class ChooseRegisterActivity extends BaseActivity {

    private RelativeLayout relative_phone;
    private RelativeLayout relative_email;


    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_choose_register;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        text_tool_bar_title.setText("选择注册方式");
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);
        relative_email = (RelativeLayout) findViewById(R.id.relative_email);
        relative_phone = (RelativeLayout) findViewById(R.id.relative_phone);

        relative_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            readyGo(EmailRegisterActivity.class);
            }
        });


        relative_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(PhoneRegisterActivity.class);
            }
        });

    }

    @Override
    public View getSnackView() {
        return null;
    }
}
