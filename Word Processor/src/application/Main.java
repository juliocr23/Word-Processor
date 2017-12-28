/**
 * Author: Julio Rosario
 * Purpose: The purpose of this project is to create a simple
 *           word processor that takes user inputed text and 
 *           perform operation such as deleting,pasting,cutting,
 *           copying,printing,saving and opening. 
 *Last updated: 12.26.2017
 */

package application;
	
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;


public class Main extends Application {
	
	private WordBrain brain;
	
	@FXML
	private TextArea document;
	@FXML
    private MenuItem cut;
    @FXML
    private MenuItem copy;
    @FXML
    private MenuItem delete;
    private Stage stage;
    
    
    @FXML
    public void initialize() {
    	brain = new WordBrain();
    	document.setStyle("-fx-background-color: white");
    }
	
	@Override
	public void start(Stage primaryStage)  throws Exception {
		
		stage = primaryStage;
		
		//Load the FXML file
		Parent parent  = FXMLLoader.load(
				getClass().getResource("WordProcessor.fxml"));
		
		//Build the scene graph
		Scene scene = new Scene(parent);
		
		//Display the window using the scene graph
		stage.setTitle("Word Processor");
		stage.setScene(scene);
	    stage.show();
	}
	
	/**
	 * The performOperations method perform operation
	 * on the text inputed by the user
	 * @param event The event triggered when the user
	 * perform an operation such as save,save as, 
	 * print,copy,delete,open,paste,cut and about.
	 */
	@FXML
	private void performOperations(ActionEvent event) {
		
		//Get the file operation pressed by the user
		MenuItem operation = (MenuItem)event.getSource();
		
		//Set the text, and selected text into the brain
		brain.setText(document.getText());	
		brain.setSelected(document.getSelectedText());
		brain.setStart(document.getSelection().getStart());
		brain.setEnd(document.getSelection().getEnd());
		
		//Perform operation
		brain.performOperation(operation.getText(),stage);
		
		//Get and set text
		document.setText(brain.getText());
		
		if(brain.getStart() != -1) {
			document.selectPositionCaret(brain.getStart());
			document.deselect();
			
			brain.setStart(-1);
			brain.setEnd(-1);
		}
	}
	
	/**
	 * The enable method enable the buttons
	 * cut,copy and delete when the user select 
	 * a section of text.
	 */
	@FXML 
	private void enable() {
		if(!document.getSelectedText().equals("")) {
				cut.setDisable(false);
				copy.setDisable(false);
				delete.setDisable(false);
		}
	}
	
	/**
	 * The disable method disable the buttons
	 * cut,copy, and delete when the user
	 * click in the text are.
	 */
	@FXML
	private void disable() {
		cut.setDisable(true);
		copy.setDisable(true);
		delete.setDisable(true);
		brain.setSelected("");
	}
	
	/**
	 * The undo method
	 * undo text 
	 */
	@FXML
	private void undo() {
		document.undo();
	}
	
	/**
	 * The redo method
	 * redo text
	 */
	@FXML
	private void redo() {
		document.redo();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}	
}
