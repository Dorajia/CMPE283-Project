package windows.views;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainController extends ViewController{

    @FXML
    private Menu file_menu;

    @FXML
    private MenuItem vCenterAccount;

    @FXML
    private MenuItem Aws_account;

    @FXML
    private MenuItem Migrate_menu;

    @FXML
    private Button migrate_btn;

    @FXML
    private Button Cancel_btn;

    @FXML
    private TreeView<String> Instance_tree_view;

    @FXML
    private AnchorPane Summary_anchor;

    @FXML
    private AnchorPane Task_anchor;

    @FXML
    private AnchorPane Event_anchor;

    @FXML
    void startMigration(MouseEvent event) {

    }
    @FXML
    private void initialize() {
    	TreeItem<String> vmRoot = new TreeItem<String> ("vSphere VMs");
    	TreeItem<String> EC2 = new TreeItem<String> ("EC2 Instance");
    	vmRoot.setExpanded(true);
    	EC2.setExpanded(true);
    	
    }

}
