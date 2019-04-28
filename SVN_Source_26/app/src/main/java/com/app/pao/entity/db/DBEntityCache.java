package com.app.pao.entity.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Raul.Fan on 2016/4/20.
 * 缓存数据库
 */

@Table(name = "cache")
public class DBEntityCache {

    @Id
    @NoAutoIncrement
    @Column(column = "cacheId")
    private String id;
    @Column(column = "type")
    public int type;//缓存类型
    @Column(column = "userId")
    public int userId;
    @Column(column = "cache")
    public String cache;//缓存内容

    public DBEntityCache(String id, int type, int userId, String cache) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.cache = cache;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
}
