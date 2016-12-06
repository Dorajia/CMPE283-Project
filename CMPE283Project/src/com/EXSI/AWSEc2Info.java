package com.EXSI;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Region;
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
	
	public static List<List<String>> GetInstance(Region region, AmazonEC2Client amazonEC2Client){
		List<List<String>>  instanceinfolist = new ArrayList<List<String>> ();
		System.out.println("-----"+region.getName());
		try{

			amazonEC2Client.setRegion(region);  

		     DescribeInstancesResult describeInstances = amazonEC2Client.describeInstances();
		     System.out.println(describeInstances.toString());
		     List<Reservation> reservations = describeInstances.getReservations();
		     System.out.println(reservations.size());
		        for (Reservation reservation : reservations) {
		            List<Instance> instances = reservation.getInstances();
		             for (int i =0;i<instances.size();i++) {
		            	 List<String> instance = new ArrayList<String>();
		            	 instance.add(instances.get(i).getInstanceId());
		            	 System.out.println(instances.get(i).getInstanceId());
		            	 instance.add(instances.get(i).getArchitecture());
		            	 System.out.println(instances.get(i).getArchitecture());
		            	 instance.add(instances.get(i).getImageId());
		            	 System.out.println(instances.get(i).getImageId());
		            	 instance.add(instances.get(i).getInstanceType());
		            	 System.out.println(instances.get(i).getInstanceType());
		            	 instance.add(instances.get(i).getKeyName());
		            	 System.out.println(instances.get(i).getKeyName());
		            	 instance.add(instances.get(i).getPublicIpAddress());
		            	 System.out.println(instances.get(i).getPublicIpAddress());
		            	 instance.add(instances.get(i).getLaunchTime().toString());
		            	 System.out.println(instances.get(i).getLaunchTime().toString());
		            	 instance.add(instances.get(i).getState().getName());
		            	 System.out.println(instances.get(i).getState().getName());
		            	 String tags = "";
		            	 for(int j = 0;j<instances.get(i).getTags().size(); j++){
		            		 tags =tags+instances.get(i).getTags().get(j).getValue()+",";
		            	 }
		            	 instance.add(tags.substring(0, tags.length()-1));
		            	 instanceinfolist.add(instance);
		            	 
		             }
		        }
		     return instanceinfolist;
		}catch (Exception e)
		{
			return instanceinfolist;
		}
		
	}
	


}
