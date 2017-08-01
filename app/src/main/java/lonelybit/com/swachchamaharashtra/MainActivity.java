package lonelybit.com.swachchamaharashtra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webview = (WebView) findViewById(R.id.activity_main_webview);
        webview.setWebViewClient(new WebViewClient());

        // Enable JS
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Stop local links and redirects from opening in browser instead of WebView
        webview.setWebViewClient(new MyAppWebViewClient());

        // Use local resource
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
}
