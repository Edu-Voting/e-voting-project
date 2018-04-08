package voteVerification;


import hash.Hash;
import multipartyComputation.DBHelper;
/*
 * Author: Arzum
 * Date : January 9,2016
 * Just demonstration for driving voterVerifier class
 */
public class VoterVerifierTest {

	public static void main(String[] args) {
		
		int electionID = 4;
		DBHelper dbHelper = new DBHelper();
		Hash hashgenerator = new Hash();
		
		//db credentials
		String userName = "dblab";
		String password = "123456";
		
		//--if(dbHelper.openConnection(userName, password)!= -1){
			
			//VoteVerifier verifier = new VoteVerifier(electionID, dbHelper, hashgenerator);
			
			/*vote validation
			verifier.validateVotes();
			System.out.println("valid counts: "+verifier.getCountofValidVotes());
			System.out.println("invalid counts: "+verifier.getCountofInvalidVotes());
			*/
			
			/*get voter regcode and coercioncode
			
			System.out.println("regCodehash:"+dbHelper.getHashRegCode(3));
			System.out.println("coercionCodehash:"+dbHelper.getHashCoercionCode(3));
			*/
			
			/*get hash(encrypted||signedEncryted||hashregcode)
			System.out.println(dbHelper.getHashE_SE_HregCode(1));
			*/
			
			/*get signedEncyptedVote
			System.out.println(dbHelper.getSignedEVote(1));
			*/
			
			/*get anonymised vote list
			System.out.println(dbHelper.getAnonymizedVoteList(4));
			*/
			
			/*save anonymized vote list
			ArrayList<AnonymizedVote> avoteList = new ArrayList<AnonymizedVote>();
			AnonymizedVote v1 = new AnonymizedVote("237221873129", 0, 4);
			AnonymizedVote v2 = new AnonymizedVote("631828381319", 1, 4);
			AnonymizedVote v3 = new AnonymizedVote("958394895836", 1, 4);
			AnonymizedVote v4 = new AnonymizedVote("134939324788", 1, 5);
			AnonymizedVote v5 = new AnonymizedVote("876723678231", 0, 4);
			AnonymizedVote v6 = new AnonymizedVote("172381302320", 0, 4);
			
			avoteList.add(v1);
			avoteList.add(v2);
			avoteList.add(v3);
			avoteList.add(v4);
			avoteList.add(v5);
			avoteList.add(v6);
		
			verifier.setAVoteList(avoteList);
			verifier.saveAnonymizedVotes();
			*/
			
			
		//--}
		

	}

}
