package com.tengo.camerayeetsfirst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class RecipeViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO spinner
        WebView webView = new WebView(getApplicationContext());
        setContentView(webView);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
