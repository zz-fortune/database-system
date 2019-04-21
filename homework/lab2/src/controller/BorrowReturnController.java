package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import model.AvailableBook;

public class BorrowReturnController {

	@FXML
	private TextField bookID;

	@FXML
	private TextField readerID;

	@FXML
	private TableView<AvailableBook> bookpreview;

	@FXML
	private TableColumn<AvailableBook, Boolean> selected;

	@FXML
	private TableColumn<AvailableBook, String> ID;

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
	private TableColumn<AvailableBook, String> intro;

	private LibManageApp app;
	private Connection connection;

	/**
	 * 存储主类实例
	 * @param app 主类实例
	 * @param connection 与数据的连接
	 */
	public void setApp(LibManageApp app, Connection connection) {
		this.app = app;
		this.connection = connection;
	}

	/**
	 * 初始化，设置表格的值
	 */
	@FXML
	private void initialize() {
		this.selected.setCellFactory(CheckBoxTableCell.forTableColumn(selected));
		this.selected.setCellValueFactory(cellData -> cellData.getValue().selelctedProperty());
		this.ID.setCellValueFactory(cellData -> cellData.getValue().IDProperty());
		this.isbn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
		this.name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		this.author.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
		this.category.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
		this.press.setCellValueFactory(cellData -> cellData.getValue().pressProperty());
		this.intro.setCellValueFactory(cellData -> cellData.getValue().intrProperty());
	}

	/**
	 * 查询借阅信息
	 */
	@FXML
	private void query() {

		ObservableList<AvailableBook> items = FXCollections.observableArrayList();
		if (this.bookID.getText().equals("")) {
			bookpreview.setItems(items);
			return;
		}
		String value = "\"%" + this.bookID.getText() + "%\"";
		Statement statement = null;
		ResultSet resultSet = null;
		
		//	构造查询语句查询
		try {
			statement = this.connection.createStatement();
			resultSet = statement.executeQuery(
					"SELECT bookID as ID, books.isbn as isbn, book_info.name as name, author, book_category.name as category, press, intro "
							+ "FROM books inner join book_info inner join book_category	"
							+ "on books.isbn=book_info.isbn and book_info.Bcategory=book_category.Bcategory "
							+ "WHERE bookID like " + value);
			while (resultSet.next()) {
				items.add(new AvailableBook(resultSet.getString("ID"), resultSet.getString("isbn"),
						resultSet.getString("name"), resultSet.getString("author"), resultSet.getString("category"),
						resultSet.getString("press"), resultSet.getString("intro")));
			}
		} catch (SQLException e) {
			app.showDialog(e.getMessage(), AlertType.ERROR);
		} finally {
			try {
				resultSet.close();
				statement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		bookpreview.setItems(items);
	}

	/**
	 * 借书
	 */
	@FXML
	private void borrowBook() {
		Statement statement = null;
		ResultSet resultSet = null;
		String RID = readerID.getText();
		try {
			statement = connection.createStatement();
			List<String> books = new ArrayList<String>();
			for (AvailableBook item : this.bookpreview.getItems()) {
				if (!item.getSelected()) {
					continue;
				}
				
				//	查询想借的书是否可借
				resultSet = statement
						.executeQuery("SELECT available FROM books WHERE bookID = \"" + item.getID() + "\"");
				if (!resultSet.next()) {
					continue;
				}
				boolean available = resultSet.getBoolean("available");
				if (!available) {
					continue;
				}
				resultSet.close();
				
				//	查询借阅者是否还可以借书
				resultSet = statement
						.executeQuery("SELECT borrowed, maxvol FROM query_reader WHERE ID = \"" + RID + "\"");
				if (!resultSet.next()) {
					app.showDialog("借阅证号错误", AlertType.ERROR);
					break;
				}
				int borrowed = resultSet.getInt("borrowed");
				int maxvol = resultSet.getInt("maxvol");
				if (borrowed >= maxvol) {
					break;
				}
				
				//	借书成功，更新数据库信息
				statement.execute("insert into borrow_info (BID,RID,WID1,borrowdate,isreturn) values(\"" + item.getID()
						+ "\",\"" + RID + "\",\"" + app.getUsr() + "\",\"2019-1-1\",0)");
				statement.execute("UPDATE reader_info SET Rnum=" + (borrowed + 1) + " WHERE readerID=\"" + RID + "\"");
				statement.execute("UPDATE books SET available=0 WHERE bookID=\"" + item.getID() + "\"");
				books.add(item.getID());
			}
			String info = "成功借取以下书籍：\n";
			for (String string : books) {
				info = info + string + "\n";
			}
			app.showDialog(info, AlertType.INFORMATION);
		} catch (SQLException e) {
			app.showDialog(e.getMessage(), AlertType.ERROR);
		} finally {
			try {
				resultSet.close();
				statement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 还书
	 */
	@FXML
	private void returnBook() {
		Statement statement = null;
		ResultSet resultSet = null;
		String RID = readerID.getText();
		try {
			statement = connection.createStatement();
			List<String> books = new ArrayList<String>();
			for (AvailableBook item : this.bookpreview.getItems()) {
				if (!item.getSelected()) {
					continue;
				}
				
				//	查询书是否已经归还
				resultSet = statement.executeQuery("SELECT isreturn FROM borrow_info WHERE BID = \"" + item.getID()
						+ "\" and RID=\"" + RID + "\"");
				if (!resultSet.next()) {
					continue;
				}
				boolean isreturn = resultSet.getBoolean("isreturn");
				if (isreturn) {
					continue;
				}
				resultSet.close();
				
				//	判断借阅者是否借了该书
				resultSet = statement.executeQuery("SELECT borrowed FROM query_reader WHERE ID = \"" + RID + "\"");
				if (!resultSet.next()) {
					app.showDialog("借阅证号不存在", AlertType.ERROR);
					break;
				}
				int borrowed = resultSet.getInt("borrowed");
				if (borrowed <= 0) {
					borrowed=0;
				}else {
					borrowed--;
				}
				
				//	换书成功，更新数据库
				statement.execute("UPDATE borrow_info SET isreturn=1,WID2=\""+app.getUsr()+"\" WHERE BID=\""+ item.getID()
				+ "\" and RID=\"" + RID + "\"");
				statement.execute("UPDATE reader_info SET Rnum=" + borrowed + " WHERE readerID=\"" + RID + "\"");
				statement.execute("UPDATE books SET available=1 WHERE bookID=\"" + item.getID() + "\"");
				books.add(item.getID());
			}
			String info = "成功归还以下书籍：\n";
			for (String string : books) {
				info = info + string + "\n";
			}
			app.showDialog(info, AlertType.INFORMATION);
		} catch (SQLException e) {
			app.showDialog(e.getMessage(), AlertType.ERROR);
		} finally {
			try {
				resultSet.close();
				statement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
