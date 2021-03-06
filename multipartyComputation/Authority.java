package multipartyComputation;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import Ecc.ECCDecryption;
import Ecc.ECCEncryption;
import Ecc.ECPoint;
import Ecc.EllipticCurve;
import Ecc.PrivateKey;
import Ecc.PublicKey;
import controller.Controller;
import helper.CertificateReader;
import systemCreation.Election;

/*
 * Author : Leyla
 * Date   : January 05, 2017
 */

public class Authority {
	
	private int authorityID;
	private int authorityCode;
	private String authorityName;
	private String certificateName;
	private String sharePath;
	private int electionID;
	
	private AuthorityManager authorityManager;
	private ArrayList<Share> shareList;
	
	private Controller controller;
	private EllipticCurve curve;
	private CertificateManager certificateManager;
	private CertificateReader certificateReader;
	private DBHelper dbHelper;
	
	public Authority(int authorityID, int authorityCode, String authorityName, String certificateName, int electionID, String sharePath) throws Exception {
		this.authorityID = authorityID;
		this.authorityCode = authorityCode;
		this.authorityName = authorityName;
		this.certificateName = certificateName;
		this.sharePath = sharePath;
		this.electionID = electionID;
		
		this.authorityManager = null;
		this.shareList = new ArrayList<>();
	}
	
	public void calculateSecret() throws Exception
	{
		System.out.println("calculateSecret: " + authorityID);
		if(authorityManager != null && controller!=null && dbHelper!=null && certificateManager!=null && certificateReader!=null){
			ArrayList<Authority> authorityList = authorityManager.getAuthorityList();
			int n = authorityList.size();
			ArrayList<Integer> idList = new ArrayList<Integer>();
			Random random = new Random();
			
			boolean flag = true;
			while(flag){
				int id = random.nextInt(n) + 1;
				if(idList.size() != n-1){
					if(id != authorityID && !idList.contains(Integer.valueOf(id))){
						Authority authority = authorityList.get(id-1);
						Share share = getShare();
						
						if(share != null){
							Election election = dbHelper.getElectionByID(electionID);
							if(certificateManager.checkCertificate(election, authority.getCertificateName(), authority.getCertificateName())){
								certificateReader.readCertificate(authority.getCertificateName());
								System.out.println("Authority Certificate: " + authority.getCertificateName());
								
								PublicKey publicKey = getPublicKeyOfAuthority();
								if(publicKey != null){
									BigInteger value = share.getValue();
									byte[] valueBytes = value.toByteArray(); 
									byte[] encValueBytes = ECCEncryption.encrypt(valueBytes, publicKey);
									EncryptedShare encShare = new EncryptedShare(authorityID, encValueBytes);
									authority.addShare(encShare);
									idList.add(id);
								}
							}
						}
					}	
				}
				else{
					flag = false;
				}
			} 	
		}
	}
	
	
	private void addShare(EncryptedShare encShare) throws Exception
	{
		if(encShare!=null && authorityManager!= null && controller!=null && dbHelper!=null){		
			BigInteger kOfPr = dbHelper.getAuthorityPrivateKey(this.authorityCode);
			PrivateKey privateKey = new PrivateKey(curve, kOfPr);
			byte[] encValueBytes = encShare.getEncValueBytes();
			byte[] valueBytes = ECCDecryption.decrypt(encValueBytes, privateKey);
			BigInteger value = new BigInteger(valueBytes);
			Share share = new Share(encShare.getId(), value);

			ShamirSecretSharing shamir = authorityManager.getShamir();
			if(shamir != null){
				shareList.add(share);
				System.out.println("Authority:" + this.authorityID + " and other authority:" + share.getId());
				
				int k = shamir.getK();
				if(shareList.size() == k){
					Share[] shares = new Share[k];
					shareList.toArray(shares);
					BigInteger secret = shamir.reconstruct(shares);
					System.out.println("Authority id:" + this.authorityID  + " code:" + this.authorityCode + " name:" + this.authorityName + " calculates system secret key:" + secret);
					authorityManager.setSecretOfAuthorities(secret);
				}	
			}
		}
	}

	public void setShare(Share share){
		if(share != null){
			SecretFileHelper.saveBigIntegerToFile(sharePath, share.getValue());
			share = null;
		}
	}
	
	private Share getShare(){
		Share share = null;
		
		File file = new File(sharePath);
		if(file.exists()){
			BigInteger value = SecretFileHelper.readBigIntegerFromFile(sharePath);
			share = new Share(authorityID, value);	
		}
		return share;
	}
	
	private PublicKey getPublicKeyOfAuthority(){
		BigInteger x = new BigInteger(certificateReader.getPublicKey1());
		BigInteger y = new BigInteger(certificateReader.getPublicKey2());
		ECPoint ecPoint = new ECPoint(x, y);
		PublicKey publicKey = new PublicKey(curve, ecPoint);	
		return publicKey;
	}
	
	public int getAuthorityID() {
		return authorityID;
	}

	public void setAuthorityID(int authorityID) {
		this.authorityID = authorityID;
	}
	
	public int getAuthorityCode() {
		return authorityCode;
	}

	public void setAuthorityCode(int authorityCode) {
		this.authorityCode = authorityCode;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public int getElectionID() {
		return electionID;
	}

	public void setElectionID(int electionID) {
		this.electionID = electionID;
	}

	public AuthorityManager getAuthorityManager() {
		return authorityManager;
	}

	public void setAuthorityManager(AuthorityManager authorityManager) {
		this.authorityManager = authorityManager;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller c) {
		if(c != null){
			this.controller = c;
			this.curve = controller.getCurve();
			this.certificateManager = controller.getCertificateManager();
			this.certificateReader = controller.getCertificateReader();
			this.dbHelper = controller.getDbHelper();	
		}
	}
}
