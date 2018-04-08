package voting;

import controller.Controller;
import multipartyComputation.DBHelper;

public class QueryCastedVote {
	/*
	private String hashOfRegCode;
	private boolean isLoginSuccessful;
	private Controller c;
	private IVoting iVoting;
		
	public QueryCastedVote() throws Exception{
		
		c = Controller.getInstance();
		iVoting = c.vm;
	}



	private String getVote()
	{
		//Check if (isLoginSuccessful == true). If not, return null
		//DB'deki Vote tablosundan VoteID'si hashOfRegCode'a eþit olan satýr bulunur.
		//hash_seHregCode deðeri çekilip vote'a set edilir.
		//vote deðeri döndürülür.
			
		String vote = null;
		this.isLoginSuccessful = iVoting.isLoginSuccessful();
		if(isLoginSuccessful)
		{
			hashOfRegCode = iVoting.getHashOfRegCode();
			if(hashOfRegCode == null)
			{
				hashOfRegCode = iVoting.getHashOfFakeRegCode();
				// DB operation
			}
		}

		return vote;	
	}
	*/
	
	private String hashOfRegCode;
	private String hashOfSeHregCode;
	private boolean isLoginSuccessful;
	private Controller controller;
	private IVoting votingModule;
	private DBHelper dbHelper;
		
	public QueryCastedVote(Controller c) throws Exception{
		
		this.controller = c;
		votingModule = controller.getVotingModule();
		dbHelper = controller.getDbHelper();
	}

	public String getVote()
	{
		//Check if (isLoginSuccessful == true). If not, return null
		//DB'deki Vote tablosundan VoteID'si hashOfRegCode'a eþit olan satýr bulunur.
		//hash_seHregCode deðeri çekilip vote'a set edilir.
		//vote deðeri döndürülür.
			
		String vote = null;
		this.isLoginSuccessful = votingModule.isLoginSuccessful();
		if(isLoginSuccessful)
		{
			hashOfRegCode = votingModule.getHashOfRegCode();
			if(hashOfRegCode == null)
			{
				hashOfRegCode = votingModule.getHashOfFakeRegCode();
			}
			
			// DB operation
			hashOfSeHregCode = dbHelper.getHashOfSavedVoteForQuery(hashOfRegCode);
			vote = hashOfSeHregCode;
		}

		return vote;
	}
}