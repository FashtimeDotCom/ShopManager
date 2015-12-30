package com.stone.shopmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stone.shopmanager.R;
import com.stone.shopmanager.model.shop.Good;

import java.util.ArrayList;
import java.util.List;

public class GoodsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Good> mGoodsList = new ArrayList<Good>(); // 商品列表信息
    private LayoutInflater mInflater = null;

    public GoodsListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 刷新列表中的数据
    public void refresh(List<Good> list) {
        mGoodsList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodsHolder goodHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_m_good_list_item,
                    null);
            goodHolder = new GoodsHolder();
            goodHolder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_good_name);
            goodHolder.tvPrice = (TextView) convertView
                    .findViewById(R.id.tv_good_price);
            goodHolder.tvStock = (TextView) convertView
                    .findViewById(R.id.tv_good_stock);
            goodHolder.tvSaleVolume = (TextView) convertView
                    .findViewById(R.id.tv_good_sale_volume);
            convertView.setTag(goodHolder);
        } else {
            goodHolder = (GoodsHolder) convertView.getTag();
        }
        goodHolder.tvName.setText(mGoodsList.get(position).getName());
        goodHolder.tvPrice.setText("￥" + mGoodsList.get(position).getPrice());
        goodHolder.tvStock.setText(String.format(mContext.getResources().getString(R.string.item_good_stock_format), mGoodsList.get(position).getStock()));
        goodHolder.tvSaleVolume.setText(String.format(mContext.getResources().getString(R.string.item_good_sale_volume_format), mGoodsList.get(position).getSaleVolume()));
        return convertView;
    }


    /**
     * 商品视图
     *
     * @author Stone
     * @date 2014-4-26
     */
    public class GoodsHolder {

        public TextView tvName;   //商品名称
        public TextView tvPrice;  //商品单价
        public TextView tvStock;  //商品库存
        public TextView tvSaleVolume;  //商品销量

    }

}
