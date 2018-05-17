package wtwd.com.superapp.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.util.ErrorDialogFragments;

import cn.xlink.restful.XLinkRestful;
import cn.xlink.restful.XLinkRestfulEnum;
import cn.xlink.restful.api.app.UserAuthApi;
import cn.xlink.sdk.common.StringUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.manager.UserManager;
import wtwd.com.superapp.util.Constant;
import wtwd.com.superapp.util.DialogUtils;
import wtwd.com.superapp.util.Utils;

public class NewPasswordActivity extends BaseActivity {
    private EditText edit_new_password;
    private EditText edit_new_password_again;

    private Button btn_commit;
    private String phone, ver;
    private Dialog mDialog;

    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_new_password;
    }

    private void getData() {
        if (null == getIntent()) {
            return;
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            ver = bundle.getString("ver");
        }
    }


    @Override
    public void onCreateView(Bundle saveInstanceState) {
        text_tool_bar_title.setText("设置新密码");
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);
        mDialog = new Dialog(this, R.style.TextDialog);
        edit_new_password = (EditText) findViewById(R.id.edit_new_password);
        edit_new_password_again = (EditText) findViewById(R.id.edit_new_password_again);
        btn_commit = (Button) findViewById(R.id.btn_commit);

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkPhoneNumber(phone) && !TextUtils.isEmpty(ver) && checkPassword()) {

                    doPhoneRegister("+86", phone, ver, Constant.PREF_KEY_COM_ID, getpassword(), "");
                } else {
                    DialogUtils.showTextDialog(NewPasswordActivity.this, mDialog, "请检查输入是否正确");
                }

            }
        });

        getData();

    }


    private String getpassword() {
        return edit_new_password.getText().toString();
    }

    private boolean checkPassword() {
        if (TextUtils.isEmpty(getpassword())) {
            return false;
        }

        if (getpassword().length() < 6 || getpassword().length() > 25) {
            return false;
        }

        if (!getpassword().equals(edit_new_password_again.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    @Override
    public View getSnackView() {
        return null;
    }


    /**
     * 手机注册操作
     *
     * @param phone    手机号码
     * @param captcha  验证码
     * @param cropid   企业ID
     * @param pwd      密码
     * @param nickName 昵称
     */
    public void doPhoneRegister(String phoneZone, final String phone, String captcha, final String cropid, final String pwd, String nickName) {
        if (StringUtil.isAllNotEmpty(phoneZone, phone, captcha, cropid, pwd)) {
            UserAuthApi.PhoneRegisterRequest request = new UserAuthApi.PhoneRegisterRequest();
            request.phoneZone = phoneZone;
            request.phone = phone;
            request.corpId = cropid;
            request.verifycode = captcha;
            request.localLang = XLinkRestfulEnum.LocalLang.ZH_CN;
            request.source = XLinkRestfulEnum.UserSource.ANDROID;
            request.password = pwd;
            request.nickname = nickName;


            Call<UserAuthApi.PhoneRegisterResponse> call = XLinkRestful.getApplicationApi().registPhoneAccount(request);
            call.enqueue(new RegisterCallback<UserAuthApi.PhoneRegisterResponse>() {
                @Override
                protected boolean saveResult(UserAuthApi.PhoneRegisterResponse result) {
                    if (phone.equals(result.phone)) {
                        //注册成功,缓存相关信息
//                        UserManager.getInstance().setCorpId(cropid);
                        UserManager.getInstance().setAccount(phone);
                        UserManager.getInstance().setPassword(pwd);
                        readyGo(LoginActivity.class);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } else {
//            getView().showPromptDialog(R.string.prompt_title, R.string.prompt_invaild_input);
        }
    }


    private abstract class RegisterCallback<T> implements Callback<T> {
        protected abstract boolean saveResult(T result);

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            boolean isSuccess = false;
            if (response.isSuccessful()) {
                T result = response.body();
                if (result != null) {
                    isSuccess = saveResult(result);
                }
            }
//            if (!isViewExisted()) {
//                return;
//            }
//
//            final String errorMsg = getErrorMsg(response, null);
//            final boolean finalResult = isSuccess;
//            getView().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String msg;
//                    if (finalResult) {
//                        //注册成功
//                        msg = "注册成功";
//                    } else {
//                        //注册失败
//                        msg = errorMsg;
//                    }
//                    getView().showRegisterResult(finalResult, null, msg);
//                }
//            });
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            //注册失败
            String msg = "请校验提交信息再重新尝试注册";
            if (t != null && t.getMessage() != null) {
                msg = t.getMessage();
            }
//            if (!isViewExisted()) {
//                return;
//            }
//            final String finalMsg = msg;
//            getView().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    getView().showRegisterResult(false, null, finalMsg);
//                }
//            });


        }
    }


}
