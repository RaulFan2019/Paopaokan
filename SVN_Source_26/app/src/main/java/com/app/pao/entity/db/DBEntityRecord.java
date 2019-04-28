package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Raul.Fan on 2016/3/30.
 * 用户跑步记录
 */
@Table(name = "DBEntityRecord")
public class DBEntityRecord implements Serializable {

    @Id
    @NoAutoIncrement
    @Column(column = "rid")
    public String rid;
    @Column(column = "userid")
    public int userId;
    @Column(column = "type")
    public int type;
    @Column(column = "record")
    public float record;
    @Column(column = "prerecord")
    public float prerecord;
    @Column(column = "percentage")
    public int percentage;
    @Column(column = "prepercentage")
    public int prepercentage;


    public DBEntityRecord(String dbid,int userId, int type, float record, float prerecord, int percentage, int prepercentage) {
        super();
        this.rid = dbid;
        this.userId = userId;
        this.type = type;
        this.record = record;
        this.prerecord = prerecord;
        this.percentage = percentage;
        this.prepercentage = prepercentage;
    }


    public DBEntityRecord() {
    }


    public String getDBID() {
        return rid;
    }

    public void setDBID(String DBID) {
        this.rid = DBID;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getRecord() {
        return record;
    }

    public void setRecord(float record) {
        this.record = record;
    }

    public float getPrerecord() {
        return prerecord;
    }

    public void setPrerecord(float prerecord) {
        this.prerecord = prerecord;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getPrepercentage() {
        return prepercentage;
    }

    public void setPrepercentage(int prepercentage) {
        this.prepercentage = prepercentage;
    }
}
