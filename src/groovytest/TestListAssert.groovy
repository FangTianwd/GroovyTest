package groovytest

//class TestListAssert {}
// 集合操作
def list = [5,6,7];
println list[0];
println list.getAt(1);
println list.get(1);
println list.size()
list.add(8);
println list.size()
list.putAt(0, 4);
println list.get(0);
// list.set(0, 3);
// set方法 是赋值并返回原值
assert list.set(0, 3) == 4
println list.get(0);



def list1 = ['a','b','c'];
def list2 = new ArrayList<String>(list1);
// 检测每个对应的item。判断是否相同
assert list2 == list1;

// 浅克隆
def list3 = list1.clone();
//list1.add('d');
assert list3 == list1;
