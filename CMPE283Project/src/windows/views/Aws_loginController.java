package windows.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import windows.Main1;
import windows.models.Aws_credentials;

public class Aws_loginController extends ViewController {
    private Aws_credentials newCred =new Aws_credentials();;
    @FXML
    private TextField aws_id;

    @FXML
    private TextField aws_secret_key;

    @FXML
    private Button login_btn;

    @FXML
    public void Login_clicked(MouseEvent event) {
    	System.out.println(aws_id.getText());
    	newCred.set_id(aws_id.getText());
    	newCred.set_secret(aws_secret_key.getText());
    	System.out.println(newCred.toString());
    	mainApp.loadNewStage("./views/SelectRegion.fxml");
    }

}
