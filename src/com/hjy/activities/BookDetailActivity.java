package com.hjy.activities;

import android.os.Bundle;

import com.JustYY.xiyoulibrary.R;
import com.hjy.model.BookDetailModel;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetailActivity extends Activity {
    TextView titleBar;
    
    TextView author;
    TextView publisher;
    TextView pubdate;
    TextView price;
    TextView pages;
    TextView isbn;
    TextView count;
    TextView summary;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        //±ÍÃ‚¿∏ 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		titleBar = (TextView) findViewById(R.id.titlebarText);	
        Intent intent=getIntent();
        BookDetailModel model=(BookDetailModel) intent.getSerializableExtra("model");
        titleBar.setText(model.getTitle());
        loadData(model);
    }
   public void loadData(BookDetailModel model){
	   author=(TextView) findViewById(R.id.bookdetail_author);
	   publisher=(TextView) findViewById(R.id.bookdetail_publisher);
	   pubdate=(TextView) findViewById(R.id.bookdetail_pubdate);
	   price=(TextView) findViewById(R.id.bookdetail_price);
	   pages=(TextView) findViewById(R.id.bookdetail_pages);
	   isbn=(TextView) findViewById(R.id.bookdetail_isbn);
	   count=(TextView) findViewById(R.id.bookdetail_count);
	   summary=(TextView) findViewById(R.id.bookdetail_summary);
	   image=(ImageView) findViewById(R.id.bookdetail_image);
	   
	   if(model!=null){
		   author.setText(model.getAuthor());
		   publisher.setText(model.getPublisher());
		   pubdate.setText(model.getPubdate());
		   price.setText(model.getPrice());
		   pages.setText(model.getPages());
		   isbn.setText(model.getIsbn13());
		   count.setText("10");
		   summary.setText("  "+model.getSummary());
	
		   Picasso.with(getApplicationContext()).load(model.getImagePath()).placeholder(R.drawable.book).error(R.drawable.book).into(image);	   
	   }
   }
   public void goToDetail(View v){
	   Intent webDetailIntent=new Intent(this,BookDetailWebActivity.class);
	   Intent intent=getIntent();
       BookDetailModel model=(BookDetailModel) intent.getSerializableExtra("model");
       webDetailIntent.putExtra("title", model.getTitle());
       webDetailIntent.putExtra("url", model.getUrl());
	   startActivity(webDetailIntent);
   }  
}
