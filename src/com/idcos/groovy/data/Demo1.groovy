package com.idcos.groovy.data

//Java中基本数据类型
//byte,short,int,long,float,double,char,boolean
byte num1 = 1
short num2 = 2
int num3 = 3
long num4 = 4
float num5 = 5.0
double num6 = 1.0
char ch = 'A'
//println "$num1,$num5"

assert num1 instanceof Byte
assert num4 instanceof Long
println 1++
println(++1)


//String,GString 普通字符串与插值字符串
String str = '张三你好'
//println str
//println "说：$str"
//创建GString时，必须得有插值，否则groovy会将后面内容直接当作String
GString gs = "aad daa $str"
//println gs
//保留格式的字符串
String s = '''a
  as 
   as
    as'''
//println s

// == 相当于Java中equals方法
// 判断两对象是否为同一对象使用 is
assert 'c'.getClass()==String
assert "c".getClass()==String

/*
   in:
    1.可以判断该对象是否是该类的实例化
    2.可以判断容器中是否有该元素
   GString:
    ${}:插入表达式（可插入闭包表达式）
    $:引用表达式
 */
assert "c${1}".getClass() in GString


/*
    其中若字符接收对象是def声明，则需明确指定类型
        使用 as 关键字
*/
def c1 = 'A' as char
assert c1 instanceof Character

//groovy也可用 /来定义字符串，主要用于正则表达式
def foo = /.*foo.*/
println "foo:"+foo



// def 自选类型数据
//def省略变量类型的定义，实际根据变量的类型进行推导。
//  若是整数数字，会自动根据值的大小进行调整
//  若是浮点数字，则默认使用的类型是BigDecimal
//  也为数字类型提供了类型后缀
def a = 1
assert a instanceof Integer
// Integer.MAX_VALUE
def b = 2147483647
assert b instanceof Integer
// Integer.MAX_VALUE + 1
def c = 2147483648
assert c instanceof Long
// Long.MAX_VALUE
def d = 9223372036854775807
assert d instanceof Long
// Long.MAX_VALUE + 1
def e = 9223372036854775808
assert e instanceof BigInteger

def decimal = 12.34
println decimal.getClass()
def a1 = 123l
println a1.getClass()
