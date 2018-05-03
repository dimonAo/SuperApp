package wtwd.com.superapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import wtwd.com.superapp.R;
import wtwd.com.superapp.entity.DeviceEntity;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class AddDeviceAdapter extends BaseQuickAdapter<DeviceEntity,BaseViewHolder> {
    public AddDeviceAdapter(int layoutResId, @Nullable List<DeviceEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceEntity item) {

        switch (item.getDevice_type()){
            case 1:
                helper.setImageResource(R.id.img_device,R.mipmap.sweeper_img)
                        .setText(R.id.text_device_name,item.getDevice_name());
                break;
        }








    }
}
