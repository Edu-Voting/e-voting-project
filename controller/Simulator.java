package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Simulator {
	
	//private static Controller controller;
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	public boolean control() throws ParseException{
		
		String electionStartDateString = "25-02-2017 08:00:00";
		String electionEndDateString = "25-02-2017 08:00:01";
		
		Date electionStartTime = sdf.parse(electionStartDateString);
		Date electionEndTime = sdf.parse(electionEndDateString);
		
		//return (sdf.format(electionStartTime).compareTo(sdf.format(electionEndTime)) < 0) ;
		
		return (electionStartTime.compareTo(electionEndTime) <0 );
		
	}

	
	public static void main(String[] args) throws Exception{
		
		
		//controller = Controller.getInstance();
		
		/*
		String electionTitle = "IYTE";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"); //Gui olmadýðý için!! Sonra silinecek.
		String regStartDateString = "15-01-2017 08:00:00";
		String regEndDateString = "25-01-2017 00:00:00";
		
		
		Date regStartTime = sdf.parse(regStartDateString);
		Date  regEndTime = sdf.parse(regEndDateString);
	
		
		String electionStartDateString = "25-02-2017 08:00:00";
		String electionEndDateString = "26-02-2017 17:00:00";
		
		Date electionStartTime = sdf.parse(electionStartDateString);
		Date electionEndTime = sdf.parse(electionEndDateString);
		
		ElectionManager electionManager = controller.getElectionManager(); 
		electionManager.generateElection(electionTitle, regStartTime, regEndTime, electionStartTime, electionEndTime);
		
		VoterRegistrationManager voterRegistrationManager = controller.getVoterRegistrationManager();
		*/
		
		
		/*
		int electionID = 2;
		
		//specifies specific dates for the election
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date bdate1 = null,bdate2 = null,bdate3 = null,bdate4 = null;
               
		try {
			bdate1 = sdf.parse("1-1-1993");
			bdate2 = sdf.parse("15-1-1995");
	    	bdate3 = sdf.parse("17-1-1991");
	    	bdate4 = sdf.parse("20-1-1992");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		//create candidates
		Candidate c5 = new Candidate("Candidate5","Computer Engineer",bdate1);
		Candidate c6= new Candidate("Candidate6","Chemical Engineer",bdate2);
		Candidate c7 = new Candidate("Candidate7","BioEngineer",bdate3);
		Candidate c8 = new Candidate("Candidate8","Physicist",bdate4);
		Candidate c9 = new Candidate("Candidate9","BioEngineer",bdate3);
		Candidate c10 = new Candidate("Candidate10","Physicist",bdate4);
		
		PreparationForVotingManager pvotingManager = new PreparationForVotingManager(controller,electionID);
		
		pvotingManager.addCandidate(c5);
		pvotingManager.addCandidate(c6);
		pvotingManager.addCandidate(c7);
		pvotingManager.addCandidate(c8);
		pvotingManager.addCandidate(c9);
		pvotingManager.addCandidate(c10);
		
		pvotingManager.saveCandidates();

		*/
		
		
		//controller.preperateForVoting(electionID);
		
		Simulator s = new Simulator();
		System.out.println(s.control());
	}
}
