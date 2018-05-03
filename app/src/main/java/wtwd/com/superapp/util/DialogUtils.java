package wtwd.com.superapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import wtwd.com.superapp.R;
import wtwd.com.superapp.widget.RingProgressView;

/**
 * Created by Administrator on 2018/5/3 0003.
 */

public class DialogUtils {





    public static void showConnectedWifiDialog(Activity mActivity, final Dialog mDialog) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_wifi_connect_success, null, false);

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(view);


        mDialog.show();
        setDialogMaxheightLayoutParams(mActivity, mDialog);

    }


    public static void showUnconnectedWifiDialog(Activity mActivity, final Dialog mDialog) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_no_wifi_connect, null, false);
        Button btn_connect_wifi = (Button) view.findViewById(R.id.btn_connect_wifi);
        ImageView img_dismiss_dialog = (ImageView) view.findViewById(R.id.img_dismiss_dialog);

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(view);

        btn_connect_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        img_dismiss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });


        mDialog.show();
        setDialogMaxheightLayoutParams(mActivity, mDialog);


    }


    public static void setDialogMaxheightLayoutParams(Activity mActivity, Dialog selectedDialog) {
        Window dialogWindow = selectedDialog.getWindow();
        WindowManager m = mActivity.getWindowManager();

        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();

//        if (p.height > (d.getHeight() * 0.4)) {
        p.height = (int) (d.getHeight() * 0.4);
//        }
        p.width = (int) (d.getWidth() * 0.75);
        p.gravity = Gravity.CENTER;
        p.y = 0; //设置Dialog与底部的margin值，与左右一致

        //设置Dialog本身透明度
//        p.alpha = 0.5f;
        dialogWindow.setAttributes(p);
    }


}
