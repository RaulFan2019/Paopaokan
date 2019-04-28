package com.app.pao.entity.network;

/**
 * Created by Raul on 2015/12/3.
 * 获取短信邀请文本
 */
public class GetSmsInviteResult {


    /**
     * text : 我正在使用123Go！跑步软件，来试试吧!
     * http://a.app.qq.com/o/simple.jsp?pkgname=com.app.pao
     */

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
