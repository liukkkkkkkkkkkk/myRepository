class Extends_Demo {
    public static void main(String[] args) {
        Cat c = new Cat();            //---------------(1)
        System.out.println("-------------------");
        Cat c1 = new Cat("花花",4);   //----------------(2)
    }
}
class Animal {
    private String color;
    private int foot;

    public Animal(){
        System.out.println("我是父类无参数构造器");
    }

    public Animal(String color,int foot){
        System.out.println("我是父类有参数构造器");
        this.color = color;
        this.foot  = foot;
    }
}
class Cat extends Animal{

    public Cat(){
        super();                     //---------------可以省略
        System.out.println("我是子类无参数构造器");
    }

    public Cat(String color,int foot){              
        //super(color,foot);         //---------------(3)
        super();                     //---------------可以省略
        System.out.println("我是子类有参数构造器");
    }
}