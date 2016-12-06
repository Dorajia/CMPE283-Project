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
import sql.Sql;
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
    void MigrateToEC2(MouseEvent event) throws Exception {
    	if(bucketKey.getText()!="" && sgroup.getText()!=""&& PairKey.getText()!=""&& InstanceType.getText()!=""){
    	Sql log =new Sql();
    	List<String> vmdkfile = new ArrayList<String>();
		log.insertTaskData(selectItem, VSphere_credentials.defaultCred.get_ip(), Aws_credentials.DefaultCred.get_region().getName(), "Migrate to EC2", "Incomplete");
		log.insertEventData("Start Task", selectItem, "Prepare to export VM", "start");
    	try {
    		dialogStage.close();
			vmdkfile = ExportFromESXi.exportfromesxi(VSphere_credentials.defaultCred.getSI(), VSphere_credentials.defaultCred.get_ip(), "./", selectItem);
			log.insertEventData("Task", selectItem, "exported VM", "completed");
			Boolean Bucketresult = S3.createBucket(Aws_credentials.DefaultCred.getS3(), bucketKey.getText() ,Aws_credentials.DefaultCred.get_region());
			System.out.print("after create bucket");
			System.out.println(Bucketresult);
			String importid= null;
			log.insertEventData("Task", selectItem, "Prepare for S3", "completed");
			if (Bucketresult)
			{
				log.insertEventData("Task", selectItem, "start to import to EC2", "start");
				importid=EC2_Import.importToEc2(vmdkfile,Aws_credentials.DefaultCred.getS3(),Aws_credentials.DefaultCred.getEC2(),bucketKey.getText());
				System.out.println(importid);
				log.insertEventData("Task", selectItem, "import to EC2", "completed");
			}
            //check import status with import id
			String checkresult;
            checkresult = EC2_Import.checkimportstatus(importid, Aws_credentials.DefaultCred.getProvider(),Aws_credentials.DefaultCred.getEC2());
            System.out.println(checkresult);
            checkresult=EC2_Import.checkimporthistory(Aws_credentials.DefaultCred.getProvider(),Aws_credentials.DefaultCred.getEC2());
            System.out.println(checkresult);
            log.insertEventData("Task", selectItem, "launch image", "start");
            EC2_Import.launchInstance(Aws_credentials.DefaultCred.getEC2(), importid, InstanceType.getText(), PairKey.getText(), sgroup.getText());
            log.insertEventData("Task", selectItem, "launch image", "completed");
            log.updateTaskData("completed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.updateTaskData("failed");
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
