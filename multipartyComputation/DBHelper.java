package multipartyComputation;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import preparationForVoting.Candidate;
import registrationOfVoters.RegisteredVoter;
import registrationOfVoters.Voter;
import systemCreation.Election;
import voteVerification.AnonymizedVote;
import voteVerification.Vote;
import voteVerification.VoteValidityResults;

/*
 * Author : Leyla
 * Date   : January 08, 2017
 */
public class DBHelper {
	
	private static final String SELECT_ALL_AUTHORITY_BY_ELECTIONID = "select * from Authority where electionID=?";
	private static final String SELECT_PRKEY_BY_AUTHORITY_CODE = "select authorityPr from AuthorityCredentials where authorityCode=?";
	private static final String INSERT_AUTHORITY_CREDENTIALS = "insert into AuthorityCredentials(authorityCode,authorityPr) values (?,?)";
	
	private static final String SELECT_PRKEY_BY_VOTER_ID = "select voterPr from VoterCredentials where voterID=?";
	private static final String SELECT_VALIDITY_COERCION_FLAG_BY_VOTER_ID = "select validityFlag, coercionFlag from Voter, RegisteredVoter where Voter.voterID=RegisteredVoter.voterID and Voter.voterID=?";
	private static final String INSERT_VOTE = "insert into Vote(voteID,voterID,signedEV,hash_seHregCode) values (?,?,?,?)";		
	private static final String INSERT_VOTER_CREDENTIALS = "insert into VoterCredentials(voterID,voterPr) values (?,?)";
	private static final String SELECT_HASHREGCODE_HASHFAKEREGCODE_BY_VOTER_EMAIL = "select hash_regcode, hash_fakeregcode from Voter, RegisteredVoter where Voter.voterID=RegisteredVoter.voterID and Voter.email=?";
	private static final String SELECT_VOTER_ID_BY_VOTER_EMAIL = "select voterID from Voter where email =?";
	private static final String UPDATE_COERCION_FLAG_BY_VOTER_ID = "update RegisteredVoter set coercionFlag=? where voterID=?";
	private static final String UPDATE_VALIDITY_FLAG_BY_VOTER_ID = "update RegisteredVoter set validityFlag=? where voterID=?";

	private static final String UPDATE_ELECTION_STATUS = "update Election set isActive=? where electionID=?";
	private static final String UPDATE_ELECTION_HASH_PR = "update Election set hash_prKey=? where electionID=?";
	private static final String SELECT_ELECTION_ID_BY_TITLE = "select electionID from Election where electionTitle=?";
	
	
	private static final String INSERT_ELECTION = "insert into Election(electionTitle,isActive,regStartTime,regEndTime,electionStartTime,electionEndTime,hash_prKey) "
			  + "VALUES (?, ?, TO_DATE(?,'dd-MM-yyyy hh24:mi'), TO_DATE(?,'dd-MM-yyyy hh24:mi'), TO_DATE(?,'dd-MM-yyyy hh24:mi'), TO_DATE(?,'dd-MM-yyyy hh24:mi'), ?)";
			
			
	private static final String SELECT_ACTIVE_ELECTION = "select electionID, electionTitle,isActive,"
	  + "TO_CHAR(regStartTime,'dd-MM-yyyy hh24:mi'), TO_CHAR(regEndTime,'dd-MM-yyyy hh24:mi'), "
	  + "TO_CHAR(electionStartTime,'dd-MM-yyyy hh24:mi'), TO_CHAR(electionEndTime,'dd-MM-yyyy hh24:mi'), "
	  + "hash_prKey from Election where isActive=?";
	
	
	private static final String SELECT_ELECTION_BY_ID = "select electionID, electionTitle,isActive,"
	  + "TO_CHAR(regStartTime,'dd-MM-yyyy hh24:mi'), TO_CHAR(regEndTime,'dd-MM-yyyy hh24:mi'), "
	  + "TO_CHAR(electionStartTime, 'dd-MM-yyyy hh24:mi'), TO_CHAR(electionEndTime, 'dd-MM-yyyy hh24:mi'), "
	  + "hash_prKey from Election where electionID=?";
	
	
	private static final String SELECT_ACTIVE_REGISTRATION_ELECTION = "select electionID, electionTitle,isActive,"
	  + "TO_CHAR(regStartTime,'dd-MM-yyyy hh24:mi'), TO_CHAR(regEndTime,'dd-MM-yyyy hh24:mi'), "
	  + "TO_CHAR(electionStartTime,'dd-MM-yyyy hh24:mi'), TO_CHAR(electionEndTime,'dd-MM-yyyy hh24:mi'), "
	  + "hash_prKey from Election "
	  + "where to_date(sysdate,'dd/MM/yyyy hh24:mi') between regStartTime and regEndTime";
			
	private static final String SELECT_MAX_ELECTION_ID = "select max(electionID) from Election";
	/*
	select electionID, electionTitle, 
	to_char(regStartTime,'DD-MM-YYYY HH24:MI') AS regStartTime, 
	to_char(regEndTime,'DD-MM-YYYY HH24:MI') AS regEndTime,
	to_char(electionStartTime,'DD-MM-YYYY HH24:MI') AS electionStartTime, 
	to_char(electionEndTime,'DD-MM-YYYY HH24:MI') AS electionEndTime
	from Election;
	*/
	
	private Connection dbConnection;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");	
	
	public DBHelper() {
		dbConnection = openConnection();
	}

	// LEYLA DBHelper
	public ArrayList<Authority> getAuthorities(int electionID) throws Exception{
		ArrayList<Authority> authorityList = new ArrayList<Authority>();
		Authority authority = null;
   	 
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
		    prep = conn.prepareStatement(SELECT_ALL_AUTHORITY_BY_ELECTIONID);
		    prep.setInt(1, electionID);
	    	rs = prep.executeQuery();

	    	int id=1;
			while(rs.next())
	        {
				authority = new Authority(id, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5));
				authorityList.add(authority);
				id++;
	        }
		 } catch(SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	     } finally {
				try {
					if(prep != null) {
						prep.clearParameters();
						prep.close();
					}
					if(conn != null)
						conn.close();
			    } catch (SQLException e) {
		            e.printStackTrace();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
	      			
   	 	return authorityList;
    }

	public BigInteger getAuthorityPrivateKey(int authorityCode){
		BigInteger authorityPr = null;
		
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			prep = dbConnection.prepareStatement(SELECT_PRKEY_BY_AUTHORITY_CODE);
		    prep.setInt(1, authorityCode);
	    	rs = prep.executeQuery();
			 
			while(rs.next())
			{
				/*
				BigDecimal bigDecimal = rs.getBigDecimal("authorityPr");
				authorityPr = (bigDecimal != null ? bigDecimal.toBigInteger() : null);
				*/

				authorityPr = new BigInteger(rs.getString(1));
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    }
	      			
		return authorityPr;
	}
	
	public boolean registerAuthorityCredentials(int authorityCode, BigInteger authorityPr)
	{
		boolean result = false;
		
		PreparedStatement prep = null;
	    try {
	    	prep = dbConnection.prepareStatement(INSERT_AUTHORITY_CREDENTIALS);
	    	prep.setInt(1, authorityCode);
	    	prep.setString(2, authorityPr.toString());
	    	prep.executeUpdate();
	    	result = true;
	    } catch (SQLException ex) {
	    	Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		} 
	    
	    return result;
	}
	
	public boolean registerVoterCredentials(int voterID, BigInteger voterPr)
	{
		boolean result = false;
		
		PreparedStatement prep = null;
	    try {
	    	prep = dbConnection.prepareStatement(INSERT_VOTER_CREDENTIALS);
	    	prep.setInt(1, voterID);
	    	prep.setString(2, voterPr.toString());
	    	prep.executeUpdate();
	    	result = true;
	    } catch (SQLException ex) {
	    	Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		} 
	    
	    return result;
	}

	
	public boolean registerElection(Election election)
	{
		boolean result = false;
			
		
		PreparedStatement prep = null;
	    try {
	    	prep = dbConnection.prepareStatement(INSERT_ELECTION);
	    	prep.setString(1, election.getElectionTitle());
	    	prep.setInt(2, 0);
	    	
	    	/*
	    	prep.setDate(3, new Date(election.getRegStartTime().getTime()));
	    	prep.setDate(4, new Date(election.getRegStartTime().getTime()));
	    	prep.setDate(5, new Date(election.getRegStartTime().getTime()));
	    	prep.setDate(6, new Date(election.getRegStartTime().getTime()));
	    	*/
	    	

	    	System.out.println("DBHelper: reg start time : " + sdf.format(election.getRegStartTime()));
	    	prep.setString(3, (sdf.format(election.getRegStartTime())));
	    	prep.setString(4, (sdf.format(election.getRegEndTime())));
	    	prep.setString(5, (sdf.format(election.getElectionStartTime())));
	    	prep.setString(6, (sdf.format(election.getElectionEndTime())));
	    	
	    	prep.setString(7, election.getHashOfPrKey());
	    	
	    	prep.executeUpdate();
	    	result = true;
	    } catch (SQLException ex) {
	    	Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		} 
	    return result;
	}
	
	public boolean updateElectionHashOfPr(int electionID, String hash_prKey)
	{
		boolean result = false;
		
		PreparedStatement prep = null;
	    try {
	    	prep = dbConnection.prepareStatement(UPDATE_ELECTION_HASH_PR);
	    	prep.setString(1, hash_prKey);
	    	prep.setInt(2, electionID);
	    	prep.executeUpdate();
	    	result = true;
	    } catch (SQLException ex) {
	    	Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		} 
	    
	    return result;
	}
	
	public Election getElectionByID(int electionID){
		Election election = null;
		
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			conn = openConnection();
			prep = conn.prepareStatement(SELECT_ELECTION_BY_ID);
		    prep.setInt(1, electionID);
	    	rs = prep.executeQuery();
			 
			while(rs.next())
			{
				String r1 = rs.getString(4);
				String r2 = rs.getString(5);
				String e1 = rs.getString(6);
				String e2 = rs.getString(7);
				
				try {
					election = new Election(rs.getInt(1), rs.getString(2), rs.getInt(3), sdf.parse(r1), sdf.parse(r2), sdf.parse(e1), sdf.parse(e2), rs.getString(8));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    } finally {
			try {
				if(prep != null) {
					prep.clearParameters();
					prep.close();
				}
				if(conn != null)
					conn.close();
		    } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	      			
		return election;
	}
	
	
	
	public int getMaxElectionID(){
		int electionID = -1;
		
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			prep = dbConnection.prepareStatement(SELECT_MAX_ELECTION_ID);
	    	rs = prep.executeQuery();
			 
			while(rs.next())
			{
				electionID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    } 
	      			
		return electionID;
	}
	
	public int getElectionIDByTitle(String electionTitle){
		int electionID = -1;
		
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			prep = dbConnection.prepareStatement(SELECT_ELECTION_ID_BY_TITLE);
		    prep.setString(1, electionTitle);
	    	rs = prep.executeQuery();
			 
			while(rs.next())
			{
				electionID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    } 
	      			
		return electionID;
	}
	
	public Election getActiveElection(){
		Election election = null;
		
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			prep = dbConnection.prepareStatement(SELECT_ACTIVE_ELECTION);
			prep.setInt(1, 1);
	    	rs = prep.executeQuery();
			
			while(rs.next())
			{
				String r1 = rs.getString(4);
				String r2 = rs.getString(5);
				String e1 = rs.getString(6);
				String e2 = rs.getString(7);
				
				try {
					election = new Election(rs.getInt(1), rs.getString(2), rs.getInt(3), sdf.parse(r1), sdf.parse(r2), sdf.parse(e1), sdf.parse(e2), rs.getString(8));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    }	
		return election;
	}
	
	public boolean updateElectionStatus(int electionID, int isActive)
	{
		boolean result = false;
		
		PreparedStatement prep = null;
	    try {
	    	prep = dbConnection.prepareStatement(UPDATE_ELECTION_STATUS);
	    	prep.setInt(1, isActive);
	    	prep.setInt(2, electionID);
	    	prep.executeUpdate();
	    	result = true;
	    } catch (SQLException ex) {
	    	Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		} 
	    return result;
	}
	
	
	public ArrayList<Election> getElectionsIfActiveRegistration()
	{
		ArrayList<Election> electionList = new ArrayList<Election>();
		Election election = null;
   	 
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = openConnection();
		    prep = conn.prepareStatement(SELECT_ACTIVE_REGISTRATION_ELECTION);
	    	rs = prep.executeQuery();
		
			while(rs.next())
			{
				String r1 = rs.getString(4);
				String r2 = rs.getString(5);
				String e1 = rs.getString(6);
				String e2 = rs.getString(7);
				
				try {
					election = new Election(rs.getInt(1), rs.getString(2), rs.getInt(3), sdf.parse(r1), sdf.parse(r2), sdf.parse(e1), sdf.parse(e2), rs.getString(8));
					electionList.add(election);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 } catch(SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	     } finally {
				try {
					if(prep != null) {
						prep.clearParameters();
						prep.close();
					}
					if(conn != null)
						conn.close();
			    } catch (SQLException e) {
		            e.printStackTrace();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
	      			
   	 	return electionList;
    }
	
		
	public BigInteger getVoterPrivateKey(int voterID){
		BigInteger voterPr = null;
		
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			prep = dbConnection.prepareStatement(SELECT_PRKEY_BY_VOTER_ID);
		    prep.setInt(1, voterID);
	    	rs = prep.executeQuery();
			 
			while(rs.next())
			{
				voterPr = new BigInteger(rs.getString(1));
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    }
	      			
		return voterPr;
	}
	
	public int getVoterID(String email){
		int voterID = -1;
		
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			prep = dbConnection.prepareStatement(SELECT_VOTER_ID_BY_VOTER_EMAIL);
		    prep.setString(1, email);
	    	rs = prep.executeQuery();
			 
			while(rs.next())
			{
				voterID = rs.getInt(1);
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    }
	      			
		return voterID;
	}
	
	public ArrayList<Integer> getRegisteredVoterFlags(int voterID){
		ArrayList<Integer> flags = new ArrayList<>();
		
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			prep = dbConnection.prepareStatement(SELECT_VALIDITY_COERCION_FLAG_BY_VOTER_ID);
			prep.setInt(1, voterID);
	    	rs = prep.executeQuery();
			 
			while(rs.next())
			{
				System.out.println("ValidityFlag:"+ rs.getInt(1) + " CoercionFlag:"+ rs.getInt(2));
				flags.add(rs.getInt(1));
				flags.add(rs.getInt(2));
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    }
	      			
		return flags;
	}
	
	public boolean registerVote(String voteID, int voterID, String signedEV, String hash_seHregCode)
	{
		boolean result = false;
		
		PreparedStatement prep = null;
	    try {
	    	prep = dbConnection.prepareStatement(INSERT_VOTE);
	    	prep.setString(1, voteID);
	    	prep.setInt(2, voterID);
	    	prep.setString(3, signedEV);
	    	prep.setString(4, hash_seHregCode);
	    	prep.executeUpdate();
	    	result = true;
	    } catch (SQLException ex) {
	    	Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		} 
	    
	    return result;
	}
	
	public boolean updateValidityFlag(int voterID, int validityFlag)
	{
		boolean result = false;
		
		PreparedStatement prep = null;
	    try {
	    	prep = dbConnection.prepareStatement(UPDATE_VALIDITY_FLAG_BY_VOTER_ID);
	    	prep.setInt(1, validityFlag);
	    	prep.setInt(2, voterID);
	    	prep.executeUpdate();
	    	result = true;
	    } catch (SQLException ex) {
	    	Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		} 
	   
	    return result;
	}
	
	public boolean updateCoercionFlag(int voterID, int coercionFlag)
	{
		boolean result = false;
		
		PreparedStatement prep = null;
	    try {
	    	prep = dbConnection.prepareStatement(UPDATE_COERCION_FLAG_BY_VOTER_ID);
	    	prep.setInt(1, coercionFlag);
	    	prep.setInt(2, voterID);
	    	prep.executeUpdate();
	    	result = true;
	    } catch (SQLException ex) {
	    	Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		} 
	   
	    return result;
	}
	
	private static Connection openConnection() {
        Connection connection = ConnectionManager.getInstance().getConnection();
        return connection;
	}

	/*
	public int openConnection(String userName, String password) throws SQLException
    {
        openConnection();
        return 0;
    }
    */
	
	// BURCU DBHelper
 	public String getHashOfSavedVoteForQuery(String hashOfRegCode)
 	{
 		System.out.println("inside get hash of casted vote for query");
 
 		String vSecret= null;
 		try 
 		{ 
	 		String query = "select HASH_SEHREGCODE from Vote where VOTEID="+hashOfRegCode;
	 		ResultSet rs= getResultOfQuery(query);
 			 
	 		while(rs.next())
	 	    {
	 			System.out.println("queriedVote:"+rs.getString(1));
	 	        vSecret = rs.getString(1);
	 	    }
 		} catch (SQLException ex) 
	 	{
	 		Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	 	}
 	      			
 		return vSecret;
 	}
 	
 	public ArrayList<String> getRegCodeAndFakeRegCodeOfVoter(String email){
 		ArrayList<String> results = new ArrayList<>();
		
 		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try { 
			conn = openConnection();
			prep = conn.prepareStatement(SELECT_HASHREGCODE_HASHFAKEREGCODE_BY_VOTER_EMAIL);
			prep.setString(1, email);
	    	rs = prep.executeQuery();
			 
			while(rs.next())
			{
	 			System.out.println("RegCode: "+rs.getString(1)+" FakeRegCode:"+rs.getString(2));
	 			results.add(rs.getString(1));
	 			results.add(rs.getString(2));
			}
		} catch (SQLException ex) {
			 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	    } finally{
	    	try{
		    	if(prep != null){
						prep.clearParameters();
						prep.close();
		    	}
				if(rs != null)
					rs.close();
				if(conn  != null)
					conn.close();
	    	}catch (SQLException ex) {
			 		Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
			 	}
	    }
	      			
		return results;
	}
 	
 	
 	// ARZUM DBHelper
	public ResultSet getResultOfQuery( String query){
	    ResultSet rs=null;
	    try {
	      
	        PreparedStatement ps = dbConnection.prepareStatement(query);/*a Statement object that carries your SQL language query to the database*/
	        rs = ps.executeQuery();/*instantiates a ResultSet object that retrieves the results of your query, and executes a simple while loop, which retrieves and displays those results.*/
	                                    /*The java.sql.ResultSet interface represents the result set of a database query. A ResultSet object maintains a cursor that points to the current row in the result set. 
	                                    The term "result set" refers to the row and column data contained in a ResultSet object.*/
	    } catch (SQLException ex) {
	        Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	        /*public static final Level SEVERE
	        SEVERE is a message level indicating a serious failure.*/
	    }
	    return rs;
    }
 
 	public Voter getVoter(String email){
		ResultSet rs=null;
        Voter voter = null;
        try {
           
        	String query = "SELECT * FROM Voter where email = '"+ email+"'";
        	//System.out.println(query);
        	PreparedStatement ps = dbConnection.prepareStatement(query);
        	rs = ps.executeQuery(query);//returns result set of the query, used for only select queries
    
	       while(rs.next())
	       {
	           // System.out.println("voterid: "+rs.getInt(1)+" name:"+rs.getString(2)+" email:"+rs.getString(3)+" birthdate: "+rs.getDate(4)+" certificateName:"+rs.getString(5));
	            voter = new Voter(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDate(4),rs.getString(5));
	       }
          
       } catch (SQLException ex) {
           Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
       }
       return voter;
	}
	 
	 
	 public boolean insertRegisteredVoter(RegisteredVoter regVoter, int electionId)
	    {
	        try {
	        	System.out.println("inside insert register method");
	        	
	            String query="INSERT INTO registeredvoter(voterid, hash_regcode, hash_fakeregcode, validityflag, coercionflag, electionID)"
	                    + "values(?,?,?,?,?,?)";
	            PreparedStatement ps = dbConnection.prepareStatement(query);/*An object that represents a precompiled SQL statement.
	                                                                          A SQL statement is precompiled and stored in a PreparedStatement object.
	                                                                        This object can then be used to efficiently execute this statement multiple times. Prepare statement is an interface extends from Statement class*/


	            ps.setInt(1,regVoter.getVid());
	            ps.setString(2, regVoter.getHashRegistrationCode());
	            ps.setString(3, regVoter.getHashCoercionCode());
	            ps.setBoolean(4, false);
	            ps.setBoolean(5, false);
	            ps.setInt(6, electionId);
	            ps.executeUpdate();
	            
	            System.out.println("executed");
	            
	            dbConnection.commit();
	            
	            return true;
	        } catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	             return false;
	        }
	    }

	 
	 public boolean insertCandidate(Candidate candidate, int electionid )
	 {
	        try {
	        	System.out.println("inside insert candidate method");
	        	
	            String query="INSERT INTO candidate(candidateid,name,job,birthday,electionid, score)"
	                    + "values(?,?,?,?,?,?)";
	            PreparedStatement ps = dbConnection.prepareStatement(query);/*An object that represents a precompiled SQL statement.
	                                                                          A SQL statement is precompiled and stored in a PreparedStatement object.
	                                                                        This object can then be used to efficiently execute this statement multiple times. Prepare statement is an interface extends from Statement class*/


	            ps.setInt(1,candidate.getCid());
	            ps.setString(2, candidate.getName());
	            ps.setString(3, candidate.getJob());
	            ps.setDate(4, new Date(candidate.getBirthdate().getTime()));
	            ps.setInt(5, electionid);
	            ps.setInt(6, 0);
	            ps.executeUpdate();
	            
	            System.out.println("executed");
	            
	            dbConnection.commit();
	            
	            return true;
	        } catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	             return false;
	        }
	 }
	 
	 
	 public boolean isRegisteredVoter(int vid, int eid){
			
		ResultSet rs=null;
		         
        try {
        	String query = "SELECT * FROM registeredvoter where voterid = "+ vid +" and electionid = "+eid;
        	//System.out.println(query);
        	PreparedStatement ps = dbConnection.prepareStatement(query);
          
        	rs = ps.executeQuery(query);//returns result set of the query, used for only select queries

           if(rs.next())
           {
               return true;
           }
       } catch (SQLException ex) {
           Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
       }
       return false;
	}
	 
	 
	 public boolean isRegisteredCandidate(int electionid, int cid){
		ResultSet rs=null;
		         
        try {
       	String query = "SELECT * FROM candidate where candidateid = "+ cid+" and electionid = "+electionid;
       	//System.out.println(query);
           PreparedStatement ps = dbConnection.prepareStatement(query);
          
           rs = ps.executeQuery(query);//returns result set of the query, used for only select queries
       	
           if(rs.next())
           {
               return true;
           }
          
       } catch (SQLException ex) {
           Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
       }
        
       return false;
	}
	 
	 
	 public int countCandidates(int eid){
		 ResultSet rs=null;
        int countOfCandidates=-1;
        try {
           
       	String query = "SELECT COUNT(*) FROM candidates where electionid="+eid;
           PreparedStatement ps = dbConnection.prepareStatement(query);
           rs = ps.executeQuery(query);//returns result set of the query, used for only select queries
    
           while(rs.next())
           {
                System.out.println("Number of Candidates:"+rs.getInt(1)+" in electionid= "+eid);
                countOfCandidates = rs.getInt(1);
           }
          
       } catch (SQLException ex) {
           Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
       }
       return countOfCandidates;
	 }

	 
	 public int countRegisteredVoters(int electionID){
		 ResultSet rs=null;
        int countOfRegisteredVoters=-1;
        try {
           
       	String query = "SELECT COUNT(*) FROM registeredvoter WHERE electionID="+electionID;
           PreparedStatement ps = dbConnection.prepareStatement(query);
           rs = ps.executeQuery(query);//returns result set of the query, used for only select queries
    
           while(rs.next())
           {
                System.out.println("Number of Eligible(Registered) Voters: "+rs.getInt(1));
                countOfRegisteredVoters = rs.getInt(1);
           }
          
       } catch (SQLException ex) {
           Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
       }
       return countOfRegisteredVoters;
	 }
	 
	 
	 public ArrayList<Candidate> getCandidates(int electionid){
    	 ArrayList<Candidate> candidateList = new ArrayList<Candidate> ();
    	 Candidate candidate = null;
    	 
    	 try { 
			 String query = "SELECT * FROM candidate where electionid="+electionid;
			 ResultSet rs= getResultOfQuery(query);
			 
			 while(rs.next())
	            {
	                 System.out.println("candidateID: "+rs.getInt(1)+" Name:"+rs.getString(2)+" Job:"+rs.getString(3)+" BirthDate:"+rs.getDate(4)+ " electionID:"+rs.getInt(5));
	                 candidate = new Candidate(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4));
	                 candidateList.add(candidate);
	            }
		 	} catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	        }
	      			
    	 return candidateList;
    	 
     }
	 
	 
     public VoteValidityResults countValidInvalidVotes(int eid){
    	 ResultSet rs=null;
    	 VoteValidityResults results = null;
    	 
    	 int countOfValid = -1;
    	 int countOfInValid = -1;
    	 
         try {
            
        	String query = "SELECT validityflag, COUNT(*) FROM anonymizedvote WHERE electionid="+eid+ " group by validityflag";
        	PreparedStatement ps = dbConnection.prepareStatement(query);
            rs = ps.executeQuery(query);//returns result set of the query, used for only select queries
     
            while(rs.next())
            {
                int flag = rs.getInt(1);
            	if(flag==1){
            		countOfValid= rs.getInt(2);
            	}
            	if(flag==0){
            		countOfInValid= rs.getInt(2);
            	}
            	
            }
            
            results = new VoteValidityResults(countOfValid, countOfInValid);
           
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
     }
     
     public String getHashRegCode(int voterID){
    	 String code= null;
		 try { 
			 String query = "SELECT hash_regcode FROM registeredvoter where voterid="+voterID;
			 ResultSet rs= getResultOfQuery(query);
			 
			 while(rs.next())
	            {
				  code = rs.getString(1);
	            }
		 	} catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	        }
	      			
		return code;
     }
    
     
     public String getHashCoercionCode(int voterID){
    	 String code= null;
		 try { 
			 String query = "SELECT hash_fakeregcode FROM registeredvoter where voterid="+voterID;
			 ResultSet rs= getResultOfQuery(query);
			 
			 while(rs.next())
	            {
				  code = rs.getString(1);
	            }
		 	} catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	        }
	      			
		return code;
     }
     
     
     public String getHashE_SE_HregCode(String voteID){
    	 String code= null;
		 try { 
			 String query = "SELECT hash_sehregcode FROM vote where voteid='"+voteID+"'";
			 ResultSet rs= getResultOfQuery(query);
			 
			 while(rs.next())
	            {
				  code = rs.getString(1);
	            }
		 	} catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	        }
	      			
		return code;
     }

     
	 public String getSignedEVote(String voteID) {
		 String code= null;
		 try { 
			 String query = "SELECT signedev FROM vote where voteid='"+voteID+"'";
			 ResultSet rs= getResultOfQuery(query);
			 
			 while(rs.next())
	            {
				  code = rs.getString(1);
	            }
		 	} catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	        }
	      			
		return code;
	}

	 
	 public ArrayList<Vote> getVoteList(){
	   	 ArrayList<Vote> voteList = new ArrayList<Vote> ();
	   	 Vote vote = null;
	   	 
	   	 try { 
				 String query = "SELECT * FROM vote";
				 ResultSet rs= getResultOfQuery(query);
				 
				 while(rs.next())
		            {
		                 
		                 vote = new Vote(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4));
		                 voteList.add(vote);
		            }
			 	} catch (SQLException ex) {
		            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		        }
		      			
	   	 return voteList;
   	 
    }

	 public ArrayList<AnonymizedVote> getAnonymizedVoteList(int electionid){
	   	 ArrayList<AnonymizedVote> avoteList = new ArrayList<AnonymizedVote> ();
	   	 AnonymizedVote avote = null;
	   	 
	   	 try { 
				 String query = "SELECT * FROM anonymizedvote where electionid="+electionid;
				 ResultSet rs= getResultOfQuery(query);
				 
				 while(rs.next())
		            {
		                 avote = new AnonymizedVote(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
		                 avoteList.add(avote);
		            }
			 	} catch (SQLException ex) {
		            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		        }
		      			
	   	 return avoteList;
	 }
	
	 
	 public boolean insertAnonymizedVote(AnonymizedVote v )
	 {
	        try {
	        	        	
	            String query="INSERT INTO anonymizedvote(avid,evote,validityflag,electionid)"
	                    + "values(ANONYMIZEDVOTE_SEQ.nextval,'"+v.getEvote()+"',"+v.getValidityFlag()+","+v.getElectionId()+")";
	            PreparedStatement ps = dbConnection.prepareStatement(query);/*An object that represents a precompiled SQL statement.
	                                                                          A SQL statement is precompiled and stored in a PreparedStatement object.
	                                                                        This object can then be used to efficiently execute this statement multiple times. Prepare statement is an interface extends from Statement class*/
                System.out.println(query);
	            ps.executeUpdate();
	            
	            System.out.println("executed");
	            
	            dbConnection.commit();
	            
	            return true;
	        } catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	             return false;
	        }
	 }
	 
	 
	 public ArrayList<String> getHashCodes(){
		 ArrayList<String>  hashCodeList = new ArrayList<String>();
		 
		 try { 
			 String query = "SELECT hash_regcode, hash_fakreregcode FROM registeredvoter";
			 ResultSet rs= getResultOfQuery(query);
			 
			 while(rs.next())
	            {
	                hashCodeList.add(rs.getString(1));
	                hashCodeList.add(rs.getString(2));
	            }
		 	} catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	        }
	      			
		 return hashCodeList;
	 }

 
	 public void  removeVotes() throws SQLException{
		 System.out.println("Vote table is being deleted ..");
		 String query = "DELETE FROM VOTE";
		 ResultSet rs= getResultOfQuery(query);
		 System.out.println("Votes are deleted!");
		 rs.close();
	 }

	public boolean updateCandidateElectionId(int electionID) {
		 try { 
			 String query = "update Candidate set electionid="+electionID;
			 System.out.println("query" + query);
			 //hata burda
			 PreparedStatement ps = dbConnection.prepareStatement(query);
			 System.out.println("between");
			 ps.executeUpdate();
             System.out.println("update candidate query");
			 
             dbConnection.commit();
			 return true;

			} catch (SQLException ex) {
	            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	        }
		return false;
	      			
		
	}
	public boolean updateAuthorityElectionId(int electionID) {
		try { 
		    String query = "update Authority set electionid="+electionID;
		    System.out.println("query" + query);
		    //hata burda
		    PreparedStatement ps = dbConnection.prepareStatement(query);
		    System.out.println("between");
		    ps.executeUpdate();
		            System.out.println("update authority query");
		    
		            dbConnection.commit();
		    return true;
		} catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
	return false;
	}
	
	 public boolean resetVoterFlags() {
		   try { 
		    String query = "update RegisteredVoter set validityFlag=0, coercionFlag=0";
		    System.out.println("query" + query);
		    PreparedStatement ps = dbConnection.prepareStatement(query);
		    ps.executeUpdate();
		    
		            dbConnection.commit();
		    return true;

		   } catch (SQLException ex) {
		             Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
		         }
		  return false;
		 }
	
	 public boolean resetRegisteredVoter()
	   {
	    try { 
	        String query = "delete from RegisteredVoter";
	        System.out.println("query" + query);
	        PreparedStatement ps = dbConnection.prepareStatement(query);
	        ps.executeUpdate();
	        
	                dbConnection.commit();
	        return true;

	       } catch (SQLException ex) {
	                 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	             }
	      return false;
	   }

	  public boolean resetAnonymizedVote()
	   {
	    try { 
	        String query = "delete from AnonymizedVote";
	        System.out.println("query" + query);
	        PreparedStatement ps = dbConnection.prepareStatement(query);
	        ps.executeUpdate();
	        
	                dbConnection.commit();
	        return true;

	       } catch (SQLException ex) {
	                 Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
	             }
	      return false;
	  }
}


