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

public class SearchWindowController {

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

	@FXML
	private TextArea cmdShowArea;

	@FXML
	private Button searceButton;

	private MainApp mainApp;

	public SearchWindowController() {
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

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

	@FXML
	private void search() {
		List<String> cnd = new ArrayList<String>();
		String input;
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

		if (this.selectAge.isSelected()) {
			input = this.inputAgeStart.getText();
			if (isInteger(input) && isInteger(this.inputAgeEnd.getText())) {
				cnd.add("(Sage>=" + input + " and Sage<=" + this.inputAgeEnd.getText() + ")");
			} else {
				System.err.println("can't find Sage");
			}
		}

		if (this.selectDept.isSelected()) {
			input = this.inputDept.getText();
			if (isInteger(input)) {
				cnd.add("(Sdept=" + input + ")");
			} else {
				System.err.println("can't find Sdept");
			}
		}

		if (this.selectClassNum.isSelected()) {
			input = this.inputClassNum.getText();
			if (isInteger(input)) {
				cnd.add("(Sclass=" + input + ")");
			} else {
				System.err.println("can't find Sclass");
			}
		}

		String cmd;
		if (cnd.size() == 0) {
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

	private void showStudentsInfo(ObservableList<Student> students) {
		this.studentInfo.setItems(students);
	}

	private boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		return pattern.matcher("-").matches();
	}

}
