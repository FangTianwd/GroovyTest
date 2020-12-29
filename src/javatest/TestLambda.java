package javatest;

public class TestLambda {
	public static void main(String[] args) {
		new Thread(() -> System.out.println("Hello")).start();
		System.out.println();
		
	}
}