package com.zsc.ljt.ljtshixun1;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresh;
    private ImageView bingPicImg;
    private TextView detail;
    private String mImagesUrl = "http://cn.bing.com";
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.everyday_image);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        detail = (TextView) findViewById(R.id.detail);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString("bing_pic", null);
        String bingContent = prefs.getString("bing_content", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
            detail.setText(bingContent);
        }
        else {
            loadBingPic();
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBingPic();
            }
        });
    }

    private void loadBingPic() {
        String requestJson = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        sendOkHttpRequest(requestJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(CActivity.this, "拉取服务器信息失败", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseAllText = response.body().string();
                String[] responseData = handleBingResponse(responseAllText);
                if (responseData != null) {
                    mImagesUrl += responseData[0];
                    mContent = responseData[1].substring(0, responseData[1].indexOf("("));
                    sendOkHttpRequest(mImagesUrl, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            Toast.makeText(CActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                            mImagesUrl = "http://cn.bing.com";
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bingPic = response.request().url().toString();
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences(CActivity.this).edit();
                            editor.putString("bing_pic", bingPic);
                            editor.putString("bing_content", mContent);
                            editor.apply();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(CActivity.this).load(bingPic).into(bingPicImg);
                                    detail.setText(mContent);
                                    swipeRefresh.setRefreshing(false);
                                }
                            });
                            mImagesUrl = "http://cn.bing.com";
                        }
                    });
                } else {
                    Toast.makeText(CActivity.this, "服务器返回数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static String[] handleBingResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            String[] responseData = new String[2];
            responseData[0] = jsonArray.getJSONObject(0).getString("url");
            responseData[1] = jsonArray.getJSONObject(0).getString("copyright");
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}