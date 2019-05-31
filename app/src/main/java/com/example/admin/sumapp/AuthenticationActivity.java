package com.example.admin.sumapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthenticationActivity extends AppCompatActivity {
    public static final String AUTHENTICATION_URL = "EXTRA_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        //Get Intent
        String url = getIntent().getStringExtra(AUTHENTICATION_URL);
        if (url == null) {
            Log.e("twitter", "URL cannot be null");
            finish();
        }

        //Webview
        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);

            if (url.contains(getResources().getString(R.string.twitter_callback_url))) {
                Uri uri = Uri.parse(url);
                String verify = uri.getQueryParameter(getString(R.string.twitter_oauth_verifier));

                //Sending result back
                Intent resultIntent = new Intent();
                resultIntent.putExtra(getString(R.string.twitter_oauth_verifier), verify);
                setResult(RESULT_OK, resultIntent);
                finish();

            }
        }
    }
}
