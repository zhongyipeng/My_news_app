package com.example.my_news_app1;

public class News {
    private String title;
    private String author;
    private String thumbnailUrl;
    private String date; // 确保有 date 字段
    private String url;

    public News(String title, String author, String thumbnailUrl, String date, String url) {
        this.title = title;
        this.author = author;
        this.thumbnailUrl = thumbnailUrl;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDate() { // 确保有 getDate 方法
        return date;
    }

    public String getUrl() {
        return url;
    }
}