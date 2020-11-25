package com.idcos.groovy

//断言操作
//def a = 12
//assert a instanceof String,'No'

//判断List是否为空
List b = []
bz = b.size()
println bz


//可直接使用println输出内容
/*
    使用这种方式时，
    groovy会自动生成一个main方法当作脚本入口，
    但实际运行的是整个脚本，自动加入一个run方法。
 */
println "hello"
//也可使用Java一样在类中运行
class Hello{
    static void main(String[] args) {
        println "你好！"
    }
}

//循环
for (i in 0..5) {
    print i+"  "
}
int x = 0
while (x++ < 5) {
    printf x+"  "
}
println()
x=3
int y = 1
//选择
//switch嵌套
switch (x) {
    case 0..3:
        switch (y){
            case 1:
                print x+"  "
        }
        break
    case 4..5:
        y++
        break
    default :
        println 0
}
println y
// ?:
// 若y为true或有值，返回y，否则返回x
def result = y ?: x
println result

//安全占位符---可以避免很多NullPointerException
//若对象不为空，调用方法，若为空，返回null
def re = b?.collect()
