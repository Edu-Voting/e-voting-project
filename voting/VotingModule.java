package voting;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import EccSign.Isign;
import EccSign.Sign;
import Paillier.IVote;
import controller.Controller;
import hash.IHash;
import multipartyComputation.DBHelper;

//Election bitince isCompleted Ã½ true yap
public class VotingModule implements IVoting{
	
	private boolean isCodeFake;
	private int voterID;
	private String hashOfRegCode;
	private String hashOfFakeRegCode;
	private String encryptedVote;
	private String signedEncryptedVote;
	private int numOfCastedVotes = 0;
	
	private Controller controller;
	
	private IVote voteA;
	private IHash hash;
	private DBHelper dbHelper;

	
	private int digestSizeBits = 512;
	private String dbHashOfRegCode;
	private String dbHashOfFakeRegCode;
	private int validityFlag;
	private int coercionFlag;

	
	public VotingModule(Controller c) throws Exception{
		this.isCodeFake = false;
		this.hashOfRegCode = null;
		this.hashOfFakeRegCode = null;
		this.encryptedVote = null;
		this.voterID = -1;
		
		this.controller = c;
		this.dbHelper = controller.getDbHelper();
		this.hash = controller.getHashGenerator();
		this.voteA = controller.getVote();
	}


	private String getCastedVote() throws Exception {
		//GUI aracÃ½lÃ½Ã°Ã½yla Ballottan seÃ§ilmiÃ¾ olan Candidate Id alÃ½nacak
		//encrypt the vote by calling encrypt() method from paillierInterface
		//set encryptedVote
		//return encrypted vote
		
		//--int id = controller.getCandidateID();//get Candidate id
		int selectedCandidateID = controller.getSelectedCandidateID();
		BigInteger encryptedVote ;
		encryptedVote = 	voteA.vote(selectedCandidateID);
		System.out.println("encryp vote " + encryptedVote);
		return encryptedVote.toString();
	} 

	// GUI'den giriÃ¾ bilgileri alÃ½nacak
	@Override
	public boolean isLoginSuccessful() {
		//GUI'den girilmiÃ¾ olan email ve regCode alÃ½nÃ½r.
		//regCode'un hash deÃ°eri hesaplanÃ½r. H(RegCode) diyelim. hashOfRegCode = H(RegCode) yapÃ½lÃ½r.
		//Voter ve RegisteredVoter db tablolarÃ½ joinlenip, girilen emaile ait regCode'un ve fakeRegCode'un hash deÃ°erleri Ã§ekilir. AyrÃ½ca VoterId Ã§ekilip set edilir.
		//H(RegCode) ile bu Ã§ekilen deÃ°erler karÃ¾Ã½laÃ¾tÃ½rÃ½lÃ½r.
		//HErhangi birine eÃ¾itse result = true yapÃ½lÃ½r.
		//EÃ°er fake code girilmiÃ¾se isCodeFake = true yap.
		//Result deÃ°eri dÃ¶ndÃ¼rÃ¼lÃ¼r.
		
		boolean result;
		String email = controller.getEmail();
		
		String regCode = controller.getRegCode();
		
		JOptionPane.showMessageDialog(null, "regCode in isLoginSuccessfull: "+regCode);
		hashOfRegCode = hash.sha3(regCode, digestSizeBits);
			
		this.voterID = dbHelper.getVoterID(email);
		
		//DBden regCode ve hashRegCode Ã§ekilir set edilir.
		ArrayList<String> regCodes = dbHelper.getRegCodeAndFakeRegCodeOfVoter(email);
		dbHashOfRegCode = regCodes.get(0);
		dbHashOfFakeRegCode = regCodes.get(1);

		
		if(hashOfRegCode.equals(dbHashOfRegCode))
		{
			result = true;
			this.isCodeFake = false;
			System.out.println("hashOfRegCode.equals(dbHashOfRegCode)");
		}
		else if(hashOfRegCode.equals(dbHashOfFakeRegCode))
		{
			this.isCodeFake = true;
			this.hashOfFakeRegCode = hashOfRegCode; // control if it successfully assign
			hashOfRegCode = null;
			result = true;
			System.out.println("hashOfRegCode.equals(dbHashOfFakeRegCode)");
		}
		else
		{
			result = false;
		}
		
		return result;
	} 

	private boolean checkCastedVoteValidity()
	{
		//First, check validity flag
		//If it is zero, check isCodeFake.
		//If isCodeFake is true, then check coercion flag. 
		       //If the coercion flag is 1, then vote cannot be accepted. 
		       //If the coercion flag is 0, set coercionFlag = 1 (Ã�u iÃ¾lem saveVote'da yapÃ½lacak: and save the vote. Check if it is successfully saved, if not change the flag.)
		//If isCodeFake is false, then set the validityFlag = 1 (Ã�u iÃ¾lem saveVote'da yapÃ½lacak: and save the vote.)
		//(Ã�u iÃ¾lem saveVote'da yapÃ½lacak: Check if it is successfully saved, if not change the validity flag.)
		
		this.validityFlag = -1;
		this.coercionFlag = -1;
		ArrayList<Integer> flags = dbHelper.getRegisteredVoterFlags(voterID); 
		int dbValidityFlag = flags.get(0);
		int dbCoercionFlag = flags.get(1);
		
		if(dbValidityFlag == 0){
			System.out.println("dbValidityFlag == 0");
			if(isCodeFake == true){
				System.out.println("isCodeFake == true");
				if(dbCoercionFlag == 0){
					System.out.println("dbCoercionFlag == 0");
					coercionFlag = 1;
					return true;
				}
			}
			else{	//isCodeFake==false
				System.out.println("hahahah !! :)");
				validityFlag = 1;
				return true;
			}
		}
		
		return false;
	}
	
	private void signEncryptedVote() throws Exception
	{
		//VoterID'ye bakarak VoterCredentials db tablosundan private key Ã§ekilir.
		//encryptedVote bu key ile imzalanÃ½r. (Elliptic curve'Ã¼n interfacei kullanÃ½lÃ½r)
		//signedEncryptedVote set edilip dÃ¶ndÃ¼rÃ¼lÃ¼r.
		
		encryptedVote = getCastedVote();
		BigInteger voterPr = dbHelper.getVoterPrivateKey(voterID);
		System.out.println("voterPr: " + voterPr);
		Isign signer = new Sign(voterPr);
		System.out.println("encryptedVote: " + encryptedVote);
		signedEncryptedVote = signer.signingMessage(encryptedVote);
	}
	
	@Override
	public boolean saveVote() throws Exception
	{
		// enc + sign + hash(reg)
		// hash : 128
		// sign:96	:String olarak dÃ¼ÅŸÃ¼nÃ¼p lenght'i 96 bulunuyor.
		//enc?
		//concatenate'in length'i ??
		
		//check vote checkCastedVoteValidity
		//If it returns true, then call signEncryptedVote
		//Calculate hash value of signedEncryptedVote. H(signedEncryptedVote) diyelim.
		//DB'deki Vote tablosuna Ã¾u bilgileri ekle (save) : voterID, signedEncryptedVote, H(EV || Signature || HashOfRegCode or HashOfFakeRegCode).
		//If successfully saved: modify the flags, whose values are not equal to -1, of registered voters as parameters validityFlag and coercionFlag. 
		//Kaydedildiyse, numOfCastedVotes deÃ°erini 1 arttÃ½r ve true dÃ¶ndÃ¼r.
		//Kaydedilmediyse false dÃ¶ndÃ¼r ve checkCastedVoteValidity fonksiyonunda set edilen flagleri deÃ°iÃ¾tir:


		if(isLoginSuccessful()){
			if(checkCastedVoteValidity()){
				signEncryptedVote();
				
				String hashOfCode = hashOfRegCode;
				if(hashOfCode == null){
					hashOfCode = hashOfFakeRegCode;
				}
				
				System.out.println("hashOfCode: " + hashOfCode);
				
				
				String seHregCode = encryptedVote.concat(signedEncryptedVote).concat(hashOfCode);
				System.out.println("seHregCode :" + seHregCode);
				String hash_seHregCode = hash.sha3(seHregCode, digestSizeBits);
				
	
				if(dbHelper.registerVote(hashOfCode, voterID, seHregCode, hash_seHregCode)){
					if(validityFlag == 1){
						if(dbHelper.updateValidityFlag(voterID, 1)){
							numOfCastedVotes++;
							return true;
						}
					}
					else if(coercionFlag == 1){
						if(dbHelper.updateCoercionFlag(voterID, 1)){
							numOfCastedVotes++;
							return true;
						}
					}
				}
			}	else {
				JOptionPane.showMessageDialog(null, "checkCastedVoteValidity failed");
			}
		} else {
			JOptionPane.showMessageDialog(null, "isLoginSuccessful failed");
		}
		
		return false;
	}
	
	
	public int getNumberOfCastedVotes()
	{
		return numOfCastedVotes;
	}

	@Override
	public String getHashOfRegCode()
	{
		return this.hashOfRegCode;
	}
	
	@Override
	public String getHashOfFakeRegCode()
	{
		return this.hashOfFakeRegCode;
	}
}