package javatest;

// 只包含一个抽象方法的接口
@FunctionalInterface
public interface Comparator<T> {
	int compare(T o1, T o2);
	
	public static void main(String[] args) {
		Comparator<Integer> comparator = ((num1, num2) -> {
			return num1-num2;
		});
		Comparator<Integer> comparator1 = (num1, num2) -> num1-num2;
	}
}
