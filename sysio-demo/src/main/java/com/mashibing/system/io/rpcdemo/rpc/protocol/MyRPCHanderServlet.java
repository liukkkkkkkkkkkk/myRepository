package com.mashibing.system.io.rpcdemo.rpc.protocol;

import com.mashibing.system.io.rpcdemo.rpc.Dispatcher;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 49178
 * @create 2021/12/23
 */
public class MyRPCHanderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream is = req.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        MyContent myContent = null;
        try {
            myContent = (MyContent) ois.readObject();
            Dispatcher dis = Dispatcher.getDis();
            Object obj = dis.get(myContent.getName());
            Class<?> clazz= obj.getClass();
            Method method = clazz.getMethod(myContent.getMethodName(), myContent.getParameterTypes());
            Object res = method.invoke(obj, myContent.getArgs());
            System.out.println("server return res:"+res);
            MyContent resContent = new MyContent();
            resContent.setRes(res);
            ServletOutputStream outputStream = resp.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(resContent);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}