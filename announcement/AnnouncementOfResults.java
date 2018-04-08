package announcement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

/* Result details will be shown on bulletin board (Details will include the percentage of eligible voters, valid and invalid votes, 
   scores for each candidate etc.). System keys will be announced to public.*/

public class AnnouncementOfResults implements IAnnouncement{
	
	private String resultPath = null;
	private String privateKeyPath = null;
	private String publicKeyPath = null;
	private int numOfEligibleVoters = -1;
	private int numOfValidVotes = -1;
	private int numOfInvalidVotes = -1;
	private int numOfCandidate = -1;
	private Map<String,String> publicKeyMap = null;
	private Map<String,String> privateKeyMap = null;
	private Map<String,String> scoreMap = null;
	
	
	
	public AnnouncementOfResults(String resultPath, String privateKeyPath, String publicKeyPath) {
		this.resultPath = resultPath;
		this.publicKeyPath = publicKeyPath;
		this.privateKeyPath = privateKeyPath;
		pullElectionResults();
		pullSystemPublicKey();
		pullSystemPrivateKey();
		
	}

	private void pullElectionResults()
	{
		FileParser parser = new FileParser(resultPath);
		
		LinkedHashMap<String,String> map = parser.parseResultFile();
		scoreMap = new HashMap<String,String>();
		
		int size = map.size();
		Set<String> keySet = map.keySet();
		Object[] keyArray = keySet.toArray();
		
		for(int i = 0; i < size; i++)
		{
			
			String value = (new ArrayList<String>(map.values())).get(i);
			value = value.trim();
			
			if(i < 2)
			{
				if(i == 0)
				{
					numOfValidVotes = Integer.parseInt(value);
					System.out.println("number of valid votes: " + numOfValidVotes);
				}
				else
				{
					numOfCandidate = Integer.parseInt(value);
					System.out.println("number of candidates: " + numOfCandidate);
				}
			}
			else
			{
				scoreMap.put(keyArray[i].toString(), value);
				System.out.println("scoreMap: " + scoreMap.toString());
			}
		}
	}
	
	private void pullSystemPublicKey()
	{
		PublicKeyFileParser puParser = new PublicKeyFileParser(publicKeyPath);
		LinkedHashMap<String,String> map = puParser.parsePublicKeyFile();
		
		int size = map.size();
		Set<String> keySet = map.keySet();
		Object[] keyArray = keySet.toArray();
		
		for(int i = 0; i < size; i++)
		{
			
			String value = (new ArrayList<String>(map.values())).get(i);
			publicKeyMap.put(keyArray[i].toString(), value);
		}
	}
	
	private void pullSystemPrivateKey()
	{
		PrivateKeyFileParser prParser = new PrivateKeyFileParser(privateKeyPath);
		
		LinkedHashMap<String,String> map = prParser.parsePrivateKeyFile();
		
		int size = map.size();
		Set<String> keySet = map.keySet();
		Object[] keyArray = keySet.toArray();
		
		for(int i = 0; i < size; i++)
		{
			
			String value = (new ArrayList<String>(map.values())).get(i);
			privateKeyMap.put(keyArray[i].toString(), value);
		}
		
	}

	public int getNumOfEligibleVoters()
	{
	//	VoterRegistrationManager vrm = new VoterRegistrationManager();
	//	numOfEligibleVoters = vrm.getNumberOfEligibleVoters();
		return numOfEligibleVoters;
	}
	
	public int getNumOfValidVotes()
	{
		return numOfValidVotes;
	}

	
	public int getNumOfInvalidVotes()
	{
		// arzumdan çekilecek
		return numOfInvalidVotes;
	}
	
	public Map<String,String> getScores()
	{
		return scoreMap;
	}
	
	public Map<String,String> getSystemPrivateKey()
	{
		return privateKeyMap;
	}
	
	public Map<String,String> getSystemPublicKey()
	{
		return publicKeyMap;
	}
	
}
