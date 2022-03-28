package com.mashibing.lambda;

public class People {
    private String name;
    private int age;

    public int getAge() {
        return age;
    }

    /**
     *
     * @param name
     * @param age
     */

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public People(String name) {
        this.name = name;
    }

    public People() {
    }
    public static  People build(String name){
        People people = new People();
        people.setName(name);
        return people;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                '}';
    }
}
