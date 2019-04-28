package com.app.pao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pao.R;

import java.util.List;

/**
 * Created by Administrator on 2016/1/15.
 *
 * 列表提示框适配器
 */
public class ListDialogAdapter extends BaseAdapter {
    private List<String> mData ;
    private Context mContext;
    private LayoutInflater inflater;

    public ListDialogAdapter(Context mContext,List<String> mData){
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_dialog_list,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mContentTv.setText(mData.get(position));
        return convertView;
    }

    class ViewHolder{
        private TextView mContentTv;

        public ViewHolder(View view){
            mContentTv = (TextView) view.findViewById(R.id.tv_dialog_content);
        }
    }
}
