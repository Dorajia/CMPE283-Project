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
	private static List<String> vmdkfile = new ArrayList<String>();
	public static void main(String[] args) throws Exception {
		
		AWSCredentialsProvider provider;
		provider = awsCredential.provider();
		String bucketName = "migration-test-2";
		Region region=Region.getRegion(Regions.US_WEST_2);
		AmazonEC2Client amazonEC2Client = new AmazonEC2Client(provider);
		amazonEC2Client.setRegion(region);
        AmazonS3 s3client = new AmazonS3Client(provider);
        //Folder path to store the vmdk and ova file
        String targetDir = "/Users/Dora/Desktop/";
        
        //EC2 import process 
/*        ServiceInstance si = new ServiceInstance(new URL("https://192.168.170.135/sdk"), "root", "yuanyuan", true);
        String vmName = "server";
        String hostip = "192.168.170.135";
 
		vmdkfile=ExportFromESXi.exportfromesxi(si,hostip,targetDir,vmName);
		for (int i = 0;i<vmdkfile.size();i++)
		{
			System.out.println(vmdkfile.get(i));
		}
		Boolean Bucketresult = S3.createBucket(s3client, bucketName, region);
		if (Bucketresult)
		{
			EC2_Import.importToEc2(vmdkfile,s3client,amazonEC2Client,bucketName);
		}*/
        //EC2 export process
		String instanceid = "i-0b6dbfc8556268095";
        String prefix = EC2_Export.ec2Export(amazonEC2Client, s3client,region.getName(), bucketName, instanceid);
        String keyname = S3.listfroms3(s3client,bucketName,prefix);
        String filepath = targetDir+keyname;
		S3.downloadfromS3(s3client, filepath, bucketName,keyname);
	
	}
}
