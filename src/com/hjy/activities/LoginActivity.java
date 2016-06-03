package com.hjy.activities;

import java.io.StringReader;
import java.security.Policy.Parameters;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.JustYY.xiyoulibrary.R;
import com.google.gson.Gson;
import com.hjy.adapters.UserInfoAdapter;
import com.hjy.handler.MyContentHandler;
import com.hjy.handler.NetworkState;
import com.hjy.model.UserInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ShowToast")
public class LoginActivity extends Activity {

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	static UserInfo userInfo = new UserInfo();
	static String USER_NAME = "";
	static String PASSWORD_STRING = "";

	String usernameString;
	String passwordString;
	 
	String recordId;
 
	ListView userinfoListView;
	UserInfoAdapter adapter;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//判断之前是否登录
		sharedPreferences= getSharedPreferences("userInfo", 
				Activity.MODE_PRIVATE); 
		recordId =sharedPreferences.getString("schoolId", ""); 
		Log.i("name", recordId);
		
		//如果之前没有登录过
	   if(recordId==""){
		   notLogin();
	 }
	   //如果之前有登陆过
	   else{
		   
		   setContentView(R.layout.activity_user_info);
		   UserInfo model=new UserInfo();		   
		   setListView(model);
	   }
  
	}
    	
	
	//未登录执行函数?
	public void notLogin(){
		setContentView(R.layout.activity_login);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
		Button loginButton = (Button) findViewById(R.id.login);
		final EditText usernameText = (EditText) findViewById(R.id.username);
		final EditText passwordText = (EditText) findViewById(R.id.password);
		// final EditText resultText = (EditText) findViewById(R.id.editText1);
		loginButton.setOnClickListener(new Button.OnClickListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {				
				if (!NetworkState.isNetworkAvailable(LoginActivity.this)) {
					Toast.makeText(LoginActivity.this, "网络连接不可用",
							1).show();
					return;
				}
				usernameString = usernameText.getText().toString();
				passwordString = passwordText.getText().toString();

				if (usernameString.isEmpty() || passwordString.isEmpty()) {
					Toast.makeText(LoginActivity.this, "学号或密码不能为空",
							1).show();
					return;
				}
				RequestParams params = new RequestParams();							
				params.put("schoolId", usernameString);
				params.put("password", passwordString);
				String methodName="loginIn";
				StringBuffer apiStart=new StringBuffer(getResources().getString(R.string.WWW));
				String methodURL=apiStart.append(methodName).toString();

				final String XMLmark = methodName + "Return";
				AsyncHttpClient client = new AsyncHttpClient();
				client.get(methodURL, params, new TextHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers, String responseBody) {
						// TODO Auto-generated method stub
						if (statusCode == 200) {
							try {
								SAXParserFactory factory = SAXParserFactory.newInstance();
								XMLReader reader = factory.newSAXParser().getXMLReader();
								MyContentHandler handler = new MyContentHandler(XMLmark);
								reader.setContentHandler(handler);
								reader.parse(new InputSource(new StringReader(responseBody)));
								String str = handler.getResult().toString();						
								Gson gson=new Gson();
								UserInfo u=gson.fromJson(str, UserInfo.class);
								
								if(u!=null){
									//登录成功后本地存储用户信�?
									sharedPreferences= getSharedPreferences("userInfo", 
											Activity.MODE_PRIVATE);
									editor = sharedPreferences.edit(); 
									editor.putString("schoolId", usernameString);
									editor.commit();
									//刷新界面
									onCreate(null);
//									startActivity(intent);
								}
								else{
									Toast.makeText(getApplicationContext(), "学号或密码错误", 1).show();
								}								
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
		});
		usernameText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (usernameText.getText().toString().isEmpty())
					return;
				if (!hasFocus) {
					String s = usernameText.getText().toString().toUpperCase();
					usernameText.setText(s);
				}
			}
		});
	}
	
	//登录之后设置listview
	public void setListView(UserInfo model){
		userinfoListView=(ListView) findViewById(R.id.userinfo_listview);
		adapter=new UserInfoAdapter(model, getApplicationContext());
		userinfoListView.setAdapter(adapter);
		userinfoListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
			    switch (position) {
				case 0:
					Intent userDetailintent=new Intent(getApplicationContext(),UserDetailActivity.class);
					userDetailintent.putExtra("schoolId",recordId);					
					startActivity(userDetailintent);
					break;
				case 1:
					Intent myBorrowintent=new Intent(getApplicationContext(),MyBorrowActivity.class);
					myBorrowintent.putExtra("schoolId", recordId);
					startActivity(myBorrowintent);
					break;
				case 2:
					Intent myFavIntent=new Intent(getApplicationContext(),MyFavActivity.class);
					myFavIntent.putExtra("schoolId", recordId);
					startActivity(myFavIntent);
					break;

				default:
					break;
				}				
			}
		});
		
		
		
		
		
	}
	public void loginOut(View v){
		sharedPreferences.edit().clear().commit();
		onCreate(null);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			EditText usernameText = (EditText) findViewById(R.id.username);
			usernameText.setText(scanResult);
			EditText passwordText = (EditText) findViewById(R.id.password);
			passwordText.requestFocus();
		}
	}
}
