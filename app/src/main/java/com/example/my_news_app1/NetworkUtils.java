package com.example.my_news_app1;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.content.Context;

public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // 获取当前活动的网络
        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork != null) {
            // 获取该网络的连接能力
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
            if (capabilities != null) {
                // 检查是否有网络连接（WIFI 或移动数据）
                return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false; // 无网络
    }
}
