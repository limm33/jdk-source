/*
 * 文件名：Example2.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： Example1.java
 * 修改人：yunhai
 * 修改时间：2015年10月27日
 * 修改内容：新增
 */

/**
 * 匿名内部接口排序与lambda排序对比
 * @author     yunhai
 */

package other.lambda;

import java.util.Arrays;
import java.util.Comparator;

public class Lambda2 {
	public static void main(String[] args) {
		String[] players = { "eea", "bbag", "ccc", "dd" };
		System.out.println("排序前：");
		print(players);

		System.out.print("\nSort by name,innerclass:\t");
		Arrays.sort(players, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return (s1.compareTo(s2));
			}
		});
		print(players);

		System.out.print("sort by name,lambda:\t\t");
		Comparator<String> sortByName = (String s1, String s2) -> (s1
				.compareTo(s2));
		Arrays.sort(players, sortByName);
		// or this
		// Arrays.sort(players, (String s1, String s2) -> (s1.compareTo(s2)));
		print(players);
		//
		//
		System.out.print("\nsort by length,innerclass:\t");
		Arrays.sort(players, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return (s1.length() - s2.length());
			}
		});
		print(players);

		System.out.print("sort by length,lambda:\t\t");
		// Comparator<String> sortByLenght = (String s1, String s2) ->
		// (s1.length() - s2.length());
		// Arrays.sort(players, sortByLenght);
		// or this
		Arrays.sort(players,
				(String s1, String s2) -> (s1.length() - s2.length()));
		print(players);
		//
		//
		System.out.print("\nsort by last letter,innerclass:\t");
		Arrays.sort(players, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return (s1.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1));
			}
		});
		print(players);
		System.out.print("sort by last letter,lambda:\t");
		Comparator<String> sortByLastLetter = (String s1, String s2) -> (s1
				.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1));
		Arrays.sort(players, sortByLastLetter);
		// or this
		// Arrays.sort(players, (String s1, String s2) -> (s1.charAt(s1.length()
		// - 1) - s2.charAt(s2.length() - 1)));
		print(players);
	}

	private static void print(String[] players) {
		Arrays.asList(players).forEach(
				(player) -> System.out.print(player + ","));
		System.out.println();
	}
}
