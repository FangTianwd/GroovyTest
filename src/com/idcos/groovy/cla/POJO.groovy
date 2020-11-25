package com.idcos.groovy.cla

import groovy.transform.PackageScope

//POJO
//默认会对所有字段 加get，set以及构造方法
//只有通过 @age的方式直接取到字段值，否则全都通过get方法取值
class Person {
    @PackageScope String name
    int age

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
def person = new Person(name: "zzz",age: 15)
println person.name
//没有通过get方法
println person.@name