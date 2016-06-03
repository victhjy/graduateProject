package com.hjy.activities;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.JustYY.xiyoulibrary.R;
import com.google.gson.Gson;
import com.hjy.handler.MyContentHandler;
import com.hjy.model.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailActivity extends Activity {
    TextView titleBar;
    TextView name;
    TextView schoolId;
    TextView uclass;
    TextView phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        //标题栏 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		titleBar = (TextView) findViewById(R.id.titlebarText);	
        titleBar.setText("个人信息");
		setContentView(R.layout.activity_user_detail);
		
		name=(TextView) findViewById(R.id.user_name);
    	schoolId=(TextView) findViewById(R.id.user_schoolid);
    	uclass=(TextView) findViewById(R.id.user_class);
    	phone=(TextView) findViewById(R.id.user_phone);
    	
		Log.i("5-30", "2");
		Intent intent=getIntent();
		String recordId=intent.getStringExtra("schoolId");
		Log.i("5-30", recordId);
		loadData(recordId);
		
	}
    public  void loadData(String recordId){

		RequestParams params = new RequestParams();							
		params.put("schoolId", recordId);
		String methodName="getUserModel";
		StringBuffer apiStart=new StringBuffer(getResources().getString(R.string.WWW));
		String methodURL=apiStart.append(methodName).toString();
		final String XMLmark = methodName + "Return";
		AsyncHttpClient client = new AsyncHttpClient();
		Log.i("5-30", methodURL);
		Log.i("5-30",recordId);
		client.get(methodURL, params, new TextHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers, String responseBody) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					try {
						Log.i("5-30", "4");
						SAXParserFactory factory = SAXParserFactory.newInstance();
						XMLReader reader = factory.newSAXParser().getXMLReader();
						MyContentHandler handler = new MyContentHandler(XMLmark);
						reader.setContentHandler(handler);
						reader.parse(new InputSource(new StringReader(responseBody)));
						String str = handler.getResult().toString();						
						Gson gson=new Gson();
						UserInfo u=gson.fromJson(str, UserInfo.class);
						Log.i("5-30", "5");
						bindingData(u);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "网络连接失败", 1).show();
			}
			@Override
	        public void onFinish() {
				
	        }		
		});
	}

    public void bindingData(UserInfo model){
    
        
    	
    	
    	schoolId.setText(model.getSchoolId());
    	uclass.setText(model.getUclass());
    	phone.setText(model.getMobile());
    	name.setText(model.getUname());
        Log.i("5-30", model.toString());
        Log.i("5-30", model.getMobile());
        Log.i("5-30", model.getUclass());
    	
    }

}
