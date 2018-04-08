package preparationForVoting;

import java.util.ArrayList;

import controller.Controller;
import multipartyComputation.DBHelper;


/*
 * Author :Arzum
 * Date : November 26, 2016
 * 
 * This class is responsible for doing operations related to candidates
 * Operations :
 * - add a candidate
 * - remove a candidate
 * - search for a candidate for a given candidate name
 * - search for a candidate for a given candidate id
 * - get candidate list
 * - save candidates into db
 */
public class CandidateManager {
	/*instance variables*/
	private ArrayList<Candidate> candidates;
	private static int cid; // candidate id
	private int electionID;
	
	private Controller controller=null;
	private DBHelper dbHelper=null;
	
	
	//constructor1
	public CandidateManager(Controller c, ArrayList<Candidate> candidates, int eid) throws Exception {
		controller = c;
		this.candidates = candidates;
		cid = 0;
		electionID = eid;
		dbHelper = controller.getDbHelper();
		
	}

	//constructor2
	public CandidateManager(Controller c, int eid){
		controller = c;
		dbHelper = controller.getDbHelper();
		candidates = new ArrayList<Candidate>();
		cid = 0;
		electionID = eid;
	}
	
	/*addCandidate
	 * input: a candidate
	 * return : none
	 * 
	 * it takes a candidate and replace the candidate into the candidate collection
	 * by giving a sequential candidate id 
	 */
	public void addCandidate(Candidate c) throws Exception{
		if(c!= null){
			c.setCid(cid);
			candidates.add(c);
			cid++;
			System.out.println("cid"+cid);
		}
	}
	
	/*findCandidate
	 * input: a candidate name
	 * return : a candidate
	 * 
	 * it takes the candidate name
	 * search for the candidate given name in the Candidate collection
	 * and if it founds, returns the candidate founded
	 */
	public Candidate findCandidate(String name){
		Candidate found = null;
		
		for(Candidate c:candidates){
			if(c.getName().equalsIgnoreCase(name))
				found = c;
		}
		
		return found;
	}
	
	/*findCandidate
	 * input: a candidate id
	 * return : a candidate
	 * 
	 * it takes the candidate 
	 * search for the candidate given name in the Candidate collection
	 * and if it founds, returns the candidate founded
	 */
	public Candidate findCandidate(int id){
		Candidate found = null;
		
		for(Candidate c:candidates){
			if(c.getCid()== id)
				found = c;
		}
		return found;
	}
	
	/* removeCandidate
	 * input: a candidate
	 * return : none
	 * 
	 * it takes a candidate 
	 * search for the candidate given in the Candidate collection
	 * and if it founds, removes the candidate founded
	 */
	public void removeCandidate(Candidate c){
		if(candidates.size()>0){
			candidates.remove(c);
		}
	}
	
	/* saveCandidatestoDB
	 * input: none
	 * return : none
	 * 
	 * it saves the candidate info in the Candidate collection into the db.
	 */
	public void saveCandidatestoDB() throws Exception{
		System.out.println("candidates.size()>=0" + candidates.size());
		if(candidates.size()>=0){
			for(Candidate c: candidates){
				System.out.println("Candidate:" + c);
				//if the candidate is not saved before for the same election
				System.out.println("!dbHelper.isRegisteredCandidate");
				if(!dbHelper.isRegisteredCandidate(electionID, c.getCid())){
					dbHelper.insertCandidate(c, electionID);
					System.out.println(c.getName()+" is saved to db successfully!");
				}
			}
		}
	}
	/* getCandidatesFromDB
	 * input: election id
	 * return : a Candidate collection
	 * 
	 * it retrieves the candidate info for a specific election(via election id,
	 * and stores them inside a Candidate collection.
	 */
	public ArrayList<Candidate> getCandidatesFromDB(int electionid){
		candidates.clear();
		
		candidates = dbHelper.getCandidates(electionid);
		return candidates;
	}

	public ArrayList<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(ArrayList<Candidate> candidates) {
		this.candidates = candidates;
	}

	public boolean updateCandidateElectionId(int electionID) {
		return dbHelper.updateCandidateElectionId(electionID);
		
	}

}
