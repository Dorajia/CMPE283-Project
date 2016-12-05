package com.EXSI;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
		provider = awsCredential.provider();
		String bucketName = "migration-test-2";
		Region region=Region.getRegion(Regions.US_WEST_2);
		AmazonEC2Client amazonEC2Client = new AmazonEC2Client(provider);
		amazonEC2Client.setRegion(region);
        AmazonS3 s3client = new AmazonS3Client(provider);
        //Folder path to store the vmdk and ova file
/*       String targetDir = "/Users/Dora/Desktop/";
        
        //EC2 import process 
       ServiceInstance si = new ServiceInstance(new URL("https://192.168.170.135/sdk"), "root", "yuanyuan", true);
        String vmName = "server";
        String hostip = "192.168.170.135";
 
		vmdkfile=ExportFromESXi.exportfromesxi(si,hostip,targetDir,vmName);

		Boolean Bucketresult = S3.createBucket(s3client, bucketName, region);
		String importid= null;
		
		if (Bucketresult)
		{
			importid=EC2_Import.importToEc2(vmdkfile,s3client,amazonEC2Client,bucketName);
		}
		
        //EC2 export process
		String instanceid = "i-00636c3f1d7938c44";
        String exportname = EC2_Export.ec2Export(amazonEC2Client, s3client,region.getName(), bucketName, instanceid); 
        
        //check import status with import id
        EC2_Import.checkimportstatus(importid, provider,amazonEC2Client);*/
        //check import history
        EC2_Import.checkimporthistory(provider,amazonEC2Client);
        //check export status with export id
//        EC2_Export.checkexportstatus(exportname, provider,amazonEC2Client);
        //check export history
        EC2_Export.checkexporthistory(provider,amazonEC2Client);
        //download exported ova
//        String filepath = targetDir+exportname;
//        S3.downloadfromS3(s3client, filepath, bucketName,exportname);
	}
}
