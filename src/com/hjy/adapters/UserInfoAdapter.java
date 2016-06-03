package com.hjy.adapters;

import com.JustYY.xiyoulibrary.R;
import com.hjy.model.UserInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoAdapter extends BaseAdapter {
   UserInfo model;
   Context context;
	private class ViewHolder {
		TextView seletedTitle;		
		ImageView image;
	}
	public  UserInfoAdapter(UserInfo model,Context context) {
		// TODO Auto-generated constructor stub
		this.model=model;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.userinfo_item, null);
			holder = new ViewHolder();
			holder.seletedTitle = (TextView) convertView
					.findViewById(R.id.userinfo_seletedtitle);
			holder.seletedTitle.setTextColor(R.color.black);
			holder.image=(ImageView) convertView.findViewById(R.id.userinfo_image);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch (arg0) {
		case 0:
			holder.seletedTitle.setText("个人信息");
			holder.image.setImageResource(R.drawable.icon_4_d);
			break;
		case 1:
			holder.seletedTitle.setText("我的借阅");
			holder.image.setImageResource(R.drawable.user_borrow);
			break;
		case 2:
			holder.seletedTitle.setText("我的收藏");
			holder.image.setImageResource(R.drawable.user_fav);
            break;
		default:
			break;
		}
		return convertView;
	}

}
