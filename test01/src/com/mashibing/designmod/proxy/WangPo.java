package com.mashibing.designmod.proxy;
/**
 * 代理人，持有干活的人
 * 和被代理的对象实现相同的接口
* */
public class WangPo implements  KindWoman {
   private KindWoman kindWoman;
    public WangPo() {
        this.kindWoman =new PanJinLian();
    }
    public WangPo(KindWoman kindWoman) {
        this.kindWoman =kindWoman;
    }

    @Override
    public void makeEyesWithMan() {
        this.kindWoman.makeEyesWithMan();

    }

    @Override
    public void playWithMan() {
        this.kindWoman.playWithMan();

    }
}
