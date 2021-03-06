package windows.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import windows.Main1;
import windows.models.Aws_credentials;


public class SelectRegionController extends ViewController{

	
    @FXML
    private ChoiceBox<String> RegionChoiceDD;

    @FXML
    private Button Select_btn;
    
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
    	System.out.println("init");
    	Aws_credentials.DefaultCred.fetchRegions();
    	RegionChoiceDD.setItems(Aws_credentials.DefaultCred.getRegions());
    	RegionChoiceDD.setValue(RegionChoiceDD.getItems().get(0));

    }
    @FXML
    void selectRegion(MouseEvent event) {
    	Aws_credentials.DefaultCred.set_region(RegionChoiceDD.getValue());
    	System.out.println(RegionChoiceDD.getValue());
    	mainApp.loadNewStage("./views/Main.fxml");
    }

}

