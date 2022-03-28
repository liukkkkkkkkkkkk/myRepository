package com.mashibing.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class ExitTest {
    public static void main(String[] args) {
        BufferedWriter writer =null;
        BufferedReader reader =null;
        InputStreamReader inputStreamReader =null;
        OutputStreamWriter outputStreamWriter =null;
        inputStreamReader =new InputStreamReader(System.in);
        outputStreamWriter =new OutputStreamWriter(System.out);
        reader =new BufferedReader(inputStreamReader);
        writer =new BufferedWriter(outputStreamWriter);
        String str ="";
        try {
            while (! str.equals("exit")){
             str =   reader.readLine();
             writer.write(str);
             writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(writer);
            close(reader);
            close(inputStreamReader);
            close(outputStreamWriter);

        }

    }

    public static void close(Closeable... closeables){
        for (Closeable closeable:closeables){
            if(closeable!=null){
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
