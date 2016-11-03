package EC2;

//import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.*;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.model.ContainerFormat;
import com.amazonaws.services.ec2.model.CreateInstanceExportTaskRequest;
import com.amazonaws.services.ec2.model.DiskImageFormat;
import com.amazonaws.services.ec2.model.ExportEnvironment;
import com.amazonaws.services.ec2.model.ExportToS3TaskSpecification;

public class EC2_export {
	private static String S3_bucket ="cmpe283-test";
	private static ContainerFormat ExportContainerFormat = ContainerFormat.Ova ;//com.amazonaws.services.ec2.model.ContainerFormat.Ova is only value for container format
	private static DiskImageFormat exportDiskFormat = DiskImageFormat.VMDK;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AmazonEC2 ec2client = new AmazonEC2Client(new ProfileCredentialsProvider());
		try{
			System.out.println("start to export ec2");
			
			//http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/CreateInstanceExportTaskRequest.htm
			 CreateInstanceExportTaskRequest exportReq = new  CreateInstanceExportTaskRequest();
			 exportReq.setDescription("test export from java sdk");
			 exportReq.setInstanceId("i-12a8348c");
			 exportReq.setTargetEnvironment(ExportEnvironment.Vmware);
			 exportReq.setExportToS3Task(new ExportToS3TaskSpecification()
					 	.withS3Bucket(S3_bucket).withContainerFormat(ExportContainerFormat)
					 	.withDiskImageFormat(exportDiskFormat));
			 //http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/ExportToS3TaskSpecification.html
			 
			 ec2client.createInstanceExportTask(exportReq);
			 
		}catch(AmazonServiceException ase){
			System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon EC2, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
			
		}catch(AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with EC2, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }finally{
        	System.out.println("check S3 bucket");
        }
	}

}
