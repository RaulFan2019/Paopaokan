package com.app.pao.entity.model;

/**
 * Created by Administrator on 2016/5/10.
 */
public class CardEntity {
    public int level;
    public String kmStr;
    public int nextKm;
    public String name;
    public int currentLv;
    public int cardBigBg;
    public int cardSmallBg;

    public CardEntity(int level, String kmStr, int nextKm,String name, int cardBigBg, int cardSmallBg) {
        this.level = level;
        this.kmStr = kmStr;
        this.nextKm = nextKm;
        this.name = name;
        this.cardBigBg = cardBigBg;
        this.cardSmallBg = cardSmallBg;
    }

    public CardEntity() {
    }
}
