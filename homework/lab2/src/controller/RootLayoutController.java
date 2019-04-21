package controller;


import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class RootLayoutController {
	@FXML
	private MenuItem workerinfo;
	
	@FXML
	private MenuItem readerinfo;
	
	@FXML
	private MenuItem bookinfo;
	
	@FXML
	private MenuItem borrowreturn;
	
	@FXML
	private MenuItem readermanagement;
	
	@FXML
	private MenuItem workermanagement;
	
	@FXML
	private MenuItem bookinfomanagement;
	
	@FXML
	private MenuItem bookmanagement;

	private LibManageApp app;
	
	public void setApp(LibManageApp app) {
		this.app=app;
	}
	
	public void setReaderMenuBar() {
		workerinfo.setVisible(false);
		borrowreturn.setVisible(false);
		readermanagement.setVisible(false);
		workermanagement.setVisible(false);
		bookinfomanagement.setVisible(false);
		bookmanagement.setVisible(false);
	}
	
	public void setWorkerMenuBar() {
		readerinfo.setVisible(false);
		readermanagement.setVisible(false);
		workermanagement.setVisible(false);
		bookinfomanagement.setVisible(false);
		bookmanagement.setVisible(false);
	}
	
	public void setAdminMenuBar() {
		readerinfo.setVisible(false);
	}
	
	@FXML
	private void loadReaderInfoView() {
		app.loadReaderInfoView();
	}
	
	@FXML
	private void loadWorkerInfoView() {
		app.loadWorkerInfoView();
	}
	
	@FXML
	private void loadBookInfoView() {
		app.loadBookInfoView();
	}
	
	@FXML
	private void loadBorrowReturnView() {
		app.loadBorrowReturnView();
	}
	
	@FXML
	private void loadReaderManagementView() {
		app.loadReaderManagementView();
	}
	
	@FXML
	private void loadWorkerManagementView() {
		app.loadWorkerManagementView();
	}
	
	@FXML
	private void loadBookInfoManagementView() {
		app.loadBookManagementView();
	}
	
	@FXML
	private void loadBookManagementView() {
		app.loadBookInfoManagementView();
	}
}
