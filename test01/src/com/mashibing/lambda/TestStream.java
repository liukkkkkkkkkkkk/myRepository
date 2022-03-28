package com.mashibing.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestStream {
    @Test
    public  void test1(){
        //通过数组来生成
        String[]str ={"a","b","c","d"};
        Stream<String> str1 = Stream.of(str);
        str1.forEach(System.out::println);
    }

    @Test
    public  void test2(){
        //通过集合来生成
        List<String> list = Arrays.asList("a", "b", "c", "d");
        Stream<String> stream = list.stream();
        stream.forEach(System.out::println);

        Stream<Integer> generate = Stream.generate(() -> 1);
      //  generate.forEach(System.out::println);//无限输出
        generate.limit(10).forEach(System.out::println); ///返回由该流的元素组成的流，截断长度不能超过10 。
        Stream<Integer> generate2 = Stream.generate(() -> {int i=1;i++;return i;});
        generate2.limit(10).forEach(System.out::println);

        Stream<Integer> iterate = Stream.iterate(1, x -> x + 1);
        iterate.limit(20).forEach(System.out::println);



    }


    @Test
    public  void test3(){
        String str ="abcdefg";
        IntStream intStream =str.chars();
        intStream.forEach(System.out::println);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5,6); //过滤出偶数 如果调用方法返回的是Stream对象，意味着是中间操作
        list.stream().filter(x->x%2==0).forEach(System.out::println);
        //求出集合中所有的偶数的个数
        long count = list.stream().filter(x -> x % 2 == 0).count();
        System.out.println(count);
        //求出集合中所有的偶数的和
        int sum = list.stream().filter((x -> x % 2 == 0)).mapToInt(x -> x).sum();
        System.out.println(sum);
        //求集合中的最大值
        Optional<Integer> max = list.stream().max((x, y) -> x - y);
        System.out.println(max.get());
        //求集合中的最小值
        Optional<Integer> min = list.stream().min((x, y) -> x-y);
        System.out.println(min.get());

        Optional<Integer> any = list.stream().filter(x -> x % 2 == 0).findAny();
        System.out.println(any.get());
        Optional<Integer> first = list.stream().filter(x -> x % 2 == 0).findFirst();
        System.out.println(first.get());

        Stream<Integer> stream = list.stream().filter(x -> {
            System.out.println("代码开始执行");
            return x % 2 == 0;
        });
        System.out.println(stream);  //打印Stream对象，但不会执行具体逻辑

        System.out.println(stream.findFirst().get());  //打印了两次"代码开始执行",表示执行两次该逻辑后取到满足条件的第一个值

    }


    @Test
    public  void test4(){//获取最大值和最小值
        List<Integer> list = Arrays.asList(1, 2,7, 3,9, 4, 5,6);
        Optional<Integer> first = list.stream().sorted().findFirst();
        System.out.println(first.get());
        Optional<Integer> max = list.stream().sorted((x, y) -> y - x).findFirst();
        System.out.println(max.get());
        //按自然序排序
        Arrays.asList("java", "python", "c++", "scala").stream().sorted().forEach(System.out::println);
        System.out.println("~~~~~~~~~~~~~~~");
        //按长度排序
        Arrays.asList("java", "python", "c++", "scala").stream().sorted((a,b)->a.length()-b.length()).forEach(System.out::println);
        //过滤集合中的元素,同时返回一个集合 重要！ Collectors.toList()
        List<Integer> list2 = list.stream().filter(x -> x % 2 == 0).collect(Collectors.toList());
        list2.forEach(System.out::println);
        System.out.println("~~~~~~~~~~~~~~~");
        //去重
        Arrays.asList(3,3,3,4,5,7,5,8).stream().distinct().forEach(System.out::println);
        Arrays.asList(3,3,3,4,5,7,5,8).stream().collect(Collectors.toSet()).forEach(System.out::println);
        System.out.println("~~~~~~~~~~~~~~~");
        //打印21到30之间的数   这样的集合数据
        Stream.iterate(1,x->x+1).limit(50).skip(20).limit(10).forEach(System.out::println);

        //求和
        String str ="11,22,44,33,55";
        String[] split = str.split(",");
        System.out.println(Stream.of(split).mapToInt(x -> Integer.valueOf(x)).sum());
        System.out.println(Stream.of(split).map(x -> Integer.valueOf(x)).mapToInt(x -> x).sum());
        System.out.println(Stream.of(split).mapToInt(Integer::valueOf).sum());
        Stream.of(split).map(Integer::valueOf).mapToInt(x->x).sum();


       ///创建一组自定义对象 Stream接口的map方法用于把每个元素进行映射，转成新的元素
        Arrays.asList("java","c++","python","scala").stream().map(x->new People(x)).forEach(System.out::println);
        String str2 = "java,c++,python,scala";
       Stream.of(str2.split(",")).map(People::new).forEach(System.out::println);
       System.out.println("`!!!!!!!!!!!!!!!!!!!!!!");
       Stream.of(str2.split(",")).map(x->People.build(x)).forEach(System.out::println);
       Stream.of(str2.split(",")).map(People::new).forEach(System.out::println);
        System.out.println("`!!!!!!!!!!!!!!!!!!!!!!");
       //将str中的每一个数值都打印出来，同时算出最终的求和结果
        System.out.println(Stream.of(str.split(",")).peek(System.out::println).mapToInt(Integer::valueOf).sum());

        //判断是否全部元素满足某个条件
        System.out.println(Stream.of(str.split(",")).mapToInt(x -> Integer.valueOf(x)).allMatch(x -> x % 2 == 0));
        System.out.println(Stream.of(str.split(",")).mapToInt(Integer::valueOf).allMatch(x -> x > 0));
        System.out.println(list.stream().allMatch(x -> x > 0));

        System.out.println("把list对象按某个标准进行分组");
        //把list对象按某个标准进行分组
        List<People> peopleList = Arrays.asList(new People("zhangsan", 22),
                new People("lisi", 23),
                new People("wangwu", 23),
                new People("zhaoliu", 24),
                new People("xafsqi", 24),
                new People("xxx8", 25));
        Map<Integer, List<People>> listMap = peopleList.stream().collect(Collectors.groupingBy(x -> x.getAge()));
        listMap.forEach((k,v)-> System.out.println("key:"+k+" val:"+v));


    }
}
