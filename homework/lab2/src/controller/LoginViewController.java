package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	private LibManageApp app;
	private Connection connection;

	public void setApp(LibManageApp app, Connection connection) {
		this.app = app;
		this.connection = connection;
	}

	/**
	 * 借阅者登录
	 */
	@FXML
	private void readerLogin() {
		String usr, passwd; // 保存用户名和密码
		usr = this.username.getText();
		passwd = this.password.getText();

		//	 检查输入合法性
		if (!checkInput()) {
			return;
		}
		String cmd = "SELECT password FROM rpassword WHERE readerID=\"" + usr + "\"";
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			
			//	查询数据库，对比用户信息
			statement = connection.createStatement();
			resultSet = statement.executeQuery(cmd);
			if (resultSet != null && resultSet.next()) {
				String p = resultSet.getString("password");
				resultSet.close();
				if (p.equals(passwd)) {
					app.login(LibManageApp.READER, usr);
					return;
				}
			}
			app.showDialog("用户名或密码错误", AlertType.ERROR);
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

	}

	/**
	 * 检查输入的合法性
	 * 
	 * @return {@code true}当输入合法
	 */
	private boolean checkInput() {
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z\\-]+$");
		String usr = this.username.getText();
		String passwd = this.password.getText();
		if (usr.equals("")) {
			app.showDialog("用户名不能为空", AlertType.ERROR);
			return false;
		}

		if (passwd.equals("")) {
			app.showDialog("密码不能为空", AlertType.ERROR);
			return false;
		}

		if (!pattern.matcher(usr).matches() || !pattern.matcher(passwd).matches()) {
			app.showDialog("密码或用户名有误", AlertType.ERROR);
			return false;
		}
		return true;
	}

	/**
	 * 工作人员登录
	 */
	@FXML
	private void workLogin() {
		String usr, passwd; // 保存用户名和密码
		usr = this.username.getText();
		passwd = this.password.getText();

		//	 检查输入合法性
		if (!checkInput()) {
			return;
		}
		String cmd = "SELECT password FROM wpassword WHERE workerID=\"" + usr + "\"";
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			
			//	查询数据库，检查信息
			statement = connection.createStatement();
			resultSet = statement.executeQuery(cmd);
			if (resultSet != null && resultSet.next()) {
				String p = resultSet.getString("password");
				resultSet.close();
				if (p.equals(passwd)) {
					
					//	查询数据库，判断工作人员类型
					resultSet = statement.executeQuery("SELECT category FROM query_worker WHERE ID=\"" + usr + "\"");
					if (resultSet.next()) {
						String type = resultSet.getString("category");
						if (type.equals("管理员")) {
							System.out.println(type);
							app.login(LibManageApp.ADMIN, usr);
							return;
						}

					}
					app.login(LibManageApp.WORKER, usr);
					return;
				}
			}
			app.showDialog("用户名或密码错误", AlertType.ERROR);
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
	}

}
