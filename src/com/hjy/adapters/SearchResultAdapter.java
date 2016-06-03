package com.hjy.adapters;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.JustYY.xiyoulibrary.R;
import com.hjy.activities.MyBorrowActivity;
import com.hjy.activities.MyFavActivity;
import com.hjy.activities.RankActivity;
import com.hjy.activities.SearchActivity;
import com.hjy.handler.MyContentHandler;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultAdapter extends BaseAdapter {

	private class ViewHolder {
		TextView bookTitle;
		TextView author;
		TextView getBookNum;
		ImageView image;
		Button button;
	}

	private List<Map<String, String>> resultItems;

	private Context context;
    private Activity activity;
    private String thisBid;
    private int positionParent;
	// private LayoutInflater inflater;

	public SearchResultAdapter(List<Map<String, String>> resultItems,
			Context context) {
		this.resultItems = resultItems;
		this.context = context;
	}

	@Override
	public int getCount() {
		return resultItems.size();
	}

	@Override
	public Object getItem(int position) {
		return resultItems.get(position).get("Title");
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		MyListener myListener=null;
		this.positionParent=position;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.searchresult_item, null);
			holder = new ViewHolder();
			holder.bookTitle = (TextView) convertView
					.findViewById(R.id.searchresult_title);
			holder.author=(TextView) convertView.findViewById(R.id.searchresult_author);
			holder.image=(ImageView) convertView.findViewById(R.id.searchresult_image);
			holder.getBookNum=(TextView) convertView.findViewById(R.id.searchresult_getbooknum);
			holder.button=(Button) convertView.findViewById(R.id.listitem_button);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.bookTitle.setText(position+1+"、"+resultItems.get(position).get("title"));
		holder.author.setText("作者："+resultItems.get(position).get("author"));
//		holder.getBookNum.setText("索书号："+resultItems.get(position).get("author"));
		holder.getBookNum.setText("索书号："+"GBN-"+resultItems.get(position).get("getBookNum"));
		   Picasso.with(this.context).load(resultItems.get(position).get("imagePath")).placeholder(R.drawable.book).error(R.drawable.book).into(holder.image);	   
		
		
		try {
		    this.activity = (Activity)context;
		} catch (Exception e) {
		    e.printStackTrace();
		    //说明是ApplicationContext
		}
		
		
		if(this.activity instanceof SearchActivity){
			holder.button.setText("收藏");
			
		}
		else if(this.activity instanceof RankActivity){
			holder.button.setVisibility(View.GONE);
			holder.getBookNum.setText("索书号："+"GBN-"+(Integer.valueOf(resultItems.get(position).get("getBookNum"))+510));
			
			
		}
		else if(this.activity instanceof MyFavActivity){
			holder.button.setText("取消收藏");
//			myListener=new MyListener(position,this.activity,resultItems.get(position).get("bId"));
		}
		this.thisBid=resultItems.get(position).get("bid");
		myListener=new MyListener(position,this.activity,thisBid);
		holder.button.setOnClickListener( myListener);
		return convertView;
	}

//	public void addItem(Map<String, String> it) {
//		resultItems.add(it);
//	}
	
	 private class MyListener implements OnClickListener{  
         int mPosition;  
         Activity activity;
         String bId;
         public MyListener(int inPosition,Activity activity1,String bid1){  
             this.mPosition= inPosition; 
             this.activity=activity1;
             this.bId=bid1;
         }  
         @Override  
         public void onClick(View v) {  
             // TODO Auto-generated method stub
        	 SharedPreferences sharedPreferences;
			     SharedPreferences.Editor editor;
		         sharedPreferences= context.getSharedPreferences("userInfo", 
					Activity.MODE_PRIVATE); 
	           String recordId =sharedPreferences.getString("schoolId","");	
	           if(recordId==""){
	        	   Toast.makeText(context,"请登录后再进行收藏操作", Toast.LENGTH_SHORT).show();
	    	 }
	           else{
	        	   Toast.makeText(context,"收藏成功", Toast.LENGTH_SHORT).show();
                   
	 	           
	  	          RequestParams params=new RequestParams();
	   			params.add("userId", recordId);
	   			params.add("bookId", bId);
	   			 Log.i("click", params.toString());
	   			String methodName="addMyFav";
	   			StringBuffer apiStart=new StringBuffer(context.getResources().getString(R.string.WWW));
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
//	   							holder.getBookNum.setText("应还日期："+sf.format(c.getTime()));
	   							
	   						} catch (Exception e) {
	   							e.printStackTrace();
	   						}
	   					}							
	   					
	   				}
	   				
	   				@Override
	   				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
	   					// TODO Auto-generated method stub
	   					Log.i("addMyFav", "error");
	   					
	   				}
	   				
	   				@Override
	   				public void onFinish() {
	   					// TODO Auto-generated method stub
	   					super.onFinish();   					
	   				}
	   				
	   			   
	   			   }
	   			);
	           }
             
         }  
           
     }  
	
}
