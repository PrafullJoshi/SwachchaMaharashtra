package lonelybit.com.swachchamaharashtra;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private WebView webview;
    private int backCount = 0;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

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

        webview.addJavascriptInterface(new MyJavascriptInterface(this), "Android");

        // Use local resource
        webview.loadUrl("file:///android_asset/swachcha_maharashtra/index.html");
    }

    @Override
    public void onBackPressed() {
        // mWebView.loadUrl("file:///android_asset/pustika/index.html");
        Toast.makeText(this, "Click Back 1 more time to minimize the app!", Toast.LENGTH_SHORT).show();
        if(backCount > 0) {
            super.onBackPressed();
        } else {
            webview.loadUrl("file:///android_asset/swachcha_maharashtra/index.html");
        }
        backCount++;
    }

    @Override
    protected void onResume() {
        backCount = 0;
        super.onResume();
    }

    @Override
    protected void onStart() {
        backCount = 0;
        super.onStart();
    }


    private class MyAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // return super.shouldOverrideUrlLoading(view, url);
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        logger.info("In onActivityResult()");
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                logger.info("In onActivityResult() for SELECT PICTURE");
                Uri selectedImage = intent.getData();
                webview.loadUrl("javascript:setFileUri('" + selectedImage.toString() + "')");
                String path = getRealPathFromURI(this, selectedImage);
                webview.loadUrl("javascript:setFilePath('" + path + "')");
                logger.info("In onActivityResult() for selectedImagePath = " + selectedImage.getPath());
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try
        {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            logger.info("In onShowFileChooser()");
            // return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
            return true;
        }


    }

    private class MyJavascriptInterface {
        private final Context context;

        MyJavascriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public String chooseImage() {
            String file = "test";
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PICTURE);
            return file;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(this.context, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
