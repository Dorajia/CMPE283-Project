package com.test;

import java.net.URL;
import java.rmi.RemoteException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
//192.168.170.135 root yuanyuan
public class VMware_Export
{ 
	public static void main(String[] args) throws Exception {
        
		ServiceInstance si = null;
//		ManagedEntity[] managedEntities = null;
		VirtualMachine findvm;

		try{	
			//si = new ServiceInstance(new URL("https://130.65.159.14/sdk"), "cmpe283_sec3_student@vsphere.local", "cmpe-W6ik", true);
			si = new ServiceInstance(new URL("https://192.168.170.135/sdk"), "root", "yuanyuan", true);
			Folder rootFolder = si.getRootFolder(); 
			String vmname = "YuanyuanJia-ubuntu-1404-322-1";
			findvm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine",vmname);
			if (findvm == null ) {
				System.out.println("No such VM");;
			}	
			else{
				System.out.println(findvm.getGuest().getIpAddress());
			}
			}catch (InvalidProperty e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				} catch (RuntimeFault e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				    }
		} 
	
}