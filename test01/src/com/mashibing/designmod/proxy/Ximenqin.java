package com.mashibing.designmod.proxy;

public class Ximenqin {
    public static void main(String[] args) {
        WangPo wangPo = new WangPo();
        wangPo.playWithMan();
        wangPo.makeEyesWithMan();

        System.out.println("~~~~~~~~~~~~~~");
        //////////
        WangPo wangPo2 = new WangPo(new Jiashi());
        wangPo2.playWithMan();
        wangPo2.makeEyesWithMan();
    }
}
