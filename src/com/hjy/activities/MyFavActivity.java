package com.hjy.activities;

import java.io.StringReader;
import java.util.ArrayList;
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
import com.hjy.adapters.MyFavAdapter;
import com.hjy.adapters.SearchResultAdapter;
import com.hjy.handler.MyContentHandler;
import com.hjy.model.BookDetailModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

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

public class MyFavActivity extends Activity {

	ListView myBorrowListView;
	MyFavAdapter ad;
	List<Map<String, String>> resultItems = new ArrayList<Map<String, String>>();
	private List<BookDetailModel> booksList;
	TextView titleBar;
	String recordId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fav);
        //标题栏 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		titleBar = (TextView) findViewById(R.id.titlebarText);	
        titleBar.setText("我的收藏");	
        Intent intent=getIntent();
        recordId=intent.getStringExtra("schoolId");
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
		String methodName="getMyFavBooksList";
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
	public void setListView(String result){
		myBorrowListView = (ListView) findViewById(R.id.myFavListView);

		resultItems = new ArrayList<Map<String, String>>();
		Log.i("result", result);
		    Gson g=new Gson();
			
			booksList=g.fromJson(result, new TypeToken<List<BookDetailModel>>(){}.getType());
			for (int i = 0; i < booksList.size(); i++) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("title", booksList.get(i).getTitle());
				temp.put("author",  booksList.get(i).getAuthor());
				temp.put("imagePath",booksList.get(i).getImagePath());
				temp.put("getBookNum",booksList.get(i).getBid());
				temp.put("bId", booksList.get(i).getBid());
				
				resultItems.add(temp);
			}        
			
			ad = new MyFavAdapter(resultItems, MyFavActivity.this);	
			
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
