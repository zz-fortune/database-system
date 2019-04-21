package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 借阅者信息
 * 
 * @author zz
 *
 */
public class ReaderInfo {

	private BooleanProperty selected;	//	当前列是否选中
	private StringProperty ID;	//	借阅证号
	private StringProperty name;	//	借阅者姓名
	private StringProperty sex;	//	借阅者性别
	private StringProperty category;	//	借阅证类型
	private IntegerProperty maxvol;	//	最大可借数
	private IntegerProperty borrowed;	//	已借的数目
	private StringProperty date;	//	办证日期
	private StringProperty telephone;	//	电话
	private StringProperty address;	//	地址
	private StringProperty password;	//	密码

	public ReaderInfo(String ID, String name, String sex, String category, int maxvol, int borrowed, String date,
			String tel, String addr, String passwd) {
		this.selected=new SimpleBooleanProperty(false);
		this.ID = new SimpleStringProperty(ID);
		this.name = new SimpleStringProperty(name);
		this.sex = new SimpleStringProperty(sex);
		this.category = new SimpleStringProperty(category);
		this.maxvol = new SimpleIntegerProperty(maxvol);
		this.borrowed = new SimpleIntegerProperty(borrowed);
		this.date = new SimpleStringProperty(date);
		this.telephone = new SimpleStringProperty(tel);
		this.address = new SimpleStringProperty(addr);
		this.password = new SimpleStringProperty(passwd);
	}

	public static ReaderInfo emptyReaderInfo() {
		return new ReaderInfo("", "", "", "", 0, 0, "", "", "", "");
	}

	public BooleanProperty selectedProperty() {
		return selected;
	}

	public StringProperty IDProperty() {
		return ID;
	}

	public StringProperty nameProperty() {
		return name;
	}

	public StringProperty sexProperty() {
		return sex;
	}

	public StringProperty categoryProperty() {
		return category;
	}

	public IntegerProperty maxvolProperty() {
		return maxvol;
	}

	public IntegerProperty borrowedProperty() {
		return borrowed;
	}

	public StringProperty dateProperty() {
		return date;
	}

	public StringProperty telephoneProperty() {
		return telephone;
	}

	public StringProperty addressProperty() {
		return address;
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public Boolean getSelected() {
		return selected.getValue();
	}

	public String getID() {
		return ID.get();
	}

	public String getName() {
		return name.get();
	}

	public String getSex() {
		return sex.get();
	}

	public String getCategory() {
		return category.get();
	}

	public String getTelephone() {
		return telephone.get();
	}

	public String getAddress() {
		return address.get();
	}

	public String getPassword() {
		return password.get();
	}
}
