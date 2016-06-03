package com.hjy.adapters;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.JustYY.xiyoulibrary.R;
import com.hjy.handler.MyContentHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LibraryBoardAdapter extends BaseAdapter {
   
	Context context;
	List<Map<String, String>> resultItems;
	public LibraryBoardAdapter(Context context,List<Map<String, String>> resultItems) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.resultItems=resultItems;
	}
	
	private class ViewHolder {
		TextView title;
		TextView content;	
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.resultItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return resultItems.get(arg0).get("title");
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.board_listview_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.library_board_listview_title);
			holder.content=(TextView) convertView.findViewById(R.id.library_board_listview_content);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		
		holder.title.setText(resultItems.get(arg0).get("boardTitle"));
		
		holder.content.setText("  "+resultItems.get(arg0).get("boardContent"));
		
		return convertView;
	}


}
