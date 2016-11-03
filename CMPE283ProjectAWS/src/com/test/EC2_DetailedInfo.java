package com.test;

import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;


import javax.annotation.processing.Filer;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.ec2.*;
import com.amazonaws.services.ec2.model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EC2_DetailedInfo {
	public static void main(String[] args) {
		String s = null;
 
		try {
			
			Process p = Runtime.getRuntime().exec("/usr/local/bin/aws ec2 describe-regions");
			 JSONParser parser = new JSONParser();
			 
			//Process awk = new ProcessBuilder("/bash/bin"scriptPath + script).start();
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	        
			String line2;
			String result = "";
			  StringBuilder responseStrBuilder = new StringBuilder();
			  while ((line2 = stdInput.readLine()) != null) {
			      responseStrBuilder.append(line2);
			  }  	
			  
			 Object obj = parser.parse(responseStrBuilder.toString());
		     JSONObject jsonObject = (JSONObject) obj;
		        
	         String Endpoint = (String) jsonObject.get("Endpoint");
	         String author = (String) jsonObject.get("Author");
	         JSONArray regionList = (JSONArray) jsonObject.get("Regions"); 
	         
		     JSONObject myobject;
	         for(int i = 0; i < regionList.size();i++)
	         {
	        	 myobject = (JSONObject) regionList.get(i);
		        System.out.println(myobject.get("Endpoint"));
		        System.out.println(myobject.get("RegionName"));
	         }

	         
			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
 
			System.exit(0);
		} catch (IOException e) {
			System.out.println("exception happened - here's what I know: ");
			e.printStackTrace();
			System.exit(-1);
		} //catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
 catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
