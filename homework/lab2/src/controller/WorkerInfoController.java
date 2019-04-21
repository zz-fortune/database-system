package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class WorkerInfoController {
	@FXML
	private Text ID;
	
	@FXML
	private Text name;
	
	@FXML
	private Text sex;
	
	@FXML
	private Text date;
	
	@FXML
	private Text category;
	
	@FXML
	private Text room;
	
	@FXML
	private Text leader;
	
	
	private LibManageApp app;
	private Connection connection;
	
	public void setApp(LibManageApp app, Connection connection) {
		this.app=app;
		this.connection=connection;
	}
	
	
	
	public void showInfo() {
		Statement statement=null;
		ResultSet resultSet=null;
		
		try {
			statement=connection.createStatement();
			resultSet=statement.executeQuery("SELECT * FROM query_worker WHERE ID=\""+app.getUsr()+"\"");
			if (resultSet.next()) {
				ID.setText(resultSet.getString("ID"));
				name.setText(resultSet.getString("name"));
				sex.setText(resultSet.getString("sex"));
				category.setText(resultSet.getString("category"));
				date.setText(resultSet.getString("date"));
				room.setText(resultSet.getString("room"));
				leader.setText(resultSet.getString("leader"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
