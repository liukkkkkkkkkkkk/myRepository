package com.mashibing.jvm;
//自定义classLoader
public class MyClassLoader extends  ClassLoader{

    @Override
    public Class<?> findClass(String name)throws  ClassNotFoundException{

        return null;
    }

}
