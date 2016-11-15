package com.glc.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Starting");
        webview = (WebView) findViewById(R.id.mian_web);

        webview.loadUrl("http://www.ganji.com");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new MyWebClick());

        webview.setWebChromeClient(webChromeClient);
    }

    class MyWebClick extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            Log.d(TAG, "shouldOverrideUrlLoading +++++++++++" + "url===" + url);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);
                    try {
                        HttpResponse response = httpClient.execute(httpGet);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            Header[] allHeaders = response.getAllHeaders();
                            for (Header header : allHeaders) {
                                String name = header.getName();
                                String value = header.getValue();
                                Log.i(TAG, "name = " + name +"&&&&&&&&&&&&&&&"+ "value == " + value);
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            webview.loadUrl(url);

            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldOverrideUrlLoading --------------");
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


            super.onPageStarted(view, url, favicon);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()){
                webview.goBack();
                return  true;
            }
        }
        return false;
    }

    private WebChromeClient webChromeClient = new WebChromeClient(){

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d(TAG,"onProgressChanged <><><><><><><>");

            View viewProgress = RelativeLayout.inflate(MainActivity.this,R.layout.main_progress, null);
            ProgressBar pb = (ProgressBar) viewProgress.findViewById(R.id.main_pb);
            pb.setVisibility(view.VISIBLE);
            MainActivity.this.setTitle("Loading...");
            MainActivity.this.setProgress(newProgress*100);
            if (newProgress == 100){
                pb.setVisibility(view.GONE);
                MainActivity.this.setTitle("完成");
            }


        super.onProgressChanged(view, newProgress);
        }
     };

        }
