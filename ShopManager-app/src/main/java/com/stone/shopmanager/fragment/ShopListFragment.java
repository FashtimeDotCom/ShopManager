package com.stone.shopmanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.LoginActivity;
import com.stone.shopmanager.activity.SaveShopActivity;
import com.stone.shopmanager.activity.ShopManagerActivity;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.adapter.ShopListAdapter;
import com.stone.shopmanager.config.BmobConfig;
import com.stone.shopmanager.manager.ShopManager;
import com.stone.shopmanager.model.shop.Shop;
import com.stone.shopmanager.model.shop.User;
import com.stone.shopmanager.util.ToastUtils;
import com.stone.shopmanager.util.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by stone on 15/4/18.
 */
public class ShopListFragment extends BaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {


    @SuppressWarnings("unused")
    private static final String TAG = "ShopAllActivity";

    private ViewGroup vgEmptyBg;  //当数据为空时显示的视图
    private View vgHeader;  //提示布局
    private ShopHeaderHodler headerHodler;

    private ListView lvShopAllList;
    private ShopListAdapter shopListAdapter;
    private SwipeRefreshLayout swipeLayout;

    private ArrayList<Shop> shopList = new ArrayList<>();

    //下拉刷新
    private static final int STATE_REFRESH = 0;// 下拉刷新
    @SuppressWarnings("unused")
    private static final int STATE_MORE = 1;// 加载更多

    private int limit = 10;        // 每页的数据是10条
    private int curPage = 0;        // 当前页的编号，从0开始


    @Override
    protected int provideLayoutResId() {
        return R.layout.fragment_shop_all;
    }

    @Override
    protected void initView(View rootView) {

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.lv_shop_all_swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        lvShopAllList = (ListView) rootView.findViewById(R.id.lv_shop_all);
        initListHeader();
        shopListAdapter = new ShopListAdapter(fragmentActivity, shopList);
        lvShopAllList.setAdapter(shopListAdapter);
        lvShopAllList.setOnItemClickListener(this);
        lvShopAllList.setOnScrollListener(this);

        // 空布局
        initEmptyView();
    }

    private void initListHeader() {
        vgHeader = LayoutInflater.from(getActivity()).inflate(R.layout.header_shop_list, null);
        lvShopAllList.addHeaderView(vgHeader);
        headerHodler = new ShopHeaderHodler();
        headerHodler.tvShopCountHint = (TextView) getActivity().findViewById(R.id.tv_hint_shop_count);
    }

    private void initEmptyView() {
        vgEmptyBg = (ViewGroup) getActivity().findViewById(R.id.rl_shop_list_empty);
        Button btnCreateShop = (Button) getActivity().findViewById(R.id.btn_shop_create);
        btnCreateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SaveShopActivity.class);
                intent.putExtra(SaveShopActivity.KEY_EXTRA_SHOW_TYPE, SaveShopActivity.SHOW_TYPE_CREATE);
                getActivity().startActivity(intent);
            }
        });
    }

    private void updateHeader(int count) {
        User curUser = BmobUser.getCurrentUser(getActivity(), User.class);
        if(curUser == null) {
            ToastUtils.showToast("抱歉，请先登录");
            return;
        }

        if(headerHodler == null) {
            return;
        }

        String username = curUser.getNickname().equals("")?curUser.getUsername():curUser.getNickname();
        String hint = String.format(getResources().getString(R.string.format_hint_shop_count), username, count);
        headerHodler.tvShopCountHint.setText(hint);
    }


    private void showEmptyView(Boolean isShow) {

        if(vgEmptyBg == null || vgHeader == null) {
            ToastUtils.showToast("视图初始化错误");
            return;
        }

        if (isShow) {
            vgEmptyBg.setVisibility(View.VISIBLE);
            vgHeader.setVisibility(View.GONE);
        } else {
            vgEmptyBg.setVisibility(View.GONE);
            vgHeader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(View rootView, Bundle savedInstanceState) {

        boolean hasNetwork = Utils.isNetworkAvailable(getActivity());
        if (hasNetwork) {
            //获取商店数据
            swipeLayout.setVisibility(View.VISIBLE);
            vgEmptyBg.setVisibility(View.GONE);
            queryData(0, STATE_REFRESH);
        } else {
            swipeLayout.setVisibility(View.GONE);
            vgEmptyBg.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Shop showShop = (Shop) parent.getAdapter().getItem(position);
        ShopManager.getInstance().setSelectedShop(showShop);
        Intent intent = new Intent(getActivity(), ShopManagerActivity.class);
        startActivity(intent);
    }

    /**
     * 分页获取数据
     *
     * @param page       页码
     * @param actionType ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(final int page, final int actionType) {
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:" + actionType);

        if (actionType == STATE_REFRESH || page == 0) {
            curPage = 0;
            shopList.clear();
            shopListAdapter.refresh(shopList);
            shopListAdapter.notifyDataSetChanged();
        }

        User curUser = BmobUser.getCurrentUser(getActivity(), User.class);
        if (curUser == null) {
            ToastUtils.showToast("抱歉，未登录");
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return;
        }

        BmobQuery<Shop> query = new BmobQuery<>();
        query.addWhereEqualTo("user", curUser);
        query.setSkip(page * limit);        // 从第几条数据开始
        query.setLimit(limit);            // 设置每页多少条数据
        query.order("-isOfficial, -isPromoted");
        query.findObjects(getActivity(), new FindListener<Shop>() {

            @Override
            public void onSuccess(List<Shop> list) {

                if (list.size() > 0) {

                    for (Shop shop : list) {
                        shopList.add(shop);
                    }

                    // 更新提示数据
                    updateHeader(shopList.size());

                    // 通知Adapter数据更新
                    shopListAdapter.refresh(shopList);
                    shopListAdapter.notifyDataSetChanged();
                    // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                    curPage++;
                    if (BmobConfig.DEBUG)
                        ToastUtils.showToast("第" + (page + 1) + "页数据加载完成");
                } else {
                    showEmptyView(page == 0);
                    ToastUtils.showToast("没有更多数据了");
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                ToastUtils.showToast("查询失败:" + arg1);
            }
        });
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                swipeLayout.setRefreshing(false);
                queryData(0, STATE_REFRESH);
            }
        }, 1000);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {

                if (shopList.size() < limit) {
                    ToastUtils.showToast("已经加载所有数据");
                    return;
                }

                if (curPage == 0)
                    return;

                new Handler().post(new Runnable() {
                    public void run() {
                        queryData(curPage, STATE_MORE);
                    }
                });
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面
        MobclickAgent.onResume(getActivity());          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(getActivity());
    }


    /**
     * 店铺列表头布局
     */
    class ShopHeaderHodler {
        public TextView tvShopCountHint;
    }

}
