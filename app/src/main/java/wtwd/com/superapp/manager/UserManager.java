package wtwd.com.superapp.manager;

import android.content.Context;

import java.lang.ref.WeakReference;

import wtwd.com.superapp.util.Constant;
import wtwd.com.superapp.util.PrefUtil;


/**
 * 用户管理
 */
public class UserManager {

    private WeakReference<Context> mContext;

    public Context getContext() {
        if (mContext == null || mContext.get() == null) {
            throw new NullPointerException("context is null");
        }
        return mContext.get();
    }

    public void init(Context context) {
        mContext = new WeakReference<>(context);
    }

    private UserManager() {
    }

    public void setCorpId(String corpId) {
        PrefUtil.setStringValue(getContext(), Constant.PREF_KEY_CORP_ID, corpId);
    }

    public String getCorpId() {
        return PrefUtil.getStringValue(getContext(), Constant.PREF_KEY_CORP_ID, "");
    }

    public String getAccessToken() {
        return PrefUtil.getStringValue(getContext(), Constant.PREF_KEY_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        PrefUtil.setStringValue(getContext(), Constant.PREF_KEY_ACCESS_TOKEN, accessToken);
    }

    public String getRefreshToken() {
        return PrefUtil.getStringValue(getContext(), Constant.PREF_KEY_REFRESH_TOKEN, "");
    }

    public void setRefreshToken(String refreshToken) {
        PrefUtil.setStringValue(getContext(), Constant.PREF_KEY_REFRESH_TOKEN, refreshToken);
    }

    public String getAuthString() {
        return PrefUtil.getStringValue(getContext(), Constant.PREF_KEY_AUTH_CODE, "");
    }

    public void setAuthString(String authString) {
        PrefUtil.setStringValue(getContext(), Constant.PREF_KEY_AUTH_CODE, authString);
    }

    public int getUid() {
        return PrefUtil.getIntValue(getContext(), Constant.PREF_KEY_USER_ID, 0);
    }

    public void setUid(int uid) {
        PrefUtil.setIntValue(getContext(), Constant.PREF_KEY_USER_ID, uid);
    }

    public String getAccount() {
        return PrefUtil.getStringValue(getContext(), Constant.PREF_KEY_ACCOUNT, "");
    }

    public void setAccount(String account) {
        PrefUtil.setStringValue(getContext(), Constant.PREF_KEY_ACCOUNT, account);
    }

    public void logout() {
        Context context = mContext.get();
        PrefUtil.clear(context, Constant.class, "PREF_KEY", new String[]{Constant.PREF_KEY_ACCOUNT, Constant.PREF_KEY_CORP_ID, Constant.PREF_KEY_PRODUCT_ID});
    }

    public void setPassword(String password) {
        PrefUtil.setStringValue(getContext(), "PREF_PASSWORD", password);
    }

    public String getPassword() {
        return PrefUtil.getStringValue(getContext(), "PREF_PASSWORD", "");
    }

    private static class LazyHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    public static UserManager getInstance() {
        return LazyHolder.INSTANCE;
    }
}
