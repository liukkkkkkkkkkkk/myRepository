package com.mashibing.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressDemo {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);
        System.out.println(InetAddress.getByName("LAPTOP-BHT2CBOD"));
        System.out.println(InetAddress.getByName("www.baidu.com"));
    }
}
