package windows;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.*;
import windows.views.*;
import windows.models.*;

public class Main1 extends Application {
	private Stage primaryStage;
	private String AwsLoginR = "./views/Aws_login.fxml";
	private String vSphereLoginR ="./views/VSphere_login.fxml";
	private String SelectRegion="";
	private String MainView ="";
	private Scene AwsLoginS;
	private Scene vSphereLoginS;
	private Scene SelectRegionS;
	private Scene MainViewS;
	private Scene MirgateToAWSS;
	private Scene MirgateTovSphere;
	private Aws_loginController Acontroller;

	@Override
	public void start(Stage primaryStage) {
		 this.primaryStage = primaryStage;
	     this.primaryStage.setTitle("Login AWS");
	     loadNewStage(vSphereLoginR);
	     primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	              System.out.println("Stage is closing");
	          }
	      }); 
	}

	public void loadNewStage(String sceneR){

		    try {
		        // Load person overview.
		        FXMLLoader loader = new FXMLLoader();
		        System.out.println("1");
		        loader.setLocation(Main1.class.getResource(sceneR));
		        System.out.println(sceneR);
		        AnchorPane pane = (AnchorPane) loader.load();
		        System.out.println("2");
		        ViewController controller =loader.getController();
		        controller.setMainApp(this);
		        System.out.println("3");
	            //controller.setMainApp(this);
		        System.out.println("4");
	            // Show the scene containing the root layout.
	            Scene scene = new Scene(pane);
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		
	}
	public Stage getStage(){
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void showEC2Config(String selectItem) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main1.class.getResource("./views/popToEC2.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Edit AWS Import configration");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        PopEC2Controller controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setSelectItem(selectItem);

	    } catch (IOException e) {
	        e.printStackTrace();

	    }
	}

	
}
