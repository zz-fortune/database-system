package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ReaderInfoController {
	
	@FXML
	private Text ID;
	
	@FXML
	private Text name;
	
	@FXML
	private Text sex;
	
	@FXML
	private Text category;
	
	@FXML
	private Text maxvol;
	
	@FXML
	private Text borrowed;
	
	@FXML
	private Text date;
	
	@FXML
	private Text telephone;
	
	@FXML
	private Text address;
	
	private LibManageApp app;
	private Connection connection;
	
	/**
	 * 获取主类的实例
	 * 
	 * @param app 主类的实例
	 * @param connection 与数据库的连接
	 */
	public void setApp(LibManageApp app, Connection connection) {
		this.app=app;
		this.connection=connection;
	}
	
	
	/**
	 * 展示用户基本信息
	 */
	public void showInfo() {
		Statement statement=null;
		ResultSet resultSet=null;
		
		try {
			statement=connection.createStatement();
			resultSet=statement.executeQuery("SELECT * FROM query_reader WHERE ID=\""+app.getUsr()+"\"");
			if (resultSet.next()) {
				ID.setText(resultSet.getString("ID"));
				name.setText(resultSet.getString("name"));
				sex.setText(resultSet.getString("sex"));
				category.setText(resultSet.getString("category"));
				maxvol.setText(resultSet.getString("maxvol"));
				borrowed.setText(resultSet.getString("borrowed"));
				date.setText(resultSet.getString("date"));
				telephone.setText(resultSet.getString("telephone"));
				address.setText(resultSet.getString("address"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
