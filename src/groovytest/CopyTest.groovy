package groovytest

assert [1, 2, 3] * 3 == [1, 2, 3, 1, 2, 3, 1, 2, 3]
assert [1, 2, 3].multiply(3) == [1, 2, 3, 1, 2, 3, 1, 2, 3]
assert Collections.nCopies(3, 'b') == ['b', 'b', 'b']

// 来自JDK的nCopies具有与列表的乘法不同的语义
assert Collections.nCopies(2, [1, 2]) == [[1, 2], [1, 2]] //而不是[1,2,1,2]
