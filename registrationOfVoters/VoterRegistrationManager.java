package registrationOfVoters;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import EccSign.CheckSign;
import EccSign.IcheckSign;
import EccSign.Isign;
import EccSign.Point;
import EccSign.Sign;
import controller.Controller;
import hash.IHash;
import helper.CodeGenerator;
import multipartyComputation.CertificateManager;
import multipartyComputation.DBHelper;
import systemCreation.Election;

/*
 * Author :Arzum
 * Date : November 27, 2016
 */

public class VoterRegistrationManager{
	/*
	 * This class is responsible for registration of the voter into the the system as registered voter
	 * Voter information already stored on db.
	 * 
	 * Registration is accepted via forms which are filled on Kiosk. Registration code and fake registration
		code are generated for each registered user and shared with them. Hash value of them are kept in the
		system. Number of eligible voters are determined.
	 */

	/*instance variables*/
	private Voter voter;
	private Election election;
	
	private CodeGenerator codeGenerator;
	private Controller controller = null;
	private CertificateManager certificateManager= null;
	private SimpleDateFormat sdf = null;
	private DBHelper dbHelper;
	private IHash hashGenerator = null;
	private int DIGESTION_SIZE;
	
	private BigInteger a = BigInteger.valueOf(-3);
    private BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
	
	/*constructor*/
	public VoterRegistrationManager(Controller c, Election election, String certficateFolderPath) throws Exception {
		
		/*those values will come from GUI*/
		this.voter = new Voter("NA", "NA", null, "NA");
		this.election = election;
		
		this.codeGenerator = new CodeGenerator();
		this.controller = c;
		this.certificateManager = controller.getCertificateManager();
		this.sdf = controller.getSdf();
		this.dbHelper = controller.getDbHelper();
		this.hashGenerator = controller.getHashGenerator();
		this.DIGESTION_SIZE = controller.getDIGESTION_SIZE();
	}
	
	/*
	 * Step1 : Check if the registration time valid from DB
	 */
	public boolean isRegistrationAvailable(){
		String currentDate = sdf.format(Calendar.getInstance().getTime());
		
		if((sdf.format(election.getRegStartTime()).compareTo(currentDate)<= 0)&&
		   (sdf.format(election.getRegEndTime()).compareTo(currentDate)>= 0))
			return true;
		
		return false;
	}
	
	public void setVoterInfo(String vname, String email, Date birthdate,
			String certificateName){
		voter.setVname(vname);
		voter.setEmail(email);
		voter.setBirthdate(birthdate);
		voter.setCertificateName(certificateName);
	}
	
	/*Step 2:Check the voter information from Voter table in the DB
	 * 
	 */
	public boolean isVoterValid(){
		Voter v = dbHelper.getVoter(voter.getEmail());
		
		if(v==null){
			return false;
		}
		System.out.println(v);
		
		Date vbirthDate = v.getBirthdate();

		System.out.println(voter.getVname());
		System.out.println(v.getVname());
		System.out.println(sdf.format(voter.getBirthdate()));
		System.out.println(sdf.format(vbirthDate));
		System.out.println(voter.getCertificateName());
		System.out.println(v.getCertificateName());
		System.out.println(voter.getEmail());
		System.out.println(v.getEmail());
		
		if(voter.getVname().equalsIgnoreCase(v.getVname())
				//&& ((sdf.format(voter.getBirthdate()).compareTo(sdf.format(vbirthDate))== 0)
				&& voter.getCertificateName().equals(v.getCertificateName())
				&& voter.getEmail().equals(v.getEmail())){
			
			voter.setVid(v.getVid());
			System.out.println("Voter id in voter valid function :"+ voter.getVid());
			return true;
		}
		return false;
	}
	
	
	/*
	 * Step 3 : If voter is saved in Voter db before,
	 *          then, generate  reg code, coercion code and save her as registered voter.
	 *          
	 *           */
	public RegisteredVoter registerVoter(String signedForm){
		String regCode = generateRegistrationCode();
		String coercionCode = generateCoercionCode();
		
		System.out.println("reg code:"+ regCode);
		System.out.println("coercion code"+ coercionCode);

		RegisteredVoter regVoter = register(signedForm, regCode,coercionCode);
		Voter voter = dbHelper.getVoter(regVoter.getEmail());
		System.out.println("*************************");
		System.out.println("voter is null? "+ voter);
		System.out.println("voter is registered before"+ dbHelper.isRegisteredVoter(voter.getVid(), election.getElectionID()));
		if(voter!= null && !dbHelper.isRegisteredVoter(voter.getVid(), election.getElectionID()))
		{
			//allow only the voters resided in voter table
			// do not allow record repetition
			dbHelper.insertRegisteredVoter(regVoter, election.getElectionID());
			System.out.println("registerVoter: saved!");
			JOptionPane.showMessageDialog(null, "Registration Code : "+regCode +"\n Coercion Code :"+coercionCode);
		}
		else{
			System.out.println("not saved to RegisteredVoter table !");
			JOptionPane.showMessageDialog(null, "not saved to RegisteredVoter table !");
		}
		return regVoter;
	}
	
	 private String generateRegistrationCode(){
		 String regCode = codeGenerator.generateACode();
		
		 return regCode;
		 
	 }
	 
	 private String generateCoercionCode(){
		 String coercionCode = codeGenerator.generateACode();
			
		 return coercionCode;
	 }
	
	 private RegisteredVoter register(String signedForm, String regCode, String coercionCode){
		 String form = voter.getEmail()+ voter.getVname()+ voter.getBirthdate()+voter.getCertificateName();
		 RegisteredVoter regVoter = null;
		 
		 System.out.println("Arzum form: " + form);
		 System.out.println("Arzum signedform: " + signedForm);
		 
		 
		 Point publicKey = getPublicKeyOfVoter();
		 IcheckSign signChecker = new CheckSign(publicKey);	//burada voter'ýn public key'i alýnmalý
		 if(signChecker.checkSignature(form, signedForm)){
			 String hashOfRegCode = hashGenerator.sha3(regCode, DIGESTION_SIZE);		 
			 String hashOfCoercionCode =hashGenerator.sha3(coercionCode, DIGESTION_SIZE);
			 
			 regVoter = new RegisteredVoter(voter,hashOfRegCode,hashOfCoercionCode);	 
		 }
		 
		 return regVoter;
	 }
	 
	 private Point getPublicKeyOfVoter(){
			//BigInteger x = new BigInteger(certificateReader.getPublicKey1());
			//BigInteger y = new BigInteger(certificateReader.getPublicKey2());
			certificateManager.getCertificateReader().readCertificate(voter.getCertificateName());
			
			BigInteger puX = new BigInteger(certificateManager.getCertificateReader().getPublicKey1());
			BigInteger puY = new BigInteger(certificateManager.getCertificateReader().getPublicKey2());
			
	        //BigInteger puX = new BigInteger("3732162847956594631894809545156809986099174278479973708403");
	        //BigInteger puY = new BigInteger("2422914999487124430251907609628717839056606892746559091288");
	        Point pu = new Point(puX, puY, a, p);
	
			return pu;
		}
	 
 	 public boolean checkCertificate(String certificateName, String signatureName) {
		return certificateManager.checkCertificateOfVoter(election, voter, certificateName, signatureName);
	 }
		
 	 /*
	 public boolean checkVoterEmailValidity(){
		String email = voter.getEmail();
		 if(EmailValidator.isIyteEmailAddress(email)||EmailValidator.isIyteStudentEmailAddress(email))
			 
			 return true;
		 
		 return false;
	}
 	  */
	
	public int getNumberOfEligibleVoters(int eid){
		return dbHelper.countRegisteredVoters(eid) ;
	}
	 
	 public String signForm() throws Exception{
		 voter.setVid(dbHelper.getVoter(voter.getEmail()).getVid());
		 BigInteger vSecret = dbHelper.getVoterPrivateKey(voter.getVid());
		 
		 System.out.println("Voter id:"+ voter.getVid()+" secret key :"+ vSecret);
		 String form = voter.getEmail()+ voter.getVname()+ voter.getBirthdate()+voter.getCertificateName();
 
		 // sign with his secret
		 Isign signer = new Sign(vSecret);
		 String signedForm = signer.signingMessage(form);
		 return signedForm;
	 }

	@Override
	public String toString() {
		return "VoterRegistrationManager [voter=" + voter.getVid()+" "+voter.getEmail() + ", election="
				+ election + "]";
	}

	public Election getElection() {
		return election;
	}

	public void setElection(Election election) {
		this.election = election;
	}
}