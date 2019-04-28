package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 埋点实体类
 *
 * Created by LY on 2016/4/18.
 */
@Table(name = "maidian")
public class DBEntityMaidian {

    @Id
    @NoAutoIncrement
    @Column(column = "maidianId")
    private String id;
    @Column(column = "code")
    public int code;
    @Column(column = "startTime")
    public String time;

    public DBEntityMaidian(String id, int code, String time) {
        this.id = id;
        this.code = code;
        this.time = time;
    }

    public DBEntityMaidian() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
