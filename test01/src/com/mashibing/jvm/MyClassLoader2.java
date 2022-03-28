package com.mashibing.jvm;
//自定义classLoader
public class MyClassLoader2 extends  ClassLoader{
     private static  MyClassLoader myClassLoader =new MyClassLoader();
    @Override
    public Class<?> findClass(String name)throws  ClassNotFoundException{

        return null;
    }

   public  MyClassLoader2 (){
       super(myClassLoader);
       /*super.getClass()是表示调用父类的方法。getClass方法来自Object类，它返回对象在运行时的类型。因为在运行时的对象类型是MyClassLoader2，所以this.getClass()和super.getClass()都是返回Mycom.mashibing.jvm.MyClassLoader2。*/
       System.out.println(super.getClass().getName()); //com.mashibing.jvm.MyClassLoader2
       System.out.println(this.getClass().getName()); //com.mashibing.jvm.MyClassLoader2

   };

    public static void main(String[] args) {
        System.out.println(MyClassLoader2.class.getClassLoader()); // sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(new MyClassLoader().getParent()); //sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(new MyClassLoader2().getParent()); //com.mashibing.jvm.MyClassLoader@135fbaa4
        System.out.println(ClassLoader.getSystemClassLoader()); ///sun.misc.Launcher$AppClassLoader@18b4aac2

    }
}
