package view;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import controller.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Student;

/**
 * 这是查询学生信息的窗口的控制器，负责处理此页面上的事件。
 * 
 * @author zz
 *
 */
public class SearchWindowController {

	/*
	 * 获取该页面上（实际上是一个场景Scene）上的所有需要操纵的控件。
	 */
	
	//	获取显示结果的TableView以及每一列
	@FXML
	private TableView<Student> studentInfo;

	@FXML
	private TableColumn<Student, String> studentIDInfo;

	@FXML
	private TableColumn<Student, String> nameInfo;

	@FXML
	private TableColumn<Student, Integer> ageInfo;

	@FXML
	private TableColumn<Student, String> sexInfo;

	@FXML
	private TableColumn<Student, Integer> deptInfo;

	@FXML
	private TableColumn<Student, Integer> classNumInfo;

	@FXML
	private TableColumn<Student, String> addressInfo;

	//	获取复选框，这些控件表明是否应用相应的输入
	@FXML
	private CheckBox selectStudentID;

	@FXML
	private CheckBox selectName;

	@FXML
	private CheckBox selectAge;

	@FXML
	private CheckBox selectSex;

	@FXML
	private CheckBox selectDept;

	@FXML
	private CheckBox selectClassNum;

	@FXML
	private CheckBox selectAdress;

	//	获取查询条件的输入区域
	@FXML
	private TextField inputStudentID;

	@FXML
	private TextField inputName;

	@FXML
	private TextField inputAgeStart;

	@FXML
	private TextField inputAgeEnd;

	@FXML
	private TextField inputSex;

	@FXML
	private TextField inputDept;

	@FXML
	private TextField inputClassNum;

	@FXML
	private TextField inputAddress;

	//	获取展示执行的查询语句的控件
	@FXML
	private TextArea cmdShowArea;

	//	获取按钮，用以触发查询事件
	@FXML
	private Button searceButton;

	//	程序的主类
	private MainApp mainApp;

	/**
	 * 构造函数
	 */
	public SearchWindowController() {
	}

	/**
	 * 获取主类的实例，用以调用主类提供的方法或者变量
	 * 
	 * @param mainApp 主类的实例
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * 初始化。在加载相应的fxml文件时被调用，这里设置展示结果的TableView的每一列取值来源
	 */
	@FXML
	private void initialize() {
		this.studentIDInfo.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty());
		this.nameInfo.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		this.ageInfo.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
		this.sexInfo.setCellValueFactory(cellData -> cellData.getValue().sexProperty());
		this.classNumInfo.setCellValueFactory(cellData -> cellData.getValue().classNumProperty().asObject());
		this.deptInfo.setCellValueFactory(cellData -> cellData.getValue().departmentProperty().asObject());
		this.addressInfo.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

	}

	/**
	 * 搜索按钮的回调函数。点击按钮时触发，读取输入的值并构造查询语句、查询数据库并展示数据。
	 */
	@FXML
	private void search() {
		List<String> cnd = new ArrayList<String>(); //	存储输入的查询条件
		String input;  //	读取输入框的值
		
		//	判断学号这一条件是否有效，并获取输入构造条件
		if (this.selectStudentID.isSelected()) {
			input = this.inputStudentID.getText();
			if (input.equals("")) {
				System.err.println("can't find Sid!");
			} else if (input.contains("%")) {
				cnd.add("(Sid like \"" + input + "\")");
			} else {
				cnd.add("(Sid=" + input + ")");
			}
		}

		//	判断姓名这一条件是否有效，并获取输入构造条件
		if (this.selectName.isSelected()) {
			input = this.inputName.getText();
			if (input.equals("")) {
				System.err.println("can't find Sname!");
			} else if (input.contains("%")) {
				cnd.add("(Sname like \"" + input + "\")");
			} else {
				cnd.add("(Sname=\"" + input + "\")");
			}
		}

		//	判断性别这一条件是否有效，并获取输入构造条件
		if (this.selectSex.isSelected()) {
			input = this.inputSex.getText();
			if (input.equals("")) {
				System.err.println("can't find Ssex!");
			} else if (input.contains("%")) {
				cnd.add("(Ssex like " + input + ")");
			} else {
				cnd.add("(Ssex=\"" + input + "\")");
			}
		}

		//	判断地址这一条件是否有效，并获取输入构造条件
		if (this.selectAdress.isSelected()) {
			input = this.inputAddress.getText();
			if (input.equals("")) {
				System.err.println("can't find address!");
			} else if (input.contains("%")) {
				cnd.add("(Saddr like \"" + input + "\")");
			} else {
				cnd.add("(Saddr=\"" + input + "\")");
			}
		}

		//	判断年龄这一条件是否有效，并获取输入构造条件
		if (this.selectAge.isSelected()) {
			input = this.inputAgeStart.getText();
			if (isInteger(input) && isInteger(this.inputAgeEnd.getText())) {
				cnd.add("(Sage>=" + input + " and Sage<=" + this.inputAgeEnd.getText() + ")");
			} else {
				System.err.println("can't find Sage");
			}
		}
		
		//	判断年级这一条件是否有效，并获取输入构造条件
		if (this.selectDept.isSelected()) {
			input = this.inputDept.getText();
			if (isInteger(input)) {
				cnd.add("(Sdept=" + input + ")");
			} else {
				System.err.println("can't find Sdept");
			}
		}

		//	判断班级这一条件是否有效，并获取输入构造条件
		if (this.selectClassNum.isSelected()) {
			input = this.inputClassNum.getText();
			if (isInteger(input)) {
				cnd.add("(Sclass=" + input + ")");
			} else {
				System.err.println("can't find Sclass");
			}
		}

		//	构造SQL查询语句
		String cmd;
		if (cnd.size() == 0) {  //	如果没有一个有效的条件，则查询全部
			cmd = "SELECT * FROM student";
		} else {  
			cmd = "SELECT * FROM student WHERE ";
			for (int i = 0; i < cnd.size(); i++) {
				cmd += cnd.get(i) + " and ";
			}
			cmd = cmd.substring(0, cmd.length() - 5);
		}
		
		this.cmdShowArea.setText(cmd);
		showStudentsInfo(mainApp.inquire(cmd));
	}

	/**
	 * 展示查询的结果
	 * 
	 * @param students 查询的结果
	 */
	private void showStudentsInfo(ObservableList<Student> students) {
		this.studentInfo.setItems(students);
	}

	/**
	 * 判断一个字符串是否是一个整数
	 * 
	 * @param str 带判断的字符串
	 * @return true 当且仅当字符串是一个整数组成，否则 false
	 */
	private boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		return pattern.matcher(str).matches();
	}

}
