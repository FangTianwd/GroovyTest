package groovytest

def map = [name: 'Gromit', likes: 'cheese', id: 1234]
assert map.get('name') == 'Gromit'
assert map.get('id') == 1234
assert map['name'] == 'Gromit'
assert map['id'] == 1234
assert map instanceof java.util.Map

// 空map
def emptyMap = [:]
assert emptyMap.size() == 0
emptyMap.put("foo", 5)
assert emptyMap.size() == 1
assert emptyMap.get("foo") == 5

// [a:1]等价于['a':1]
def a = 'Tom'
def b = [(a) : 23]
assert b[(a)] == 23
assert b['Tom'] == 23
//assert b['a'] == 23


// map 的clone()是浅拷贝
def map1 = [
	simple : 123,
	complex: [a: 1, b: 2]
]
def map2 = map1.clone()
assert map2.get('simple') == map1.get('simple')
assert map2.get('complex') == map1.get('complex')
map2.get('complex').put('c', 3)
assert map1.get('complex').get('c') == 3

def map3 = [name: 'Gromit', likes: 'cheese', id: 1234]
assert map3.class == null
assert map3.get('class') == null
assert map3.getClass() == LinkedHashMap

map3 = [1      : 'a',
	   (true) : 'p',
	   (false): 'q',
	   (null) : 'x',
	   'null' : 'z']
assert map3.containsKey(1) // 数字1不是一个标识符，所以得这样调用
assert map3.true == null
assert map3.false == null
assert map3.get(true) == 'p'
assert map3.get(false) == 'q'
assert map3.null == 'z'
assert map3.get(null) == 'x'

// map的迭代
def map4 = [
	Bob  : 42,
	Alice: 54,
	Max  : 33
]
map4.each { entry ->
	println "key : $entry.key"
	println "value : $entry.value"
}
map4.eachWithIndex { entry, i ->
	println "$i - Name: $entry.key Age: $entry.value"
}
map4.each { key, value ->
	println "Name: $key Age: $value"
}
map4.eachWithIndex { key, value, i ->
	println "$i - Name: $key Age: $value"
}

// 不应该使用GString作为Map的键，因为GString的哈希码与等效String的哈希码不同
def key = 'some key'
def map5 = [:]
def gstringKey = "${key.toUpperCase()}"
map5.put(gstringKey,'value')
assert map5.get('SOME KEY') == null
assert map5.get(gstringKey) == 'value'

def map6 = [1:'a', 2:'b', 3:'c']

def entries = map6.entrySet()
entries.each { entry ->
  assert entry.key in [1,2,3]
  assert entry.value in ['a','b','c']
}

def keys = map6.keySet()
assert keys == [1,2,3] as Set

