package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import java.awt.event.InputEvent;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import controller.DestinationController;
import controller.ExecutionController;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Destination;
import storage.Storage;

public class MainApp extends Application {
	
	private static int maxESResults = 100;
	
    public static void main(String[] args) throws Exception {
    	if (args.length > 0) {
    		try {
    			maxESResults = Integer.parseInt(args[0]);	
    		} catch (NumberFormatException e) {
    			maxESResults = 100;
    		}
    	}
    	
    	Storage.load();
        Application.launch(args);
    }
    
    private Stage stage;
    private AddDestinationStage addDestinationStage;
    private EditDestinationStage editDestinationStage;

    @Override
    public void start(final Stage stage) throws Exception {    	
    	this.stage = stage;
    	
    	Platform.setImplicitExit(false);
    	
    	GridPane root = new GridPane();
    	root.setPadding(new Insets(10d));
    	root.setHgap(10d);
    	root.setVgap(10d);
        root.setPrefSize(400, 400);
        buildPane(root);
        Scene scene = new Scene(root);

        stage.setTitle("Launchdex");
//        stage.getIcons().add(new Image("https://d1nhio0ox7pgb.cloudfront.net/_img/o_collection_png/green_dark_grey/256x256/plain/drop_down_list.png"));
        stage.getIcons().add(new Image("https://raw.githubusercontent.com/Fillipuster/Launchdex/master/icon.png"));
        stage.setScene(scene);
        stage.show();
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				backView();
				event.consume();
			}
		});
        
        // Hotkey Related
        JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			public void onHotKey(int identifier) {
				if (stage.isShowing()) {
					backView();
				} else {
					frontView();	
				}
			}
		});
         
        JIntellitype.getInstance().registerSwingHotKey(1, InputEvent.ALT_MASK, java.awt.event.KeyEvent.VK_SPACE);
    }
    
    private TextField txfQuery;
    private ListView<Destination> lvwDestinations;

    private void buildPane(GridPane pane) {
    	buildTextField(pane);
    	buildListView(pane);
    }
    
    private void buildTextField(GridPane pane) {
    	txfQuery = new TextField("Search Query");
    	txfQuery.setMinWidth(400);
    	txfQuery.textProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue.isEmpty()) {
    			reloadListView();
    			lvwDestinations.setStyle("-fx-font-size: 20;");
    			return;
    		}
    		
    		lvwDestinations.getItems().removeAll(lvwDestinations.getItems());
    		if (txfQuery.getText().charAt(0) == '@') {
    			lvwDestinations.getItems().addAll(ExecutionController.everythingSearch(txfQuery.getText().substring(1, txfQuery.getText().length()), maxESResults));
    			lvwDestinations.setStyle("");
    		} else {
        		lvwDestinations.getItems().addAll(DestinationController.orderMatch(newValue));
        		lvwDestinations.setStyle("-fx-font-size: 20;");
    		}
    		selectFirst();
    	});
    	txfQuery.setOnAction(event -> txfAction());
    	txfQuery.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.DOWN) {
					lvwDestinations.requestFocus();
					selectFirst();
				}
			}
		});
    	pane.add(txfQuery, 0, 0);
    }
    
    private void buildListView(GridPane pane) {
    	lvwDestinations = new ListView<>();
    	lvwDestinations.getItems().addAll(Storage.getDestinations());
    	selectFirst();
    	lvwDestinations.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER) && !lvwDestinations.getSelectionModel().isEmpty()) {
					ExecutionController.execDestination(lvwDestinations.getSelectionModel().getSelectedItem());
					backView();
				}
				
				if (event.getCode().equals(KeyCode.UP) && lvwDestinations.getSelectionModel().getSelectedIndex() == 0) {
					txfQuery.requestFocus(); 
				}
			}
		});
    	lvwDestinations.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				ContextMenu cm = new ContextMenu();
				
				MenuItem pathItem = new MenuItem("Open Location");
				pathItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						ExecutionController.openExplorerPath(lvwDestinations.getSelectionModel().getSelectedItem().getPath());
					}
				});
				
				MenuItem addItem = new MenuItem("Save as Shortcut");
				addItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						addDestinationStage = new AddDestinationStage("Add Destination", stage, lvwDestinations.getSelectionModel().getSelectedItem().getPath());
						addDestinationStage.showAndWait();
					}
				});
				
				MenuItem editItem = new MenuItem("Edit");
				editItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						editDestinationStage = new EditDestinationStage("Edit Destination", stage, lvwDestinations.getSelectionModel().getSelectedItem());
						editDestinationStage.showAndWait();
						reloadListView();
					}
				});
				
				MenuItem removeItem = new MenuItem("Remove");
				removeItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Storage.removeDestination(lvwDestinations.getSelectionModel().getSelectedItem());
						reloadListView();
					}
				});
				
				if (!txfQuery.getText().isEmpty() && txfQuery.getText().charAt(0) == '@') {
					cm.getItems().addAll(pathItem, addItem);
				} else {
					cm.getItems().addAll(pathItem, editItem, removeItem);	
				}
				cm.show(stage);
			}
		});
    	pane.add(lvwDestinations, 0, 1);
    	lvwDestinations.setStyle("-fx-font-size: 20;");
    }
    
    private void frontView() {
    	Platform.runLater(new Runnable() {
    		@Override
			public void run() {
    			stage.show();
//				stage.setIconified(false);
//				stage.toFront();
    			
				txfQuery.requestFocus();
				txfQuery.selectAll();
			}
		});
    }
    
    private void backView() {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stage.hide();
//				stage.toBack();
//				stage.setIconified(true);
			}
		});
    }
    
    private void txfAction() {
    	ExecutionController.execDestination(lvwDestinations.getItems().get(0));
    	backView();    	
    }
    
    private void reloadListView() {
    	lvwDestinations.getItems().removeAll(lvwDestinations.getItems());
    	lvwDestinations.getItems().addAll(Storage.getDestinations());
    }
    
    private void selectFirst() {
    	if (lvwDestinations.getItems().size() > 0) {
    		lvwDestinations.getSelectionModel().select(0);
    	}
    }

}
