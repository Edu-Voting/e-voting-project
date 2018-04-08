package systemCreation;

import java.util.Date;

public class Election {

	private int electionID;
	private String electionTitle;
	private int isActive;
	private Date regStartTime;
	private Date regEndTime;
	private Date electionStartTime;
	private Date electionEndTime;
	private String hashOfPrKey;
	
	public Election(String electionTitle, int isActive, Date regStartTime, Date regEndTime, Date electionStartTime, Date electionEndTime, String hashOfPrKey) {
		this.electionTitle = electionTitle;
		this.isActive = isActive;
		this.regStartTime = regStartTime;
		this.regEndTime = regEndTime;
		this.electionStartTime = electionStartTime;
		this.electionEndTime = electionEndTime;
		this.hashOfPrKey = hashOfPrKey;
	}
	
	
	public Election(int electionID, String electionTitle, int isActive, Date regStartTime, Date regEndTime, Date electionStartTime, Date electionEndTime, String hashOfPrKey) {
		this.electionID = electionID;
		this.electionTitle = electionTitle;
		this.isActive = isActive;
		this.regStartTime = regStartTime;
		this.regEndTime = regEndTime;
		this.electionStartTime = electionStartTime;
		this.electionEndTime = electionEndTime;
		this.hashOfPrKey = hashOfPrKey;
	}

	public int getElectionID() {
		return electionID;
	}

	public void setElectionID(int electionID) {
		this.electionID = electionID;
	}

	public Date getRegStartTime() {
		return regStartTime;
	}

	public void setRegStartTime(Date regStartTime) {
		this.regStartTime = regStartTime;
	}

	public Date getRegEndTime() {
		return regEndTime;
	}

	public void setRegEndTime(Date regEndTime) {
		this.regEndTime = regEndTime;
	}

	public Date getElectionStartTime() {
		return electionStartTime;
	}

	public void setElectionStartTime(Date electionStartTime) {
		this.electionStartTime = electionStartTime;
	}

	public Date getElectionEndTime() {
		return electionEndTime;
	}

	public void setElectionEndTime(Date electionEndTime) {
		this.electionEndTime = electionEndTime;
	}

	public int isActive() {
		return isActive;
	}

	public void setActive(int isActive) {
		this.isActive = isActive;
	}

	public String getHashOfPrKey() {
		return hashOfPrKey;
	}

	public void setHashOfPrKey(String hashOfPrKey) {
		this.hashOfPrKey = hashOfPrKey;
	}

	public String getElectionTitle() {
		return electionTitle;
	}

	public void setElectionTitle(String electionTitle) {
		this.electionTitle = electionTitle;
	}
}
