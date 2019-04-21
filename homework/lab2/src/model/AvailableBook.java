package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 记录可借的一些图书信息
 * 
 * @author zz
 *
 */
public class AvailableBook {

	private BooleanProperty selected;	//	是否选中当前列
	private StringProperty ID;	//	图书的 ID
	private StringProperty isbn;	//	图书的 isbn 编码
	private StringProperty name;	//	书名
	private StringProperty author;	//	书的作者
	private StringProperty category;	//	书的类别
	private StringProperty press;	//	出版社
	private IntegerProperty available;	//	可借的副本数
	private StringProperty intr;	//	书的简介

	/**
	 * 构造器
	 * 
	 * @param isbn
	 * @param name
	 * @param author
	 * @param category
	 * @param press
	 * @param available
	 * @param intr
	 */
	public AvailableBook(String isbn, String name, String author, String category, String press, int available,
			String intr) {
		this.isbn = new SimpleStringProperty(isbn);
		this.name = new SimpleStringProperty(name);
		this.author = new SimpleStringProperty(author);
		this.category = new SimpleStringProperty(category);
		this.press = new SimpleStringProperty(press);
		this.available = new SimpleIntegerProperty(available);
		this.intr = new SimpleStringProperty(intr);
	}

	/**
	 * 构造器
	 * 
	 * @param ID
	 * @param isbn
	 * @param name
	 * @param author
	 * @param category
	 * @param press
	 * @param intr
	 */
	public AvailableBook(String ID, String isbn, String name, String author, String category, String press,
			String intr) {
		this.selected = new SimpleBooleanProperty(false);
		this.ID = new SimpleStringProperty(ID);
		this.isbn = new SimpleStringProperty(isbn);
		this.name = new SimpleStringProperty(name);
		this.author = new SimpleStringProperty(author);
		this.category = new SimpleStringProperty(category);
		this.press = new SimpleStringProperty(press);
		this.intr = new SimpleStringProperty(intr);
	}

	public StringProperty isbnProperty() {
		return isbn;
	}

	public StringProperty nameProperty() {
		return name;
	}

	public StringProperty authorProperty() {
		return author;
	}

	public StringProperty categoryProperty() {
		return category;
	}

	public StringProperty pressProperty() {
		return press;
	}

	public IntegerProperty availableProperty() {
		return available;
	}

	public StringProperty intrProperty() {
		return intr;
	}

	public BooleanProperty selelctedProperty() {
		return selected;
	}

	public StringProperty IDProperty() {
		return ID;
	}
	
	public Boolean getSelected() {
		return selected.get();
	}
	
	public String getID() {
		return ID.get();
	}
}
