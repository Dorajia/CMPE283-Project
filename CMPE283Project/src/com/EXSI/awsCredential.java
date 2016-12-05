package com.EXSI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

public class awsCredential {
	
	public static AWSCredentialsProvider provider() throws IOException{

		File file = new File("credential.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fileInput);
		fileInput.close();
		
		String AccessKeyID = properties.getProperty("AccessKeyID");
		String SecretAccessKey = properties.getProperty("SecretAccessKey");
	    
		AWSCredentialsProvider provider;
	    if ( AccessKeyID != null && SecretAccessKey != null ) {
	        AWSCredentials credentials = new BasicAWSCredentials( AccessKeyID, SecretAccessKey );
	        provider = new StaticCredentialsProvider( credentials );
	    }
	    else {
	        provider = new DefaultAWSCredentialsProviderChain();
	    }
		return provider;
		}	
	
	public static AWSCredentialsProvider provider(String AccessKeyID, String  SecretAccessKey) throws IOException{
	    
		AWSCredentialsProvider provider;
	    if ( AccessKeyID != null && SecretAccessKey != null ) {
	        AWSCredentials credentials = new BasicAWSCredentials( AccessKeyID, SecretAccessKey );
	        provider = new StaticCredentialsProvider( credentials );
	    }
	    else {
	        provider = new DefaultAWSCredentialsProviderChain();
	    }
		return provider;
		}	
	
	public static Boolean checkcredential(String AccessKeyID, String SecretAccessKey){
		Boolean validcredential = false;
		List<String> instanceinfo = new ArrayList<String>();
		try{
			AWSCredentialsProvider provider = provider(AccessKeyID,SecretAccessKey);
			AmazonEC2Client amazonEC2Client = new AmazonEC2Client(provider);				
			Region region=Region.getRegion(Regions.US_WEST_2);
			amazonEC2Client.setRegion(region);  
		     DescribeInstancesResult describeInstances = amazonEC2Client.describeInstances();
		     validcredential = true;
		     List<Reservation> reservations = describeInstances.getReservations();
		        for (Reservation reservation : reservations) {
		            List<Instance> instances = reservation.getInstances();
		             for (Instance instance : instances) {
		            	 instanceinfo.add(instance.getInstanceId());	            
		             }
		        }
		        
		     return validcredential;
		}catch (Exception e)
		{
			return validcredential;
		}
		
	}

}
