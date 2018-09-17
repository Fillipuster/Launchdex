package application;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Destination;
import storage.Storage;

public class EditDestinationStage extends Stage {

	private Destination destination;
	
    public EditDestinationStage(String title, Stage owner, Destination destination) {
    	this.destination = destination;
    	
        initOwner(owner);
        initStyle(StageStyle.DECORATED);
        initModality(Modality.APPLICATION_MODAL);
        setMinHeight(100);
        setMinWidth(200);
        setResizable(false);

        setTitle(title);
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(false);
        pane.setPadding(new Insets(10));
        pane.setHgap(5);
        pane.setVgap(5);
        initContent(pane);

        Scene scene = new Scene(pane);
        setScene(scene);
    }

    private TextField txfName, txfPath;
    private Button btnEdit, btnCancel;
    private Label lblErr;

    private void initContent(GridPane pane) {
    	txfName = new TextField(destination.getName());
    	pane.add(txfName, 0, 0, 2, 1);
    	
    	txfPath = new TextField(destination.getPath());
    	pane.add(txfPath, 0, 1, 2, 1);
    	
    	btnEdit = new Button("Edit");
    	btnEdit.setOnAction(event -> btnEditAction());
    	pane.add(btnEdit, 0, 2);
    	
    	btnCancel = new Button("Cancel");
    	btnCancel.setOnAction(event -> btnCancelAction());
    	pane.add(btnCancel, 1, 2);
    	
    	lblErr = new Label("");
    	pane.add(lblErr, 0, 3);
    }
    
    private void btnEditAction() {
    	File f = new File(txfPath.getText());
    	if (f.exists()) {
    		Storage.removeDestination(destination);
    		Storage.addDestination(new Destination(txfName.getText(), txfPath.getText()));
    		this.close();
    	} else {
    		lblErr.setText("Invalid path.");
    	}
    }
    
    private void btnCancelAction() {
    	this.close();
    }
	
}
