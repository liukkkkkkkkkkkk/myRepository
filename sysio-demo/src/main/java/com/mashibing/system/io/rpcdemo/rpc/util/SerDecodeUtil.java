package com.mashibing.system.io.rpcdemo.rpc.util;

import com.mashibing.system.io.rpcdemo.rpc.protocol.MyContent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerDecodeUtil {
    static ByteArrayOutputStream out = new ByteArrayOutputStream();

    public static synchronized byte[] ser(Object msg) {
        out.reset();
        ObjectOutputStream oout = null;
        byte[] msBody = null;
        try {
            oout = new ObjectOutputStream(out);
            oout.writeObject(msg);
            msBody = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msBody;
    }



    public static byte[] ser2(Object object){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos =null;
        byte[] msgBody =null;
        try {
             oos = new ObjectOutputStream(bos);
             oos.writeObject(object);
            msgBody= bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msgBody;
    }
}
