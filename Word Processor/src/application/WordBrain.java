/**
 * Author: Julio Rosario
 * Purpose:The purpose of this class is to act as the brain\model of
 *         the word processor.
 *Last updated: 12.16.2017
 */
package application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WordBrain {
	
	private String text;
	private File file;
	private String selectedText;
	private int start;
	private int end;
	
	/**
	 * No-arg Constructor
	 */
	WordBrain(){
		text = "";
		selectedText = "";
		start = -1;
		end = -1;
	}
	
	/**
	 * The performOperation method perform operations
	 * such as save, save as, opening a file, printing a file,
	 * pasting, copying, cutting, and deleting as well as showing
	 * a about windows.
	 * @param action The action to be perform 
	 * @param stage The frame/windows to be the action show on. 
	 */
	public void performOperation(String action,Stage stage) {
		if(action.equals("Save"))
		     saveFile(text,stage);
		else if(action.equals("Save As"))
			saveFileAs(text,stage);
		else if(action.equals("Open"))
			openFile(stage);
		else if(action.equals("Print"))
			printFile(text,stage);
		else if(action.equals("Paste"))
			paste();
		else if(action.equals("Copy"))
			copy();
		else if(action.equals("Cut"))
			cut();
		else  if(action.equals("Delete"))
			delete();
		else 
			about();
	}
	
	/**
	 * The setText method receives 
	 * @param str A string and set it
	 * equal to the text field.
	 */
	public void setText(String str) {
		text = str;
	}
	
	/**
	 * The setSelected method receives 
	 * @param str a string that has been selected
	 * by the user and set it equal to the field
	 * selectedText
	 */
	public void setSelected(String str) {
		selectedText = str;
	}
	
	/**
	 * The saveFile method save the text into the current 
	 * file, no new file is create. 
	 * @param text The text to be save
	 * @param stage The windows\frame of the application
	 */
	private void saveFile(String text,Stage stage) {
		if(file != null) {
			try {
				PrintWriter out = new PrintWriter(file);
				out.print(text);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}else
			saveFileAs(text,stage);
	}
	

	/***
	 * The saveFileAs method is in charge of saving the current file
	 * into a new location chosen by the user.
	 * @param text The text to be save
	 * @param stage  The frame/windows of the application
	 */
	
	private void saveFileAs(String text,Stage stage) {
		//Create file chooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		
		//Add extensions
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Text Documents (*.txt)", "*.txt"));
		
		//Get file selected by the user
	    file = fileChooser.showSaveDialog(stage);
		
		//check that the user selected a file
		if(file != null) {
			try {
				PrintWriter out = new PrintWriter(file);
				out.print(text);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/***
	 * The openFile method prompt the user to look
	 * for a file,and it will read it and store it
	 * on the text field, this method receives 
	 * @param stage The windows/frame of the application
	 */
	private void openFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
	    file = fileChooser.showOpenDialog(stage);
		
		if(file != null) {
			try {
				Scanner  in = new Scanner(file);
				while(in.hasNext()) {
					text = in.nextLine();
				}
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * The printFile method print the user typing, and receives 
	 * @param text The text to be printed
	 * @param stage The windows\frame where the print dialog will
	 * show on. 
	 */
	private void printFile(String text,Stage stage) {
		
		PrinterJob job = PrinterJob.createPrinterJob();
		if(job != null) {
			
			//Show the print setup dialog
			boolean proceed = job.showPrintDialog(stage);
			if(proceed) {
				//Print the node
				Label page = new Label(text);
				page.setStyle("-fx-background-color: WHITE");
				
				//Print page
				boolean printed = job.printPage(page);
		
				//Show progress
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Printing");
				alert.setHeaderText("Printing is in progress");
				alert.setContentText(job.jobStatusProperty().asString().getValue());
				alert.show();
				
				if(printed) {
					job.endJob();
				}else {
					job.cancelJob();
					Alert warning = new Alert(AlertType.WARNING);
					warning.setHeaderText("Sorry! CANNOT PRINT");
					warning.show();
				}
			}
		}else
			return;
	}
	
	/**
	 * The getText method
	 * @return The text typed by user
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * The cut method copy and delete 
	 * selected text
	 */
	private void cut() {
		copy();
		delete();
	}
	
	/**
	 * The copy method copy the selected
	 * text into the clipboard
	 */
	private void copy() {
		Clipboard  clipboard =	Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(selectedText);
		clipboard.setContents(stringSelection, null);
	}
	
	/**
	 * The paste  method get text
	 * from the clipboard and append it to the text
	 */
	private void paste() {
		Clipboard  clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			text += clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The delete method delete selected text
	 * from the text.
	 */
	private void delete() {
		
		String tmp = "";
		//Take care of first word
		if(start == 0) {
			text = text.substring(end);
		}//Take care of last word
		else if(end == text.length()) {
			text = text.substring(0,start);
		}//Take care of word in between 
		else {
			tmp = text;
			text = tmp.substring(0,start);
			text += tmp.substring(end,tmp.length());
		}
	}
	
	/**
	 * The setStart method receives 
	 * @param index The beginning index
	 * of the selected text and set it
	 * equal to the field start
	 */
	public void setStart(int index) {
		start = index;
	}
	
	/**
	 * The setEnd method receives 
	 * @param index The end index of 
	 * the selected text and set it
	 * equal to the field end
	 */
	public void setEnd(int index) {
		end = index;
	}
	
	private void about() {
		Alert about = new Alert(AlertType.INFORMATION);
		about.setTitle("About this program");
		about.setHeaderText("About:");
		about.setContentText("Word processor\n"+
				             "Developer: Julio Rosario\n"+
		                     "Release Date: 12.26.2017\n"+
				             "Contact: rosariojulio40@gmail.com");
		about.show();
	}

	/**
	 * The getStart method 
	 * @return The starting index of the selected
	 * text
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * The getEnd method
	 * @return The ending index of the selected
	 * text
	 */
	public int getEnd() {
		return end;
	}
}
