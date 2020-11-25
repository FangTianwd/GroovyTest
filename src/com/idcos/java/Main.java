package com.idcos.java;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {
        //使用Java代码加载groovy脚本
        //1.使用GroovyClassLoader加载脚本
        GroovyClassLoader loader = new GroovyClassLoader();
        //指定脚本文件
        Class groovy = loader.parseClass(new File("/Users/mac/eclipse-workspace/GroovyTest/src/com/idcos/groovy/Basic1.groovy"));
        //初始化脚本
        GroovyObject o = (GroovyObject) groovy.newInstance();
        //调用脚本文件中生成的run方法，若有返回指，直接接收
        String run = (String) o.invokeMethod("run", null);
        System.out.println(run);


        //2.使用GroovyShell方式加载脚本
        GroovyShell shell = new GroovyShell();
        //指定脚本文件，并生成script对象
        Script parse = shell.parse(new File("/Users/mac/eclipse-workspace/GroovyTest/src/com/idcos/groovy/Basic1.groovy"));
        //通过Script对象调用其方法
        parse.invokeMethod("run",null);

    }
}
