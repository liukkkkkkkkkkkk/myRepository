package com.msb.multi;

/**
 * 动态的切换数据源
 * 一个数据源切换处理类，有对数据源变量的获取、设置和情况的方法，其中threadlocal用于保存某个线程共享变量。
 */
public class DynamicDataSourceContextHolder {
    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     *  所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static ThreadLocal<String>contextHolder =new ThreadLocal<>();

    public static ThreadLocal<String> getContextHolder() {
        return contextHolder;
    }
    /**
     * 设置数据源变量
     * @param dataSourceType
     */
   public static  void setDataSourceType(String dataSourceType){
       System.out.printf("切换到{%s}数据源", dataSourceType);
        contextHolder.set(dataSourceType);
   }

    /**
     * 获取数据源变量
     * @return
     */
   public static String getDataSourceType(){
       return contextHolder.get();
   }

    /**
     * 清空数据源变量
     */
    public static void clearDataSource(){
        contextHolder.remove();
    }
}
