package announcement;

import java.util.Map;

public interface IAnnouncement {
	
	public Map<String,String> getSystemPublicKey();
	public Map<String,String> getSystemPrivateKey();
	public Map<String,String> getScores();
	public int getNumOfInvalidVotes();
	public int getNumOfValidVotes();
	public int getNumOfEligibleVoters();

}
