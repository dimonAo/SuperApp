package wtwd.com.superapp.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import wtwd.com.superapp.R;
import wtwd.com.superapp.activity.ChooseDeviceTypeActivity;
import wtwd.com.superapp.activity.SweeperActivity;
import wtwd.com.superapp.adapter.MainFamilyAdapter;
import wtwd.com.superapp.base.BaseFragment;
import wtwd.com.superapp.entity.Device;

/**
 * Created by Administrator on 2018/5/17 0017.
 */

public class DeviceListFragment extends BaseFragment {

    private RecyclerView recycler_device;
    private Button btn_add_device;
    private LinearLayout lin_add_remind;
    private MainFamilyAdapter mAdapter;
    private List<Device> mDeviceEntirys = new ArrayList<>();

    public static DeviceListFragment getInstance(String title) {
        DeviceListFragment sf = new DeviceListFragment();
//        sf.mTitle = title;
        return sf;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_device_list;
    }

    public void setData(ArrayList<Device> mDevices) {
        if (mDevices.size() <= 0) {
            recycler_device.setVisibility(View.GONE);
            lin_add_remind.setVisibility(View.VISIBLE);
            return;
        }

        recycler_device.setVisibility(View.VISIBLE);
        lin_add_remind.setVisibility(View.GONE);

        mDeviceEntirys.clear();
        mDeviceEntirys.addAll(mDevices);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void initFragmentView(View mView) {
        recycler_device = (RecyclerView) mView.findViewById(R.id.recycler_device);
        btn_add_device = (Button) mView.findViewById(R.id.btn_add_device);
        lin_add_remind = (LinearLayout) mView.findViewById(R.id.lin_add_remind);

        mAdapter = new MainFamilyAdapter(R.layout.item_main_family_device_display, mDeviceEntirys);

        recycler_device.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        DividerItemDecoration mItem = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        mItem.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.transparent_devider));
        recycler_device.addItemDecoration(mItem);
        recycler_device.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putString("device", mDeviceEntirys.get(position).getXDevice().getMacAddress());
                readyGo(SweeperActivity.class, bundle);
            }
        });


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_device_view, null, false);
//        ImageView img_item_recycler = view.findViewById(R.id.img_item_recycler);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGoForResult(ChooseDeviceTypeActivity.class, 100);
            }
        });
        mAdapter.setFooterViewAsFlow(true);
        mAdapter.setFooterView(view);

    }
}
