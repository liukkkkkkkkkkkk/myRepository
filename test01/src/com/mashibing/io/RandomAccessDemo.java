package com.mashibing.io;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessDemo {
    /*读文件，按块读*/
    @Test
    public void test1(){
        File file = new File("src/com/mashibing/io/doc.txt");
        long restLength = file.length(); //剩余要读的文件长度 ，刚开始等于整个文件长度
        //分块读取文件,num每次读的块的大小
        int sizeBlock =1024;
        int num =(int)Math.ceil(restLength*1.0/sizeBlock); //向上取整数
        System.out.printf("要被切成%s块",num);
        int beginPos =0;
        int actulSize =(int)(sizeBlock>restLength?restLength:sizeBlock);
        for (int i=0;i<num;i++){
            beginPos =i*sizeBlock; //每次读取时的下标偏移量
           if(i==num-1){
               actulSize =(int)restLength;
           }else{
             actulSize =sizeBlock;
             restLength -=actulSize;
           }
            System.out.println(i+"---起始位置是："+beginPos+"---读取的块大小为："+actulSize);
           readSplit(i,beginPos,actulSize);
        }
    }


    public static  void readSplit(int i,int beginPos,int acturalSize){
        RandomAccessFile randomAccessFile =null;
        try {
             randomAccessFile =new RandomAccessFile(new File("src/com/mashibing/io/doc.txt"),"r");
            randomAccessFile.seek(beginPos); //表示从哪个偏移量开始读数据
            byte[] bytes = new byte[1024];
            int length =0;
            while ((length =randomAccessFile.read(bytes))!=-1){
                if(acturalSize>length){
                    System.out.println( new String(bytes,0,length));
                }else {
                    System.out.println(new String(bytes,0,acturalSize));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
