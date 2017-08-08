package lonelybit.com.swachchamaharashtra;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (WebView) findViewById(R.id.activity_main_webview);
        // webview.setWebViewClient(new WebViewClient());

        // Enable JS
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);


        // Stop local links and redirects from opening in browser instead of WebView
        webview.setWebViewClient(new MyAppWebViewClient());

        webview.setWebChromeClient(new MyWebChromeClient());

        // Use local resource
        webview.loadUrl("file:///android_asset/swachcha_maharashtra/index.html");
    }

    @Override
    public void onBackPressed() {
        webview.loadUrl("file:///android_asset/swachcha_maharashtra/index.html");
    }


    private class MyAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // return super.shouldOverrideUrlLoading(view, url);
            view.loadUrl(url);
            return true;
        }


    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    }
}
