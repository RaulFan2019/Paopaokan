package com.app.pao.entity.adapter;

import com.app.pao.entity.network.GetDynamicListResult;

/**
 * Created by Raul on 2016/1/11.
 */
public class DynamicLineItem {

    public boolean isHeader;

    public GetDynamicListResult result;

    public String hearderString;

    public DynamicLineItem(String hearderString, GetDynamicListResult result, boolean isHeader) {
        this.isHeader = isHeader;
        this.hearderString = hearderString;
        this.result = result;
    }
}
