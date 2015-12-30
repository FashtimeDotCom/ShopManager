package com.stone.shopmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.shopmanager.R;
import com.stone.shopmanager.model.shop.Shop;
import com.stone.shopmanager.model.helper.ShopInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stonekity.shi on 2015/4/4.
 */
public class ShopInfoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private List<String> mShopInfo = new ArrayList<String>();

    public ShopInfoAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setmShop(Shop shop) {
        if(shop == null)
            return;
        mShopInfo = ShopInfo.getShopInfoList(shop);
    }

    @Override
    public int getCount() {
        return mShopInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mShopInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh(ArrayList<String> list) {
        mShopInfo = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopInfoHolder shopInfoHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_shop_info, null);
            shopInfoHolder = new ShopInfoHolder();
            shopInfoHolder.imgShopInfo = (ImageView) convertView
                    .findViewById(R.id.img_shop_info_item);
            shopInfoHolder.tvShopInfo = (TextView) convertView
                    .findViewById(R.id.tv_shop_info_item);
            convertView.setTag(shopInfoHolder);
        } else {
            shopInfoHolder = (ShopInfoHolder) convertView.getTag();
        }
        shopInfoHolder.tvShopInfo.setText(mShopInfo.get(position));
        return convertView;
    }


    public class ShopInfoHolder {
        public ImageView imgShopInfo;
        public TextView tvShopInfo;
    }


}
