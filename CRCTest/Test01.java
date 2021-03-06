package CRCTest;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

public class Test01 {

    @Test
    public void test1() {
        String tx = "70470120210525174634702700111027000605658027110338888341";
        int len = tx.length();
        System.out.println("tx长度：" + len);
        byte[] bb = gbk2str(tx, len);
        String crc = CRC.getCRC3(bb);
        System.out.println("crc:" + crc);

        int crc3 = CRC.getCRC1021(bb, bb.length); //18321
        System.out.println("crc3hex:" + String.format("%x", crc3));//4791


    }

    public static byte[] gbk2str(String inputstr, int len) {
        int i = 0;
        String tenp;
        int nun;
        byte c;
        byte[] bytes = new byte[len / 2];
        String outputstr = "";
        StringBuffer sb = new StringBuffer();
        while (len > 0) {
            tenp = inputstr.substring(i * 2, i * 2 + 2 <= inputstr.length() ? i * 2 + 2 : inputstr.length());//每次取两个字
            //十六进制转成10进制
            nun = Integer.valueOf(tenp, 16);
            c = (byte) nun;
            bytes[i] = c;
            sb.append(c);
            len = len - 2;
            i++;
        }
        outputstr = sb.toString();//1127113233537237052112390171639065101-1283917356-120-12565
        String ss = new String(bytes);//pG !%F4p' ' e�'8��A
        return bytes;
    }


    public static String gbk2str2(String inputstr, int len) {
        int i = 0;
        String tenp;
        int nun;
        char c;
        String outputstr = "";
        StringBuffer sb = new StringBuffer();
        while (len > 0) {
            tenp = inputstr.substring(i * 2, i * 2 + 2 <= inputstr.length() ? i * 2 + 2 : inputstr.length());//每次取两个字
            //十六进制转成10进制
            nun = Integer.valueOf(tenp, 16);
            nun = Integer.parseInt(tenp, 16);
            c = (char) nun;
            sb.append(c);
            len = len - 2;
            i++;
        }
        outputstr = sb.toString();
        return outputstr;
    }


    @Test
    public void test2() throws UnsupportedEncodingException {

        byte[] bytes0 = new byte[]{50, 0, -1, 28, -24};

        String string = new String(bytes0, "utf-8");
        byte[] res = string.getBytes("utf-8");//res变成了9位长度的字节
        String string2 = new String(res, "utf-8");

        string = new String(bytes0, "GBK");
        res = string.getBytes("GBK");//res仍为5位长度的字节，但字节内容已经变了
        string2 = new String(res, "GBK");

        //        0 = 50
        //        1 = 0
        //        2 = 63
        //        3 = 28
        //        4 = 63

        string = new String(bytes0, "ISO-8859-1");
        res = string.getBytes("ISO-8859-1");
        //res为5位长度的字节，和原来一样,证明想要还原最初的byte数组，用ISO-8859-1单字节去编码最靠谱，
        // 除非能确定通过String.getBytes(String decode)方法来得到byte[]时，一定要确定decode的编码表中确实存在String表示的码值，这样得到的byte[]数组才能正确被还原。
//        0 = 50
//        1 = 0
//        2 = -1
//        3 = 28
//        4 = -24


        byte[] b_gbk = "中".getBytes("GBK");
        byte[] b_utf8 = "中".getBytes("UTF-8");//[-28,-72,-83]
        System.out.println(new String(b_utf8, "utf-8").getBytes("utf-8"));
//        0 = -28
//        1 = -72
//        2 = -83
        byte[] b_iso88591 = "中".getBytes("ISO8859-1");
        System.out.println(new String(b_iso88591));
        System.out.println(new String(b_iso88591, Charset.forName("GBK")));
        System.out.println(new String(b_iso88591, Charset.forName("utf-8")));
        System.out.println(new String(b_iso88591, "ISO8859-1"));
        System.out.println(new String(b_utf8));
        System.out.println(new String(b_utf8, Charset.forName("GBK")));
        System.out.println(new String(b_utf8, Charset.forName("utf-8")));
        System.out.println(new String(b_utf8, "ISO8859-1"));
        System.out.println(new String(b_gbk));
        System.out.println(new String(b_gbk, Charset.forName("GBK")));
        System.out.println(new String(b_gbk, Charset.forName("utf-8")));
        System.out.println(new String(b_gbk, "ISO8859-1"));


        byte[] bytes = "1a91031001".getBytes();
        String str = new String(bytes);
        String str2 = new String(bytes, "GBK");
        String str3 = new String(bytes, "UTF-8");
        String str4 = new String(bytes, "ISO-8859-1");

        byte[] after = str.getBytes();
        System.out.println("before: " + Arrays.toString(bytes));
        System.out.println("after : " + Arrays.toString(after));

//        before: 1a91031001--[26, -111, 3, 16, 1]
//        after : 1a3f031001--[26, 63, 3, 16, 1]
//        查找资料之后，发现是编码的问题，在我的系统上java默认的编码是GBK：
        System.out.println(Charset.defaultCharset().name()); // 输出：GBK
    }


    @Test
    public void testRandomWrite() throws IOException {
        try {
            // create a new RandomAccessFile with filename test
            RandomAccessFile raf = new RandomAccessFile("D:/test.txt", "rw");

            // write bytes in the file
            raf.writeBytes("Hello World");

            // set the file pointer at 0 position
            raf.seek(0);

            // read line
            System.out.println("" + raf.readLine());

            // set the file pointer at 0 position
            raf.seek(0);

            // write bytes at the start
            raf.writeBytes("This is an example");

            // set the file pointer at 0 position
            raf.seek(0);

            // read line
            System.out.println("" + raf.readLine());
        } catch (IOException ex) {
            ex.printStackTrace();
        }//原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/java/io/randomaccessfile_writebytes.html


    }

    @Test
    public void testJson() {
        String jsonStr = "{\"runoob\":\"菜鸟教程\",\"class\":%s}";
        String jsonStr2 = String.format(jsonStr, "5年级");
        String jsonStr3 = "abcd/s/d" + "$" + jsonStr;
        String[] split = jsonStr3.split("$");
        // System.out.println(split[0]);
        // System.out.println(split[1]);
        //  String sjon =split[1];
        System.out.println(jsonStr2);
    }

}
