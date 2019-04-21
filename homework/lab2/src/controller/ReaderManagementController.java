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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import model.ReaderInfo;

public class ReaderManagementController {

	@FXML
	private ComboBox<String> type;

	@FXML
	private TextField condition;

	@FXML
	private TableView<ReaderInfo> readers;

	@FXML
	private TableColumn<ReaderInfo, Boolean> selected;

	@FXML
	private TableColumn<ReaderInfo, String> ID;

	@FXML
	private TableColumn<ReaderInfo, String> name;

	@FXML
	private TableColumn<ReaderInfo, String> sex;

	@FXML
	private TableColumn<ReaderInfo, String> category;

	@FXML
	private TableColumn<ReaderInfo, Integer> maxvol;

	@FXML
	private TableColumn<ReaderInfo, Integer> borrowed;

	@FXML
	private TableColumn<ReaderInfo, String> date;

	@FXML
	private TableColumn<ReaderInfo, String> telephone;

	@FXML
	private TableColumn<ReaderInfo, String> address;

	@FXML
	private TableColumn<ReaderInfo, String> password;

	private LibManageApp app;
	private Connection connection;

	public void setApp(LibManageApp app, Connection connection) {
		this.app = app;
		this.connection = connection;
	}

	@FXML
	private void initialize() {
		this.selected.setCellFactory(CheckBoxTableCell.forTableColumn(this.selected));
		this.selected.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		this.ID.setCellFactory(TextFieldTableCell.forTableColumn());
		this.ID.setCellValueFactory(cellData -> cellData.getValue().IDProperty());
		this.name.setCellFactory(TextFieldTableCell.forTableColumn());
		this.name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		this.sex.setCellFactory(TextFieldTableCell.forTableColumn());
		this.sex.setCellValueFactory(cellData -> cellData.getValue().sexProperty());
		this.category.setCellFactory(TextFieldTableCell.forTableColumn());
		this.category.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
		this.maxvol.setCellValueFactory(cellData -> cellData.getValue().maxvolProperty().asObject());
		this.borrowed.setCellValueFactory(cellData -> cellData.getValue().borrowedProperty().asObject());
		this.date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		this.telephone.setCellFactory(TextFieldTableCell.forTableColumn());
		this.telephone.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());
		this.address.setCellFactory(TextFieldTableCell.forTableColumn());
		this.address.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
		this.password.setCellFactory(TextFieldTableCell.forTableColumn());
		this.password.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
		setType();
	}

	/**
	 * 查询借阅者信息
	 */
	@FXML
	private void query() {
		String col = this.type.getValue();
		if (col.equals("阅读证号")) {
			col = "ID";
		} else {
			col = "name";
		}
		
		//	构造语句查询
		String value = "\"%" + this.condition.getText() + "%\"";
		Statement statement = null;
		ResultSet resultSet = null;
		ObservableList<ReaderInfo> items = FXCollections.observableArrayList();
		try {
			statement = this.connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM query_reader WHERE " + col + " like " + value);
			while (resultSet.next()) {
				items.add(new ReaderInfo(resultSet.getString("ID"), resultSet.getString("name"),
						resultSet.getString("sex"), resultSet.getString("category"), resultSet.getInt("maxvol"),
						resultSet.getInt("borrowed"), resultSet.getString("date"), resultSet.getString("telephone"),
						resultSet.getString("address"), resultSet.getString("password")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		//	展示
		readers.setItems(items);
	}

	private void setType() {
		ObservableList<String> options = FXCollections.observableArrayList("阅读证号", "姓名");
		type.setItems(options);
		type.setValue("阅读证号");
	}

	/**
	 * 
	 * 获得{@code TableView}中的一个空行
	 */
	@FXML
	private void emptyRaw() {
		ObservableList<ReaderInfo> items = FXCollections.observableArrayList();
		items.add(ReaderInfo.emptyReaderInfo());
		this.readers.setItems(items);
	}

	/**
	 * 删除所选的用户
	 */
	@FXML
	private void deleteSelected() {
		Statement statement = null;
		try {
			
			//	构造语句删除
			statement = connection.createStatement();
			for (ReaderInfo item : this.readers.getItems()) {
				System.out.println(item.getSelected());
				if (item.getSelected()) {
					statement.execute("DELETE FROM reader_info WHERE readerID=\"" + item.getID() + "\"");
					statement.execute("DELETE FROM rpassword WHERE readerID=\"" + item.getID() + "\"");
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 更新所选的用户的信息
	 */
	@FXML
	private void updateSelected() {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			for (ReaderInfo item : this.readers.getItems()) {
				
				//	构造语句更新
				if (item.getSelected()) {
					resultSet = statement.executeQuery(
							"SELECT Rcategory FROM reader_category WHERE catename=\"" + item.getCategory() + "\"");
					if (resultSet.next()) {
						int cate = resultSet.getInt("Rcategory");
						statement.execute("UPDATE rpassword SET password=\"" + item.getPassword()
								+ "\" WHERE readerID=\"" + item.getID() + "\"");
						statement.execute("UPDATE reader_info SET Rcategory=" + cate + ",Rname=\"" + item.getName()
								+ "\", Rsex=\"" + item.getSex() + "\",Rtel=\"" + item.getTelephone() + "\",Raddr=\""
								+ item.getAddress() + "\" WHERE readerID=\"" + item.getID() + "\"");
					} else {
						app.showDialog("类别不存在", AlertType.ERROR);
					}
				}

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

	}

	/**
	 * 插入一个用户信息
	 */
	@FXML
	private void insertSelected() {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			
			//	构造语句插入
			for (ReaderInfo item : this.readers.getItems()) {
				if (item.getSelected()) {
					resultSet = statement.executeQuery(
							"SELECT Rcategory FROM reader_category WHERE catename=\"" + item.getCategory() + "\"");
					if (resultSet.next()) {
						int cate = resultSet.getInt("Rcategory");
						statement.execute("INSERT INTO rpassword (readerID, password)  values(\"" + item.getID()
								+ "\",\"" + item.getPassword() + "\")");
						statement.execute(
								"INSERT INTO reader_info (readerID, Rcategory,Rname,Rsex,Rdate,Rtel,Raddr,Rnum)  values(\""
										+ item.getID() + "\",\"" + cate + "\",\"" + item.getName() + "\",\""
										+ item.getSex() + "\",\"2019-1-1\",\"" + item.getTelephone() + "\",\""
										+ item.getAddress() + "\",0)");
					} else {
						app.showDialog("类别不存在", AlertType.ERROR);
					}
				}

			}
		} catch (SQLException e) {
			app.showDialog(e.getMessage(), AlertType.ERROR);
		} finally {
			try {
				statement.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
