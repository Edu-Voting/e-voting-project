package multipartyComputation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import controller.Controller;

/*
 * Author : Leyla
 * Date   : January 05, 2017
 */
public class AuthorityManager implements IAuthorityManager {
	
	private int electionID;
	private ArrayList<Authority> authorityList;
	private int k;
	private int n;
	private ShamirSecretSharing shamir;
	private boolean isShared;
	private BigInteger secretOfAuthorities;
	
	private Controller controller = null;
	private DBHelper dbHelper = null;
	private String secretPath;
		
	public AuthorityManager(Controller c, int electionID) throws Exception{
		this.electionID = electionID;
		this.authorityList = new ArrayList<>();
		this.controller = c;
		this.dbHelper = controller.getDbHelper();
		initialize();
	}
	
	private void initialize() throws Exception{
		setSecretPath(controller.getSecretPath());
		setAuthorityList(dbHelper.getAuthorities(electionID));
		n = authorityList.size();
		k = (int) Math.ceil((double) n/2);
		System.out.println("number of authorities: " + n);
		System.out.println("threshold: " + k);
		shamir = new ShamirSecretSharing(k, n);
		
		for(Authority authority : authorityList){
			authority.setAuthorityManager(this);
			authority.setController(controller);
		}
		
		isShared = false;
		secretOfAuthorities = null;
	}
	
	
	@Override
	public void shareSecret(){
		BigInteger secret = SecretFileHelper.readBigIntegerFromFile(secretPath);
		System.out.println("Secret read from private key file:" + secret);
		
		if(secret != null){
			Share[] shares = shamir.distribute(secret);
			for(int i=0;i<n;i++){
				Authority authority = authorityList.get(i);
				authority.setShare(shares[i]);
				System.out.println("Authority Code:" + authority.getAuthorityCode() + " and Share:" + shares[i]);
			}
			secret = null;
			SecretFileHelper.saveBigIntegerToFirstLine(secretPath, BigInteger.ZERO);
			
			isShared = true;
		}
	}
	

	@Override
	public void reconstructSecret() throws Exception{
		if(isShared == true){
			ArrayList<Integer> idList = new ArrayList<Integer>();
			Random random = new Random();

			while(idList.size() != n){
				int id = random.nextInt(n) + 1;

				if(!idList.contains(Integer.valueOf(id))){
					Authority authority = authorityList.get(id-1);
					authority.calculateSecret();
					idList.add(id);	
				}
			}
		}
	}
	
	
	public void setSecretOfAuthorities(BigInteger secret){
		if(secret != null){
			if(secretOfAuthorities == null){
				secretOfAuthorities = secret;
				SecretFileHelper.saveBigIntegerToFirstLine(secretPath, secretOfAuthorities);
			} else{
				if(secretOfAuthorities.compareTo(secret) != 0){
					System.out.println("Reset election!");
					controller.resetSystem();
				}
			}	
		}
	}
	
	
	public ArrayList<Authority> getAuthorityList() {
		return authorityList;
	}
	
	public void setAuthorityList(ArrayList<Authority> authorities){
		for(Authority authority : authorities){
			authorityList.add(authority);
		}
	}

	public ShamirSecretSharing getShamir() {
		return shamir;
	}

	public void setSecretPath(String secretPath){
		if(secretPath == null)
			throw new NullPointerException();
		this.secretPath = secretPath;
	}
}
