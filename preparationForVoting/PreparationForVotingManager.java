package preparationForVoting;

import java.util.ArrayList;

import controller.Controller;



/*
 * Author :Arzum
 * Date : January 06, 2017
 */
public class PreparationForVotingManager {

	/*instance variables*/
	private CandidateManager candidateManager;
	private int eid;
	
	private Controller controller;
	
	// constructor
	public PreparationForVotingManager(Controller c, int eid) {
		controller = c;
		this.eid = eid;
		candidateManager = new CandidateManager(controller,eid);
	}
	
	public void addCandidate(Candidate c) throws Exception{
		candidateManager.addCandidate(c);
	}
	
	public void removeCandidate(Candidate c){
		candidateManager.removeCandidate(c);
	}

	public void saveCandidates() throws Exception{
		candidateManager.saveCandidatestoDB();
	}
	
	public Ballot generateBallot(){
		Ballot ballot= null;
		
		if(candidateManager.getCandidates().size()>0){
			
			ballot = new Ballot(candidateManager.getCandidates());
			
		}
		return ballot;
	}
	
	public BulletinBoard generateBulletinBoard(){
		BulletinBoard board = new BulletinBoard(controller.getVoterRegistrationManager().getNumberOfEligibleVoters(eid),candidateManager.getCandidates());
		return board;
	}
	
	public ArrayList<Candidate> getCandidateListFromDB(){
		return candidateManager.getCandidatesFromDB(eid);
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public CandidateManager getCandidateManager() {
		return candidateManager;
	}

	public boolean updateCandidateElectionId(int electionID) {
		// TODO Auto-generated method stub
		return candidateManager.updateCandidateElectionId(electionID);
	}
}
