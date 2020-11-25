package com.idcos.groovy.io

String uri = "/Users/mac/eclipse-workspace/GroovyTest/"
String fileName = "test.txt"
String url = uri + fileName
def file = new File(url)
//获取文件绝对路径以及文件大小
println "This file ${file.absolutePath} has ${file.length()} bytes"
//判断文件是否是目录
println "File ? ${file.isFile()}"
println "Directory ? ${file.isDirectory()}"


//删除文件
//file.delete()
//复制文件
def newFile = new File(uri + "testDemo.txt")
//newFile << file.text

//获取根目录
def rootFiles = new File("/Users/mac/eclipse-workspace/GroovyTest").listRoots()
rootFiles.each { println it.absolutePath}

//列出特定目录中的文件（当前目录下）
new File(uri).eachFile { println it.getAbsolutePath()}.collect { it }
//列出目录中所有文件（包含子目录）
//new File(uri).eachFileRecurse {println it.getName()}

def asd = []
new File(uri).eachDir { path ->
	asd.add(path)
}
//println asd
def asdf = []
new File(uri, "pom.xml").eachLine { str ->
	asdf.add(str)
}
println asdf