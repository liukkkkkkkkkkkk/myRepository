package com.mashibing.io;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class IOTest {

    @Test
    public void testCopyFile() { //文件复制
        File readFile = new File("src\\com\\mashibing\\io\\aa.txt");
        File writeFile = new File("src\\com\\mashibing\\io\\bb.txt");
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(readFile);
            os = new FileOutputStream(writeFile);
            //带缓存的输入方式
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void test2() {
        Reader reader = null;
        try {
            /*
             * 需要注意  字符流可以直接读取中文汉字，而字节流在处理带中文的文件的时候会出现乱码
             * */
            reader = new FileReader("src\\com\\mashibing\\io\\cc.txt");
            int read = 0;
            while ((read = reader.read()) != -1) {
                System.out.println((char) read);
            }
            System.out.println("`````````````````````````");
            /* int read = reader.read(); //读一个字符   0 to 65535*/
            FileInputStream fileInputStream = new FileInputStream(new File("src\\com\\mashibing\\io\\aa.txt"));
            int read1 = fileInputStream.read(); //读字节 -1 to 255
            System.out.println((char) read1);  //乱码
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void test3() {
        try {
            FileReader fileReader = new FileReader("src/com/mashibing/io/cc.txt");
            char[] chars = new char[1024]; //添加缓冲区
            int len = 0;
            if ((len = fileReader.read(chars)) != -1) {
                System.out.println(new String(chars, 0, len));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test4() {
        File file = new File("src/com/mashibing/io/dd.txt");
        Writer writer = null;
        try {
            writer = new FileWriter(file);
            writer.write("www.baidu.com");
            writer.write("马士兵教育");
            writer.flush();  //最保险的方式是，在输出流关闭之前，调flush方法 再关闭，
            // 当一个输出流对象带有缓冲区时，就需要flush

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test5() {
        //处理纯文本用字节和字符流都可以 ，但是处理图片、视频及其它的格式时，最好用字节流，用字符流可能出现无法解析
        //这里使用的是字符流读图片，mv.jpg无法显示  Reader,Writer都是字符流
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        try {
            fileReader = new FileReader("114159-15479557194645.jpg");
            fileWriter = new FileWriter("src/com/mashibing/io/mv.jpg");
            char[] chars = new char[1024];
            int len = 0;
            while ((len = fileReader.read(chars)) != -1) {
                fileWriter.write(chars);  //
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void test6() {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream("114159-15479557194645.jpg");
            os = new FileOutputStream("src/com/mashibing/io/114159-15479557194645.jpg");
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                //os.write(buf);  os.write(buf)和 os.write(buf,0,len)结果是一样的
                os.write(buf,0,len);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(is);
            close(os);
        }
    }


    @Test
    public void testOutPutStreamWriter() {
        OutputStreamWriter outputStreamWriter =null;
        OutputStream outputStream =null;
        File file = new File("src/com/mashibing/io/cc.txt");
        try {
            outputStream =new FileOutputStream(file);
            outputStreamWriter =new OutputStreamWriter(outputStream);
            outputStreamWriter.write("马士兵教育2");
            outputStreamWriter.write("abcdefgh",0,5);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
             outputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testInputStreamReader(){
      File file =new File("src/com/mashibing/io/aa.txt");
        FileInputStream inputStream =null;
        InputStreamReader inputStreamReader =null;
        try {
             inputStream =new FileInputStream(file);
             inputStreamReader =new InputStreamReader(inputStream);
            char[] buf = new char[1024];
            int len = 0;
            while((len=inputStreamReader.read(buf))!=-1){
                String str = new String(buf,0,len);
                System.out.println(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(inputStreamReader);
            close(inputStream);
        }
    }


    @Test
    public void testByteArrayInputStream(){
       // byte[] buf = new byte[1024];
        byte[]  buf="www.baidu.com".getBytes();
        ByteArrayInputStream byteArrayInputStream =null;
         byteArrayInputStream =new ByteArrayInputStream(buf);
        int read =0;
        while ((read =byteArrayInputStream.read())!=-1){
            byteArrayInputStream.skip(2);
            System.out.println((char)read);
        }

    }


    @Test
    public void testByteArrayOutputStream(){
        ByteArrayOutputStream byteArrayOutputStream =null;
         byteArrayOutputStream = new ByteArrayOutputStream();
         byteArrayOutputStream.write(56);
        try {
            byteArrayOutputStream.write("com.mashibing.com".getBytes());
            System.out.println(byteArrayOutputStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testBuffedInputStream(){
        File file = new File("src/com/mashibing/io/aa.txt");
        FileInputStream inputStream =null;
        BufferedInputStream bufferedInputStream =null;
        try {
            inputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(inputStream);
            int read = bufferedInputStream.read();
            while ((read =( bufferedInputStream.read()))!=-1){
                System.out.println((char) read);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(bufferedInputStream);
            close(inputStream);
        }
    }

    @Test
    public void testBuffedOutputStream(){
        BufferedOutputStream bufferedOutputStream =null;
        FileOutputStream  fileOutputStream=null;
        File file = new File("src/com/mashibing/io/aa.txt");
        try {
            fileOutputStream =new FileOutputStream(file);
            bufferedOutputStream =new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write("测试bufferdOutputstream".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(bufferedOutputStream);
            close(fileOutputStream);
        }

    }

    @Test
    public void testDataInputStream(){
        DataInputStream dataInputStream =null;
        DataOutputStream dataOutputStream =null;
        FileInputStream fileInputStream =null;
        FileOutputStream fileOutputStream =null;
        try {

                ///////////////////////////写入数据
             fileOutputStream = new FileOutputStream("src/com/mashibing/io/yy.txt");
             dataOutputStream = new DataOutputStream(new FileOutputStream("src/com/mashibing/io/yy.txt"));
             dataOutputStream.writeUTF("测试dataOutpStream");
             dataOutputStream.write(11);
             dataOutputStream.writeBoolean(true);

            //读数据
            fileInputStream =new FileInputStream("src/com/mashibing/io/yy.txt");
            dataInputStream =new DataInputStream(fileInputStream);
            System.out.println(dataInputStream.readUTF());
      /*      System.out.println(dataInputStream.readInt());
            System.out.println(dataInputStream.readBoolean());*/
            System.out.println("~~~~~~~~~~~~~~~~~~~");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(dataInputStream);
            close(fileInputStream);
            close(dataOutputStream);
            close(fileOutputStream);
        }
    }

    @Test
    public void testObjectOutputStream(){
        FileOutputStream fileOutputStream =null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream =new FileOutputStream("src/com/mashibing/io/yy.txt");
             objectOutputStream = new ObjectOutputStream(fileOutputStream);
           // objectOutputStream.writeUTF("www.马士兵.com");
            objectOutputStream.writeObject(new Person(22,"zhangsan","123456")); //将对象写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(objectOutputStream);
        }
    }


    @Test
    public void testObjectInputStream(){
        ObjectInputStream objectInputStream =null;
        FileInputStream fileInputStream =null;
        try {
            fileInputStream = new FileInputStream("src/com/mashibing/io/yy.txt");
            objectInputStream =  new ObjectInputStream(fileInputStream);
            Person person = (Person)objectInputStream.readObject();
            System.out.println(person);  //Person{id=22, name='zhangsan', pwd='null'}

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 输入流InputStream和输出流OutputStream都实现了同一个接口：Closeable。可以试试JDK1.5的新特性：可变参数。
     * @param ios
     */
    public void close(Closeable... ios){
        for (Closeable closeable:ios) {
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