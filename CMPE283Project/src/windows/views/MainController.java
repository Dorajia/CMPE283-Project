package windows.views;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.EXSI.*;
import com.amazonaws.regions.Regions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import windows.models.Aws_credentials;
import windows.models.VM;
import windows.models.VSphere_credentials;

import javafx.scene.control.TableView;

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
    private Pane awsSummary;
    @FXML
    private TableView<String> Task_Table;
    @FXML
    private Label InstanceNamelabel;

    @FXML
    private Label Architecture_label;

    @FXML
    private Label InstanceType_label;

    @FXML
    private Label ImageId_label;

    @FXML
    private Label PairKey_label;

    @FXML
    private Label Tags_label;
    

    @FXML
    private Label Status_label;

    @FXML
    private Label IP_label;

    @FXML
    private Pane vsphereSummary;

    @FXML
    private Label Tool_label;

    @FXML
    private Label vm_name_label;

    @FXML
    private Label guestOS_label;

    @FXML
    private Label VM_status_label;

    @FXML
    private Label power_label;

    @FXML
    private Label Memory_label;

    @FXML
    private Label CPU_label;

    @FXML
    private MenuItem close_menu_btn;
    
    @FXML
    private AnchorPane Task_anchor;

    @FXML
    private AnchorPane Event_anchor;
    private List<List<String>> EC2s;
    private List<VM> vms;
    private TreeItem<String> vmRoot;
    private TreeItem<String> EC2;

    
    @FXML
    private void startMigration(){
    	if(Instance_tree_view.getSelectionModel().getSelectedItem().getParent().equals(vmRoot)){
    		mainApp.showEC2Config(Instance_tree_view.getSelectionModel().getSelectedItem().getValue());
    	}else if(Instance_tree_view.getSelectionModel().getSelectedItem().getParent().equals(EC2)){
    		String s3_bucket = "testcmpe283";
    		String instanceid = Instance_tree_view.getSelectionModel().getSelectedItem().getValue();
    		String exportname;
			try {
				exportname = EC2_Export.ec2Export(Aws_credentials.DefaultCred.getEC2(),Aws_credentials.DefaultCred.getS3(),Aws_credentials.DefaultCred.get_region(), s3_bucket, instanceid);
		         String checkresult;
		            //check import history

		            //check export status with export id
		            checkresult=EC2_Export.checkexportstatus(exportname, Aws_credentials.DefaultCred.getProvider(),Aws_credentials.DefaultCred.getEC2());
		            System.out.println(checkresult);
		            //check export history
		            checkresult=EC2_Export.checkexporthistory(Aws_credentials.DefaultCred.getProvider(),Aws_credentials.DefaultCred.getEC2());
		            System.out.println(checkresult);
		            
		            //download exported ova       
		            String filepath = "./"+exportname;
		            S3.downloadfromS3(Aws_credentials.DefaultCred.getS3(), filepath, s3_bucket,exportname);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            

   
    	}
    }
    
    @FXML
    private void initialize() {
    	vsphereSummary.setVisible(false);
		awsSummary.setVisible(false);
    	vmRoot = new TreeItem<String> ("VMs on "+ VSphere_credentials.defaultCred.get_ip());
    	EC2 = new TreeItem<String> ("Instances on "+Aws_credentials.DefaultCred.get_region().getName());
    	vmRoot.setExpanded(true);
    	EC2.setExpanded(true);

    	vms = VSphere_credentials.defaultCred.getVMs();
    	vms.forEach((vm)->{
    		vmRoot.getChildren().add(new TreeItem<String> (vm.getName()));
    	});
    	
    	EC2s = AWSEc2Info.GetInstance(Aws_credentials.DefaultCred.get_region(), Aws_credentials.DefaultCred.getEC2());
    	EC2s.forEach((instance)->{
    		System.out.println(instance.get(0));
    		EC2.getChildren().add(new TreeItem<String>(instance.get(0)));
    	});
    	
    	TreeItem<String> root = new TreeItem<String>();
    	root.getChildren().add(vmRoot);
    	root.getChildren().add(EC2);
    	
    	Instance_tree_view.setRoot(root);
    	Instance_tree_view.setShowRoot(false);
    	Instance_tree_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue,newValue)->loadDetail(newValue));
    }
    private void loadDetail(TreeItem<String> value){
    	if(value.getParent().equals(vmRoot)){
    		vsphereSummary.setVisible(true);
    		awsSummary.setVisible(false);
    		vms.forEach((vm)->{
    			if(vm.getName()==value.getValue()){
    				vm_name_label.setText(vm.getName());
    				guestOS_label.setText(vm.getGuestOS());
    				Tool_label.setText(vm.getToolStatus());
    				power_label.setText(vm.getPowerState());
    				Memory_label.setText(new Integer(vm.getMemory()).toString());
    				CPU_label.setText(new Integer(vm.getCPU()).toString());
    				VM_status_label.setText(vm.getStatus());
    			}
    		});
    	}else{
    		vsphereSummary.setVisible(false);
    		awsSummary.setVisible(true);
    		EC2s.forEach((instance)->{
    			if(instance.get(0)==value.getValue()){
    				InstanceNamelabel.setText(instance.get(0));
    				Architecture_label.setText(instance.get(1));
    				InstanceType_label.setText(instance.get(3));
    				ImageId_label.setText(instance.get(2));
    				PairKey_label.setText(instance.get(4));
    				Status_label.setText(instance.get(7));
    				IP_label.setText(instance.get(5));
    				Tags_label.setText(instance.get(8));
    			}
    		});
    	}
    	
    }
    
    @FXML
    void cencelMigrate(MouseEvent event) {
    	
    }

    @FXML
    void closeWindow(ActionEvent event) {
    	mainApp.getStage().close();
    }
    

}
