package com.example.my_news_app1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        searchEditText = findViewById(R.id.et_search);
        recyclerView = findViewById(R.id.recyclerView);

        // 添加日志
        Log.d("MainActivity", "onCreate() called");

        // 设置 RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 初始化 NewsAPICaller
        NewsAPICaller newsAPICaller = new NewsAPICaller(recyclerView, this);
        newsAPICaller.fetchNews();

        // 设置搜索框监听器
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 不需要实现
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当搜索框内容改变时，执行搜索
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 不需要实现
            }
        });

        // 设置按钮点击事件
        Button btnHome = findViewById(R.id.btn_home);
        Button btnDiscovery = findViewById(R.id.btn_discovery);
        Button btnMine = findViewById(R.id.btn_mine);

        btnHome.setOnClickListener(v -> {
            // 显示新闻列表
            recyclerView.setVisibility(View.VISIBLE);
            // 移除当前的Fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment()).commit();
        });

        btnDiscovery.setOnClickListener(v -> {
            // 隐藏新闻列表
            recyclerView.setVisibility(View.GONE);
            // 切换到发现页面
            switchFragment(new DiscoverFragment());
        });

        btnMine.setOnClickListener(v -> {
            // 隐藏新闻列表
            recyclerView.setVisibility(View.GONE);
            // 切换到我的页面
            switchFragment(new ProfileFragment());
        });

    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    // 实现搜索功能
    private void performSearch(String query) {
        NewsAPICaller newsAPICaller = new NewsAPICaller(recyclerView, this);
        newsAPICaller.fetchFilteredNews(query);
    }

    // 获取搜索框内容的方法
    public String getSearchContent() {
        if (searchEditText != null) {
            return searchEditText.getText().toString().trim();
        }
        return "";
    }
}