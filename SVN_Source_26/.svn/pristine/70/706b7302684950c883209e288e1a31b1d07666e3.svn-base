package com.app.pao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class SimpleBaseAdapter<T> extends BaseAdapter {

	protected Context context;

	protected LayoutInflater layoutInflater = null;

	protected List<T> datas = null;

	protected OnItemBtnClickListener mListener;


	/**
	 * 一个按钮
	 * @author Administrator
	 *
	 */
	public interface OnItemBtnClickListener{
		void onItemBtnClick(int pos);
	}


	public void setOnItemBtnClickListener(OnItemBtnClickListener mListener){
		this.mListener = mListener;
	}

	public SimpleBaseAdapter(Context context, List<T> datas){
		this.context = context;
		this.datas = datas;
		layoutInflater = LayoutInflater.from(context);
	}

	public void refreshDatas(List<T> datas){
		this.datas = datas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {return datas !=null ? datas.size() : 0;
	}

	@Override
	public T getItem(int position) {
		return datas !=null ? datas.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

}
