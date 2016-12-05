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
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
//import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
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
	
	public static String importToEc2(List<String> vmdkfile,AmazonS3 s3, AmazonEC2Client amazonEC2Client,String bucketName)
	{
		
		try {	    
			for(int i=0;i<vmdkfile.size();i++){
				S3.uploadtoS3(s3,vmdkfile.get(i),bucketName);	
			}
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
	                
	                ImportImageResult result = amazonEC2Client.importImage(iir);

	                System.out.println("EC2 Import image");
	                iir.setGeneralProgressListener(new ProgressListener() {
						@Override
						public void progressChanged(ProgressEvent progressEvent) {
							System.out.println("Imported bytes: " + 
									progressEvent.getBytesTransferred());
						}
						});
	                
	                
	                System.out.println(result.getProgress());	                	                
	                String importid= result.getImportTaskId();
	                System.out.println(importid);
	                System.out.println(result.getProgress());
	                System.out.println(result.getStatus());
	                return importid;	            

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
	            return "error";
	        } catch (AmazonClientException ace) {
	            return ("Error: Caught an AmazonClientException, which " +
	            		"means the client encountered " +
	                    "an internal error while trying to " +
	                    "communicate with S3, " +
	                    "such as not being able to access the network."+" Error Message: " + ace.getMessage());
	        } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ("Error: "+e);
			}
		
	}
	
	public static String checkimportstatus(String importid, AWSCredentialsProvider provider,AmazonEC2Client amazonEC2Client) {		
        DescribeImportImageTasksRequest imagerequest = new DescribeImportImageTasksRequest();
        imagerequest.withImportTaskIds(importid);
        imagerequest.setRequestCredentialsProvider(provider);
        DescribeImportImageTasksResult importresult=amazonEC2Client.describeImportImageTasks(imagerequest);
               
        System.out.println(importresult);
        
        return importresult.toString();
        
	}
	
	public static String checkimporthistory(AWSCredentialsProvider provider,AmazonEC2Client amazonEC2Client) {		
        DescribeImportImageTasksRequest imagerequest = new DescribeImportImageTasksRequest();
        imagerequest.setRequestCredentialsProvider(provider);
        DescribeImportImageTasksResult importresult=amazonEC2Client.describeImportImageTasks(imagerequest);
               
        System.out.println(importresult.toString());
        
        
 /*       imagerequest.setGeneralProgressListener(new ProgressListener() {
			@Override
			public void progressChanged(ProgressEvent progressEvent) {
				System.out.println("Imported bytes: " + 
						progressEvent.getBytesTransferred());
			}
			});*/
        return importresult.toString();
        
	}

	public static String cancelimporttask(String importid,AWSCredentialsProvider provider,AmazonEC2Client amazonEC2Client) 
	{

		CancelImportTaskRequest cancelimport = new CancelImportTaskRequest();
		cancelimport.withImportTaskId(importid);
		CancelImportTaskResult cancelresult=amazonEC2Client.cancelImportTask(cancelimport);
		System.out.println(cancelresult);
		return cancelresult.toString();
	}
}
