package com.mashibing.annotation;

public class Student extends Person {
    private String classNmae;
    private String address;

    public String getClassNmae() {
        return classNmae;
    }

    public void setClassNmae(String classNmae) {
        this.classNmae = classNmae;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Student() {
    }

    private Student(String name, String classNmae, int age) {
        System.out.println(String.format("姓名[%s],班级[%s],年龄[%s]", name, classNmae, age));
        this.name = name;
        this.classNmae = classNmae;
        this.age = age;
    }

    public Student(String name, int age, String classNmae, String address) {
        super(name, age);
        this.classNmae = classNmae;
        this.address = address;
    }

    private void add(int a, int b) {
        System.out.println(a + b);
    }

    @Override
    public String toString() {
        return "Student{" +
                "classNmae='" + classNmae + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}