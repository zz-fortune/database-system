package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Student;
import view.SearchWindowController;

public class MainApp extends Application {

	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost:3306/STU_MANAGEMENT";
	private final String USER = "root";
	private final String PAWD = "zz1160300620";

	private final String title = "学生信息查询";
//	private final String rootLayoytResource="view/RootLayout.fxml";
	private final String searchWindowResource = "/view/SearchWindow.fxml";
	private Stage stage;
//	private BorderPane rootLayout;
	private AnchorPane searchWindow;

	private Connection connection;

	private ObservableList<Student> students = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		this.stage.setTitle(this.title);
		connectDatabase();
		showSearchWindow();
	}

	public void showSearchWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(this.searchWindowResource));
			this.searchWindow = (AnchorPane) loader.load();
			Scene scene = new Scene(this.searchWindow);
			this.stage.setScene(scene);
			this.stage.setResizable(false);
			this.stage.show();

			// close database before closing the window
			this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					System.out.println("关闭数据库...");
					stage.close();
				}
			});

			SearchWindowController controller = loader.getController();
			controller.setMainApp(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connectDatabase() {
		try {
			Class.forName(JDBC_DRIVER);

			System.out.println("连接数据库...");
			this.connection = DriverManager.getConnection(DB_URL, USER, PAWD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ObservableList<Student> getStudents() {
		return this.students;
	}

	public ObservableList<Student> inquire(String cmd) {

		try {
			Statement statement = this.connection.createStatement();
			ResultSet results = statement.executeQuery(cmd);

			this.students.clear();
			while (results.next()) {
				Student student = new Student(results.getString("Sid"), results.getString("Sname"),
						results.getInt("Sage"), results.getString("Ssex"), results.getInt("Sdept"),
						results.getInt("Sclass"), results.getString("Saddr"));
				this.students.add(student);
//				System.out.println(student.getStudentID());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.students;

	}

	public static void main(String[] args) {
		launch(args);
	}
}
