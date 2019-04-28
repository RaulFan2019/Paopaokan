package com.app.pao.entity.model;

/**
 * Created by Raul.Fan on 2016/3/23.
 */
public class ProcessInfo {
    private int pid; // 进程id
    private int uid; // 进程所在的用户id
    private int memSize; //进程占用的内存,kb
    private String processName; // 进程名
    public String pkgnameList[] ;//运行在进程里的对应的所有程序的包名
    public int getPid() {
        return this.pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMemSize() {
        return this.memSize;
    }

    public void setMemSize(int memSize) {
        this.memSize = memSize;
    }

    public String getProcessName() {
        return this.processName;
    }

    public void setPocessName(String processName) {
        this.processName = processName;
    }

    public String[] getPkgnameList() {
        return pkgnameList;
    }

    public void setPkgnameList(String[] pkgnameList) {
        this.pkgnameList = pkgnameList;
    }
}
