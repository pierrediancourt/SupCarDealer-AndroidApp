package com.supinfo.supcardealer.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.supinfo.supcardealer.entities.Category;

public class CategoriesAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Category> categories;
	
	public CategoriesAdapter(Context context, ArrayList<Category> categories) {
		this.context = context;
		this.categories = categories;
	}
	
	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		return categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(context);
		textView.setTextSize(18);
		textView.setPadding(0, 0, 0, 30);
		textView.setText(categories.get(position).getName());
		return textView;
	}
	
	
	public int getPositionOf(Category category) {
		int i = 0;
		for(Category c : categories) {
			if(c.getName().equals(category.getName())){
				return i;
			}
			i++;
		}
		return -1;
	}

}
