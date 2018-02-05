/*
 * 文件名：person.java
 * 版权：Copyright 2007-2015 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： person.java
 * 修改人：yunhai
 * 修改时间：2015年10月27日
 * 修改内容：新增
 */
package other.lambda;

/**
 * @author yunhai
 */
public class Person {
	public String name;
	public boolean bool;
	public int level;
	public int salary;
	public int year;

	public Person(String name, int level, int salary) {
		this.name = name;
		this.level = level;
		this.salary = salary;
	}

	public Person(String name, int level, int salary, boolean bool) {
		this.name = name;
		this.level = level;
		this.salary = salary;
		this.bool = bool;
	}

	/**
	 * 设置salary.
	 * 
	 * @return 返回salary
	 */
	public int getSalary() {
		return salary;
	}

	/**
	 * 获取salary.
	 * 
	 * @param salary
	 *            要设置的salary
	 */
	public void setSalary(int salary) {
		this.salary = salary;
	}

	/**
	 * 设置name.
	 * 
	 * @return 返回name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取name.
	 * 
	 * @param name
	 *            要设置的name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
