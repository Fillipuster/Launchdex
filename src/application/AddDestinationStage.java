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

public class AddDestinationStage extends Stage {

	private String path;
	
    public AddDestinationStage(String title, Stage owner, String path) {
    	this.path = path;
    	
        initOwner(owner);
        initStyle(StageStyle.DECORATED);
        initModality(Modality.APPLICATION_MODAL);
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
    private Button btnAdd, btnCancel;
    private Label lblErr;

    private void initContent(GridPane pane) {
    	txfName = new TextField("Name");
    	txfName.setOnAction(event -> btnAddAction());
    	pane.add(txfName, 0, 0, 2, 1);
    	
    	txfPath = new TextField(path);
    	txfPath.setOnAction(event -> btnAddAction());
    	pane.add(txfPath, 0, 1, 2, 1);
    	
    	btnAdd = new Button("Add");
    	btnAdd.setOnAction(event -> btnAddAction());
    	pane.add(btnAdd, 0, 2);
    	
    	btnCancel = new Button("Cancel");
    	btnCancel.setOnAction(event -> btnCancelAction());
    	pane.add(btnCancel, 1, 2);
    	
    	lblErr = new Label("");
    	pane.add(lblErr, 0, 3);
    	
    	txfName.requestFocus();
    }
    
    private void btnAddAction() {
    	File f = new File(txfPath.getText());
    	if (f.exists()) {
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
