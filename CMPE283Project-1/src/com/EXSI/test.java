package com.EXSI;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.vmware.vim25.mo.ServiceInstance;
	
public class test {

	public static void main(String[] args) throws Exception {
		List<String> vmdkfile = new ArrayList<String>();
		AWSCredentialsProvider provider;
        
		File file = new File("credential.properties");
		FileInputStream fileInput = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fileInput);
		fileInput.close();
		
		String AccessKeyID = properties.getProperty("AccessKeyID");
		String SecretAccessKey = properties.getProperty("SecretAccessKey");
		
		//Get provider
        provider = awsCredential.provider(AccessKeyID,SecretAccessKey);
        
        //check if awsCredential is valid
        Boolean credentialValid = awsCredential.checkcredential(AccessKeyID, SecretAccessKey);
        if(credentialValid)
        {
        	System.out.println("valid credential!");
        }
        else
        {
        	System.out.println("invalid credential!");
        }
        //get region list
        List<String> regionlist = new ArrayList<String>();
        regionlist = AWSEc2Info.getAllEc2Regions();
        for(int i=0;i<regionlist.size();i++ )
        {
        	System.out.println(regionlist.get(i));
        }
		
        
        String regionname = "us-west-2";
        Region region=RegionUtils.getRegion(regionname);
		
		AmazonEC2Client amazonEC2Client = new AmazonEC2Client(provider);
		amazonEC2Client.setRegion(region);

        //Get instance based on region
		List<List<String>> instanceInfo = new ArrayList<List<String>>();
        instanceInfo = AWSEc2Info.GetInstance(region, amazonEC2Client);
        for(int i =0;i<instanceInfo.size();i++)
        {
        	//the first instance
        	for (int j=0;j<instanceInfo.get(i).size();j++)
        	{
        		//intance information list
        		System.out.println(instanceInfo.get(i).get(j));
        	}
        }
        
        AmazonS3 s3client = new AmazonS3Client(provider);
        //Folder path to store the vmdk and ova file
        String targetDir = "/Users/Dora/Desktop/";
        //bucketName
        String bucketName = "migration-final-test-import";

        //EC2 import process 
        ServiceInstance si = new ServiceInstance(new URL("https://192.168.170.167/sdk"), "root", "yuanyuan", true);
        String vmName = "Ubuntu-14.04-server";
        String hostip = "192.168.170.167";
        String VMuser = "dora";
        String VMPassword = "yuanyuan";
		
        vmdkfile=ExportFromESXi.exportfromesxi(si,hostip,targetDir,vmName,VMuser,VMPassword);

		Boolean Bucketresult = S3.createBucket(s3client, bucketName, region);
		String importid= null;
		
		if (Bucketresult)
		{
			importid=EC2_Import.importToEc2(vmdkfile,s3client,amazonEC2Client,bucketName);
		}
		
        //EC2 export process
		String instanceid = "i-0d3c7aedf6649bc2f";
        String exportname = EC2_Export.ec2Export(amazonEC2Client,s3client,region,bucketName,instanceid); 
        

        String checkresult;
        //check import status with import id
        checkresult = EC2_Import.checkimportstatus(importid, provider,amazonEC2Client);
        System.out.println(checkresult);
        //check import history
        checkresult=EC2_Import.checkimporthistory(provider,amazonEC2Client);
        System.out.println(checkresult);
        //check export status with export id
        checkresult=EC2_Export.checkexportstatus(exportname, provider,amazonEC2Client);
        System.out.println(checkresult);
        //check export history
        checkresult=EC2_Export.checkexporthistory(provider,amazonEC2Client);
        System.out.println(checkresult);
        
        //download exported ova       

        String filepath = targetDir+exportname+".ova";
        S3.downloadfromS3(s3client, filepath, bucketName,exportname);
        
        //import process 
        //untar ova file to OVF and vmdk file
		 List<String> untarresult = new ArrayList<String>();
		 untarresult = unTarOva.unTar(filepath, targetDir);
		
		 for(int i=0;i<untarresult.size();i++)
		{
			if(untarresult.get(i).substring(untarresult.get(i).lastIndexOf(".") + 1).equals("ovf"))
			{
				//import to ESXi host
				String ovflocal = targetDir+untarresult.get(i);
				String newVmName = "New VM name";
				ImportToESXi.importVM(si,hostip,ovflocal,newVmName);
			}
			
		}
        
	}
}
