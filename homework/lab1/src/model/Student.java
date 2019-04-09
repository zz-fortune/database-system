package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 
 * model class for a Student
 * 
 * @author zz
 *
 */
public class Student {
	
	private final StringProperty studentID;
	private final StringProperty name;
	private final IntegerProperty age;
	private final StringProperty sex;
	private final IntegerProperty department;
	private final IntegerProperty classNum;
	private final StringProperty address;
	
	public Student(String sid, String name, int age, String sex, int dept, int classNum, String address) {
		this.studentID = new SimpleStringProperty(sid);
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(age);
		this.sex = new SimpleStringProperty(sex);
		this.department = new SimpleIntegerProperty(dept);
		this.classNum = new SimpleIntegerProperty(classNum);
		this.address = new SimpleStringProperty(address);
		
	}
	
	public void setStudentID(String sid) {
		this.studentID.set(sid);
	}
	
	public String getStudentID() {
		return this.studentID.get();
	}
	
	public StringProperty studentIDProperty() {
		return this.studentID;
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public String getName() {
		return this.name.get();
	}
	
	public StringProperty nameProperty() {
		return this.name;
	}
	
	public void setAge(int age) {
		this.age.set(age);
	}
	
	public int getAge() {
		return this.age.get();
	}
	
	public IntegerProperty ageProperty() {
		return this.age;
	}
	
	public void setSex(String sex) {
		this.sex.set(sex);
	}
	
	public String getSex() {
		return this.sex.get();
	}
	
	public StringProperty sexProperty() {
		return this.sex;
	}
	
	public void setDepartment(int dept) {
		this.department.set(dept);
	}
	
	public int getDepartment() {
		return this.department.get();
	}
	
	public IntegerProperty departmentProperty() {
		return this.department;
	}
	
	
	public void setClassNum(int classNum) {
		this.classNum.set(classNum);
	}
	
	public int getClassNum() {
		return this.classNum.get();
	}
	
	public IntegerProperty classNumProperty() {
		return this.classNum;
	}
	
	public void setAddress(String address) {
		this.address.set(address);
	}
	
	public String getAddress() {
		return this.address.get();
	}
	
	public StringProperty addressProperty() {
		return this.address;
	}

}
