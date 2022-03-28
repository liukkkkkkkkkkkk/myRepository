package com.mashibing.io;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

public class IOTest2 {
    @Test
    public void testCharArrayReader() {
        char[] buf = "马士兵教育".toCharArray();
        CharArrayReader charArrayReader = null;
        charArrayReader = new CharArrayReader(buf);
        int read = 0;
        try {
            while ((read = charArrayReader.read()) != -1) {
                System.out.println((char) read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            charArrayReader.close();
        }
    }

    @Test
    public void testCharArrayWriter() {
        CharArrayWriter charArrayWriter = null;
        charArrayWriter = new CharArrayWriter();
        charArrayWriter.write(11);
        charArrayWriter.write(12);
        charArrayWriter.write(65);
        System.out.println(charArrayWriter);
        charArrayWriter.close();
    }

    @Test
    public void testBufferedReader() { //注意，bufferdReader可以每次读一整行记录
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("src/com/mashibing/io/aa.txt"));
            int read = bufferedReader.read();
            System.out.println((char) read);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) { //包含行的内容的字符串，不包括任何行终止字符，如果已达到流的末尾，则为null

                System.out.println(str);
            }
            ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bufferedReader);
            close(inputStream);

        }

    }

    @Test
    public void testBufferedWriter() {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/com/mashibing/io/zz.txt")));
            bufferedWriter.write("测试bufferedWriter");
            bufferedWriter.newLine(); //换行
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bufferedWriter);

        }


        try {
            bufferedWriter = new BufferedWriter(new FileWriter("src/com/mashibing/io/zz.txt"));
            bufferedWriter.write("测试bufferedWriter2");
            bufferedWriter.newLine(); //换行
            bufferedWriter.write("测试bufferedWriter3");
            bufferedWriter.append("dddasdfasf"); ///追加

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bufferedWriter);

        }

    }

    @Test
    public void testWriter() {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream("src/com/mashibing/io/zz.txt"));
            writer.write("abc");
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }

    @Test
    public void testBaidu() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new URL("http://www.baidu.com").openStream(), "utf-8"));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/com/mashibing/io/baidu.html")));
            String msg = null;

            while (!((msg = reader.readLine()) == null)) {
                writer.write(msg);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(writer);
            close(reader);
        }
    }


    @Test
    public void tesePrintStream(){
        PrintStream printStream =null;
        printStream =new PrintStream(System.out);
        try {
            printStream.write("你好，java".getBytes()); //标准输出流，打印到控制台 System.out 等同于printStream
            printStream =new PrintStream(new File("src/com/mashibing/io/print.txt"));
            printStream.write("asdfasfd，切菜模压".getBytes());
            System.out.printf("%s---%d----%.2f","safd",23,11.1111);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(printStream);
        }
    }

    public void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
