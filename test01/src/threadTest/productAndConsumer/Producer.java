package threadTest.productAndConsumer;

public class Producer implements  Runnable {
    private Goods goods;

    public Producer(Goods goods) {
        this.goods = goods;
    }

    @Override
    public void run() {
        for(int i =0;i<10;i++){
            if (i%2==0){
                goods.setBrand("娃哈哈");
                goods.setName("矿泉水");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                goods.setBrand("旺仔");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                goods.setName("小馒头");

            }
        }

    }
}
