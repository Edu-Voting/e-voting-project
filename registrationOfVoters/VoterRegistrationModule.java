/*package registrationOfVoters;

import java.util.Date;

import controller.Controller;
import systemCreation.Election;
import java.text.ParseException;
import java.text.SimpleDateFormat;



 * Author :Arzum
 * Date : Jan. 04, 2017
 * 
�? A voter, who wants to cast a ballot, fills the registration form on Kiosk
within defined time interval. Then, he signs hash value of the form and
send the signed form to system. (Identification process)
�? System first checks the certificates of the voter, if he hasn’t a valid
certificate in the system he cannot be registered. Otherwise, as a second
process, system verifies the signature. If he is verified then system
gives a unique, randomly generated registration code. System also gives
a unique, randomly generated fake registration code to voter for a
possible coercion case.
�? Hash value of each registration code and also the hash values of fake
registration codes will be kept in the system.
�? Number of eligible voters are designated.
 
public class VoterRegistrationModule {

	instance variables
	//private Election election;
	//private Voter voter;
	
	
	public static void main(String[] args) throws Exception{
		
		// Move them into  config file
		
		//--String dbUserName ="dblab";
		//--String dbPassword = "123456";
		
		
		   
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date regStart = null,regEnd = null,elStart = null,elEnd = null;
        Date bdate =  null;

        try {
        	regStart = sdf.parse("1-1-2017");
        	regEnd  = sdf.parse("15-1-2017");
        	elStart = sdf.parse("17-1-2017");
        	elEnd = sdf.parse("20-1-2017");
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Election election = new Election("IYTE-Election", 0, regStart, regEnd, elStart, elEnd, null);
		
		VoterRegistrationManager manager = new VoterRegistrationManager(election, "D:/CENG661_TermProject/Certificates/");
		RegisteredVoter regVoter;
		
		//--System.out.println("DB connection status :"+manager.connectDB(dbUserName, dbPassword));
			
		// check whether registration is available or not
		// if so
		
		
		if( manager.isRegistrationAvailable()){
			
			// lets assume the values obtained from GUI  -- filled the form 
			String email = "johndoe@iyte.edu.tr";
			String name = "John Doe";
		
			String certificateName = "johndoe.txt";
			  
			try {
				bdate = sdf.parse("1-1-1988");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// when REGISTER ME button is clicked----
			
			// set voter info
			manager.setVoterInfo(name, email, bdate, certificateName);
			
			if(!manager.checkVoterEmailValidity()){
				System.out.println("Not valid email"); // aslında bu e-mail checking işi GUIye kaydırılmalı!
			}
			
			//String signedForm = manager.signForm();
			String signedForm = "signedformtestdata";
			System.out.println("managerIsValid :"+manager.isVoterValid());
			if(manager.isVoterValid()){
				if(manager.checkCertificate(certificateName, certificateName)){
					System.out.println("saved!");
					//regVoter = manager.registerVoter(signedForm);
					
				}
			}
			
			System.out.println(manager.getNumberOfEligibleVoters());
			
			
		}else
			System.out.println("Registrations are closed!");
		
		
	}
}
*/