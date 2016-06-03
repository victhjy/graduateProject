package com.hjy.activities;

import android.os.Bundle;
import android.util.Log;

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
import com.hjy.adapters.SearchResultAdapter;
import com.hjy.handler.MyContentHandler;
import com.hjy.model.BookDetailModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RankActivity extends Activity {
	ListView rankBooksListView;
	SearchResultAdapter ad;
	List<Map<String, String>> resultItems = new ArrayList<Map<String, String>>();
	private List<BookDetailModel> booksList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		
		loadData();
	}
	public void loadData(){
		final ProgressDialog tips = new ProgressDialog(this);
		tips.setCancelable(false);
		tips.setIndeterminate(true);
		tips.setMessage("º”‘ÿ÷–£¨«Î…‘∫Û...");		
		tips.setCanceledOnTouchOutside(false);
		tips.show();
		
		rankBooksListView=(ListView) findViewById(R.id.rankListView);
		String methodName="getRankBooks";
		StringBuffer apiStart=new StringBuffer(getResources().getString(R.string.WWW));
		String methodURL=apiStart.append(methodName).toString();
		final String XMLmark = methodName + "Return";
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(methodURL, null, new TextHttpResponseHandler() {
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
		rankBooksListView = (ListView) findViewById(R.id.rankListView);

		resultItems = new ArrayList<Map<String, String>>();
		Log.d("result", result);
		    Gson g=new Gson();
			
			booksList=g.fromJson(result, new TypeToken<List<BookDetailModel>>(){}.getType());
			for (int i = 0; i < booksList.size(); i++) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("title", booksList.get(i).getTitle());
				temp.put("author",  booksList.get(i).getAuthor());
				temp.put("imagePath",booksList.get(i).getImagePath());
				temp.put("getBookNum", booksList.get(i).getBid());
				resultItems.add(temp);
			}        
			
			ad = new SearchResultAdapter(resultItems, RankActivity.this);		
			rankBooksListView.setAdapter(ad);			
			rankBooksListView.setOnItemClickListener(new OnItemClickListener() {

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
