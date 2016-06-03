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
        //WebView����web��Դ
       webView.loadUrl(urlString);
       WebSettings settings = webView.getSettings();
       settings.setJavaScriptEnabled(true);
       webView.getSettings().setJavaScriptEnabled(true);
       webView.getSettings().setDefaultTextEncodingName("gb2312");
        //����WebViewĬ��ʹ�õ�������ϵͳĬ�����������ҳ����Ϊ��ʹ��ҳ��WebView��
       webView.setWebViewClient(new WebViewClient(){
           @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
               //����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ�����������������
             view.loadUrl(url);
            return true;
        }
           public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
        	                  //handler.cancel(); // AndroidĬ�ϵĴ���ʽ
        	                  handler.proceed();  // ����������վ��֤��       	            
        	               }
       });
    }
}
