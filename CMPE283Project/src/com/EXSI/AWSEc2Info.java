package com.EXSI;



import java.util.ArrayList;
import java.util.List;

import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

public class AWSEc2Info {
	
	public static List<String> getAllEc2Regions(){
		List<String> regionlist = new ArrayList<String>();
		for (Regions region : Regions.values()) { 
			if(!region.getName().equals("us-gov-west-1") && !region.getName().equals("cn-north-1")){
				String regionname = region.getName(); 
				regionlist.add(regionname);
			}
		}
		return regionlist;
	}
	
	public static List<String> GetInstance(String regionname, AmazonEC2Client amazonEC2Client){
		List<String> instanceinfo = new ArrayList<String>();
		try{

			amazonEC2Client.setRegion(RegionUtils.getRegion(regionname));  

		     DescribeInstancesResult describeInstances = amazonEC2Client.describeInstances();
		     List<Reservation> reservations = describeInstances.getReservations();
		        for (Reservation reservation : reservations) {
		            List<Instance> instances = reservation.getInstances();
		             for (Instance instance : instances) {
		            	 instanceinfo.add(instance.getInstanceId());
		            	 instanceinfo.add(instance.getInstanceType());
		            	 instanceinfo.add(instance.getPublicIpAddress());		            
		             }
		        }
		     return instanceinfo;
		}catch (Exception e)
		{
			instanceinfo.add("Incorrect credential");
			return instanceinfo;
		}
		
	}
	


}
