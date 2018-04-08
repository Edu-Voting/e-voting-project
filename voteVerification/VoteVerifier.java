package voteVerification;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import controller.Controller;
import hash.IHash;


/*
 * Author : Arzum
 * Date : Jan 9,2016
 */
public class VoteVerifier {
	/*instance variables*/
	private int electionId;
	private multipartyComputation.DBHelper dbHelper;
	private VoteValidityResults results;
	private ArrayList<Vote> voteList;
	private ArrayList<AnonymizedVote> anonymizedVoteList;
	private IHash hashGenerator;
	private int digestSizeBit;
	private Controller controller;
/*
 * Check the validity of each encrypted vote saved to system. 
 * Make two pool for valid and invalid ones.
 * Number of valid and invalid votes are shown on bulletin board.
 * 
 * Two pools are created: (i)valid-vote pool, (ii)invalid-vote pool.
â—? System takes each recorded vote and tries to verifies its validity in this
step.
â—? For each vote form, first checks the hash of registration code: if it is fake,
verifies the signature of encrypted vote and then puts the encrypted vote
to invalid vote pool. If the registration code is not fake, again verifies
the signature and then puts that encrypted vote to valid vote pool.
â—? Recorded(signed) votes are removed from the system.
â—? Number of valid and invalid votes are determined and showed on
bulletin board.
 */
	/*constructor*/
	public VoteVerifier(Controller c){
		this.controller = c;

		this.electionId = controller.getPreparedElection().getElectionID();
		this.dbHelper = controller.getDbHelper();
		hashGenerator = controller.getHashGenerator();
		this.digestSizeBit = controller.getDIGESTION_SIZE();
		anonymizedVoteList = new ArrayList<AnonymizedVote>();
	}
	
	
	

	public void setVoteList(ArrayList<Vote> voteList) {
		this.voteList = voteList;
	}


	/*
	 * this method returns back the number of valid votes in anonymized votes pool
	 * */
	public int getCountofValidVotes(){
		int count = results.getNumOfvalidVotes();
		return count;
	}
	
	/*
	 * this method returns back the number of invalid votes in anonymized votes pool
	 * */
    public int getCountofInvalidVotes(){
    	int count = results.getNumOfInvalidVotes();
		return count;
	}
    
    /*
	 * this method returns nothing,but sets valid invalid count numbers
	 * 
	 * */
    public void validateVotes(){
    	results = dbHelper.countValidInvalidVotes(electionId);
    }
     /*
     * Input: voterid:int
     * returns : hash of registration code given voter.
     */
    private String getHashRegCodeHash(int voterid){
		String code = dbHelper.getHashRegCode(voterid);
    	return code;
    	
    }

    /*
     * Input: voterid:int
     * returns : hash of coercion code given voter.
     */
    private String getCoercionCodeHash(int voterid){
		
    	String code = dbHelper.getHashCoercionCode(voterid);
    	return code;
    	
    }
    
    /*
     * Input: voteID: hash of registration code
     * returns signed encrypted vote from db
     */
    private String getSignedEncryptedVote(String voteID){
    	String signedEVote = dbHelper.getSignedEVote(voteID);
    	return signedEVote;
    }

    /*
     * Input: voteID: hash of registration code
     * returns hash of concatination of encrypted vote, signed encrypted vote and hash of registration code from db
     */
    private String getSignedConcatanatedVote(String voteID){
		String lastHashedForm = dbHelper.getHashE_SE_HregCode(voteID);
    	return lastHashedForm;
    }

    /*
     * Input: none
     * It checks validity of all votes, and save all encrypted votes by anonymizing them
     */
    public boolean verifyAllVotes(){
    	
    	boolean isVerificationDone= false;
    	String  encryptedVote=null, signedEncryptedVoteH = null, signedEV = null;
    	String hashE_SE_HregCode = "", hashE_SE_HregCodeDB="", e_SE_HregCode="";
    	int validityFlag = -1;
    	AnonymizedVote  avote=null;
    	
    	if(voteList.size()>0){
    		
    		for(Vote v: voteList){
    			
    	    	
    	    	//signed Encryped <= encrypted+sign+hash(regcode) from db
    			signedEncryptedVoteH = getSignedEncryptedVote(v.getVoteID());
    	    	
    			encryptedVote = getEncryptedVote(signedEncryptedVoteH);
    			
    			signedEV = getSEncryptedVote(signedEncryptedVoteH);
    			
    			e_SE_HregCode = signedEV + dbHelper.getHashRegCode(v.getVoterid());
    			
    			// get db from HashE_SE_HregCode
    	    	hashE_SE_HregCodeDB = getSignedConcatanatedVote(v.getVoteID());
    	    
    			hashE_SE_HregCode= hashGenerator.sha3(e_SE_HregCode, digestSizeBit);
    	    	/* compare calculated and retrieved one. if they equals  validity flag = 1,  else validity flag = 0; */
    	    	if(hashE_SE_HregCode.equals(hashE_SE_HregCodeDB))
    	    		validityFlag = 1;
    	    	else
    	    		validityFlag = 0;
    	    	
    	    	avote = new AnonymizedVote(encryptedVote, validityFlag, electionId);
    	    	anonymizedVoteList.add(avote);
    		}
    		if(saveAnonymizedVotes()){
    			isVerificationDone= true;
    		}
    	    
    	}
    	return isVerificationDone;
    }
    
 


	/*
     * Save anonymized votes into the db
     */
    public boolean saveAnonymizedVotes(){
    	if(anonymizedVoteList.size()>0){
    		for(AnonymizedVote v: anonymizedVoteList){
    			if(!dbHelper.insertAnonymizedVote(v))
    				return false;
    			System.out.println(v);
    		}
    	}
		return true;
    }
    
    /*
     * sets anonymized vote list
     */
    public void setAVoteList(ArrayList<AnonymizedVote> voteList){
    	this.anonymizedVoteList = voteList;
    }
    
    private String getEncryptedVote(String ev_SEv_HregCode){
    	String eVote = ev_SEv_HregCode.substring(0, (ev_SEv_HregCode.length()-(96+128)));
    	return eVote;
    }
    
    private String getSEncryptedVote(String ev_SEv_HregCode) {
    	String signedEV = ev_SEv_HregCode.substring(0, (ev_SEv_HregCode.length()-128));
		return signedEV;
	}
}
