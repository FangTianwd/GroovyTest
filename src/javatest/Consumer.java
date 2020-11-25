package javatest;
/**
 * 一个参数
 * @author mac
 *
 * @param <T>
 */
@FunctionalInterface
public interface Consumer<T> {
	void accept(T t);
	
//	实现接口或抽象类并且完成方法的声明
	
	
	public static void main(String[] args) {
		 Consumer<String> consumer = (String t) -> System.out.println(t);
		 Consumer<String> consumer1 = System.out::println;
		 Consumer<String> consumer2 = (t) -> System.out.println("直接输出");
		 consumer.accept("123");
		 consumer1.accept("234");
		 consumer2.accept(null);
		 
		 
		 
	}
}
