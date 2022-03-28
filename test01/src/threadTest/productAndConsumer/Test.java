package threadTest.productAndConsumer;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {
    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue<Goods>(5);
        ProducerQueue producerQueue =new ProducerQueue(queue);
        ConSumerQueue conSumerQueue =new ConSumerQueue(queue);
        new Thread(producerQueue).start();
        new Thread(conSumerQueue).start();



    }
}
