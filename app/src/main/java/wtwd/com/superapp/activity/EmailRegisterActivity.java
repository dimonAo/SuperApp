package wtwd.com.superapp.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import cn.xlink.restful.XLinkRestful;
import cn.xlink.restful.XLinkRestfulEnum;
import cn.xlink.restful.api.app.UserAuthApi;
import cn.xlink.sdk.common.StringUtil;
import cn.xlink.sdk.v5.module.main.XLinkErrorCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wtwd.com.superapp.R;
import wtwd.com.superapp.base.BaseActivity;
import wtwd.com.superapp.manager.UserManager;
import wtwd.com.superapp.util.Constant;
import wtwd.com.superapp.util.DialogUtils;
import wtwd.com.superapp.util.Utils;

public class EmailRegisterActivity extends BaseActivity {

    private EditText edit_email;
    private EditText edit_register_password;
    private EditText edit_register_password_again;
    private Button btn_next;
    private Dialog mToastDialog;

    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_email_register;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        text_tool_bar_title.setText("邮箱注册");
        setTitleToolbarStyle(SOLID_COLOR_TITLE,R.color.colorWhite);
        mToastDialog = new Dialog(this, R.style.TextDialog);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_register_password = (EditText) findViewById(R.id.edit_register_password);
        edit_register_password_again = (EditText) findViewById(R.id.edit_register_password_again);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkEmail(getEditEmail())) {
                    if (getEditPassword().length() > 6 && getEditPassword().length() < 25) {
                        if (getEditPassword().equals(getEditPasswordAgain())) {


                            doMailRegister(getEditEmail(), Constant.PREF_KEY_COM_ID, getEditPassword(), "");
                        } else {
                            DialogUtils.showTextDialog(EmailRegisterActivity.this, mToastDialog, "两次输入面不一致");
                        }
                    } else {
                        DialogUtils.showTextDialog(EmailRegisterActivity.this, mToastDialog, "密码长度6-25位");
                    }
                } else {
                    DialogUtils.showTextDialog(EmailRegisterActivity.this, mToastDialog, "请填写正确邮箱");
                }


            }
        });


    }


    private String getEditEmail() {
        return edit_email.getText().toString();
    }

    private String getEditPassword() {
        return edit_register_password.getText().toString();
    }

    private String getEditPasswordAgain() {
        return edit_register_password_again.getText().toString();
    }


    @Override
    public View getSnackView() {
        return null;
    }


    /**
     * 邮箱注册操作
     *
     * @param mail     邮箱号码
     * @param cropid   企业ID
     * @param pwd      密码
     * @param nickName 昵称
     */
    public void doMailRegister(final String mail, final String cropid, String pwd, String nickName) {
        if (StringUtil.isAllNotEmpty(mail, cropid, pwd)) {
            UserAuthApi.EmailRegisterRequest request = new UserAuthApi.EmailRegisterRequest();
            request.email = mail;
            request.corpId = cropid;
            request.password = pwd;
            request.localLang = XLinkRestfulEnum.LocalLang.ZH_CN;
            request.source = XLinkRestfulEnum.UserSource.ANDROID;
            request.nickname = nickName;

            Call<UserAuthApi.EmailRegisterResponse> call = XLinkRestful.getApplicationApi().registEmailAccount(request);
            call.enqueue(new RegisterCallback<UserAuthApi.EmailRegisterResponse>() {
                @Override
                protected boolean saveResult(UserAuthApi.EmailRegisterResponse result) {
                    if (mail.equals(result.email)) {
                        //注册成功,缓存数据
                        UserManager.getInstance().setCorpId(cropid);
                        UserManager.getInstance().setAccount(mail);
                        UserManager.getInstance().setPassword(getEditPassword());
                        readyGo(RegisterSuccessActivity.class);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } else {

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

            final String errorMsg = getErrorMsg(response, null);
            final boolean finalResult = isSuccess;

            if (finalResult) {

            }


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
            final String finalMsg = msg;
            DialogUtils.showTextDialog(EmailRegisterActivity.this, mToastDialog, msg);
//            getView().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    getView().showRegisterResult(false, null, finalMsg);
//                }
//            });
        }
    }

    private String getErrorMsg(Response response, Throwable t) {
        String msg = "请求出错,请稍后重试";
        if (t != null) {
            t.printStackTrace();
        }
        if (response.errorBody() != null) {
            String error = "unknowError";
            try {
                error = response.errorBody().string();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.e("Register response error", error);
            if (!"null".equals(error)) {
                try {
                    JSONObject object = new JSONObject(error);
                    if (object.has("error")) {
                        JSONObject errorObj = object.getJSONObject("error");
                        if (errorObj != null && errorObj.has("code")) {
                            int code = errorObj.getInt("code");
                            if (code == XLinkErrorCode.ERROR_API_CORP_NOT_EXISTS.getValue()) {
                                msg = "企业不存在,请重新确认";
                            } else if (code == XLinkErrorCode.ERROR_API_REGISTER_PHONE_EXISTS.getValue()) {
                                msg = "该手机已注册,请使用该邮箱登录";
                            } else if (code == XLinkErrorCode.ERROR_API_REGISTER_EMAIL_EXISTS.getValue()) {
                                msg = "该邮箱已注册,请使用该邮箱登录";
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return msg;
    }


}
