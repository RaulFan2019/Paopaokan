package com.app.pao.entity.network;

import java.util.List;

/**
 * Created by Raul on 2015/12/4.
 * 获取搜索跑团结果
 */
public class GetSearchGroupListResult {

    private int count;//搜索个数

    List<GetGroupListResult> rungroup;//跑团列表

    public GetSearchGroupListResult() {

    }

    public GetSearchGroupListResult(int count, List<GetGroupListResult> rungroup) {
        super();
        this.count = count;
        this.rungroup = rungroup;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<GetGroupListResult> getRungroup() {
        return rungroup;
    }

    public void setRungroup(List<GetGroupListResult> rungroup) {
        this.rungroup = rungroup;
    }
}
