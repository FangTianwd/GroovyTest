package groovytest

//class TestList {
//}
// 正向超过长度，其值为null
assert [1,2,3,4,5][0] == 1
assert [1,2,3,4,5][1] == 2
assert [1,2,3,4,5][2] == 3
assert [1,2,3,4,5][3] == 4
assert [1,2,3,4,5][4] == 5
assert [1,2,3,4,5][5] == null
assert [1,2,3,4,5][6] == null

// 反向超过长度，会抛java.lang.ArrayIndexOutOfBoundsException
assert [1,2,3,4,5][-1] == 5
assert [1,2,3,4,5][-2] == 4
assert [1,2,3,4,5][-3] == 3
assert [1,2,3,4,5][-4] == 2
assert [1,2,3,4,5][-5] == 1

// get方法不能用负数index
//assert [1,2,3,4,5].get(-1) == 5
// getAt可以使用负数index
assert [1,2,3,4,5].getAt(-1) == 5

// List迭代
[1,2,3].each { 
	println "元素 ${it}"
}
[1,2,3].eachWithIndex { it, i ->
	println "${it} ${i}"
}
assert [1,2,3].collect { it*2 } == [2,4,6]
assert [1,2,3].collect([0]) { it*2 } == [0,2,4,6]
assert [1,2,3].empty == false;
assert [1,2,3].clear() == null;
assert [1,2,3].findAll { it >= 3 } == [3]
assert [1,2,3].findIndexOf { it in [2] } == 1

assert true;
// 每个都满足条件返回true
assert [1,2,3].every { it > 0 }
// 有一个满足条件返回true
assert [1,2,3].any { it > 2 }

assert [false, true].any();
assert [true, true].every();
// subList是前包后不包
assert [1,2,3,4,5].subList(0, 1) == [1]

assert [1,2,3,4,5].sum() == 15
assert [1,2,3,4,5].sum(10) == 25
assert ['a','b','c'].sum() == 'abc'
assert ['a','b','c'].sum { 
	it == 'a' ? 1 : it == 'b' ? 2 : it == 'c' ? 3 : 0
} == 6

// 由大到小排序
Comparator c = {
	a, b -> a == b ? 0 : a > b ? -1 : 1
}
def aaa = [1,2,3,4,5];
aaa.sort(c)
assert aaa == [5,4,3,2,1]

// join是在list中添加元素
assert [1,2,3].join("s") == '1s2s3'
assert String.join("-", "1","2","3") == '1-2-3'
// inject是在list中操作
assert [1,2,3].inject('数字相加'){ acc, var ->
	acc + var
} == '数字相加123'
assert [1,2,3].inject(0){ acc, var ->
	acc + var
} == 6

def list = ['abbbb', 'ab','abc','abcd'];
println list.max()
println list.max { it.length() }

// 使用比较器

Comparator mc = {a, b ->
	a == b ? 0 : a > b ? 1 : -1
}

Comparator mc1 = {a, b ->
	a == b ? 0 : Math.abs(a) > Math.abs(b) ? 1 : -1
}
def list1 = [8, -12, 6, -2, 9, -6, 5, -8]	
assert list1.max(mc) == 9
assert list1.max() == 9
assert list1.min() == -12

assert list1.max(mc1) == -12
assert list1.min(mc1) == -2

// List添加元素

def list2 = []
assert list2.empty
list2 << 'a'
assert list2 == ['a']
list2 << 'b' << 'c' << ['d']
assert list2 ==['a', 'b', 'c', ['d']]
// 使用leftShift方法等价于使用 <<
assert list2 << 'e' == list2.leftShift('e')
// +运算符不会改变List本身。 与<<相比，+运算符会创建一个新的列表
assert [1, 2] + 3 + [4, 5] + 6 == [1, 2, 3, 4, 5, 6]
// 等价于调用plus方法
assert [1, 2].plus(3).plus([4, 5]).plus(6) == [1, 2, 3, 4, 5, 6]

def a = [1, 2, 3]
a += 4      //创建了一个新的List
a += [5, 6]
assert a == [1, 2, 3, 4, 5, 6]

assert [1, *[222, 333], 456] == [1, 222, 333, 456]
assert [*[1, 2, 3]] == [1, 2, 3]
// 此列表和任何嵌套数组或集合的内容（递归地）添加到新列表中
assert [1, [2, 3, [4, 5], 6], 7, [8, 9]].flatten() == [1, 2, 3, 4, 5, 6, 7, 8, 9]


def list3 = [1, 2]
list3.add(3)
// addAll 将集合中的每个元素添加进来
list3.addAll([5, 4])
assert list3 == [1, 2, 3, 5, 4]

list3 = [1, 2]
// 在索引1前面插入元素3
list3.add(1, 3)
assert list3 == [1, 3, 2]
// addAll(int index, Collection)在索引1前面插入元素4和5
list3.addAll(1, [4, 5])
assert list3 == [1, 4, 5, 3, 2]
// []运算符根据需要使列表增长
list3[6] = 6
assert list3 == [1, 4, 5, 3, 2, null, 6]

// -= 从原始list创建一个新的list，并删除元素
assert ['a','b','c','b','b'] - 'c' == ['a','b','b','b']
assert ['a','b','c','b','b'] - 'b' == ['a','c']
assert ['a','b','c','b','b'] - ['b','c'] == ['a']

def list4 = [1,2,3,4,3,2,1]
list4 -= 3
assert list4 == [1,2,4,2,1]
assert ( list4 -= [2,4] ) == [1,1]

// remove 删除元素
def list5 = [1,2,3,4,5,6,2,2,1]
assert list5.remove(2) == 3
assert list5 == [1,2,4,5,6,2,2,1]

def list6= ['a','b','c','b','b']
// 删除元素'c'如果删除成功返回true
assert list6.remove('c')
// 删除第一个找到的元素'b'，如果删除成功返回true
assert list6.remove('b')
// 返回false，因为没有任何元素删除
assert ! list6.remove('z')
assert list6 == ['a','b','b']

// clear
def list7= ['a',2,'c',4]
list7.clear()
assert list7 == []


assert 'a' in ['a','b','c']
assert ['a','b','c'].contains('a')
assert [1,3,4].containsAll([1,4])
// 返回元素3在列表中包含的数量
assert [1,2,3,3,3,3,4,5].count(3) == 4
// 返回符合断言的元素在列表中包含的数量
assert [1,2,3,3,3,3,4,5].count {
	it%2==0
} == 2
// intersect 返回两个列表的交集
assert [1,2,4,6,8,10,12].intersect([1,3,6,9,12]) == [1,6,12]
// 两个列表是互斥的，返回true
assert [1,2,3].disjoint( [4,6,9] )
assert ![1,2,3].disjoint( [2,4,6] )
