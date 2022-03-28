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

public class MyHttpRPCHandler extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("req:"+req.toString());
        ServletInputStream is = req.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);
        try {
            MyContent content = (MyContent)ois.readObject();
            String methodName = content.getMethodName();
            String serviceName = content.getName();
            Class<?>[] parameterTypes = content.getParameterTypes();
            Object[] args = content.getArgs();
            Dispatcher dis = Dispatcher.getDis();
            Object o = dis.get(serviceName);
            Class<?> clazz = o.getClass();
            Method method = clazz.getMethod(methodName, parameterTypes);
            Object res = method.invoke(o, args);

            ServletOutputStream os = resp.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            MyContent resContent = new MyContent();
            resContent.setRes(res);
            oos.writeObject(resContent);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


    }
}
