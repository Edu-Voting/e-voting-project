package systemCreation;

import java.util.ArrayList;
import java.util.Date;

import controller.Controller;
import multipartyComputation.DBHelper;

public class ElectionManager {

	private Controller controller=null;
	private DBHelper dbHelper=null;
	private Election currentElection=null;
	
	
	public ElectionManager(Controller c) throws Exception{
		controller = c;
		this.dbHelper = controller.getDbHelper();
	}

	
	public void generateElection(String electionTitle, Date regStartTime, 
							Date regEndTime, Date electionStartTime, Date electionEndTime) throws Exception{
		
		if(electionTitle!=null && regStartTime!=null && regEndTime!=null && electionStartTime!=null && electionEndTime!=null){
			if( (regStartTime.compareTo(regEndTime)<0) && (regEndTime.compareTo(electionStartTime)<0) && (electionStartTime.compareTo(electionEndTime)<0))
			{
				Election election = new Election(electionTitle, 0, regStartTime, regEndTime, electionStartTime, electionEndTime, null);
				if(dbHelper.registerElection(election)){
					currentElection = election;
					int maxEID = dbHelper.getMaxElectionID();
					currentElection.setElectionID(maxEID);
				}
			}
			else{
				System.out.println("Registration and election times should be compatible!");
			}
		}
		else{
			System.out.println("Please fill all fields in Admin panel!");
		}
	}
	
	public Election getActiveElection() {
		return dbHelper.getActiveElection();
	}


	public Election getCurrentElection() {
		return currentElection;
	}

	public ArrayList<Election> getElectionsIfActiveRegistration(){
		return dbHelper.getElectionsIfActiveRegistration();
	}
	
	public Election getElectionByIDFromDB(int eID){
		return dbHelper.getElectionByID(eID);
	}
	
	public boolean updateElectionHashOfPr(int eID, String hash_prKey){
		return dbHelper.updateElectionHashOfPr(eID, hash_prKey);
	}
	
	public boolean updateElectionStatusToDB(int eID, int isActive){
		return dbHelper.updateElectionStatus(eID, isActive);
	}
	
	public int getElectionIDByTitleFromDB(String electionTitle){
		return dbHelper.getElectionIDByTitle(electionTitle);
	}

}
