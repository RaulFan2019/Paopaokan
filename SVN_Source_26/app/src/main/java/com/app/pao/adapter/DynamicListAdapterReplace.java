package com.app.pao.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.DynamicViewHolder;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.adapter.DynamicLineItem;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Raul on 2015/11/29.
 * 动态列表适配器
 */
public class DynamicListAdapterReplace extends RecyclerView.Adapter<DynamicViewHolder> {

    /* contains */
    private static final int VIEW_TYPE_HEADER = 0x01;//标题类型
    private static final int VIEW_TYPE_PARTY = 0x02;//活动类型
    private static final int VIEW_TYPE_WOKROUT = 0x03;//跑步历史类型

    private static final String TAG = "DynamicListAdapterReplace";

    private Typeface typeface;//用于设置字体类型


    private ArrayList<DynamicLineItem> mItems;
    private Context mContext;

    public DynamicListAdapterReplace(Context context, ArrayList<DynamicLineItem> datas) {
        mContext = context;
        mItems = datas;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
    }

    @Override
    public DynamicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_dynamic_title, parent, false);
        } else if (viewType == VIEW_TYPE_WOKROUT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_dynamic_workout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_dynamic_party, parent, false);
        }
        return new DynamicViewHolder(view,viewType,typeface);
    }

    @Override
    public void onBindViewHolder(DynamicViewHolder holder, int position) {
        final DynamicLineItem item = mItems.get(position);
        holder.bindItem(mContext, item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position).isHeader) {
            return VIEW_TYPE_HEADER;
        } else {
            if (mItems.get(position).result.getDynamictype() == AppEnum.dynamicType.WORKOUT) {
                return VIEW_TYPE_WOKROUT;
            } else {
                return VIEW_TYPE_PARTY;
            }
        }
    }
}
