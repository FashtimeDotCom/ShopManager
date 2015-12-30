package com.stone.shopmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.stone.shopmanager.R;
import com.stone.shopmanager.model.shop.Order;

import java.util.List;

/**
 * 适配器--适配订单列表中的数据
 *
 * @author Stone
 * @date 2014-5-27
 */
public class OrderListAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    private Context mContext;

    private LayoutInflater mInflater = null;
    private List<Order> mOrderList = null; // 所选分类下的所有店铺列表

    @SuppressWarnings("unused")
    private String mType; // 商店的分类

    public OrderListAdapter(Context context, List<Order> orderList) {
        mContext = context;
        mOrderList = orderList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh(List<Order> list) {
        mOrderList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderInfoHolder orderInfoHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_order_info, null);
            orderInfoHolder = new OrderInfoHolder();
            orderInfoHolder.tvOrderInfoObjectId = (TextView) convertView
                    .findViewById(R.id.tv_order_info_id);
            orderInfoHolder.tvOrderInfoGoodName = (TextView) convertView
                    .findViewById(R.id.tv_order_info_good_name);
            orderInfoHolder.tvOrderInfoShopName = (TextView) convertView
                    .findViewById(R.id.tv_order_info_shop_name);
            orderInfoHolder.tvOrderInfoShopLocation = (TextView) convertView
                    .findViewById(R.id.tv_order_info_shop_loc);

            // 收货地址(扩展)
            orderInfoHolder.tvOrderInfoMyLocation = (TextView) convertView
                    .findViewById(R.id.tv_order_info_my_loc);

            orderInfoHolder.tvOrderInfoCount = (TextView) convertView
                    .findViewById(R.id.tv_order_info_count);
            orderInfoHolder.tvOrderInfoPrice = (TextView) convertView
                    .findViewById(R.id.tv_order_info_price);
            orderInfoHolder.tvOrderInfoState = (TextView) convertView
                    .findViewById(R.id.tv_order_info_state);
            orderInfoHolder.tvOrderInfoTime = (TextView) convertView
                    .findViewById(R.id.tv_order_info_time);
            convertView.setTag(orderInfoHolder);

            // 评分、点赞(扩展)
            orderInfoHolder.btnOrderScore = (Button) convertView
                    .findViewById(R.id.btn_order_info_score);
            orderInfoHolder.btnOrderScore = (Button) convertView
                    .findViewById(R.id.btn_order_info_praise);
        } else {
            orderInfoHolder = (OrderInfoHolder) convertView.getTag();
        }
        orderInfoHolder.tvOrderInfoObjectId.setText("订单号 " + mOrderList.get(position).getObjectId());
        orderInfoHolder.tvOrderInfoGoodName.setText("商品:  " + mOrderList.get(position).getGood().getName());
        orderInfoHolder.tvOrderInfoShopName.setText("店铺:  " + mOrderList.get(position).getGood().getShop().getName());

        //店铺地址(扩展)
        //orderInfoHolder.tvOrderInfoShopLocation.setText(mOrderList.get(position).get);

        orderInfoHolder.tvOrderInfoCount.setText("数量: " + mOrderList.get(position).getCount());
        double price = mOrderList.get(position).getCount() * Double.parseDouble(mOrderList.get(position).getGood().getPrice());
        orderInfoHolder.tvOrderInfoPrice.setText("￥" + price);
        orderInfoHolder.tvOrderInfoState.setText(mOrderList.get(position).getState());
        orderInfoHolder.tvOrderInfoTime.setText("下单时间:  " + mOrderList.get(position).getCreatedAt());
        return convertView;
    }


    public class OrderInfoHolder {

        public TextView tvOrderInfoObjectId;   //订单号

        public TextView tvOrderInfoGoodName;   //商品名称
        public TextView tvOrderInfoShopName;   //店铺名称
        public TextView tvOrderInfoShopLocation;   //店铺地址

        public TextView tvOrderInfoMyLocation;      //收货地址

        public TextView tvOrderInfoCount;      //订单数量
        public TextView tvOrderInfoPrice;      //订单价格
        public TextView tvOrderInfoState;      //订单状态
        public TextView tvOrderInfoTime;       //下单时间

        public Button btnOrderScore;    //评分
        public Button btnOrderPraise;   //点赞

    }

}
