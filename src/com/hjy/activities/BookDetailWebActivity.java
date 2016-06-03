package com.hjy.activities;

import com.JustYY.xiyoulibrary.R;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class BookDetailWebActivity extends Activity {
     WebView webView;
     TextView titleBar;
     String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail_web);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		titleBar = (TextView) findViewById(R.id.titlebarText);
		Intent intent=getIntent();
		titleBar.setText(intent.getStringExtra("title"));
		url=intent.getStringExtra("url");
		initWebView(url);
	}	
	private void initWebView(String urlString){
        webView = (WebView) findViewById(R.id.detailwebView);
        //WebView加载web资源
       webView.loadUrl(urlString);
       WebSettings settings = webView.getSettings();
       settings.setJavaScriptEnabled(true);
       webView.getSettings().setJavaScriptEnabled(true);
       webView.getSettings().setDefaultTextEncodingName("gb2312");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
       webView.setWebViewClient(new WebViewClient(){
           @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
             view.loadUrl(url);
            return true;
        }
           public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
        	                  //handler.cancel(); // Android默认的处理方式
        	                  handler.proceed();  // 接受所有网站的证书       	            
        	               }
       });
    }
}
