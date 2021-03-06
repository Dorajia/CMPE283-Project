package com.EXSI;
/*================================================================================
Copyright (c) 2008 VMware, Inc. All Rights Reserved.
<pre>Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:
 
 * Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.
 
 * Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
 
 * Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior
written permission.
 
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
 
import com.vmware.vim25.HttpNfcLeaseDeviceUrl;
import com.vmware.vim25.HttpNfcLeaseInfo;
import com.vmware.vim25.HttpNfcLeaseState;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.OvfCreateDescriptorParams;
import com.vmware.vim25.OvfCreateDescriptorResult;
import com.vmware.vim25.OvfFile;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.HttpNfcLease;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.EXSI.command;
import com.jcraft.jsch.JSchException;
 
/**
http://www.doublecloud.org/2010/04/how-to-import-and-export-ovf-packages/ 
 */
 
public class ExportFromESXi
{
   public static LeaseProgressUpdater leaseProgUpdater;
	private static String VMIPADDRESS;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
     
	public static List<String> exportfromesxi(ServiceInstance si,String hostip, String targetDir,String vmName,String VMUser,String VMPassword) throws IOException, JSchException, InterruptedException
=======
>>>>>>> Stashed changes
    
	static HostSystem getHost(ServiceInstance si, String vm) throws InvalidProperty, RuntimeFault, RemoteException{
		Folder rootFolder = si.getRootFolder();
		ManagedEntity[] MHosts = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
		for(ManagedEntity mhost: MHosts){
			ManagedEntity thisVM = new InventoryNavigator(mhost).searchManagedEntity("VirtualMachine", vm);
			if(thisVM != null){
				HostSystem host = (HostSystem) mhost;
				return host;
				
			}
		}
		return null;
	
	}
	
	public static List<String> exportfromesxi(ServiceInstance si,String hostip, String targetDir,String vmName) throws IOException
>>>>>>> origin/master
      {
    	
        List<String> vmdkfile = new ArrayList<String>();
        HostSystem host = (HostSystem) si.getSearchIndex().findByIp(null, hostip, false);
        if(host == null){
        	host = getHost(si,vmName);
        }
 
        System.out.println("Host Name : " + host.getName());
        System.out.println("Network : " + host.getNetworks()[0].getName());
        System.out.println("Datastore : " + host.getDatastores()[0].getName());
 
        VirtualMachine findvm = findVM(vmName,si);
		if (findvm == null ) {
			System.out.println("No such VM");;
		}	
		else{
			VMIPADDRESS = findvm.getGuest().getIpAddress();
			System.out.println(VMIPADDRESS);
			command.runCommand("apt-get install python-pip -y", VMIPADDRESS,VMUser,VMPassword);
			command.runCommand("pip install awscli", VMIPADDRESS,VMUser,VMPassword);
			command.runCommand("vmware-uninstall-tools.pl",VMIPADDRESS,VMUser,VMPassword);
			Task poweroffTask = findvm.powerOffVM_Task();
			poweroffTask.waitForTask();
<<<<<<< HEAD
			//if(poweroffTask.equals(Task.SUCCESS)){
				vmdkfile=exportOvf(findvm,targetDir,si,hostip);		
			//}
=======
			if(poweroffTask.equals(Task.SUCCESS)){*/
		
			vmdkfile=exportOvf(findvm,targetDir,si,hostip);
		//}
				return vmdkfile;

       // si.getServerConnection().logout();
>>>>>>> origin/master
      }
		return vmdkfile;
	}
     
    private static VirtualMachine findVM(String vmName, ServiceInstance si) throws InvalidProperty, RuntimeFault, RemoteException{
		VirtualMachine findvm;
		Folder rootFolder = si.getRootFolder(); 

		findvm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine",vmName);
		return findvm;
    }
    
 	private static List<String> exportOvf(VirtualMachine VM, String targetDir, ServiceInstance si, String hostip) throws IOException{
        
 		HttpNfcLease hnLease = null;
        hnLease = VM.exportVm();
        List<String> vmdklist = new ArrayList<String>();
 
        // Wait until the HttpNfcLeaseState is ready
        HttpNfcLeaseState hls;
        for(;;)
        {
          hls = hnLease.getState();
          if(hls == HttpNfcLeaseState.ready)
          {
            break;
          }
          if(hls == HttpNfcLeaseState.error)
          {
            si.getServerConnection().logout();
            vmdklist.add("Error: HTTPError");
            return vmdklist;
          }
        }
 
        System.out.println("HttpNfcLeaseState: ready ");
        HttpNfcLeaseInfo httpNfcLeaseInfo = hnLease.getInfo();
        httpNfcLeaseInfo.setLeaseTimeout(300*1000*1000);
        printHttpNfcLeaseInfo(httpNfcLeaseInfo);
 
        //Note: the diskCapacityInByte could be many time bigger than
        //the total size of VMDK files downloaded.
        //As a result, the progress calculated could be much less than reality.
        long diskCapacityInByte = (httpNfcLeaseInfo.getTotalDiskCapacityInKB()) * 1024;
 
        leaseProgUpdater = new LeaseProgressUpdater(hnLease, 5000);
        leaseProgUpdater.start();
 
        long alredyWrittenBytes = 0;
        HttpNfcLeaseDeviceUrl[] deviceUrls = httpNfcLeaseInfo.getDeviceUrl();
        if (deviceUrls != null)
        {
          OvfFile[] ovfFiles = new OvfFile[deviceUrls.length];
          System.out.println("Downloading Files:");
          for (int i = 0; i < deviceUrls.length; i++)
          {
            String deviceId = deviceUrls[i].getKey();
            String deviceUrlStr = deviceUrls[i].getUrl();
            String diskFileName = deviceUrlStr.substring(deviceUrlStr.lastIndexOf("/") + 1);
            System.out.println(diskFileName);
            String diskUrlStr = deviceUrlStr.replace("*", hostip);
            String diskLocalPath = targetDir + diskFileName;
            if(diskFileName.substring(diskFileName.lastIndexOf(".") + 1).equals("vmdk"))
            {
            	vmdklist.add(diskLocalPath);
            }
            System.out.println("File Name: " + diskFileName);
            System.out.println("VMDK URL: " + diskUrlStr);
            String cookie = si.getServerConnection().getVimService().getWsc().getCookie();
            long lengthOfDiskFile = writeVMDKFile(diskLocalPath, diskUrlStr, cookie, alredyWrittenBytes, diskCapacityInByte);
            alredyWrittenBytes += lengthOfDiskFile;
            OvfFile ovfFile = new OvfFile();
            ovfFile.setPath(diskFileName);
            ovfFile.setDeviceId(deviceId);
            ovfFile.setSize(lengthOfDiskFile);
            ovfFiles[i] = ovfFile;
          }
 
          OvfCreateDescriptorParams ovfDescParams = new OvfCreateDescriptorParams();
          ovfDescParams.setOvfFiles(ovfFiles);
          OvfCreateDescriptorResult ovfCreateDescriptorResult =
            si.getOvfManager().createDescriptor(VM, ovfDescParams);
 
          String ovfPath = targetDir + VM.getName() + ".ovf";
          FileWriter out = new FileWriter(ovfPath);
          out.write(ovfCreateDescriptorResult.getOvfDescriptor());
          out.close();
          System.out.println("OVF Desriptor Written to file: " + ovfPath);
        }
 
        System.out.println("Completed Downloading the files");
        leaseProgUpdater.interrupt();
        hnLease.httpNfcLeaseProgress(100);
        hnLease.httpNfcLeaseComplete();
        return vmdklist;
 	}
 
    private static void printHttpNfcLeaseInfo(HttpNfcLeaseInfo info)
    {
        System.out.println("########################  HttpNfcLeaseInfo  ###########################");
        System.out.println("Lease Timeout: " + info.getLeaseTimeout());
        System.out.println("Total Disk capacity: "  + info.getTotalDiskCapacityInKB());
        HttpNfcLeaseDeviceUrl[] deviceUrlArr = info.getDeviceUrl();
        if (deviceUrlArr != null)
        {
            int deviceUrlCount = 1;
            for (HttpNfcLeaseDeviceUrl durl : deviceUrlArr)
            {
                System.out.println("HttpNfcLeaseDeviceUrl : "
                        + deviceUrlCount++);
                System.out.println("    Device URL Import Key: "
                        + durl.getImportKey());
                System.out.println("    Device URL Key: " + durl.getKey());
                System.out.println("    Device URL : " + durl.getUrl());
                System.out.println("    SSL Thumbprint : "  + durl.getSslThumbprint());
            }
        }
        else
        {
            System.out.println("No Device URLS Found");
        }
    }
 
    private static long writeVMDKFile(String localFilePath, String diskUrl, String cookie,
            long bytesAlreadyWritten, long totalBytes) throws IOException
    {
        HttpsURLConnection conn = getHTTPConnection(diskUrl, cookie);
        InputStream in = conn.getInputStream();
        OutputStream out = new FileOutputStream(new File(localFilePath));
        byte[] buf = new byte[102400];
        int len = 0;
        long bytesWritten = 0;
        while ((len = in.read(buf)) > 0)
        {
            out.write(buf, 0, len);
            bytesWritten += len;
            int percent = (int)(((bytesAlreadyWritten + bytesWritten) * 100) / totalBytes);
            leaseProgUpdater.setPercent(percent);
            System.out.println("written: " + bytesWritten);
        }
        in.close();
        out.close();
        return bytesWritten;
    }
 
    private static HttpsURLConnection getHTTPConnection(String urlStr, String cookieStr) throws IOException
    {
        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String urlHostName, SSLSession session)
            {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
 
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(true);
        conn.setRequestProperty("Cookie",   cookieStr);
        conn.connect();
        return conn;
    }
 
}