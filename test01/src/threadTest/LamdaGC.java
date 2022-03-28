package threadTest;

public class LamdaGC {
    public static void main(String[] args) {
        I ii =C::n;
        ii.m();
        for(;;){
            I i =C::n;   //每一个lamda表达式对象都会在内存里产生一个新的class, 在方法区，死循环会产生方法区溢出
        }
    }

    public static interface  I{
        void m();
    }
    public static class C{
        static  void n(){
            System.out.println("hello");
        }
    }
}
