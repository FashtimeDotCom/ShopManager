package com.stone.shopmanager.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.adapter.NewsListAdapter;
import com.stone.shopmanager.model.hbut.News;
import com.stone.shopmanager.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class NewsManagerActivity extends BaseActivity implements
        OnItemClickListener {

    private static final String TAG = "NewsManagerActivity";

    // 校园新闻
    private ListView lvNewsList;
    private List<News> newsList = new ArrayList<News>();
    private NewsListAdapter newsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_news);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getNewsData();
        initView();
    }

    private void initView() {
        lvNewsList = (ListView) findViewById(R.id.lv_news);
        // 新闻
        newsListAdapter = new NewsListAdapter(this, newsList);
        lvNewsList.setAdapter(newsListAdapter);
        lvNewsList.setOnItemClickListener(this);
    }

    /**
     * 初始化新闻列表数据
     *
     * @date 2014-5-3
     * @author Stone
     */
    public void getNewsData() {
        BmobQuery<News> query = new BmobQuery<News>();
        query.order("-updatedAt");
        query.findObjects(this, new FindListener<News>() {

            @Override
            public void onSuccess(List<News> object) {
                newsList = object;
                // 通知Adapter数据更新
                newsListAdapter.refresh((ArrayList<News>) newsList);
                newsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code, String message) {
                ToastUtils.showToast("都怪小菜我, 获取数据失败了");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // Intent toNewsDetail = new Intent(NewsManagerActivity.this,
        // NewsActivity.class);
        // toNewsDetail.putExtra("NewsTitle",
        // newsList.get(position).getTitle());
        // toNewsDetail.putExtra("NewsAuthor",
        // newsList.get(position).getAuthor());
        // toNewsDetail.putExtra("NewsTime",
        // newsList.get(position).getCreatedAt());
        // toNewsDetail.putExtra("NewsContent",
        // newsList.get(position).getContent());
        // startActivity(toNewsDetail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
