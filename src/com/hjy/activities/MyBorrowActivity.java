package com.hjy.activities;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.JustYY.xiyoulibrary.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjy.adapters.MyBorrowAdapter;
import com.hjy.adapters.SearchResultAdapter;
import com.hjy.handler.MyContentHandler;
import com.hjy.model.BookDetailModel;
import com.hjy.model.BorrowModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("SimpleDateFormat")
public class MyBorrowActivity extends Activity {
	ListView myBorrowListView;
	MyBorrowAdapter ad;
	List<Map<String, String>> resultItems = new ArrayList<Map<String, String>>();
	private List<BookDetailModel> booksList;
	private List<BorrowModel> borrowList;
	TextView titleBar;
	String recordId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_borrow);
        //标题栏 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		titleBar = (TextView) findViewById(R.id.titlebarText);	
        titleBar.setText("我的借阅");
        Intent intent=getIntent();
        recordId=intent.getStringExtra("schoolId");
		setContentView(R.layout.activity_my_borrow);
		loadData();
	}
	public void loadData(){
		final ProgressDialog tips = new ProgressDialog(this);
		tips.setCancelable(false);
		tips.setIndeterminate(true);
		tips.setMessage("加载中，请稍后...");		
		tips.setCanceledOnTouchOutside(false);
		tips.show();
		
		RequestParams params=new RequestParams();
		params.add("userId", recordId);
		String methodName="getMyBorrowBooksList";
		StringBuffer apiStart=new StringBuffer(getResources().getString(R.string.WWW));
		String methodURL=apiStart.append(methodName).toString();
		final String XMLmark = methodName + "Return";
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(methodURL, params, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					try {
						SAXParserFactory factory = SAXParserFactory.newInstance();
						XMLReader reader = factory.newSAXParser().getXMLReader();
						MyContentHandler handler = new MyContentHandler(XMLmark);
						reader.setContentHandler(handler);
						reader.parse(new InputSource(new StringReader(responseString)));
						String str = handler.getResult().toString();						
//						Log.i("b", str);
						setListView(str);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}							
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				tips.dismiss();
			}
			
		   
		   }
		);
	}
	
	public void setListView(String result) throws ParseException{
		myBorrowListView = (ListView) findViewById(R.id.myBorrowListView);      
		resultItems = new ArrayList<Map<String, String>>();
		String[] arr=result.split("]\\u005B");
		
		String bookJSON=arr[0]+"]";
		String timeJSON="["+arr[1];
//		Log.i("1", bookJSON);
//		Log.i("2", timeJSON);
		    Gson g=new Gson();
			
			booksList=g.fromJson(bookJSON, new TypeToken<List<BookDetailModel>>(){}.getType());
			borrowList=g.fromJson(timeJSON, new TypeToken<List<BorrowModel>>(){}.getType());
			
			for (int i = 0; i < booksList.size(); i++) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("title", booksList.get(i).getTitle());
				temp.put("author",  booksList.get(i).getAuthor());
				temp.put("imagePath",booksList.get(i).getImagePath());
				temp.put("bid",booksList.get(i).getBid());
				
//				Calendar c = Calendar.getInstance(); 
//				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 
//				c.setTime(sf.parse(borrowList.get(i).getShouldReturn())); 
//				c.add(Calendar.MONTH, +1); //加一个月 
				
//				temp.put("time", sf.format(c.getTime()));
				temp.put("time", borrowList.get(i).getShouldReturn());
				temp.put("renew", borrowList.get(i).getRenew()+"");
				resultItems.add(temp);
			}        
			
			ad = new MyBorrowAdapter(resultItems, MyBorrowActivity.this);		
			myBorrowListView.setAdapter(ad);			
			myBorrowListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {	
				Intent bookDetailIntent=new Intent(getApplicationContext(),BookDetailActivity.class);
				BookDetailModel model=booksList.get(arg2);
				Bundle bundle = new Bundle();
				bundle.putSerializable("model", model);
				bookDetailIntent.putExtras(bundle);
				Log.i("clicked", model.getTitle());
				startActivity(bookDetailIntent);				
			}
		});
		
	}
}
