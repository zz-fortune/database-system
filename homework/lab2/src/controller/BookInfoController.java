package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.AvailableBook;

public class BookInfoController {

	@FXML
	private ComboBox<String> type;

	@FXML
	private TextField condition;

	@FXML
	private TableView<AvailableBook> books;

	@FXML
	private TableColumn<AvailableBook, String> isbn;

	@FXML
	private TableColumn<AvailableBook, String> name;

	@FXML
	private TableColumn<AvailableBook, String> author;

	@FXML
	private TableColumn<AvailableBook, String> category;

	@FXML
	private TableColumn<AvailableBook, String> press;

	@FXML
	private TableColumn<AvailableBook, Integer> available;

	@FXML
	private TableColumn<AvailableBook, String> intr;

	private LibManageApp app;
	private Connection connection;

	public void setApp(LibManageApp app, Connection connection) {
		this.app = app;
		this.connection = connection;
	}

	@FXML
	private void initialize() {
		this.isbn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
		this.name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		this.author.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		this.category.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
		this.press.setCellValueFactory(cellData -> cellData.getValue().pressProperty());
		this.available.setCellValueFactory(cellData -> cellData.getValue().availableProperty().asObject());
		this.intr.setCellValueFactory(cellData -> cellData.getValue().intrProperty());
		setType();
	}

	/**
	 * 查询图书信息
	 */
	@FXML
	private void query() {
		String col = this.type.getValue();
		if (col.equals("ISBN")) {
			col = "isbn";
		} else if (col.equals("书名")) {
			col = "name";
		} else if (col.equals("作者")) {
			col = "author";
		} else {
			col = "press";
		}
		ObservableList<AvailableBook> items = FXCollections.observableArrayList();
		if (this.condition.getText().equals("")) {
			books.setItems(items);
			return;
		}
		String value = "\"%" + this.condition.getText() + "%\"";
		Statement statement = null;
		ResultSet resultSet = null;
		
		//	构建语句查询
		try {
			statement = this.connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM query_books WHERE " + col + " like " + value);
			while (resultSet.next()) {
				items.add(new AvailableBook(resultSet.getString("isbn"), resultSet.getString("name"),
						resultSet.getString("author"), resultSet.getString("category"), resultSet.getString("press"),
						resultSet.getInt("available"), resultSet.getString("intro")));
			}
		} catch (SQLException e) {
			app.showDialog(e.getMessage(), AlertType.ERROR);
		}finally {
			try {
				resultSet.close();
				statement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		books.setItems(items);
	}

	/**
	 * 设置初始时的选择框的值
	 */
	private void setType() {
		ObservableList<String> options = FXCollections.observableArrayList("ISBN", "书名", "作者", "出版社");
		type.setItems(options);
		type.setValue("ISBN");
	}

}
