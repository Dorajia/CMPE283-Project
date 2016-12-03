package com.EXSI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.internal.StaticCredentialsProvider;

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

}
