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
import com.amazonaws.auth.AWSStaticCredentialsProvider;
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
public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        AWSCredentials credentials = new BasicAWSCredentials( "AKIAJT5RTUCKEWPX4KGA", "8339zgp6MhIYIWVPYMCzmYeBmYaMybDooLIWCjzw" );
        AWSCredentialsProvider provider = new AWSStaticCredentialsProvider( credentials );
        AmazonEC2Client amazonEC2Client = new AmazonEC2Client(provider);				
		Region region=Region.getRegion(Regions.US_EAST_1);
		amazonEC2Client.setRegion(region);
		System.out.println(EC2_Import.launchInstance(amazonEC2Client, "ami-c3eabcd4","t2.micro","hw1","default"));
	}

}
