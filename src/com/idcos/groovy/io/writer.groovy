package com.idcos.groovy.io

//io操作---写文件
String uri = "/Users/mac/eclipse-workspace/GroovyTest/"
String fileName = "test1.txt"

//指定文件路径及文件名，并通过BufferedWriter写入数据
new File(uri,fileName).withWriter('utf-8') {
    it.writeLine 'helle world'
}
//创建目录
def file = new File(uri+"testDir")
file.mkdir()
