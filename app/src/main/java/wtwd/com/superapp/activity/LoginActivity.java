package wtwd.com.superapp.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhy.http.okhttp.OkHttpUtils;

import cn.xlink.restful.api.app.UserAuthApi;
import cn.xlink.sdk.common.CommonUtil;
import cn.xlink.sdk.common.StringUtil;
import cn.xlink.sdk.v5.listener.XLinkTaskListener;
import cn.xlink.sdk.v5.module.http.XLinkUserAuthorizeTask;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import cn.xlink.sdk.v5.module.main.XLinkSDK;
import okhttp3.internal.Util;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.manager.UserManager;
import wtwd.com.superapp.util.Constant;
import wtwd.com.superapp.util.DialogUtils;
import wtwd.com.superapp.util.Utils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText edit_phone_number;
    private EditText edit_password;
    private Button btn_login;
    private Button btn_register;

    private Dialog mTextDialog;

    @Override
    public void initToolBar(Toolbar toolbar) {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {

        mTextDialog = new Dialog(this, R.style.TextDialog);

        edit_phone_number = (EditText) findViewById(R.id.edit_phone_number);
        edit_password = (EditText) findViewById(R.id.edit_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        String password = UserManager.getInstance().getPassword();
        String account = UserManager.getInstance().getAccount();
        if (StringUtil.isAllNotEmpty(password, account)) {
            edit_phone_number.setText(account);
            edit_password.setText(password);
//            doLogin(Constant.PREF_KEY_COM_ID, account, password);
        }
        addListener();
    }

    private void addListener() {
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private String getLoginAccount() {
        return edit_phone_number.getText().toString();
    }

    private String getLoginPassword() {
        return edit_password.getText().toString();
    }


    @Override
    public View getSnackView() {
        return btn_login;
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_login == v.getId()) {
            doLogin(Constant.PREF_KEY_COM_ID, getLoginAccount(), getLoginPassword());
        } else if (R.id.btn_register == v.getId()) {
            readyGo(ChooseRegisterActivity.class);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        XLinkSDK.start();

    }

    /**
     * 登录
     *
     * @param corpId
     * @param account
     * @param pwd
     */
    public void doLogin(String corpId, String account, String pwd) {
        if (StringUtil.isAllNotEmpty(corpId, account, pwd) && Utils.checkAccount(account)) {
            if (Utils.checkPhoneNumber(account)) {
                login(corpId, account, null, pwd);
                return;
            }

            if (Utils.checkEmail(account)) {
                login(corpId, null, account, pwd);
                return;
            }
            DialogUtils.showTextDialog(this, mTextDialog, getString(R.string.prompt_invaild_input));
        } else {
            DialogUtils.showTextDialog(this, mTextDialog, getString(R.string.prompt_invaild_input));
        }
    }

    /**
     * 执行用户验证
     */
    private void login(final String corpId, String phone, String email, final String password) {
        final String account = TextUtils.isEmpty(phone) ? email : phone;

        UserManager.getInstance().setCorpId(corpId);
        UserManager.getInstance().setAccount(account);

        XLinkUserAuthorizeTask task = XLinkUserAuthorizeTask.newBuilder()
                .setPhone(account.toLowerCase(), password)
                .setEmail(account.toLowerCase(), password)
                .setCorpId(corpId)
                .setListener(new XLinkTaskListener<UserAuthApi.UserAuthResponse>() {
                    @Override
                    public void onError(XLinkErrorCode errorCode) {
                        showLogError(errorCode);
                    }

                    @Override
                    public void onStart() {


                    }

                    @Override
                    public void onComplete(UserAuthApi.UserAuthResponse result) {
                        XLinkSDK.start();
                        // app要保存好授权信息，下次打开app跳过登陆的步骤
                        UserManager.getInstance().setUid(result.userId);
                        UserManager.getInstance().setAccessToken(result.accessToken);
                        UserManager.getInstance().setAuthString(result.authorize);
                        UserManager.getInstance().setRefreshToken(result.refreshToken);
                        UserManager.getInstance().setPassword(password);

                        readyGo(MainActivity.class);
                        finish();
                    }
                })
                .build();
        XLinkSDK.startTask(task);
    }


    void showLogError(XLinkErrorCode code) {
        String account = UserManager.getInstance().getAccount();
        String tips;
        switch (code) {
            case ERROR_API_ACCOUNT_VAILD_ERROR:
                tips = getString(R.string.error_account_invalid, account);
                break;
            case ERROR_API_ACCOUNT_PASSWORD_ERROR:
                tips = getString(R.string.error_account_or_pwd_invalid);
                break;
            case ERROR_API_USER_NOT_EXISTS:
                if (Utils.checkEmail(account)) {
                    tips = getString(R.string.error_email_none_register, account);
                    break;
                }
                if (Utils.checkPhoneNumber(account)) {
                    tips = getString(R.string.error_phone_none_register, account);
                    break;
                }
            default:
                tips = getString(R.string.error_login_failure);
                break;
        }
        DialogUtils.showTextDialog(this, mTextDialog, tips);

    }


}
