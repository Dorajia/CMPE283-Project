package windows.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import windows.models.*;
import windows.Main1;
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
    void onclick(MouseEvent event) {
//    	VSphere_credentials vc= new VSphere_credentials(ip_addr.getText(),user.getText(),password.getText());
//    	ServiceInstance SI = vc.connect();
//    	if(SI !=null){
    		mainApp.loadNewStage("./views/Aws_login.fxml");
//    	}
    }

}
