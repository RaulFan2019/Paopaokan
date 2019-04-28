package com.app.pao.entity.adapter;

import com.app.pao.entity.network.GetWorkoutListResult;

/**
 * Created by LY on 2016/5/8.
 */
public class HistoryListWithTitleItem {
    public static final int ITEM_HISTORY = 1;
    public static final int ITEM_TITEL = 2;

    public int type;
    public GetWorkoutListResult.WorkoutEntity entity;
    public String title;

    public HistoryListWithTitleItem(int type, GetWorkoutListResult.WorkoutEntity entity, String title) {
        this.type = type;
        this.entity = entity;
        this.title = title;
    }
}
