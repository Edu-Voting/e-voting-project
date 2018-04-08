package voteVerification;
/*
 * Author : Arzum
 * Date : January 9, 2016
 * 
 * This holds number of valid votes and invalid votes for touching db one time to count the results
 */
public class VoteValidityResults {
    /*instance variables*/
	private int numOfvalidVotes;
	private int numOfInvalidVotes;
	
	
	public VoteValidityResults(int numOfvalidVotes, int numOfInvalidVotes) {
		super();
		this.numOfvalidVotes = numOfvalidVotes;
		this.numOfInvalidVotes = numOfInvalidVotes;
	}


	public int getNumOfvalidVotes() {
		return numOfvalidVotes;
	}


	public int getNumOfInvalidVotes() {
		return numOfInvalidVotes;
	}
	
	
}
