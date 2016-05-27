package com.iansoft.android.ExchangeRates;

import java.util.ArrayList;

import android.widget.TextView;
import android.widget.BaseAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class GridViewAdapter extends BaseAdapter {
	private ArrayList<String> datas = new ArrayList<String>();

	public GridViewAdapter(ArrayList<String> datas) {
		this.datas = datas;
    }

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_grid_view, parent, false);

		TextView tvValue = (TextView)convertView.findViewById(R.id.tvValue);
		tvValue.setText(datas.get(position));
		
		return convertView;
	}
}