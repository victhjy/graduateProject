package com.hjy.activities;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.jsoup.Jsoup;

import com.JustYY.xiyoulibrary.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hjy.adapters.LibraryBoardAdapter;
import com.hjy.adapters.SearchResultAdapter;
import com.hjy.handler.MyContentHandler;
import com.hjy.model.BookDetailModel;
import com.hjy.model.LibraryBoardModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LibraryBoardActivity extends Activity {

	ListView libraryBoardListView;
	LibraryBoardAdapter ad;
	List<LibraryBoardModel> resultItems ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_board);
		init();
	}
	public void init(){
		
		final ProgressDialog tips = new ProgressDialog(this);
		tips.setCancelable(false);
		tips.setIndeterminate(true);
		tips.setMessage("º”‘ÿ÷–£¨«Î…‘∫Û...");		
		tips.setCanceledOnTouchOutside(false);
		tips.show();	
		String methodName="getLibraryBoard";
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
		libraryBoardListView = (ListView) findViewById(R.id.library_board_listview);

		 List<Map<String,String>> listMaps = new ArrayList<Map<String, String>>();
		    Gson g=new Gson();
			
		    resultItems=g.fromJson(result, new TypeToken<List<LibraryBoardModel>>(){}.getType());
			for (int i = 0; i < resultItems.size(); i++) {
				Map<String, String> temp = new HashMap<String, String>();
				temp.put("boardId", resultItems.get(i).getBoardId());
				temp.put("boardTitle",  resultItems.get(i).getBoardTitle());
				temp.put("boardContent",resultItems.get(i).getBoardContent());				
				listMaps.add(temp);
			} 
			ad = new LibraryBoardAdapter(LibraryBoardActivity.this,listMaps);		
			libraryBoardListView.setAdapter(ad);	
			libraryBoardListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {	
				Intent boardDetailIntent=new Intent(getApplicationContext(),LibraryBoardDetailActivity.class);			        
				boardDetailIntent.putExtra("title", resultItems.get(arg2).getBoardTitle());
				boardDetailIntent.putExtra("content", resultItems.get(arg2).getBoardContent());				
				startActivity(boardDetailIntent);				
			}
		});
		
	}

}
