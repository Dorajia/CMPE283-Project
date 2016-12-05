package windows.models;

import com.amazonaws.auth.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.EXSI.*;
import java.util.List;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;


public class Aws_credentials {
	private String key_id;
	private String secret_key;
	private String region;
	private boolean auth;
	public static Aws_credentials DefaultCred;
	private ObservableList<String> aws_region = FXCollections.observableArrayList();
	private AWSCredentialsProvider provider;
	
	public ObservableList<String> getRegions(){
		return aws_region;
	}
	public void fetchRegions(){
		List<String>regions = AWSEc2Info.getAllEc2Regions();
		regions.forEach((temp)->{aws_region.add(temp);});
	}
	public Aws_credentials(){
		key_id = "";
		secret_key = "";
		region = "";
		auth = false;
		DefaultCred =this;
	}
	public Aws_credentials(String id,String secret){
		key_id = id;
		secret_key = secret;
		region = "";
		auth = false;
		DefaultCred =this;
	}
	
	public Aws_credentials(String id,String secret,String region){
		key_id = id;
		secret_key = secret;
		this.region = region;
		auth = false;
		DefaultCred =this;
	}
	
	public String get_id(){
		return key_id;
	}
	public String get_secret(){
		return secret_key;
	}
	public void set_id(String id){
		key_id = id;
	}
	public void set_secret(String secret){
		secret_key = secret;
	}
	public AWSCredentialsProvider getProvider(){
        AWSCredentials credentials = new BasicAWSCredentials( key_id, secret_key );
        provider = new AWSStaticCredentialsProvider( credentials );
        return provider;
	}
	public String toString(){
		return "id:"+key_id+";secret:"+secret_key+";region:"+region+";authen:"+ auth;
	}
}
