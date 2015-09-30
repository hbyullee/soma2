package com.swmaestro.vocaapp;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> dataList;
	Context context;
	public SearchAdapter(Context c) {
		super();
		context = c;
		inflater = LayoutInflater.from(c);
		dataList = new ArrayList<String>();

	}

	public void addData(String dataText){
		if (dataList != null && dataText != null){
			dataList.add(dataText);
		}
	}
	public void setData(ArrayList<String> dataList){
		if (this.dataList != null && dataList != null){
			this.dataList=(dataList);
		}
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {

		if (dataList.size() > arg0) {
			return dataList.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {

		View v = arg1;
		
		if (v == null) {
			v = inflater.inflate(R.layout.data_search, null);
		}
		
		((TextView)v.findViewById(R.id.data_text)).setText(dataList.get(position));

		return v;

	}

}