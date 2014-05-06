package com.supinfo.supcardealer.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.supinfo.supcardealer.entities.Car;

public class CarsAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Car> cars;
	
	public CarsAdapter(Context context, ArrayList<Car> cars) {
		this.context = context;
		this.cars = cars;
	}
	
	public void setCars(ArrayList<Car> cars) {
		this.cars = cars;
	}

	@Override
	public int getCount() {
		return cars.size();
	}

	@Override
	public Object getItem(int position) {
		return cars.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(context);
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(18);
		textView.setPadding(0, 0, 0, 30);
		textView.setText(cars.get(position).getName());
		if(position%2 == 0)
			textView.setBackgroundColor(Color.DKGRAY);
		else
			textView.setBackgroundColor(Color.BLACK);
		return textView;
	}

}
