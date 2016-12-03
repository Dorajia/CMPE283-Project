package com.EXSI;

import com.amazonaws.services.ec2.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.model.ContainerFormat;
import com.amazonaws.services.ec2.model.CreateInstanceExportTaskRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.DiskImageFormat;
import com.amazonaws.services.ec2.model.ExportEnvironment;
import com.amazonaws.services.ec2.model.ExportToS3TaskSpecification;
import com.amazonaws.services.ec2.model.ImageDiskContainer;
import com.amazonaws.services.ec2.model.ImportImageRequest;
import com.amazonaws.services.ec2.model.ImportImageResult;
import com.amazonaws.services.ec2.model.ImportImageTask;
import com.amazonaws.services.ec2.model.UserBucket;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.EXSI.awsCredential;

import javafx.beans.property.Property;

import com.amazonaws.services.ec2.model.*;

public class EC2_Import {
	private static String S3_bucket ="bucketformigrateaws";
	
	private static ContainerFormat ExportContainerFormat = ContainerFormat.Ova ;//com.amazonaws.services.ec2.model.ContainerFormat.Ova is only value for container format
	private static DiskImageFormat exportDiskFormat = DiskImageFormat.VMDK;
	
	private static TransferManager tx;
	private static String bucketName;
	private static List<String> vmdkfile = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {
		
		AWSCredentialsProvider provider;
		provider = awsCredential.provider();
		bucketName = "bucketformation222";
		Region region=Region.getRegion(Regions.US_WEST_2);
		AmazonEC2Client amazonEC2Client = new AmazonEC2Client(provider);
//		amazonEC2Client.setEndpoint("ec2.us-west-2.amazonaws.com");
		amazonEC2Client.setRegion(region);
        AmazonS3 s3 = new AmazonS3Client(provider);
				
		try {	    
			vmdkfile=ExportFromESXi.exportfromesxi();
        	//vmdkfile.add("/Users/Dora/Desktop/disk-1.vmdk");
			for(int i=0;i<vmdkfile.size();i++){
				S3.uploadtoS3(s3,vmdkfile.get(i),bucketName);	
			}
	                CreateInstanceExportTaskRequest exportReq = new  CreateInstanceExportTaskRequest();
	                ImportImageRequest iir = new ImportImageRequest();
	                ImageDiskContainer idc = new ImageDiskContainer();
	                Collection<ImageDiskContainer> diskContainers = new ArrayList<ImageDiskContainer>();
	                idc.withDescription("test");
	                idc.setFormat("vmdk");

	    			for(int i=0;i<vmdkfile.size();i++){
	    				String tmp = vmdkfile.get(i);
	    				String key = tmp.substring(tmp.lastIndexOf("/") + 1);
		                UserBucket userbucket = new UserBucket();
		                userbucket.setS3Bucket(bucketName);
		                userbucket.setS3Key(key);
		                idc.withUserBucket(userbucket);
		                diskContainers.add(idc);
	    			}	                                
	                iir.setDiskContainers(diskContainers);
	                System.out.println("EC2 Import image");
	                ImportImageResult result = amazonEC2Client.importImage(iir);
	                System.out.println(result.getProgress());
	                	                
	                String importid= result.getImportTaskId();
	                System.out.println(importid);
	                System.out.println(result.getProgress());
	                System.out.println(result.getStatus());
	                DescribeImportImageTasksRequest imagerequest = new DescribeImportImageTasksRequest();
	                imagerequest.withImportTaskIds(importid);
	                return;
	            

	         } catch (AmazonServiceException ase) {
	            System.out.println("Caught an AmazonServiceException, which " +
	            		"means your request made it " +
	                    "to Amazon S3, but was rejected with an error response" +
	                    " for some reason.");
	            System.out.println("Error Message:    " + ase.getMessage());
	            System.out.println("HTTP Status Code: " + ase.getStatusCode());
	            System.out.println("AWS Error Code:   " + ase.getErrorCode());
	            System.out.println("Error Type:       " + ase.getErrorType());
	            System.out.println("Request ID:       " + ase.getRequestId());
	        } catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which " +
	            		"means the client encountered " +
	                    "an internal error while trying to " +
	                    "communicate with S3, " +
	                    "such as not being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }/*catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
		
	}

	
}
