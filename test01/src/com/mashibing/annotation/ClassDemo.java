package com.mashibing.annotation;

import com.mashibing.lambda.People;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class ClassDemo {
    @Test
    public void test1() throws ClassNotFoundException {
        //通过Class.forname（）获取Class对象
        Class<?> pClass = Class.forName("com.mashibing.lambda.People");
        Class<?> pClass2 = Class.forName("com.mashibing.lambda.People");
        System.out.println(pClass == pClass2);
        System.out.println(pClass.getPackage());
        System.out.println(pClass.getName());
        System.out.println(pClass.getSimpleName());
        //通过类名.class获取
        System.out.println(People.class.getSimpleName());
        //通过对象的getClass方法获取
        System.out.println(new People().getClass().getName());

        //基本数据类型通过TYPE获取Class对象
        Class typ = Integer[].class;
        System.out.println(typ.getName());
        System.out.println(typ.getCanonicalName());

    }

    @Test
    public void test2() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> aClass = Class.forName("com.mashibing.annotation.Student");
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field);
            System.out.println(field.getName());
            System.out.println(field.getType().getModifiers());

        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
        Field address = aClass.getDeclaredField("address");
        System.out.println(address.getName());
        Object o = aClass.newInstance();
/*
       私有属性不能随意访问，如果不加setAccessible 会报Class com.mashibing.annotation.ClassDemo can not access a member of class com.mashibing.annotation.Student with modifiers "private"
       反射在一定程度上破会了封装性，需要合理使用
*/
        address.setAccessible(true);
        address.set(o, "武汉市");
        System.out.println(((Student) o).getAddress());

        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        ///获取对象的普通方法,包含当前对象及其父类的公共方法
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            System.out.println(method);
            System.out.println(method.getName());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        //获取当前类里的所有方法，包括私有的
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.println(method.getName());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        //调用方法 需传入方法名，以及参数的Class对象
        Method addMethod = aClass.getDeclaredMethod("add", int.class, int.class);
        Object o2 = aClass.newInstance();  //如果没有定义无参构造方法，会抛出NoSuchMethodException

        /*
         要加上 addMethod.setAccessible(true) 否则通过反射访问私有方法会抛出IllegalAccessException异常
        * java.lang.IllegalAccessException: Class com.mashibing.annotation.ClassDemo can not access a member of class com.mashibing.annotation.Student with modifiers "private"*/
        addMethod.setAccessible(true);
        Object invoke = addMethod.invoke(o2, 1, 2);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        //getConstructors 获取对象的所有构造方法,只能获取public修饰的,不能获取父类的
        Constructor<?>[] constructors = aClass.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor.getName());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        //getDeclaredConstructors 获得类的所有的构造方法，包含私有
        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        for (Constructor constructor : declaredConstructors) {
            System.out.println(constructor.getName());
        }
        Constructor<?> construt = aClass.getDeclaredConstructor(String.class, String.class, int.class);
        construt.setAccessible(true); //设置可以访问私有构造方法
        Object obj = construt.newInstance("zhangsan", "二班", 20);
        System.out.println(obj);
    }

    /**
     * 统一的表查询方法
     *
     * @param sql    传入的sql
     * @param params 查询参数
     * @param clazz  查询返回的对象
     * @return
     */
    public List getRows(String sql, Object[] params, Class clazz) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
             resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()){
                Object o = clazz.newInstance();  //创建存放结果集的对象
                for (int i=0;i<columnCount;i++){
                    Object objVal = resultSet.getObject(i + 1);
                    //获取列的名称
                    String columnName = metaData.getColumnName(i + 1).toLowerCase();
                    Field declaredField = clazz.getDeclaredField(columnName);
                    Method method =clazz.getMethod(getSetName(columnName),declaredField.getType());
                    if(objVal instanceof  Number){
                        Number number =(Number)objVal;
                        if ("int".equals(declaredField.getType().getSimpleName())){
                            method.invoke(o,number);
                        }
                        if("String".equals(declaredField.getType().getSimpleName())){
                            method.invoke(o,objVal.toString());
                        }
                        if("float".equals(declaredField.getType().getSimpleName())){
                            method.invoke(o,((Number) objVal).floatValue());
                        }
                    }




                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String getSetName(String name){
        return "set"+name.substring(0,1).toUpperCase()+name.substring(1);
    }

}
