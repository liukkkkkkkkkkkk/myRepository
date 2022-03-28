package com.mashibing.system.io.rpcdemo.rpc.protocol;

import java.io.Serializable;
import java.util.UUID;

public  class MyHeader implements Serializable {

    /*
    通信上的协议
    1，ooxx值
    2，UUID:requestID
    3，DATA_LEN
    */
    int length;
    long reqUid;
    int flag;

    public MyHeader() {

    }
    public static MyHeader createHeader(byte[] contentBytes) {
        int f = 0x14141414;
        MyHeader myHeader = new MyHeader();
        myHeader.setFlag(f);
        myHeader.setLength(contentBytes.length);
        myHeader.setReqUid(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        return myHeader;
    }

    public static MyHeader createHeader2(byte[] contentBytes) {
        int f =0x14141414;
        MyHeader myHeader = new MyHeader();
        myHeader.setReqUid(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
        myHeader.setFlag(f);
        myHeader.setLength(contentBytes.length);
        return myHeader;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getReqUid() {
        return reqUid;
    }

    public void setReqUid(long reqUid) {
        this.reqUid = reqUid;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
