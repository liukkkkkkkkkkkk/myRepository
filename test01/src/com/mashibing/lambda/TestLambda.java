package com.mashibing.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestLambda {
    @Test
    public void test1() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("runing");
            }
        }).start();
        new Thread(() -> {
            System.out.println("runing");
        }).start();

        Runnable r = () -> {
            System.out.println("Runnng````````");
        };
        Runnable r2 = () -> System.out.println("Running2~~~~~~~~~~~~");

        List<String> list = Arrays.asList("java", "javascript", "c", "c++", "python", "scala");

        Callable<String> ca = new Callable() {
            @Override
            public String call() throws Exception {
                return "mashibing";
            }
        };
        System.out.println(ca.call());
        Callable<String> c2 = () -> {
            return "mashibing";
        };
        System.out.println(c2.call());
        Callable<String> c3 = () -> "mashibing";
        System.out.println(c3.call());
        /////////////排序
      /*  Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length()-o2.length();
            }
        });
        for (String str :list){
            System.out.println(str);
        }*/
        Collections.sort(list, (a, b) -> a.length() - b.length());
        list.forEach(System.out::println);
    }

    @Test
    public void test2() {
        StudentDao studentDao = new StudentDao() {
            @Override
            public void inser(Student student) {
                System.out.println("插入学生记录");
            }
        };

        StudentDao studentDao2 = (Student student) -> {
            System.out.println("插入学生记录2");
        };
        StudentDao studentDao3 = (student) -> {
            System.out.println("插入学生记录3");
        };
        studentDao.inser(new Student());
        studentDao2.inser(new Student());
        studentDao3.inser(new Student());

        TeacherDao t1 = new TeacherDao() {
            @Override
            public int getTeacher(Teacher teacher) {
                return 0;
            }
        };

        TeacherDao t2 = (teacher) -> {
            return 2;
        };
        TeacherDao t3 = (Teacher teacher) -> {
            return 3;
        };
        TeacherDao t4 = (teacheeee) -> 4;
        TeacherDao t5 = (Teacher teeeee) -> 5;
        System.out.println(t1.getTeacher(new Teacher()));
        System.out.println(t2.getTeacher(new Teacher()));
        System.out.println(t3.getTeacher(new Teacher()));
        System.out.println(t4.getTeacher(new Teacher()));
        System.out.println(t5.getTeacher(new Teacher()));

        Function<String, Integer> function = (str) -> {
            return str.length();
        };
        System.out.println(function.apply("abcde"));

        Supplier<String> supplier = () -> {
            return "sssss";
        };
        System.out.println(supplier.get());
        Supplier<String> supplier2 = () -> "ssssss";
        System.out.println(supplier.get());

        Consumer<String> consumer = (str) -> {
            System.out.println("comsumer");
        };
        Consumer<String> consumer2 = (str) -> System.out.println("comsumer");
        consumer.accept("ss");
        consumer2.accept("ss");


        BiFunction<String,String,Integer>biFunction =(a,b)->{return a.length()+b.length();};
        System.out.println(biFunction.apply("aaa","bbb"));


    }


    @Test
    public void test3() {
        ArrayList<Student> list = new ArrayList<>();
        list.add(new Student("zhangsan", 22, 50));
        list.add(new Student("lisi", 25, 60));
        list.add(new Student("wangwu", 26, 70));
        list.add(new Student("zhaoliu", 24, 80));
        list.add(new Student("xxqi", 23, 90));
        //查找年龄大于24的学生
        getStudentByFilter(list, new AgeFilter());
        //查找学分大于70的学生
        getStudentByFilter(list, new ScoreFilter());

        System.out.println("-------------------------------------");
        getStudentByFilter(list, new StudentFilter() {
            @Override
            public boolean compare(Student s) {
                return s.getAge() > 24;
            }
        });
        //查找学分大于70的学生
        getStudentByFilter(list, new StudentFilter() {
            @Override
            public boolean compare(Student s) {
                return s.getScore() > 70;
            }
        });

        System.out.println("----------------lambda 类型推导 减少代码量---------------------");
        getStudentByFilter(list, (e) -> e.getAge() > 24);
        getStudentByFilter(list, (e) -> e.getScore() > 70);
    }


    @Test
    public void test4() {
        Runnable r1 = () -> get();
        Runnable r2 = () -> {
            int i = get();
            System.out.println(i);
        };

        Runnable r3 =()->find();

        Runnable r4 =()->{String ss =find();
            System.out.println(ss);};
        r1.run();
        r2.run();
        r3.run();
        r4.run();

        LambdaInterface lambdaInterface =()->get();
        System.out.println(lambdaInterface.get());
       // LambdaInterface lambdaInterface2 =()->find();  //报错      String返回值 不能转为int
        LambdaInterface lambdaInterface3 =()->1;
       // LambdaInterface lambdaInterface4 =()->"SS"; //报错      String返回值 不能转为int
        LambdaInterface lambdaInterface5 =()->true?1:0;
    }

    @Test
    public void test5(){
        List<String> list = Arrays.asList("a", "b", "c");
        list.forEach(System.out::println);
        Function<String,Integer> function =(str)->{return str.length();};
        function.apply("abc");
        Consumer<String>consumer =(str)->{
            System.out.println(str);
        };
        consumer.accept("def");
    }

    @Test
    public void test6(){  //静态方法的引用 类名：：方法名
       // System.out.println(put());
        Supplier<String>supplier =()-> TestLambda.put();
        System.out.println(supplier.get());
        Supplier supplier2 =TestLambda::put;
        System.out.println(supplier2.get());

        Supplier<String>supplier3 =Fun::method1;
        System.out.println(supplier3.get());

        Consumer<Integer>consumer =Fun::getSize;
        consumer.accept(32);
        Consumer<Integer> consumer2= (size)->Fun.getSize(size);
        consumer2.accept(33);

        Function<String,String>function =(str)->toUpperStr(str);
        System.out.println(function.apply("abc"));
        Function<String,String>fUnction2 =TestLambda::toUpperStr;
        System.out.println(fUnction2.apply("abc"));
        Function<String,String>function3 =(string)->TestLambda.toUpperStr(string);
        System.out.println(function3.apply("abc"));

        BiFunction<String,String,Integer>biFunction =(b1,b2)->b1.length()+b2.length();
        System.out.println(biFunction.apply("aa", "bb"));
        BiFunction<String,String,Integer> biFunction2 =TestLambda::getLen;
        System.out.println(biFunction2.apply("aa", "bb"));

    }

    @Test
    public void test7(){ //测试lambda实例方法的引用  new 对象::方法名
        System.out.println(new Mytest().put());
        Supplier<String> supplier =()->{return new Mytest().put();};
        Supplier<String> supplier2 =new Mytest()::put;
        System.out.println(supplier.get());
        System.out.println(supplier2.get());

        Mytest mytest = new Mytest();
        Consumer<Integer> consumer =(n)->new Mytest().getSize(n);
        Consumer<Integer>consumer2 =new Mytest()::getSize;
        Consumer<Integer>consumer3 =mytest::getSize;
        consumer.accept(123);
        consumer2.accept(123);
        consumer3.accept(123);

        Function<String,String>function = (str)->mytest.toUppperCase(str);
        Function<String,String> function2 =mytest::toUppperCase;
        System.out.println(function.apply("aaa"));
        System.out.println(function2.apply("aaa"));
    }


    @Test
    public  void test8(){//
        /* 对象方法引用：抽象方法的第一个参数类型刚好是实例方法的类
型，抽象方法剩余的参数恰好可以当做实例方法的参数。如果函
数式接口的实现能由上面说的实例方法调用来实现的话，那么就
可以使用对象方法引用*/
      Consumer<Tool>consumer =(tool)->new Tool().foo();
      Consumer<Tool>consumer2 =(Tool tool)->new Tool().foo();
      consumer.accept(new Tool());

      Consumer<Tool> consumer3 =(Tool tool)->new Tool2().foo();
      consumer3.accept(new Tool());
       Consumer<Tool> consumer4 =Tool::foo;
        consumer4.accept(new Tool());

        BiConsumer<Tool2,String>biConsumer =(too2,str)->new Tool2().show(str);
        BiConsumer<Tool2,String>biConsumer2 =Tool2::show;
        biConsumer.accept(new Tool2(),"abc");
        biConsumer.accept(new Tool2(),"abc");


        BiFunction<Exec,String,Integer>biFunction =(e,s)->{return e.test(s);};
        System.out.println(biFunction.apply(new Exec(), "zhangsna "));
        BiFunction<Exec,String,Integer>biFunction2 =Exec::test;
        System.out.println(biFunction2.apply(new Exec(), "lisi"));

    }

    @Test
    public void test9(){
        /*构造方法引用：如果函数式接口的实现恰好可以通过调用一个类
的构造方法来实现，那么就可以使用构造方法引用*/
        Supplier<Person> supplier =()->new Person();
        supplier.get();
        Supplier<Person> supplier2 =Person::new;
        supplier2.get();
        Supplier<List>supplier3 =()->new ArrayList();
        Supplier<Set> supplier4 = HashSet::new;
        supplier.get();
        Supplier<Thread> supplier5 =Thread::new;
        Supplier<String> supplier6 =()->new String("ss");
        Supplier<String> supplier7 =String::new;

        Consumer<Integer>consumer =(age)->new Account(age);
        Consumer<Integer>consumer2 =Account::new;
        consumer.accept(123);
        consumer.accept(456);


        Function<String,Account>function =(str)->new Account(str);
        Function<String,Account>function2=Account::new;
        function.apply("abc");
        function2.apply("def");


    }

    static  String put(){
        return "put";
    }

    public static int getLen(String s1,String s2){
        return  s1.length()+s2.length();
    }

   public static String toUpperStr(String str){
       return str.toUpperCase();
   }
    public static void getStudentByFilter(ArrayList<Student> students, StudentFilter filter) {
        ArrayList<Student> list = new ArrayList();
        for (Student student : students) {
            if (filter.compare(student)) {
                list.add(student);
            }
        }
        printStudents(list);
    }

    public static void printStudents(ArrayList<Student> students) {
        for (Student student : students) {
            System.out.println(student);
        }

    }


    static int get() {
        return 1;
    }

    static String find() {
        return "hello world";
    }
}
class Fun{
    public static String method1(){
        return "aaabbb";
    }

    public static void getSize(int size){
        System.out.println(size);
    }

}

class  Tool{
    public Integer func(String str){
        return  str.length();
    }
    public  void foo(){
        System.out.println("foo");
    }
}
class  Tool2{
    public Integer func(String str){
        return  str.length();
    }
    public  void foo(){
        System.out.println("foo222222");
    }
    public  void  show(String s){
        System.out.println("show");

    }
}
class Mytest{
    public String put(){
        return "putttt";
    }
    void getSize(int size){
        System.out.println("size");
    }
    public String toUppperCase(String str1){
        return str1.toUpperCase();
    }

}

class Exec{
    public int test(String name){
        return name.length();

    }
}
class Person{
    public Person(){
        System.out.println("person initial`````");
    }
}
class Account{
    public Account() {
        System.out.println("account no param initial....");
    }
    public Account(String str){
        System.out.println("String 参数构造"+str);
    }
    public Account(int i){
        System.out.println("int 参数构造"+i);
    }
}