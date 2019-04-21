package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LibManageApp extends Application {

	//	账号类别
	public static final int READER=0;
	public static final int WORKER=10;
	public static final int ADMIN=11;
	
	//	资源信息
	private final String title = "图书管理系统";
	private final String rootLayoutResource = "/view/RootLayout.fxml";
	
	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/LIBRARY_MANAGEMENT";
	private static final String USER = "root";
	private static final String PAWD = "zz1160300620";
	private Connection connection;
	
	public static final String loginView="/view/LoginView.fxml";
	public static final String readerInfo="/view/ReaderInfoView.fxml";
	public static final String workerInfo="/view/WorkerInfoView.fxml";
	public static final String bookInfo="/view/BookInfoView.fxml";
	public static final String borrowReturn="/view/BorrowReturnView.fxml";
	public static final String readerManagement="/view/ReaderManagementView.fxml";
	public static final String workerManagement="/view/WorkerManagementView.fxml";
	public static final String bookManagement="/view/bookManagementView.fxml";
	public static final String bookInfoManagement="/view/bookInfoManagementView.fxml";

	private Stage stage;
	private BorderPane rootLayout;
	
//	private int type;
	private String usr;

	
	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		this.stage.setTitle(title);
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PAWD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		showLoginView();
	}

	/**
	 * 初始化 RootLayout
	 */
	private void initRootLayout(int type,String usr) {
		
		//	加载rootLayout
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LibManageApp.class.getResource(this.rootLayoutResource));
		try {
			this.rootLayout = (BorderPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(this.rootLayout);
		this.stage.setScene(scene);
		this.stage.setResizable(false);
		this.stage.show();
		
		//	根据用户类别加载初始化视图
		RootLayoutController controller=loader.getController();
		controller.setApp(this);
//		this.type=type;
		this.usr=usr;
		if (type==READER) {
			controller.setReaderMenuBar();
			loadReaderInfoView();
		}else if (type==WORKER) {
			controller.setWorkerMenuBar();
			loadWorkerInfoView();
		}else if (type==ADMIN) {
			controller.setAdminMenuBar();
			loadWorkerInfoView();
		}

	}

	/**
	 * 展示登陆界面
	 */
	private void showLoginView() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LibManageApp.class.getResource(loginView));
		AnchorPane loginView;
		try {
			loginView = (AnchorPane) loader.load();
			Scene scene=new Scene(loginView);
			this.stage.setScene(scene);
			this.stage.setResizable(false);
			this.stage.show();
			LoginViewController controller = loader.getController();
			controller.setApp(this,this.connection);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置视图。用于窗口跳转
	 * 
	 * @param resource 资源文件
	 * @return 对应的 loader
	 */
	public FXMLLoader setView(String resource) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LibManageApp.class.getResource(resource));
		AnchorPane view;
		try {

			//	 新建Pane，放置在RootLayout上
			view = (AnchorPane) loader.load();
			this.rootLayout.setCenter(view);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loader;
	}
	
	/**
	 * 展示一个提示窗口
	 * 
	 * @param info 提示的信息
	 * @param type 提示的类型
	 */
	public void showDialog(String info, AlertType type) {
		Alert alert=new Alert(type);
		alert.setTitle("NOTE");
		alert.setHeaderText(null);
		alert.setContentText(info);
		alert.showAndWait();

	}
	
	
	/**
	 * 登录
	 * 
	 * @param type 用户类型
	 * @param usr 用户名
	 */
	public void login(int type, String usr) {
		initRootLayout(type, usr);
	}
	
	/***************************************************
	 * 加载各类窗口
	 ***************************************************/
	
	
	public void loadReaderInfoView() {
		FXMLLoader loader=setView(LibManageApp.readerInfo);
		ReaderInfoController controller=loader.getController();
		System.out.println("test");
		controller.setApp(this, connection);
		controller.showInfo();
	}
	
	public void loadWorkerInfoView() {
		FXMLLoader loader=setView(LibManageApp.workerInfo);
		WorkerInfoController controller=loader.getController();
		controller.setApp(this, connection);
		controller.showInfo();
	}
	
	public void loadBookInfoView() {
		FXMLLoader loader=setView(LibManageApp.bookInfo);
		BookInfoController controller=loader.getController();
		controller.setApp(this, connection);
	}
	
	public void loadBorrowReturnView() {
		FXMLLoader loader=setView(LibManageApp.borrowReturn);
		BorrowReturnController controller=loader.getController();
		controller.setApp(this, connection);
	}
	
	public void loadReaderManagementView() {
		FXMLLoader loader=setView(LibManageApp.readerManagement);
		ReaderManagementController controller=loader.getController();
		controller.setApp(this, connection);
	}
	
	public void loadWorkerManagementView() {
		setView(LibManageApp.workerManagement);
	}
	
	public void loadBookInfoManagementView() {
		setView(LibManageApp.bookManagement);
	}
	
	public void loadBookManagementView() {
		setView(LibManageApp.bookInfoManagement);
	}
	
	public String getUsr() {
		return usr;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
