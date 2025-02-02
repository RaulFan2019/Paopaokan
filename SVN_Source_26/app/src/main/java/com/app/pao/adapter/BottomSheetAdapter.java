package com.app.pao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.model.BottomSheet;

import java.util.ArrayList;


/**
 * Created by Bowyer on 15/08/06.
 */
public class BottomSheetAdapter extends BaseAdapter {

    Context mContext;

    LayoutInflater mLayoutInflater = null;

    ArrayList<BottomSheet> mBottomSheets;

    public BottomSheetAdapter(Context context, ArrayList<BottomSheet> bottomSheets) {
        mContext = context;
        mBottomSheets = bottomSheets;
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mBottomSheets.size();
    }

    @Override
    public Object getItem(int position) {
        return mBottomSheets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_item_bottom_sheet, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BottomSheet sheet = (BottomSheet) getItem(position);
        viewHolder.mMenuTitle.setText(sheet.getBottomSheetMenuType().getName());
        return convertView;
    }

    static class ViewHolder {

        TextView mMenuTitle;

        public ViewHolder(View view) {
            mMenuTitle = (TextView) view.findViewById(R.id.menu_title);
        }
    }
}
