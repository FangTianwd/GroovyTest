package groovytest;
println 'hello world';
def method1(){
	return 'hello';
}
assert method1() == 'hello'
String method2() {
	return 'hello';
}
assert method2() == "hello"

// 1.动态类型
def t1 = 't1';
String t2 = 't2';
t3 = 't3';

def var = 'test';
println var;
var = 5;
println var + 1;

// 单引号字符串
def a = 'hello world';
// 双引号字符串
def b = "are you ok";
assert 'ab' == 'a' + 'b';
//assert 'ab' == a + b:'错误';

// 三个单引号字符串，支持直接换行
def d = ''''1'''';
println 'd的值为' + d;
def e = '''1
2
3
4
''';
println 'e的值为' + e;

def fooPattern = /.*foo.*/;
println fooPattern;
// 双引号字符串支持用$嵌入变量及方法
def name = 'Tom';
def greeting = "hello ${name}"
println greeting
def pai = '3.14';
def paiToString = "${pai.toString()}";
println paiToString;

println '**************闭包**************'

// 闭包
def c1 = {
	println 'hello';
}
// 闭包的调用
c1.call();
c1();
// 返回的地址
println c1.toString();
// 返回的null
println c1.println();
def c2 = { a1, b1 ->
	println a1;
	println b1;
}
c2('1','2');
def c3 = {int a2, String b2 ->
	println a2;
	println b2;
}
c3(1,'2');
def c4 = { ->
	println 'hello'
}
c4();
def c5 = {
	println it
}
println 'c5()的值为' + c5();
println 'c5(1)的值为' + c5(1);
// 没有打印 直接返回数据
def c6 = {
	return it + 1
}

println c6(1);
println c6('1');

// 没有打印 直接返回数据
def c7 = {
	it + 1
}
println c7(1);
println c7('1');
println '***********闭包为参数***********'
def func = { text,Closure closure ->
	println text;
	closure.call();
}
func('1', c4);

println '***********闭包的成员变量***********'

//class MyDelegate {
//	def func = {
//		println('hello')
//	}
//	def c = {
//		func()
//	}
//	c.delegate = new MyDelegate()
//	c.call()
//}
println new File('text').absolutePath

