package com.hjy.activities;

import com.JustYY.xiyoulibrary.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

public class LibraryBoardDetailActivity extends Activity {
	TextView titleBar;
	String title;
	String content;
	TextView contextTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_borrow);
        //±ÍÃ‚¿∏ 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		Intent intent=getIntent();
        title=intent.getStringExtra("title");
        content=intent.getStringExtra("content");
		titleBar = (TextView) findViewById(R.id.titlebarText);	
        titleBar.setText(title);        
        setContentView(R.layout.activity_library_board_detail);	
        
        contextTV=(TextView) findViewById(R.id.library_board_detail_content);     
        contextTV.setText("  "+content);
	}
}
