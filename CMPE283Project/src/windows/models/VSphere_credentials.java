package windows.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;


public class VSphere_credentials {
	private String ip;
	private String user;
	private String password;
	private ServiceInstance SI;
	public static VSphere_credentials defaultCred;
	
	public VSphere_credentials(){
		ip  = "";
		user = "";
		password="";
		defaultCred = this;
	}
	public VSphere_credentials(String ip, String user, String password){
		this.ip  = ip;
		this.user = user;
		this.password=password;
		defaultCred = this;
	}
	public void set_ip(String ip){
		
	}
	public void set_user(String user){
		
	}
	public void set_password(String ps){
		
	}
	public String get_ip(){
		return this.ip;
	}
	public String get_user(){
		return this.user;
	}
	public String get_password(){
		return this.password;
	}
	public ServiceInstance connect(){
		try {
			SI = new ServiceInstance(new URL("https://"+ip+"/sdk"),user,password,true);
			System.out.println("vsphere login Success");
		} catch (RemoteException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SI;
	}
	public ServiceInstance getSI(){
		return SI;
	}
	public void close(){
		SI.getServerConnection().logout();
	}
	
	public List<VM> getVMs(){
		if(SI==null){
			connect();
		}
		Folder rootFolder = SI.getRootFolder();
		List<VM> vms = new ArrayList<VM>();
		try {
			ManagedEntity[] VMs = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
			System.out.println(VMs.length);
			for(ManagedEntity VM : VMs){
				System.out.println(VM.getName());
				if(!((VirtualMachine)VM).getConfig().template)
					vms.add(new VM((VirtualMachine)VM));
			}
			return vms;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return vms;
		}
		
	}
}
