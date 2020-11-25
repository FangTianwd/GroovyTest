package com.idcos.groovy.io

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

//io操作---读文件

String uri = "/Users/qjq/Desktop/"
String fileName = "test.txt"
String url = uri + fileName
//读取文件

//1.基础方式读取文件
Path file = Paths.get(url)
def charset = Charset.forName("utf-8")
def reader = Files.newBufferedReader(file, charset)
String line = null
while ((line = reader.readLine()) != null) {
    println line
}
//Groovy方式
new File(url).eachLine('utf-8'){println it}
//只读文件，简单方式
println new File(url).text

