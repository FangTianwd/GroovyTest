package com.idcos.groovy.data

//容器
//不能使用 {} 来给数组初始化

//List
/*
    默认使用ArrayList
    初始化时可直接使用[]包含要放入的元素
 */
def nums = [1,2,3,4]
assert nums instanceof List
assert nums.class == java.util.ArrayList
assert nums.size() == 4
println nums
//其中对于List中每个元素数据类型可不同
def arr = [1,"asd"]
for (a in arr) {
    println a.class
}
println arr

//Arrays
/*
    可直接声明数据类型
    通过as关键子制定数据类型
 */
String[] arrStr = ['a','b','c']
assert !(arrStr instanceof List)

def numArr = [1,2,3] as int[]
println numArr

//Set
Set set = [1,2,3,4]
println set.class

//Map
/*
    其中，key默认不支持变量，实际上，这里的key被当作字符串来处理
    需要使用括号，才可以定义可变的key
    底层默认使用LinkedHashMap

    可直接使用[]初始化内容。
 */
def map = [a:'asd',b:'bnm',1:'123']
println map.a
println map['b']

def keyVal = 'name'
def map1 = [keyVal:'张三']
assert !map1.containsKey('name')

def map2 = [(keyVal):'张三']
assert map2.containsKey('name')

//Range
def range = 0..5
//输出0到5
//底层实际上是List
println range.collect()