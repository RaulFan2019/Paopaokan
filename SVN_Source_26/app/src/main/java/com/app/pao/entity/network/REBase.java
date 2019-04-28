package com.app.pao.entity.network;

import com.app.pao.network.api.BaseResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by Administrator on 2016/6/28.
 */
@HttpResponse(parser = BaseResponseParser.class)
public class REBase {

    public String result;//正确返回的内容
    public int errorcode;//错误编号
    public String errormsg;//错误描述


    public REBase() {
    }

    public REBase(String result, int errorcode, String errormsg) {
        super();
        this.result = result;
        this.errorcode = errorcode;
        this.errormsg = errormsg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }
}
