package com.example.my_news_app1;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;

    // 构造函数：初始化新闻列表
    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    // 创建 ViewHolder：加载新闻项布局
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    // 绑定 ViewHolder：设置新闻项数据
    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.titleTextView.setText(news.getTitle());
        holder.timeTextView.setText(news.getDate());

        String imageUrl = news.getThumbnailUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(holder.thumbnailImageView);
        } else {
            Picasso.get()
                    .load(R.drawable.placeholder_image)
                    .into(holder.thumbnailImageView);
        }

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
            intent.putExtra("title", news.getTitle());
            intent.putExtra("imageUrl", news.getThumbnailUrl());
            intent.putExtra("url", news.getUrl()); // 传递 URL
            v.getContext().startActivity(intent);
        });
    }

    // 获取新闻项数量
    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // ViewHolder 类：持有新闻项视图
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView timeTextView;
        ImageView thumbnailImageView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            timeTextView = itemView.findViewById(R.id.tvTime);
            thumbnailImageView = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}