package com.app.pao.entity.network;

/**
 * Created by Raul on 2015/11/15.
 * 手机号码是否已注册
 */
public class GetPhoneNumIsRegistResult {

    public int isExist;
    public int isBound;

    public GetPhoneNumIsRegistResult() {
    }

    public GetPhoneNumIsRegistResult(int isExist) {
        super();
        this.isExist = isExist;
    }

    public GetPhoneNumIsRegistResult(int isExist, int isBound) {
        this.isExist = isExist;
        this.isBound = isBound;
    }

    public int getIsExist() {
        return isExist;
    }

    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }

    public int getIsBound() {
        return isBound;
    }

    public void setIsBound(int isBound) {
        this.isBound = isBound;
    }
}
