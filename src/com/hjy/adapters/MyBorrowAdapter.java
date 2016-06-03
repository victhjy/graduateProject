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
import com.hjy.activities.MyBorrowActivity;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyBorrowAdapter extends BaseAdapter {
 
	private class ViewHolder {
		TextView bookTitle;
		TextView author;
		TextView getBookNum;
		ImageView image;
		Button button;
	}
	private List<Map<String, String>> resultItems;

	private Context context;
	private List<Map<String, String>> resultItemsTime;

	private Context contextTime;
	private String bid;
	
     public MyBorrowAdapter(List<Map<String, String>> resultItems,
			Context context) {
		this.resultItems = resultItems;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return resultItems.size();		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return resultItems.get(arg0).get("Title");
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		MyListener myListener=null;
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
		this.bid=resultItems.get(position).get("bid");
		myListener=new MyListener(position,holder,Integer.valueOf(resultItems.get(position).get("renew")),context,bid);
		holder.bookTitle.setText(position+1+"、"+resultItems.get(position).get("title"));
		holder.author.setText("作者："+resultItems.get(position).get("author"));
//		holder.getBookNum.setText("索书号："+resultItems.get(position).get("author"));
		holder.getBookNum.setText("应还日期："+resultItems.get(position).get("time"));
		holder.button.setText("续借");
		   Picasso.with(this.context).load(resultItems.get(position).get("imagePath")).placeholder(R.drawable.book).error(R.drawable.book).into(holder.image);	   
		   holder.button.setOnClickListener( myListener);

		return convertView;
	}
	 private class MyListener implements OnClickListener{  
         int mPosition;
         ViewHolder holder;
         int x;
         Context context;
         String bid;
         public MyListener(int inPosition,ViewHolder holder1,int n,Context context1,String bid1){  
             mPosition= inPosition; 
             holder=holder1;
             x=n;
             context=context1;
             bid=bid1;
         }  
         @Override  
         public void onClick(View v) {  
             // TODO Auto-generated method stub  
        	 Log.i("borrow", "click");
        	 if(x==0){
        		 Toast.makeText(context,"已经续借过一次，不可再次续借",0).show();       		 
        	 }else{
        		 x=0;
        		 Toast.makeText(context,"续借成功",0).show();
        		 
        		 Calendar c = Calendar.getInstance(); 
 				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 
 				try {
					c.setTime(sf.parse(resultItems.get(mPosition).get("time")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
 				c.add(Calendar.MONTH, +1); //加一个月 
        		 holder.getBookNum.setText("应还日期："+sf.format(c.getTime()));
        		 
        		 SharedPreferences sharedPreferences;
        			SharedPreferences.Editor editor;
        		 sharedPreferences= context.getSharedPreferences("userInfo", 
        					Activity.MODE_PRIVATE); 
        	  String recordId =sharedPreferences.getString("schoolId", "");
        		 RequestParams params=new RequestParams();
        			params.add("userId", recordId);
        			params.add("bookId", bid);
        			params.add("shouldReturn", sf.format(c.getTime()));
        			String methodName="renew";
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
        							Log.i("b", str);
//        							holder.getBookNum.setText("应还日期："+sf.format(c.getTime()));
        							
        						} catch (Exception e) {
        							e.printStackTrace();
        						}
        					}							
        					
        				}
        				
        				@Override
        				public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        					// TODO Auto-generated method stub
        					Log.i("myborrow", "error");
        					
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
