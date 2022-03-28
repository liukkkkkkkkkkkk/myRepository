package threadTest.productAndConsumer;

import java.util.concurrent.BlockingQueue;

public class ConSumerQueue implements  Runnable {
    private  Goods goods;

    private BlockingQueue<Goods> blockingQueue;

    public ConSumerQueue(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        for (int i=0;i<10;i++){
            try {
                Goods goods = blockingQueue.take();
                System.out.println("消费者消费了商品"+goods.getName()+"--"+goods.getBrand());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
