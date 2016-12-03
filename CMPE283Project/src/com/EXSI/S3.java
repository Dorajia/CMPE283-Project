package com.EXSI;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Region;
import com.amazonaws.services.ec2.model.ContainerFormat;
import com.amazonaws.services.ec2.model.DiskImageFormat;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class S3 {

	private static TransferManager tx;

        	
	public static void createBucket(AmazonS3 s3client, String bucketname, Region region)
	{
		try {
	        if(!(s3client.doesBucketExist(bucketname)))
	        {
	        	// Note that CreateBucketRequest does not specify region. So bucket is 
	        	// created in the region specified in the client.
	        	String regionname = region.getName();
	        	System.out.println(regionname);
	        	CreateBucketRequest bucketrequest = new CreateBucketRequest(bucketname,regionname);	        	
	        	s3client.createBucket(bucketrequest);
	        	System.out.println("Bucket created succesfully");			
	        }
	        else
	        {
	        	System.out.println("Bucket exist!");
	        }
		
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
        }
		

	}
	
	public static void uploadtoS3(AmazonS3 s3client, String filePath, String bucketName) throws InterruptedException{
   try{ 	
	   	tx = new TransferManager(s3client);
	    File fileToUpload = new File(filePath);
	    PutObjectRequest request = new PutObjectRequest(
	            bucketName, fileToUpload.getName(), fileToUpload);
	    Upload upload;
	    upload = tx.upload(request);    
    
        ProgressListener progressListener = new ProgressListener() {
           public void progressChanged(ProgressEvent progressEvent) {
               if (upload == null) return;

               switch (progressEvent.getEventCode()) {
               case ProgressEvent.COMPLETED_EVENT_CODE:
                   break;
               case ProgressEvent.FAILED_EVENT_CODE:
                   try {
                       AmazonClientException e = upload.waitForException();
                       System.out.printf(
                               "Unable to upload file to Amazon S3: " + e.getMessage(),
                               "Error Uploading File");
                   } catch (InterruptedException e) {}
                   break;
               }
           }
       };
           	
       // upload.waitForCompletion();

        if (upload.isDone() == false) {
            System.out.println("Transfer: " + upload.getDescription());
            System.out.println("  - State: " + upload.getState());
            System.out.println("  - Progress: "
                            + upload.getProgress().getBytesTransferred());
     }
      
     // Transfers also allow you to set a <code>ProgressListener</code> to receive
     // asynchronous notifications about your transfer's progress.
        upload.addProgressListener(progressListener);
      
     // Or you can block the current thread and wait for your transfer to
     // to complete. If the transfer fails, this method will throw an
     // AmazonClientException or AmazonServiceException detailing the reason.
        upload.waitForCompletion();
      
     // After the upload is complete, call shutdownNow to release the resources.
       tx.shutdownNow();
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
       return;
   } catch (AmazonClientException ace) {
       System.out.println("Caught an AmazonClientException, which " +
       		"means the client encountered " +
               "an internal error while trying to " +
               "communicate with S3, " +
               "such as not being able to access the network.");
       System.out.println("Error Message: " + ace.getMessage());
       return;
   }
   
	
}
}
