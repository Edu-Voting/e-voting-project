package voteVerification;

public class Vote {
/*
 * Arzum
 */
	private String voteID;
	private int voterid;
	private String signedEncyptedVote;
	private String hash_regCode;
	
	
	public Vote(String voteID, int voterid, String signedEncyptedVote,
			String hash_regCode) {
		super();
		this.voteID = voteID;
		this.voterid = voterid;
		this.signedEncyptedVote = signedEncyptedVote;
		this.hash_regCode = hash_regCode;
	}


	public String getVoteID() {
		return voteID;
	}


	public void setVoteID(String voteID) {
		this.voteID = voteID;
	}


	public int getVoterid() {
		return voterid;
	}


	public void setVoterid(int voterid) {
		this.voterid = voterid;
	}


	public String getSignedEncyptedVote() {
		return signedEncyptedVote;
	}


	public void setSignedEncyptedVote(String signedEncyptedVote) {
		this.signedEncyptedVote = signedEncyptedVote;
	}


	public String getHash_regCode() {
		return hash_regCode;
	}


	public void setHash_regCode(String hash_regCode) {
		this.hash_regCode = hash_regCode;
	}
	
	
	
	
}
