package com.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.*;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.ec2.*;
import com.amazonaws.services.ec2.model.*;

public class AWSCredential {
	public static void main(String[] args) {
		String AccessKeyID = "AKIAJF3QROSKAZIDNE7A";
		String SecretAccessKey = "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";
		String keyName = "keyFromEclipse";
		
	    AWSCredentialsProvider provider;
	    if ( AccessKeyID != null && SecretAccessKey != null ) {
	        AWSCredentials credentials = new BasicAWSCredentials( AccessKeyID, SecretAccessKey );
	        provider = new StaticCredentialsProvider( credentials );
	    }
	    else {
	        provider = new DefaultAWSCredentialsProviderChain();
	    }
		
		CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();

		createKeyPairRequest.withKeyName(keyName);
		
		
//		AmazonEC2 amazonEC2Client = AmazonEC2ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("us-west-2").build();
		
		AmazonEC2Client amazonEC2Client = new AmazonEC2Client(provider);
		amazonEC2Client.setEndpoint("https://ec2.eu-west-2.amazonaws.com");
		
		CreateKeyPairResult createKeyPairResult =
				  amazonEC2Client.createKeyPair(createKeyPairRequest);
		KeyPair keyPair = new KeyPair();

		keyPair = createKeyPairResult.getKeyPair();

		String privateKey = keyPair.getKeyMaterial();
		System.out.println(privateKey);
		
		try{
			CreateSecurityGroupRequest csgr = new CreateSecurityGroupRequest();
			csgr.withGroupName("JavaSecurityGroup").withDescription("My security group");
			CreateSecurityGroupResult createSecurityGroupResult =
				    amazonEC2Client.createSecurityGroup(csgr);			
		}catch (AmazonServiceException ase) {
		    // Likely this means that the group is already created, so ignore.
		    System.out.println(ase.getMessage());
		}

		// Get the IP of the current host, so that we can limit the Security
		// Group by default to the ip range associated with your subnet.
		IpPermission ipPermission =
			    new IpPermission();

			ipPermission.withIpRanges("111.111.111.111/32", "150.150.150.150/32")
			            .withIpProtocol("tcp")
			            .withFromPort(22)
			            .withToPort(22);
			

		try {
		    // Authorize the ports to the used.
			AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
				    new AuthorizeSecurityGroupIngressRequest();

				authorizeSecurityGroupIngressRequest.withGroupName("JavaSecurityGroup")
				                                    .withIpPermissions(ipPermission);
				amazonEC2Client.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
		} catch (AmazonServiceException ase) {
		    // Ignore because this likely means the zone has
		    // already been authorized.
		    System.out.println(ase.getMessage());
		}
		
		RunInstancesRequest runInstancesRequest =
			      new RunInstancesRequest();

		runInstancesRequest.withImageId("ami-4b814f22")
			                     .withInstanceType("t2.micro")
			                     .withMinCount(1)
			                     .withMaxCount(1)
			                     .withKeyName("keyFromEclipse")
			                     .withSecurityGroups("JavaSecurityGroup");
			  
		RunInstancesResult runInstances = amazonEC2Client.runInstances(runInstancesRequest);
		List<Instance> instances = runInstances.getReservation().getInstances();
		int idx = 1;
		for (Instance instance : instances) {
		  CreateTagsRequest createTagsRequest = new CreateTagsRequest();
		  createTagsRequest.withResources(instance.getInstanceId()) //
		      .withTags(new Tag("Name", "travel-ecommerce-" + idx));
		  amazonEC2Client.createTags(createTagsRequest);

		  idx++;
		}

	}
}
