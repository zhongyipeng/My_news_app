package com.example.my_news_app1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewsAPICaller {
    private RecyclerView recyclerView;
    private List<News> newsList;
    private NewsAdapter newsAdapter;
    private Context context;

    public NewsAPICaller(RecyclerView recyclerView, Context context) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.newsList = new ArrayList<>();
        this.newsAdapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(newsAdapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            Network network = cm.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false;
    }

    public void fetchNews() {
        if (!isNetworkAvailable()) {
            return;
        }

        new Thread(() -> {
            String result = doInBackground();
            recyclerView.post(() -> processResult(result));
        }).start();
    }

    public void fetchFilteredNews(String query) {
        if (!isNetworkAvailable()) {
            return;
        }

        new Thread(() -> {
            String result = doInBackground();
            recyclerView.post(() -> processFilteredResult(result, query));
        }).start();
    }

    private String doInBackground() {
        String apiKey = "7ea7d76d4094e3565955127983d0ec1f";
        String apiUrl = "https://v.juhe.cn/toutiao/index";

        HashMap<String, String> map = new HashMap<>();
        map.put("key", apiKey);
        map.put("type", "top");
        map.put("page", "1");
        map.put("page_size", "18");

        try {
            URL url = new URL(apiUrl + "?" + params(map));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8)
            );
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            Log.e("NewsAPICaller", "Error fetching news", e);
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    private void processResult(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.optString("reason", "").equals("success!")) {
                    JSONArray articles = jsonObject.optJSONObject("result").optJSONArray("data");
                    if (articles != null && articles.length() > 0) {
                        newsList.clear();

                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject article = articles.optJSONObject(i);
                            if (article != null) {
                                News news = new News(
                                        article.optString("title", "标题缺失"),
                                        article.optString("author_name", "作者未知"),
                                        article.optString("thumbnail_pic_s", ""),
                                        article.optString("date", "时间缺失"),
                                        article.optString("url", "")
                                );
                                newsList.add(news);
                            }
                        }

                        recyclerView.post(() -> newsAdapter.notifyDataSetChanged());
                    }
                }
            } catch (Exception e) {
                Log.e("NewsAPICaller", "Error processing result", e);
            }
        }
    }

    private void processFilteredResult(String result, String query) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.optString("reason", "").equals("success!")) {
                    JSONArray articles = jsonObject.optJSONObject("result").optJSONArray("data");
                    if (articles != null && articles.length() > 0) {
                        List<News> filteredNewsList = new ArrayList<>();

                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject article = articles.optJSONObject(i);
                            if (article != null) {
                                String title = article.optString("title", "标题缺失");
                                if (title.toLowerCase().contains(query.toLowerCase())) {
                                    News news = new News(
                                            title,
                                            article.optString("author_name", "作者未知"),
                                            article.optString("thumbnail_pic_s", ""),
                                            article.optString("date", "时间缺失"),
                                            article.optString("url", "")
                                    );
                                    filteredNewsList.add(news);
                                }
                            }
                        }

                        newsList.clear();
                        newsList.addAll(filteredNewsList);
                        recyclerView.post(() -> newsAdapter.notifyDataSetChanged());
                    }
                }
            } catch (Exception e) {
                Log.e("NewsAPICaller", "Error processing filtered result", e);
            }
        }
    }

    private static String params(Map<String, String> map) {
        return map.entrySet().stream()
                .map(entry -> {
                    try {
                        return entry.getKey() + "=" +
                                URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
                    } catch (Exception e) {
                        Log.e("NewsAPICaller", "Error encoding parameters", e);
                        return entry.getKey() + "=" + entry.getValue();
                    }
                })
                .collect(Collectors.joining("&"));
    }
}