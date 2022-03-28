package test;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author 49178
 * @create 2022/2/19
 */
public class DateFormatTest {
    @Test
    public void test1() { //不在BigDecimal里用数字
        System.out.println("不要用数字构造BigDecimal，精度有时会出问题");

        BigDecimal a = new BigDecimal(2.1);
        BigDecimal b = new BigDecimal(1.2);
        BigDecimal e = new BigDecimal(0.0);
        System.out.println("a:" + a);
        System.out.println("b:" + b);
        System.out.println("a+b:" + a.add(b));
        System.out.println("e:" + e);

        System.out.println("用字符串构造BigDecimal才是对的");

        BigDecimal aa = new BigDecimal("2.1");
        BigDecimal bb = new BigDecimal("1.2");
        BigDecimal ee = new BigDecimal("0.0");
        System.out.println("aa:" + aa);
        System.out.println("bb:" + bb);
        System.out.println("aa+bb:" + aa.add(bb));
        System.out.println("ee:" + ee);
    }


    @Test
    public void test2() throws ParseException {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long timeInMillis = cal.getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formatDate = simpleDateFormat.format(new Date(timeInMillis));
        System.out.println(formatDate); //2022-03-16T10:05:29+0800
        String formatStr = formatDate.substring(0, formatDate.length() - 2) + ":" + formatDate.substring(formatDate.length() - 2);
        System.out.println("后缀时区转换成+08:00格式" + formatStr);  //2022-03-16T10:22:32+08:00


        SimpleDateFormat simpleDateFormat0 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSXX");
        String formatDate0 = simpleDateFormat0.format(new Date(timeInMillis));
        System.out.println("formatDate0:" + formatDate0);   //formatDate0:2022-03-16T11:04:090+08:00


        SimpleDateFormat simpleDateFormat01 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        String formatDate01 = simpleDateFormat01.format(new Date(timeInMillis));
        System.out.println("formatDate01:" + formatDate0);   //formatDate0:2022-03-16T11:04:090+08:00


        Date date = simpleDateFormat.parse(formatDate);
        String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        System.out.println("yyyyMMddHHmmss:" + yyyyMMddHHmmss);

        System.out.println("-----------------------------");
        Date parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse("2022-03-16T11:11:48+08:00");
        String yyyyMMddHHmmss2 = new SimpleDateFormat("yyyyMMddHHmmss").format(parse);
        System.out.println("yyyyMMddHHmmss2 : " + yyyyMMddHHmmss2);


    }



    @Test
    public void test3(){
        System.out.println("    ".isEmpty()); //false

        // 记录下获取GMT时间的方法：
//格式可根据需要自定义，如yyyy-MM-dd HH:mm:ss 等等
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Calendar calendar = Calendar.getInstance();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        String dateStr = sdf.format(calendar.getTime());
        System.out.println("格林威治时间为：" + dateStr);
//        输出结果为：格林威治时间为：Tue, 15 Mar 2022 06:16:37 GMT

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long seconds = cal.getTimeInMillis()/1000;
        System.out.println("当前格林威治时间总秒数："+seconds); //1647325167
    }

    /**
     * 将1647325086(当前时间的秒)转换成：yyyyMMdd HH:mm:ss格式
     *
     *
     *
     */
    @Test
    public void getTimeToDay() {
        long seconds =1647325086L;
        Date date = new Date(seconds * 1000L);
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(date)); //20220315141806
    }


    /**
     * 支持jdk1.6的写法<br/>
     * 解析2015-12-27T14:20:34+08:00格式类型的时间<br/>
     * 将2015-12-27T14:20:34+08:00转换成2015-12-27 14:20:34<br/>
     *
     * @param str
     * @return
     * @throws Exception
     */
    @Test
    public void getTimestampTimeV16() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        sdf.setTimeZone(tz);
        Date date = sdf.parse("2015-12-27T14:20:34+08:00");
        String dataStr = date.toString();
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(date)); //20151227142034
        System.out.println(dataStr);//Sun Dec 27 14:20:34 CST 2015
    }

    @Test
    public void parseRfc3339() throws ParseException {
        Date date= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2020-05-13T13:29:14+08:00");
        System.out.println(date);//Wed May 13 13:29:14 CST 2020
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(date)); //20200513132914
    }

    /**
     * 支持jdk1.7的写法<br/>
     * 解析2015-12-27T14:20:34+08:00格式类型的时间<br/>
     * 将2015-12-27T14:20:34+08:00转换成2015-12-27 14:20:34<br/>
     * @param str
     * @return
     * @throws Exception
     */
    @Test
    public void  getTimestampTimeV17( ) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date date = sdf.parse("2015-12-27T14:20:34+08:00");
        System.out.println(date); //Sun Dec 27 14:20:34 CST 2015
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(date));  //20151227142034

    }

}
