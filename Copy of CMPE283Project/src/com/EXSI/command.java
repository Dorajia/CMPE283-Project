package com.EXSI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class command
{ 
	
	public static void runCommand(String command,String ip) throws JSchException, IOException{
		 JSch js = new JSch();
		    Session s = js.getSession("dora", ip, 22);
		    s.setPassword("yuanyuan");
		    java.util.Properties config = new java.util.Properties();
		    config.put("StrictHostKeyChecking", "no");
		    s.setConfig(config);
		    
		    UserInfo ui = new MyUserInfo(){
		          public void showMessage(String message){
		            System.out.println(message);
		          }

		    };		        
		        
		    s.setUserInfo(ui);
		    s.connect();
		    Channel c = s.openChannel("exec");
		    
		    ChannelExec ce = (ChannelExec) c;
		    String sudo_pass="yuanyuan";
		    
		    ce.setCommand("sudo -S -p '' "+command);
		    //ce.setCommand(command);
		    ce.setPty(true);
		    InputStream in=c.getInputStream();
		    OutputStream out=c.getOutputStream();
		    ce.setErrStream(System.err);

		    ce.connect();
		    out.write((sudo_pass+"\n").getBytes());
		    out.flush();
		      
		    BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
		    String line;
		    while ((line = reader.readLine()) != null) {
		      System.out.println(line);
		    }

		    ce.disconnect();
		    s.disconnect();
		    reader.close();

		    System.out.println("Exit code: " + ce.getExitStatus());
	}
	
	  public static abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive{
		  
			public String getPassword(){ return "yuanyuan"; }
			public boolean promptYesNo(String str) {
		        str = "Yes";
		        return true;
		    }
			public String getPassphrase(){ return null; }
			public boolean promptPassphrase(String message){ return false; }
			public boolean promptPassword(String message){ return false; }
			public void showMessage(String message){ }
			public String[] promptKeyboardInteractive(String destination,
			                          String name,
			                          String instruction,
			                          String[] prompt,
			                          boolean[] echo){
			return null;
			}
}
	
}
