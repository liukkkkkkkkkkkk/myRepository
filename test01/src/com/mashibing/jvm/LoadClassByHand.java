package com.mashibing.jvm;

public class LoadClassByHand {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = LoadClassByHand.class.getClassLoader();
        Class<?> aClass = classLoader.loadClass("com.mashibing.jvm.LoadClassByHand");
        System.out.println(aClass); //class jvm.com.LoadClassByHand


    }
}
