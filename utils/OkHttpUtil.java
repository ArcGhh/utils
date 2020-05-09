package com.arcghh.utilslibs;

import android.support.annotation.NonNull;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtil {

    public static String doGetSync(String url){
        OkHttpClient client = getOkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        String result = null;
        try {
            Response response = client.newCall(request).execute();//回调方法异步
            result = response.body().string();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return result;
    }
    public static String doJsonPostSync(String url, Map<String, Object> map){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = getOkHttpClient();
        String json = new Gson().toJson(map);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        String result = null;
        try {
            Response response = client.newCall(request).execute();//回调方法异步
            result = response.body().string();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return result;
    }

    public static String doFormPostSync(String url, Map<String, Object> params){
        OkHttpClient client = getOkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.addEncoded(entry.getKey(), String.valueOf(entry.getValue()));
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        String result = null;
        try {
            Response response = client.newCall(request).execute();//回调方法异步
            result = response.body().string();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return result;
    }

    public static void doFormPostAsync(String url, Map<String, Object> params, Callback callback){
        OkHttpClient client = getOkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.addEncoded(entry.getKey(), String.valueOf(entry.getValue()));
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        try {
            client.newCall(request).enqueue(callback);//回调方法异步

        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    @NonNull
    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

}
