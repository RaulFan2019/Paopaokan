package com.app.pao.entity.event;

/**
 * Created by Raul.Fan on 2016/5/10.
 */
public class EventUpdateVideoPos {

    public int pos;


    public EventUpdateVideoPos() {
    }

    public EventUpdateVideoPos(int pos) {
        super();
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
