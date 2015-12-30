package com.stone.shopmanager.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.stone.shopmanager.R;
import com.stone.shopmanager.activity.base.BaseActivity;
import com.stone.shopmanager.model.hbut.News;
import com.stone.shopmanager.util.ToastUtils;

import cn.bmob.v3.listener.SaveListener;

public class AddNewsActivity extends BaseActivity implements OnClickListener ,OnItemSelectedListener{

	private EditText etNewsTitle;
	private Spinner  spNewsType = null;
	private EditText etNewsAuthor;
	private EditText etNewsContent;
	
	private Button btnAddNews;
	private String newsType="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnews);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		initView();
	}

	public void initView() {
		etNewsTitle = (EditText) findViewById(R.id.et_news_title);
		spNewsType = (Spinner) findViewById(R.id.sp_news_type);
		etNewsAuthor = (EditText) findViewById(R.id.et_news_author);
		etNewsContent = (EditText) findViewById(R.id.et_news_content);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.news_type_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spNewsType.setAdapter(adapter);
		spNewsType.setOnItemSelectedListener(this);
		
		btnAddNews = (Button) findViewById(R.id.btn_add_news);
		btnAddNews.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_news:
			if (etNewsTitle.getText().equals("")
					|| etNewsAuthor.getText().toString().equals("")
					|| etNewsContent.getText().toString().equals("")
					|| newsType.equals("")) {
				ToastUtils.showToast("请完善信息后再次提交");
			} else {
				addShop(new String[] { etNewsTitle.getText().toString(),
						"【"+newsType+"】",
						etNewsAuthor.getText().toString(),
						etNewsContent.getText().toString() });
			}
			break;

		default:
			break;
		}
	}

	private void addShop(String[] shopInfo) {
		News news = new News();
		news.setTitle(shopInfo[0]);
		news.setType(shopInfo[1]);
		news.setAuthor(shopInfo[2]);
		news.setContent(shopInfo[3]);
		news.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                ToastUtils.showToast("添加成功");
            }

            @Override
            public void onFailure(int i, String arg0) {
                ToastUtils.showToast("添加失败");
            }
        });
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		newsType = getResources().getStringArray(R.array.news_type_array)[position];
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

    @Override
	public void onNothingSelected(AdapterView<?> parent) {
		newsType = "";
		ToastUtils.showToast("请选择新闻的类型");
	}

}
