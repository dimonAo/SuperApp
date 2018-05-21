package wtwd.com.superapp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import wtwd.com.superapp.R;

/**
 * Created by Administrator on 2018/5/21 0021.
 */

public class SimpleStringAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SimpleStringAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text_seepage, item);
    }
}
