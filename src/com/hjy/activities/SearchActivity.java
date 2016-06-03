package com.hjy.activities;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParserFactory;
import org.apache.http.Header;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.JustYY.xiyoulibrary.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjy.adapters.SearchResultAdapter;
import com.hjy.handler.MyContentHandler;
import com.hjy.handler.NetworkState;
import com.hjy.model.BookDetailModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;

public class SearchActivity extends Activity implements OnQueryTextListener
		{
	ListView searchResultView;
	SearchView searchKeyword;
	Spinner searchType;
	List<Map<String, String>> resultItems = new ArrayList<Map<String, String>>();
	private String lastSearched = "";
	private List<BookDetailModel> booksList;
	SearchResultAdapter ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);
		searchKeyword = (SearchView) findViewById(R.id.searchKeyword);
		searchKeyword.setSubmitButtonEnabled(true);
		searchKeyword.setOnQueryTextListener(this);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return true;
	}

	@SuppressWarnings({ "null", "deprecation" })
	@SuppressLint("HandlerLeak")
	@Override
	public boolean onQueryTextSubmit(String query) {
		final ProgressDialog tips = new ProgressDialog(this);
		if (!NetworkState.isNetworkAvailable(this)) {
			Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show();
			return false;
		}
		 
		if (lastSearched.equals(query)) {
			searchKeyword.clearFocus();
			return false;
		}
		searchKeyword.clearFocus();
		RequestParams params = new RequestParams();
		String methodName = null;
		if (this.getSearchType()==0){
			methodName="searchBooksWithTitle"; 
			String strBase64 = new String(Base64.encode(query.getBytes(), Base64.DEFAULT));
				params.add("title", strBase64);
			
			
			
		}else if(this.getSearchType()==1){
			
			methodName="searchBooksWithAuthor";
			String strBase64 = new String(Base64.encode(query.getBytes(), Base64.DEFAULT));
			params.add("author", strBase64);
		}
		else if(this.getSearchType()==2){
			
			methodName="searchBooksWithISBN";
			params.add("ISBN", query);
		}
		Log.i("params", params.toString());

//		String methodName="getAllBooksInfo";
		StringBuffer apiStart=new StringBuffer(getResources().getString(R.string.WWW));
		String methodURL=apiStart.append(methodName).toString();
		tips.setCancelable(false);
		tips.setIndeterminate(true);
		tips.setMessage("加载中，请稍等...");		
		tips.setCanceledOnTouchOutside(false);
		tips.show();
				
		 params.setContentEncoding(HTTP.UTF_8);
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
						setListView(str);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "网络请求出错", 1).show();
			}
			@Override
	        public void onFinish() {
				tips.dismiss();
	        }		
		});
		return false;
	}

	public void setListView(String result) {

		searchResultView = (ListView) findViewById(R.id.searchResultView);

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
				temp.put("bid", booksList.get(i).getBid());
				resultItems.add(temp);
			}        
			
			ad = new SearchResultAdapter(resultItems, SearchActivity.this);		
			searchResultView.setAdapter(ad);		
		    searchResultView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {	
				Intent bookDetailIntent=new Intent(SearchActivity.this,BookDetailActivity.class);
				BookDetailModel model=booksList.get(arg2);
				Bundle bundle = new Bundle();
				bundle.putSerializable("model", model);
				bookDetailIntent.putExtras(bundle);				
				startActivity(bookDetailIntent);				
			}
		});
	}

	protected int getSearchType() {
		Spinner searchType = (Spinner) findViewById(R.id.options);
		int index = searchType.getSelectedItemPosition();
		return index;
	}
}
