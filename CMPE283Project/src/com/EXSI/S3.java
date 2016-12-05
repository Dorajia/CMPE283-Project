package com.EXSI;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

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
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.EmailAddressGrantee;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;

public class S3 {

	private static TransferManager tx;
	private static final String SUFFIX = "/";
        	
	public static Boolean createBucket(AmazonS3 s3client, String bucketname, Region region)
	{
		Boolean result = false;
		try {
	        if(!(s3client.doesBucketExist(bucketname)))
	        {
	        	// Note that CreateBucketRequest does not specify region. So bucket is 
	        	// created in the region specified in the client.
	        	String regionname = region.getName();
	        	System.out.println(regionname);
	        	CreateBucketRequest bucketrequest = new CreateBucketRequest(bucketname,regionname);

	        	s3client.createBucket(bucketrequest);
	            AccessControlList bucketAcl = s3client.getBucketAcl(bucketname);
	            bucketAcl.grantPermission(new EmailAddressGrantee("vm-import-export@amazon.com"), Permission.Write);
	            bucketAcl.grantPermission(new EmailAddressGrantee("vm-import-export@amazon.com"), Permission.ReadAcp);

	            s3client.setBucketAcl(bucketname, bucketAcl);
	        	
	            result = true;
	        	
	        	System.out.println("Bucket created succesfully");	        	
	        	return result;
	        }
	        else
	        {
	        	System.out.println("Bucket exist!");
	        	result = true;
	        	return result;
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
            result = false;
            return result;
            
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            result = false;
            return result;
        }
		
	}
	
	
	public static Boolean uploadtoS3(AmazonS3 s3client, String filePath, String bucketName) throws InterruptedException{
		Boolean result;
   try{ 	
	   System.out.println("Uploading file to S3");
		//String folderName = "importImage";
		//createFolder(bucketName, folderName, s3client);
		File fileToUpload = new File(filePath);
	   	tx = new TransferManager(s3client);
	   	
	   	//String fileName = folderName + SUFFIX + fileToUpload.getName();	
	   	String fileName = fileToUpload.getName();
	    PutObjectRequest request = new PutObjectRequest(
	            bucketName, fileName, fileToUpload);
        request.setGeneralProgressListener(new ProgressListener() {
			@Override
			public void progressChanged(ProgressEvent progressEvent) {
				System.out.println("Transferred bytes: " + 
						progressEvent.getBytesTransferred());
			}
		});
        
	    Upload upload;
	    upload = tx.upload(request);    
	     
        System.out.println("Transfer: " + upload.getDescription());
            
	      try{
	        upload.waitForCompletion();
	      }catch (AmazonClientException amazonClientException) {
	      	System.out.println("Unable to upload file, upload aborted.");
	      	amazonClientException.printStackTrace();
	      }
	            
     // After the upload is complete, call shutdownNow to release the resources.
       tx.shutdownNow();
       result = true;
       return result;
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
       result = false;
       return result;
   } catch (AmazonClientException ace) {
       System.out.println("Caught an AmazonClientException, which " +
       		"means the client encountered " +
               "an internal error while trying to " +
               "communicate with S3, " +
               "such as not being able to access the network.");
       System.out.println("Error Message: " + ace.getMessage());
       result = false;
       return result;
   }
   
	
}
		
	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
		try{
			// create meta-data for your folder and set content-length to 0
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);
			// create empty content
			InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
			// create a PutObjectRequest passing the folder name suffixed by /
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
					folderName + SUFFIX, emptyContent, metadata);
			// send request to S3 to create folder
			client.putObject(putObjectRequest);
		}catch (Exception e)
		{
			return;
		}
	}
	
	public static void downloadfromS3(AmazonS3 s3client, String filePath, String bucketName,String exportid) throws InterruptedException{
		   try{ 	
			   System.out.println("Downloading an object");
			   String key = exportid+".ova";
	            S3Object s3object = s3client.getObject(new GetObjectRequest(
	            		bucketName, key));
	            System.out.println("Content-Type: "  + 
	            		s3object.getObjectMetadata().getContentType());
	           // displayTextInputStream(s3object.getObjectContent(),filePath);	            
	           // Get a range of bytes from an object.
	            
	            GetObjectRequest rangeObjectRequest = new GetObjectRequest(
	            		bucketName, key);
	            //rangeObjectRequest.setRange(0, 10);
	            S3Object objectPortion = s3client.getObject(rangeObjectRequest);
	            rangeObjectRequest.setGeneralProgressListener(new ProgressListener() {
					@Override
					public void progressChanged(ProgressEvent progressEvent) {
						System.out.println("Exported bytes: " + 
								progressEvent.getBytesTransferred());
					}
					});
	            File file = new File(filePath); 
	            IOUtils.copy(objectPortion.getObjectContent(), new FileOutputStream(file));
	            System.out.println("Printing bytes retrieved.");	            
	            //displayTextInputStream(objectPortion.getObjectContent(),filePath);
	            
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
		   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		   
			
		}

    
	public static String listfroms3(AmazonS3 s3client, String bucketName,String prefix) throws InterruptedException{
		   try{ 	
			   String keyname = null;
			   ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
					    .withBucketName(bucketName).withPrefix(prefix);
					ObjectListing objectListing;

					do {
					        objectListing = s3client.listObjects(listObjectsRequest);
					        for (S3ObjectSummary objectSummary : 
					            objectListing.getObjectSummaries()) {
					              keyname= objectSummary.getKey();
					        }
					        listObjectsRequest.setMarker(objectListing.getNextMarker());
					} while (objectListing.isTruncated());
					return keyname;
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
		       return "Error";
		   } catch (AmazonClientException ace) {
		       System.out.println("Caught an AmazonClientException, which " +
		       		"means the client encountered " +
		               "an internal error while trying to " +
		               "communicate with S3, " +
		               "such as not being able to access the network.");
		       System.out.println("Error Message: " + ace.getMessage());
		       return "Error";
		   }
		   
			
		}
	
}
