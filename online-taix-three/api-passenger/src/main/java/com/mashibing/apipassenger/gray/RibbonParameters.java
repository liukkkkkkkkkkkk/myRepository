package com.mashibing.apipassenger.gray;

import org.springframework.stereotype.Component;

/**
 * @author 49178
 * @create 2022/3/7
 */
@Component
public  class RibbonParameters {
    private static ThreadLocal local =new ThreadLocal ();

    // get
    public  static <T> T get(){
       return (T)local.get();
    }
    // set
    public static <T> void set(T t){
        local.set(t);
    }

}
