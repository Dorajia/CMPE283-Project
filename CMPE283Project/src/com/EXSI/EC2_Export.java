package com.EXSI;

import com.amazonaws.services.ec2.*;

import java.io.IOException;
import java.sql.Timestamp;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.model.ContainerFormat;
import com.amazonaws.services.ec2.model.CreateInstanceExportTaskRequest;
import com.amazonaws.services.ec2.model.CreateInstanceExportTaskResult;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.DiskImageFormat;
import com.amazonaws.services.ec2.model.ExportEnvironment;
import com.amazonaws.services.ec2.model.ExportToS3TaskSpecification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class EC2_Export {

	private static ContainerFormat ExportContainerFormat = ContainerFormat.Ova ;//com.amazonaws.services.ec2.model.ContainerFormat.Ova is only value for container format
	private static DiskImageFormat exportDiskFormat = DiskImageFormat.VMDK;
	
	public static String ec2Export(AmazonEC2Client amazonEC2Client, AmazonS3 s3client,String regionname,String bucketName, String instanceID) throws IOException {		
		try{
			String prefixName = null;

			Region region=RegionUtils.getRegion(regionname);
			Boolean Bucketresult = S3.createBucket(s3client, bucketName, region);
			if(Bucketresult){
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				prefixName = "ExportImage"+timestamp;
				//S3.createFolder(S3_bucket, folderName, s3client);
		
					System.out.println("start to export ec2");					
					
					//http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/CreateInstanceExportTaskRequest.htm
					 CreateInstanceExportTaskRequest exportReq = new  CreateInstanceExportTaskRequest();					 
					 exportReq.setDescription("test export from java sdk");
					 exportReq.setInstanceId(instanceID);
					 exportReq.setTargetEnvironment(ExportEnvironment.Vmware);
					 exportReq.setExportToS3Task(new ExportToS3TaskSpecification()
							 	.withS3Bucket(bucketName).withS3Prefix(prefixName).withContainerFormat(ExportContainerFormat).withDiskImageFormat(exportDiskFormat));
					 //http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/ec2/model/ExportToS3TaskSpecification.html
					 exportReq.setGeneralProgressListener(new ProgressListener() {
						@Override
						public void progressChanged(ProgressEvent progressEvent) {
							System.out.println("Exported bytes: " + 
									progressEvent.getBytesTransferred());
						}
						});										 
			}
			 return prefixName;
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
            return "Error";
			
		}catch(AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with EC2, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            return "Error";
        }
	}

}

