package wtwd.com.superapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xlink.sdk.v5.model.XDevice;
import wtwd.com.superapp.R;
import wtwd.com.superapp.entity.Device;
import wtwd.com.superapp.entity.DeviceEntity;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

public class MainFamilyAdapter extends BaseQuickAdapter<Device, BaseViewHolder> {
    public MainFamilyAdapter(int layoutResId, @Nullable List<Device> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Device item) {

//        switch (item.getDevice_type()) {
//            case 1:
//                helper.setBackgroundRes(R.id.img_device_type, R.drawable.main_light_select);
//                break;
//
//            case 2:
//                helper.setBackgroundRes(R.id.img_device_type, R.drawable.main_air_condition_select);
//                break;
//
//            case 3:
//                helper.setBackgroundRes(R.id.img_device_type, R.drawable.main_cooker_select);
//                break;
//
//            case 4:
        helper.setBackgroundRes(R.id.img_device_type, R.mipmap.sweeper_icon);
//                break;
//
//            case 5:
//                helper.setBackgroundRes(R.id.img_device_type, R.drawable.main_filter_select);
//                break;
//
//            case 6:
//                helper.setBackgroundRes(R.id.img_device_type, R.drawable.main_socket_select);
//                break;
//
//            case 7:
//                helper.setBackgroundRes(R.id.img_device_type, R.drawable.main_fridge_select);
//                break;
//        }

        helper
//                .setText(R.id.text_position, item.getPosition_name())
                .setText(R.id.text_device_name, item.getXDevice().getDeviceName());

        if (XDevice.State.CONNECTED == item.getXDevice().getConnectionState()) {
            helper.setText(R.id.text_device_on_line, "设备在线");
            helper.getView(R.id.img_device_type).setSelected(false);

//        } else if (1 == item.getOn_line()) {
        } else {
            helper.setText(R.id.text_device_on_line, "设备离线");
            helper.getView(R.id.img_device_type).setSelected(true);
        }


    }
}
