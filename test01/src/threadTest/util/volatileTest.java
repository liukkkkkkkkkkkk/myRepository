package threadTest.util;

import org.junit.Test;

/**
 * @author 49178
 * @create 2022/1/17
 */
public class volatileTest {



    public static void main(String[] args) {
        System.out.println("主线程访问到flag变量为false");
        MyThread t1 = new MyThread();
        t1.start();
        while (true){
            if (t1.getFlag()==true){
                System.out.println("flag被改成true了");
            }
        }
    }

    @Test
    public  void test1() {
        System.out.println("主线程访问到flag变量为false");
        MyThread t1 = new MyThread();
        t1.start();
        while (true){
          //  synchronized (t1){
                if (t1.getFlag()==true){
                    System.out.println("flag被改成true了");
                }
          //  }

        }
    }

    static  class MyThread extends  Thread{
     //   private   volatile  boolean flag =false;
        private     boolean flag =false;

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag =true;
        }
        public boolean getFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }


    }
}
