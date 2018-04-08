package voteVerification;
/*
 * Author :Arzum
 * Date: Jan. 09, 2016
 * 
 */
public class AnonymizedVote {
	/*instance variables*/
	private int avoteID;
	private String evote;
	private int validityFlag;
	private int electionId;
	
	// constructor1
	public AnonymizedVote(int avoteID, String evote, int validityFlag,int electionId) {
		this.avoteID = avoteID;
		this.evote = evote;
		this.validityFlag = validityFlag;
		this.electionId = electionId;
	}

	// constructor2
	public AnonymizedVote(String evote, int validityFlag, int electionId) {
		this.evote = evote;
		this.validityFlag = validityFlag;
		this.electionId = electionId;
	}

	//getters and setters
	public int getAvoteID() {
		return avoteID;
	}


	public String getEvote() {
		return evote;
	}


	public void setEvote(String evote) {
		this.evote = evote;
	}


	public int getValidityFlag() {
		return validityFlag;
	}


	public void setValidityFlag(int validityFlag) {
		this.validityFlag = validityFlag;
	}


	public int getElectionId() {
		return electionId;
	}


	public void setElectionId(int electionId) {
		this.electionId = electionId;
	}


	//toString
	@Override
	public String toString() {
		return "AnonymizedVote [avoteID=" + avoteID + ", evote=" + evote
				+ ", validityFlag=" + validityFlag + ", electionId="
				+ electionId + "]";
	}
	
	
	
	
}
