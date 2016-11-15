package com.glc.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview =  (WebView) findViewById(R.id.mian_wv);
        webview.setWebViewClient(new YourWebClient());

    }
    private class YourWebClient extends WebViewClient{

        // you want to catch when an URL is going to be loaded
        public boolean  shouldOverrideUrlLoading  (WebView  view, String  urlConection){
            // here you will use the url to access the headers.
            // in this case, the Content-Length one
            URL url;
            URLConnection conexion;
            try {
                url = new URL(urlConection);
                conexion = url.openConnection();
                conexion.setConnectTimeout(3000);
                conexion.connect();
                // get the size of the file which is in the header of the request
                int size = conexion.getContentLength();
            }


            // and here, if you want, you can load the page normally
            String htmlContent = "";
            HttpGet httpGet = new HttpGet(urlConection);
            // this receives the response
            HttpResponse response;
            try {
                response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    // la conexion fue establecida, obtener el contenido
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream inputStream = entity.getContent();
                        htmlContent = convertToString(inputStream);
                    }
                }
            } catch (Exception e) {}

            webview.loadData(htmlContent, "text/html", "utf-8");
            return true;
        }

        public String convertToString(InputStream inputStream){
            StringBuffer string = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    string.append(linea + "\n");
                }
            } catch (IOException e) {}
            return string.toString();
        }
    }
