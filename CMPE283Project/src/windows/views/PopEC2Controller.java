package windows.views;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.EXSI.EC2_Import;
import com.EXSI.ExportFromESXi;
import com.EXSI.S3;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import windows.models.Aws_credentials;
import windows.models.VSphere_credentials;

public class PopEC2Controller {

    @FXML
    private TextField bucketKey;

    @FXML
    private TextField sgroup;

    @FXML
    private TextField PairKey;

    @FXML
    private TextField InstanceType;

    private Stage dialogStage;
    private String selectItem;
    @FXML
    void MigrateToEC2(MouseEvent event) {
    	if(bucketKey.getText()!="" && sgroup.getText()!=""&& PairKey.getText()!=""&& InstanceType.getText()!=""){
    	List<String> vmdkfile = new ArrayList<String>();
    	try {
			vmdkfile = ExportFromESXi.exportfromesxi(VSphere_credentials.defaultCred.getSI(), VSphere_credentials.defaultCred.get_ip(), "./", selectItem);
			Boolean Bucketresult = S3.createBucket(Aws_credentials.DefaultCred.getS3(), bucketKey.getText() ,Aws_credentials.DefaultCred.get_region());
			System.out.print("after create bucket");
			System.out.println(Bucketresult);
			String importid= null;
			
			if (Bucketresult)
			{
				importid=EC2_Import.importToEc2(vmdkfile,Aws_credentials.DefaultCred.getS3(),Aws_credentials.DefaultCred.getEC2(),bucketKey.getText());
				System.out.println(importid);
			}
            //check import status with import id
			String checkresult;
            checkresult = EC2_Import.checkimportstatus(importid, Aws_credentials.DefaultCred.getProvider(),Aws_credentials.DefaultCred.getEC2());
            System.out.println(checkresult);
            checkresult=EC2_Import.checkimporthistory(Aws_credentials.DefaultCred.getProvider(),Aws_credentials.DefaultCred.getEC2());
            System.out.println(checkresult);
            EC2_Import.launchInstance(Aws_credentials.DefaultCred.getEC2(), importid, InstanceType.getText(), PairKey.getText(), sgroup.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    }

    @FXML
    void closeStage(MouseEvent event) {
    	dialogStage.close();
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    public void setSelectItem(String selected){
    	this.selectItem=selected;
    }

}
