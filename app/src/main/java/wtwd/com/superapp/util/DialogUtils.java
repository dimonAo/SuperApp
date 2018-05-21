package wtwd.com.superapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import wtwd.com.superapp.R;
import wtwd.com.superapp.adapter.SimpleStringAdapter;
import wtwd.com.superapp.widget.RingProgressView;

/**
 * Created by Administrator on 2018/5/3 0003.
 */

public class DialogUtils {

    public static void showSeepageDialog(Activity mActivity, final Dialog mDialog, List<String> lists, BaseQuickAdapter.OnItemClickListener mListener) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_choose_seepage, null, false);
        RecyclerView recycler_seepage = view.findViewById(R.id.recycler_seepage);
        SimpleStringAdapter mAdapter = new SimpleStringAdapter(R.layout.item_choose_seepage);
        recycler_seepage.setLayoutManager(new LinearLayoutManager(mActivity));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(mActivity, R.drawable.shape_line));
        recycler_seepage.addItemDecoration(itemDecoration);
        mAdapter.getData().addAll(lists);
        recycler_seepage.setAdapter(mAdapter);

        mDialog.setCanceledOnTouchOutside(true);
        mAdapter.setOnItemClickListener(mListener);
        mDialog.setContentView(view);
        mDialog.show();
        setDialogMaxheightLayoutParamsBottom(mActivity, mDialog);

    }


    public static void showWifiStateDialog(Activity mActivity, final Dialog mDialog, String content) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_device_off_line, null, false);

        TextView text_dialog_alarm = (TextView) view.findViewById(R.id.text_dialog_alarm);
        text_dialog_alarm.setText(content);

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(view);


        mDialog.show();
        setDialogMaxheightLayoutParams(mActivity, mDialog);

    }

    public static void showTextDialog(Activity mActivity, final Dialog mDialog, String content) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_device_off_line, null, false);

        TextView text_dialog_alarm = (TextView) view.findViewById(R.id.text_dialog_alarm);
        text_dialog_alarm.setText(content);

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }, 1000);

        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }


        mDialog.show();
        setDialogMaxheightLayoutParams1(mActivity, mDialog);

    }


    public static void showConnectedWifiDialog(Activity mActivity, final Dialog mDialog) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_wifi_connect_success, null, false);

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(view);


        mDialog.show();
        setDialogMaxheightLayoutParams(mActivity, mDialog);

    }


    public static void showUnconnectedWifiDialog(final Activity mActivity, final Dialog mDialog) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_no_wifi_connect, null, false);
        Button btn_connect_wifi = (Button) view.findViewById(R.id.btn_connect_wifi);
        ImageView img_dismiss_dialog = (ImageView) view.findViewById(R.id.img_dismiss_dialog);

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(view);

        btn_connect_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                it.setComponent(cn);
                mActivity.startActivity(it);
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
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

    public static void setDialogMaxheightLayoutParams1(Activity mActivity, Dialog selectedDialog) {
        Window dialogWindow = selectedDialog.getWindow();
        WindowManager m = mActivity.getWindowManager();

        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();


//        if (p.height > (d.getHeight() * 0.4)) {
//        p.height = (int) (d.getHeight() * 0.4);
////        }
//        p.width = (int) (d.getWidth() * 0.75);
        p.gravity = Gravity.CENTER;
//        p.y = 0; //设置Dialog与底部的margin值，与左右一致

        //设置Dialog本身透明度
//        p.alpha = 0.5f;
        dialogWindow.setAttributes(p);
    }

    public static void setDialogMaxheightLayoutParamsBottom(Activity mActivity, Dialog selectedDialog) {
        Window dialogWindow = selectedDialog.getWindow();
        WindowManager m = mActivity.getWindowManager();

        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();


//        if (p.height > (d.getHeight() * 0.4)) {
//        p.height = (int) (d.getHeight() * 0.4);
////        }
        p.width = (int) (d.getWidth() * 0.9);
        p.gravity = Gravity.BOTTOM;
        p.y = (int) (d.getHeight() * 0.05); //设置Dialog与底部的margin值，与左右一致

        //设置Dialog本身透明度
//        p.alpha = 0.5f;
        dialogWindow.setAttributes(p);
    }

}
