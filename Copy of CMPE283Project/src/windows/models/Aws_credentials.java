package windows.models;

import com.amazonaws.auth.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.EXSI.*;
import java.util.List;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.s3.AmazonS3Client;


public class Aws_credentials {
	private String key_id;
	private String secret_key;
	public Region get_region() {
		return Region.getRegion(Regions.fromName(region));
	}
	public void set_region(String r) {
		this.region = r;
	}
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
		auth = awsCredential.checkcredential(key_id, secret_key);
		DefaultCred =this;
	}
	public Aws_credentials(String id,String secret){
		key_id = id;
		secret_key = secret;
		region = "";
		auth = awsCredential.checkcredential(key_id, secret_key);
		DefaultCred =this;
	}
	
	public Aws_credentials(String id,String secret,String region){
		key_id = id;
		secret_key = secret;
		this.region = region;
		auth = awsCredential.checkcredential(key_id, secret_key);
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
	public boolean get_auth(){
		auth = awsCredential.checkcredential(key_id, secret_key);
		return this.auth;
	}
	public AWSCredentialsProvider getProvider(){
		if(awsCredential.checkcredential(key_id, secret_key)){
			System.out.println("passcheck");
        AWSCredentials credentials = new BasicAWSCredentials( key_id, secret_key );
        provider = new AWSStaticCredentialsProvider(credentials );
        return provider;
		}else{
			System.out.println("!@#$%^&*-- invalid awsCredentials");
			return null;
		}
	}
	public String toString(){
		return "id:"+key_id+";secret:"+secret_key+";region:"+region+";authen:"+ auth;
	}
	public AmazonEC2Client getEC2(){
		return new AmazonEC2Client(getProvider());
	}
	public AmazonS3Client getS3(){
		return new AmazonS3Client(getProvider());
	}
}
