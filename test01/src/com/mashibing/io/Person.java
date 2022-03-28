package com.mashibing.io;

import java.io.Serializable;

/*如果要将对象通过io流进行传输，必须要实现serialable接口，类中必须声明一个 serialVersionUID，值无所谓，但必须要有，
* transient:使用此关键字修饰的变量，在序列化时，不会被序列化保存到文件里，不想被可见的关键字可以加上这个修饰符*/
public class Person implements Serializable {
    long serialVersionUID =1L;
    private int id;
    private String name;
    private transient  String pwd;

    public Person() {
    }

    public Person(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
