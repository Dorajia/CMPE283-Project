package windows.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import windows.models.*;
import windows.Main1;

import org.controlsfx.dialog.Dialogs;

import com.vmware.vim25.mo.ServiceInstance;

public class VSphere_loginController extends ViewController {
	
    @FXML
    private TextField ip_addr;

    @FXML
    private TextField user;

    @FXML
    private Button login_btn;

    @FXML
    private TextField password;
    @FXML
    private Label Warning;
    @FXML
    private void initialize(){
    	Warning.setVisible(false);
    }

    @FXML
    void onclick(MouseEvent event) {
    	VSphere_credentials vc= new VSphere_credentials(ip_addr.getText(),user.getText(),password.getText());
    	ServiceInstance SI = vc.connect();
    	//vc.getVMs();
    	if(SI !=null){
    		mainApp.loadNewStage("./views/Aws_login.fxml");
    		
    	}else{
    		Warning.setVisible(true);
    	}
    }

}
