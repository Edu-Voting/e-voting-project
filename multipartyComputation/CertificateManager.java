package multipartyComputation;

import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import EccSign.CheckSign;
import EccSign.IcheckSign;
import EccSign.Isign;
import EccSign.Point;
import EccSign.Sign;
import helper.CertificateReader;
import registrationOfVoters.Voter;
import systemCreation.Election;

/*
 * Author : Leyla
 * Date   : January 08, 2017
 */
public class CertificateManager {

	private File certificateFolder;
	private File signatureFolder;
	private CertificateReader certificateReader;
	private SignatureReader signatureReader;
	private Isign signer;
	private IcheckSign signChecker;
	private SimpleDateFormat sdf;
	
	
	public CertificateManager(BigInteger privateKey, Point publicKey, String certificateFolderPath, String signatureFolderPath){
		certificateFolder = new File(certificateFolderPath);
		signatureFolder = new File(signatureFolderPath);
		certificateReader = new CertificateReader(certificateFolderPath);
		signatureReader = new SignatureReader(signatureFolderPath);
		signer = new Sign(privateKey);
		signChecker = new CheckSign(publicKey);
		sdf = new SimpleDateFormat("dd-MM-yyyy");
	}
	
	private String getCertificate(String certificateName){
		certificateReader.readCertificate(certificateName);
		ArrayList<String> certificateInfo = certificateReader.getCertificateInfo();
		
		String certificate="";
		for(int i=0;i<certificateInfo.size();i++){
			certificate += certificateInfo.get(i);
		}	
		return certificate;
	}
	
	public String signCertificate(String certificateName) throws Exception{
		String certificate = getCertificate(certificateName);
		System.out.println("Cerificate: " + certificate);		
		String signOfCertificate = signer.signingMessage(certificate);
		System.out.println("Signature:" + signOfCertificate);
		
		return signOfCertificate;	
	}
	

	public boolean checkCertificate(Election election, String certificateName, String signatureName) {
		// are certificate and signature in certificate folder in disk?
		if(checkForCertificateAndSignature(certificateFolder, signatureFolder, certificateName, signatureName)){
			String certificate = getCertificate(certificateName);
			String signature = signatureReader.readSignature(signatureName);
			//System.out.println(signature);
			if(signChecker.checkSignature(certificate, signature)){
				if(isCertificateValidTime(election, certificateReader.getCertificateStartDate(),certificateReader.getCertificateEndDate())){
					return true;	
				}
			}
		}
		return false;
	}
	
	
	public boolean checkCertificateOfVoter(Election election, Voter voter, String certificateName, String signatureName) {
		if(checkCertificate(election, certificateName, signatureName)){
			if(certificateReader.getCertificateUserName().trim().equalsIgnoreCase(voter.getVname())){
				return true;
			}
		}
		return false;
		
		//içindeki bilgilerle voterýn guiden girdiði bilgiler uyuþuyor mu?
		 // valid mi hala?
		//dosyadan okunacak
	 }
	 
	
	private boolean isCertificateValidTime(Election election, String sTime, String eTime){
		Date startTime = null,endTime = null;
  	  	try {
  	  		startTime = sdf.parse(sTime);
			endTime  = sdf.parse(eTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	
  	  	if((sdf.format(startTime).compareTo(sdf.format(election.getElectionStartTime()))<= 0)
  	  			&& (sdf.format(endTime)).compareTo(sdf.format(election.getRegEndTime()))>= 0)
  	  		return true;
  	 
  	  	return false;
	}
	
	
	private boolean checkFolder(final File folder, String fileName){
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				checkFolder(fileEntry,fileName);
	        } else {
	        	if(fileEntry.getName().equals(fileName))
	        		return true; 
	        }
	    }
		return false;
	}
	
	private boolean checkForCertificateAndSignature(final File certificateFolder, final File signatureFolder, String certificateName, String signatureName){
		if(checkFolder(certificateFolder, certificateName) && checkFolder(signatureFolder, signatureName)){
			return true;
		}
		return false;
	}

	public CertificateReader getCertificateReader() {
		return certificateReader;
	}

	public void setCertificateReader(CertificateReader certificateReader) {
		this.certificateReader = certificateReader;
	}
}
