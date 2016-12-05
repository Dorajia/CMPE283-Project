package windows.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.mo.ServiceInstance;

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
}
