package wtwd.com.superapp.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

public class PhoneRegisterActivity extends BaseActivity {
    private EditText edit_register_phone_number;
    private EditText edit_register_verifycode;
    private EditText edit_new_password;
    private EditText edit_new_password_again;

    private Button btn_next;
    private Button btn_send_verify;

    private Dialog mSelecDialog;
    private TimeCount time;

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_send_verify.setText(String.format(getResources().getString(R.string.register_retest), (millisUntilFinished / 1000)));
            btn_send_verify.setTextColor(ContextCompat.getColor(PhoneRegisterActivity.this, R.color.colorWhite));
            btn_send_verify.setBackground(ContextCompat.getDrawable(PhoneRegisterActivity.this, R.drawable.shape_gery_btn_bg));
            btn_send_verify.setEnabled(false);


        }

        @Override
        public void onFinish() {
            btn_send_verify.setText(getString(R.string.register_verify_code_text));
            btn_send_verify.setTextColor(ContextCompat.getColor(PhoneRegisterActivity.this, R.color.colorWhite));
            btn_send_verify.setBackground(ContextCompat.getDrawable(PhoneRegisterActivity.this, R.drawable.btn_register_choose_selector));
            btn_send_verify.setEnabled(true);
        }
    }


    @Override
    public void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.arrow_grey);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_phone_register;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        int mCodeTimeCount = 60 * 1000;
        time = new TimeCount(mCodeTimeCount, 1000);
        setTitleToolbarStyle(SOLID_COLOR_TITLE, R.color.colorWhite);
        text_tool_bar_title.setText("手机号注册");

        mSelecDialog = new Dialog(this, R.style.TextDialog);

        edit_register_phone_number = (EditText) findViewById(R.id.edit_register_phone_number);
        edit_register_verifycode = (EditText) findViewById(R.id.edit_register_verifycode);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_send_verify = (Button) findViewById(R.id.btn_send_verify);

        edit_new_password = (EditText) findViewById(R.id.edit_new_password);
        edit_new_password_again = (EditText) findViewById(R.id.edit_new_password_again);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Utils.checkPhoneNumber(getPhone()) && !TextUtils.isEmpty(getVerify())) {
//                    checkVerifyCode(getPhone(), getVerify());
//                }
                if (Utils.checkPhoneNumber(getPhone()) && !TextUtils.isEmpty(getVerify()) && checkPassword()) {

                    doPhoneRegister("+86", getPhone(), getVerify(), Constant.PREF_KEY_COM_ID, getpassword(), "");
                }
            }
        });

        btn_send_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkPhoneNumber(getPhone())) {
                    time.start();
                    sendVerifyCode("+86", getPhone(), Constant.PREF_KEY_COM_ID);
                }
            }
        });

    }

    @Override
    public View getSnackView() {
        return null;
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

    private String getPhone() {
        return edit_register_phone_number.getText().toString();
    }

    private String getVerify() {
        return edit_register_verifycode.getText().toString();
    }


    private void checkVerifyCode(final String phone, String verifycode) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("corp_id", Constant.PREF_KEY_COM_ID);
        mMap.put("phone", phone);
        mMap.put("phone_zone", "+86");
        mMap.put("verifycode", verifycode);


        OkHttpUtils.post()
                .url(Constant.CHECK_VERIFY_CODE)
                .addHeader("Content-Type", "application/json")
                .params(mMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Log.e("TAG", "e: " + e.toString());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "response : " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String ver = object.optString("verifycode");
                            Bundle bundle = new Bundle();
                            bundle.putString("verifycode", ver);
                            bundle.putString("phone", phone);
                            readyGo(NewPasswordActivity.class, bundle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /**
     * 发送手机验证码,仅支持中国大陆手机<br>
     * 发送手机验证码实际上存在可能反复发送的情况,如果短时间内反复发送需要进行图片验证码校验,此处没有进行图片验证码的操作
     *
     * @param phone  手机号
     * @param cropId 企业ID
     */
    public void sendVerifyCode(String phoneZone, String phone, String cropId) {
        if (StringUtil.isAllNotEmpty(phoneZone, cropId, phone)) {
            UserAuthApi.RegisterVerifyCodeRequest request = new UserAuthApi.RegisterVerifyCodeRequest();
            //中国大陆手机
            request.phoneZone = phoneZone;
            request.phone = phone;
            request.corpId = cropId;
            //图片验证码,不一定需要,当有图片验证时才需要
//            request.captcha="xxx";
            Call<String> call = XLinkRestful.getApplicationApi().registerPhoneVerifyCode(request);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    boolean isSuccess = false;
                    if (response.isSuccessful()) {
                        isSuccess = true;
                    }

                    if (isSuccess) {
                        DialogUtils.showTextDialog(PhoneRegisterActivity.this, mSelecDialog, "发送验证码成功");
                    } else {
                        DialogUtils.showTextDialog(PhoneRegisterActivity.this, mSelecDialog, "发送验证码失败");
                    }

//                    //界面存在时才进行界面通知与刷新
//                    if (!isViewExisted()) {
//                        return;
//                    }
//                    final boolean finalResult = isSuccess;
//                    getView().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //通知发送结果
//                            getView().showSendCodeResult(finalResult, null, null);
//                        }
//                    });


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    String msg = "验证码发送失败";
//                    if (t != null && t.getMessage() != null) {
//                        msg = t.getMessage();
//                    }
//                    if (!isViewExisted()) {
//                        return;
//                    }
//                    final String finalMsg = msg;
//                    getView().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            getView().showSendCodeResult(false, null, finalMsg);
//                        }
//                    });
                    DialogUtils.showTextDialog(PhoneRegisterActivity.this, mSelecDialog, msg);
                }
            });
        } else {

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
                        DialogUtils.showTextDialog(PhoneRegisterActivity.this, mSelecDialog, "注册成功");

                        UserManager.getInstance().setAccount(phone);
                        UserManager.getInstance().setPassword(pwd);

                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isPhone", true);
                        readyGo(RegisterSuccessActivity.class, bundle);
                        return true;
                    } else {
//                        getErrorMsg(result);
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
            final String errorMsg = getErrorMsg(response, null);
            DialogUtils.showTextDialog(PhoneRegisterActivity.this, mSelecDialog, errorMsg);
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
            final String finalMsg = msg;
            DialogUtils.showTextDialog(PhoneRegisterActivity.this, mSelecDialog, finalMsg);
//            getView().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    getView().showRegisterResult(false, null, finalMsg);
//                }
//            });


        }
    }

}
