package controller;

import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import Ecc.EllipticCurve;
import EccSign.CheckSign;
import EccSign.ECDSA;
import EccSign.IcheckSign;
import EccSign.Point;
import Paillier.CountAndDecrypt;
import Paillier.CreatingEvoting;
import Paillier.ICreateElection;
import Paillier.IPaillierResultComputation;
import Paillier.IVote;
import Paillier.Voting;
import announcement.AnnouncementOfResults;
import hash.Hash;
import hash.IHash;
import helper.CertificateReader;
import multipartyComputation.AuthorityManager;
import multipartyComputation.CertificateManager;
import multipartyComputation.DBHelper;
import preparationForVoting.Ballot;
import preparationForVoting.BulletinBoard;
import preparationForVoting.Candidate;
import preparationForVoting.PreparationForVotingManager;
import registrationOfVoters.VoterRegistrationManager;
import systemCreation.Election;
import systemCreation.ElectionManager;
import voteManaging.IShuffling;
import voteManaging.Shuffler;
import voteVerification.AnonymizedVote;
import voteVerification.VoteVerifier;
import voting.IVoting;
import voting.QueryCastedVote;
import voting.VotingModule;

//@generic, emre
public class Controller{
	
	private static Controller instance = null;
	
	//GUI'den alýnacaklar
	private int selectedCandidateID = -1;
	private String email = null;
	private String regCode = null;
	private String queriedRegCode = null;
	public String queriedVote = null;
	
	
	private String electionTitle = null;
	private Date regStartTime = null;
	private Date regEndTime = null;
	private Date electionStartTime = null;
	private Date electionEndTime = null;
	
	
	private final int DIGESTION_SIZE = 512;
	private final String secretPath = "paillierPr";
	private final String publicPath = "paillierPu";
	private final String resultPath = "countingResults";
	private final String certificateFolderPath = "Certificates/";
	private final String signatureFolderPath = "Signatures/";
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");	
	
	
	private ICreateElection paillier = null;
	private IVote vote = null;
	private IcheckSign signChecker=null;
	private EllipticCurve curve = null;
	private ECDSA ecDSA = null;
	private BigInteger CAPrivateKey = null;
	private Point CAPublicKey = null;
	
	
	private DBHelper dbHelper = null;
	private IHash hashGenerator = null;
	private CertificateManager certificateManager = null;
	private CertificateReader certificateReader = null;
	
	private ElectionManager electionManager = null;
	private Election currentElection = null;
	private VoterRegistrationManager voterRegistrationManager = null;
	private PreparationForVotingManager preparationForVotingManager = null;
	
	private Map<Integer,String> reaultMap = new HashMap<>();
	public void setResultMap(Integer i, String score){
	reaultMap.put(i, score);
	}
	public Map<Integer, String> getResultMap(){
	return this.reaultMap;
	}
	
	public PreparationForVotingManager getPreparationForVotingManager() {
		return preparationForVotingManager;
	}


	public void setPreparationForVotingManager(PreparationForVotingManager preparationForVotingManager) {
		this.preparationForVotingManager = preparationForVotingManager;
	}

	private IVoting votingModule = null;
	private QueryCastedVote queryCastedVote = null;
	
	private AuthorityManager authorityManager = null;
	private VoteVerifier voteVerifier = null;
	public VoteVerifier getVoteVerifier() {
		return voteVerifier;
	}


	public void setVoteVerifier(VoteVerifier voteVerifier) {
		this.voteVerifier = voteVerifier;
	}

	private IShuffling shuffler = null;
	private ArrayList<AnonymizedVote> anonymousVotes = null;
	
	private IPaillierResultComputation voteCounter = null;
	private AnnouncementOfResults announcement = null;
	private BulletinBoard bulletinBoard = null;

	private Map<String,String> scoreMap;
	private Map<String,String> privateKeyMap;
	


	public Map<String, String> getPrivateKeyMap() {
		return privateKeyMap;
	}


	public void setPrivateKeyMap(Map<String, String> privateKeyMap) {
		this.privateKeyMap = privateKeyMap;
	}


	public Map<String, String> getPublicKeyMap() {
		return publicKeyMap;
	}


	public void setPublicKeyMap(Map<String, String> publicKeyMap) {
		this.publicKeyMap = publicKeyMap;
	}

	private Map<String,String> publicKeyMap;

	private Ballot ballot;
	
	
	public Map<String, String> getScoreMap() {
		return scoreMap;
	}


	public void setScoreMap(Map<String, String> scoreMap) {
		this.scoreMap = scoreMap;
	}


	private Controller() throws Exception{
		
		
		signChecker = new CheckSign(CAPublicKey);
		curve = EllipticCurve.NIST_P_192;
		ecDSA = new ECDSA();
		CAPrivateKey = new BigInteger("113030449655396724366806196427178321280749587523943565521585687101690651316050");
		ecDSA.setPu(CAPrivateKey);
		CAPublicKey = ecDSA.getQA();
		
		
		hashGenerator = new Hash();
		certificateManager = new CertificateManager(CAPrivateKey, CAPublicKey, certificateFolderPath, signatureFolderPath);
		certificateReader = new CertificateReader(certificateFolderPath);
		dbHelper = new DBHelper();
		
		
		electionManager = new ElectionManager(this);
		//currentElection = electionManager.getCurrentElection();
		voterRegistrationManager = new VoterRegistrationManager(this, null, certificateFolderPath);
		preparationForVotingManager = new PreparationForVotingManager(this, -1);

		
		queryCastedVote = new QueryCastedVote(this);
		
		// AuthorityManager sýnýfýna hangi election için secret key'i daðýtýp toplamak istiyorsak o election'ýn electionID'si verilmeli !!
		// O election için Authority'ler database'e eklenmeli. AuthorityCredentials tablosu doldurulmalý.
		// Authority'lerin sertifikalarý sertifika klasörüne eklenmeli.
		// Ayný zamanda sertifikalarýn signature'larý da signature klasörüne eklenmeli.
		
		
		
		/*
		 * bunlarý create election methodunda oluþturduk
		 * voteVerifier = new VoteVerifier(this);
		 * shuffler = new Shuffler(this);
		 */
	}
	

	public static Controller getInstance() throws Exception{
		if(instance == null){
			instance = new Controller();
		}
		return instance;
	}
	

	
	public ICreateElection getPaillier() {
		return paillier;
	}

	public void setPaillier(ICreateElection paillier) {
		this.paillier = paillier;
	}

	public IcheckSign getSignChecker() {
		return signChecker;
	}

	public void setSignChecker(IcheckSign signChecker) {
		this.signChecker = signChecker;
	}

	public DBHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(DBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public IVoting getVotingModule() {
		return votingModule;
	}

	public void setVotingModule(IVoting votingModule) {
		this.votingModule = votingModule;
	}

	public IHash getHashGenerator() {
		return hashGenerator;
	}

	public void setHashGenerator(IHash hashGenerator) {
		this.hashGenerator = hashGenerator;
	}

	public CertificateManager getCertificateManager() {
		return certificateManager;
	}

	public void setCertificateManager(CertificateManager certificateManager) {
		this.certificateManager = certificateManager;
	}

	public CertificateReader getCertificateReader() {
		return certificateReader;
	}

	public void setCertificateReader(CertificateReader certificateReader) {
		this.certificateReader = certificateReader;
	}

	
	public AuthorityManager getAuthorityManager() {
		return authorityManager;
	}

	public void setAuthorityManager(AuthorityManager authorityManager) {
		this.authorityManager = authorityManager;
	}
	
	
	public ElectionManager getElectionManager() {
		return electionManager;
	}

	public void setElectionManager(ElectionManager electionManager) {
		this.electionManager = electionManager;
	}
	
	public VoterRegistrationManager getVoterRegistrationManager() {
		return voterRegistrationManager;
	}


	public void setVoterRegistrationManager(VoterRegistrationManager voterRegistrationManager) {
		this.voterRegistrationManager = voterRegistrationManager;
	}


	public String getSecretPath() {
		return secretPath;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public IVote getVote() {
		System.out.println("vote get: " + this.vote);
		return vote;
	}

	public void setVote(IVote voteA) {
		System.out.println("vote set1: " + voteA);
		this.vote = voteA;
		System.out.println("vote set2: " + this.vote);
		
	}

	public EllipticCurve getCurve() {
		return curve;
	}

	public void setCurve(EllipticCurve curve) {
		this.curve = curve;
	}
	
	public QueryCastedVote getQueryCastedVote() {
		return queryCastedVote;
	}

	public void setQueryCastedVote(QueryCastedVote queryCastedVote) {
		this.queryCastedVote = queryCastedVote;
	}


	public int getDIGESTION_SIZE() {
		return DIGESTION_SIZE;
	}

	public int getSelectedCandidateID()
	{
		return this.selectedCandidateID;
	};
	
	public void setSelectedCandidateID(int id)
	{
		this.selectedCandidateID = id;
		//vo
	};
	
	public String getEmail()
	{
		return this.email;
	};
	
	public void setEmail(String mail)
	{
		this.email = mail;
	}

	public String getRegCode() {
		return regCode;
	}

	public void setRegCode(String regCode) {
		this.regCode = regCode;
	};
	
	//VoterModule tekrar bak
	public void resetParameters()
	{
		this.selectedCandidateID = -1;
		this.email = null;
		this.regCode = null;
	}
	
	public void resetDB() throws SQLException
	 {  dbHelper.resetAnonymizedVote();
	    dbHelper.removeVotes();
	    dbHelper.resetRegisteredVoter();
	  //dbHelper.resetVoterFlags();
	 }
	
	public String getQueriedVote() {
		return queriedVote;
	}

	public void setQueriedVote(String queriedVote) {
		this.queriedVote = queriedVote;
	}

	public String getElectionTitle() {
		return electionTitle;
	}

	public void setElectionTitle(String electionTitle) {
		this.electionTitle = electionTitle;
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
	
	
	public String getQueriedRegCode() {
		return queriedRegCode;
	}

	public void setQueriedRegCode(String queriedRegCode) {
		this.queriedRegCode = queriedRegCode;
	}

	public void createElection(String electionTitle, Date regStartTime,Date regEndTime, Date electionStartTime, Date electionEndTime) throws Exception {
		this.electionTitle = electionTitle;
		this.regStartTime = regStartTime;
		this.regEndTime = regEndTime;
		this.electionStartTime = electionStartTime;
		this.electionEndTime = electionEndTime;
		
		electionManager.generateElection(electionTitle, regStartTime, regEndTime, electionStartTime, electionEndTime);
		
	
		
		currentElection = electionManager.getCurrentElection();
		JOptionPane.showMessageDialog(null, "dbye geldik");
		dbHelper.updateCandidateElectionId(currentElection.getElectionID());
		dbHelper.updateAuthorityElectionId(currentElection.getElectionID());
		
		JOptionPane.showMessageDialog(null, "prepare baþladý");
		authorityManager = new AuthorityManager(this, currentElection.getElectionID());
		
		JOptionPane.showMessageDialog(null, "pr bitti");
		
		
		// Just for trial- this is not the correct place for vote verification
		
		//voterRegistrationManager.setElection(currentElection);
	}
	
	//Election'ý GUI'den combobox'tan geliyor.
	public void registerVoter(Election election, String vname, String email, Date birthdate, String certificateName) throws Exception{
		/*it is closed because we fixed election as the last created one
		 * Combo ile baðý kesildi, son election alýnýyor. 
		 * */
		//currentElection = election;
		voterRegistrationManager.setElection(currentElection);
		JOptionPane.showMessageDialog(null, "current election id"+currentElection.getElectionID());
		voterRegistrationManager.setVoterInfo(vname, email, birthdate, certificateName);
		
		if(voterRegistrationManager.isVoterValid()){
			System.out.println("valid voter");
			if(voterRegistrationManager.checkCertificate(certificateName, certificateName)){
				System.out.println("certificate valid");
				String signedForm = voterRegistrationManager.signForm();
				System.out.println("Controller signedForm:" + signedForm);
				//regVoter = voterRegistrationManager.registerVoter(signedForm);
				System.out.println("sign form");
				voterRegistrationManager.registerVoter(signedForm);
				
			}
		}
	}
	

	
	//Election id registrationu tamamlanmýþ, ve seçim zamaný baþlamamýþ olan electiona ait olmalý (bir seferde sadece bir election olabilir)
	//public Ballot preperateForVoting(int electionID){
	public Ballot preperateForVoting(){	
		int electionID = getPreparedElection().getElectionID();
		
				
		preparationForVotingManager.setEid(electionID);
		//preparationForVotingManager.updateCandidateElectionId(electionID);
		ArrayList<Candidate> candidates= preparationForVotingManager.getCandidateListFromDB();
		preparationForVotingManager.getCandidateManager().setCandidates(candidates);
		System.out.println("candidates"+candidates);
		
		try {
			System.out.println("generate System keys");
			generateSystemKeys(electionID);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ballot = preparationForVotingManager.generateBallot();
		System.out.println("ballot"+ballot);
		
		getAuthorityManager().shareSecret();
		
		return ballot;
	}
	
	public void generateSystemKeys(int electionID) throws Exception{
		int candidateNumber = preparationForVotingManager.getCandidateManager().getCandidates().size();
		//int candidateNumber  = voterRegistrationManager.getNumberOfEligibleVoters(electionID);
		System.out.println("candidateNumber: " + candidateNumber);
		paillier = new CreatingEvoting(BigInteger.valueOf(voterRegistrationManager.getNumberOfEligibleVoters(electionID)), candidateNumber);
		this.vote = new Voting(paillier);
		votingModule = new VotingModule(this);
	}
	
	//Election id registrationu tamamlanmýþ, ve seçim zamaný baþlamamýþ olan electiona ait olmalý (bir seferde sadece bir election olabilir) 
	public Election getPreparedElection(){
		//return dbHelper.getElectionByID(1);
		return currentElection;
	}
	
	//public BulletinBoard preperateBulletinBoard(int electionID){
	public BulletinBoard prepareBulletinBoard(){
		
		int electionID = getPreparedElection().getElectionID();
		System.out.println("electionID in bulletin board"+electionID);
		/*
		preparationForVotingManager.setEid(electionID);
		preparationForVotingManager.updateCandidateElectionId(electionID);
		ArrayList<Candidate> candidates= preparationForVotingManager.getCandidateListFromDB();
		preparationForVotingManager.getCandidateManager().setCandidates(candidates);
		System.out.println("candidates"+candidates);
		
      */
		bulletinBoard = new BulletinBoard(voterRegistrationManager.getNumberOfEligibleVoters(electionID), ballot.getCandidates());
		
		return bulletinBoard;
	}
	
	public boolean saveVote() throws Exception{
		return votingModule.saveVote();
	}
	
	public void queryVote(){
		getQueryCastedVote().getVote();
	}
	
	//v6_newly added
	public void verifyVotes(){
		System.out.println("Votes are verified now!");
		// take vote list from DB
		voteVerifier.setVoteList(dbHelper.getVoteList());
		// if all votes can be taken from DB successfully,
		if( voteVerifier.verifyAllVotes()){
			// validate each votes
			voteVerifier.validateVotes();
			// display valid and invalid vote counts
			System.out.println("valid counts: "+voteVerifier.getCountofValidVotes());
			System.out.println("invalid counts: "+voteVerifier.getCountofInvalidVotes());
		}
	}
	
	//v6_newly added
	public void shuffleVotes(){
		System.out.println("Votes are shuffling now!");
		anonymousVotes = shuffler.getShuffledVotes();
		
	}
	
	public void countVotes() throws Exception{
		
		System.out.println("Votes are counting now!");
		voteCounter = new CountAndDecrypt(paillier);
		ArrayList<AnonymizedVote> avotes =dbHelper.getAnonymizedVoteList(currentElection.getElectionID());
		//System.out.println(avotes.toString());
		//System.out.println("avotes size :"+avotes.size());
		
		//System.out.println("anonymousVotes size :"+anonymousVotes.size());
		for(AnonymizedVote avote: avotes){
			System.out.println("***---*** Validity flag: " +avote.getValidityFlag());
			if(avote.getValidityFlag()==1){
				System.out.println("***** Count a girdi");
				voteCounter.count(new BigInteger(avote.getEvote()));
				System.out.println("");
			}
		}
		
		try {
			voteCounter.setPr("paillierPr");
			voteCounter.decrypt();
			voteCounter.getResult("countingResults");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void announceResults() throws Exception{
		announcement = new AnnouncementOfResults(resultPath, secretPath, publicPath);
		
		bulletinBoard.setNoOfValidVotes(announcement.getNumOfValidVotes());
		bulletinBoard.setNoOfInvalidVotes(announcement.getNumOfInvalidVotes());
		
		scoreMap = announcement.getScores();
		privateKeyMap = announcement.getSystemPrivateKey();
		publicKeyMap = announcement.getSystemPublicKey();
		
		
	}
	
	public void endVoting() throws Exception{
		/*
		 * bu metoda bir yerde vote tablosunu silen method çaðýrýlacak
		 */
		voteVerifier = new VoteVerifier(this);
		shuffler = new Shuffler(this);
		
		verifyVotes();
		// announcement - user information
		shuffleVotes();
		
		getAuthorityManager().reconstructSecret();
		
		// announcement - user information
		countVotes();
		
		resetDB();
		// announcement - user information
		//announceResults();
		
			
	}
	
	public void startRegistration(){
		
	}
	
	public void stopRegistration(){
		preperateForVoting();
		prepareBulletinBoard();
	}
	
	public void startVoting(){
		
	}
	//GUI'den butona týklamaya gerek var mý? Bu metodu AuthorityManager, Authority'ler build edemezse tetikleyecek. Ýçerisinde ne olacak?
	public void resetSystem(){
		
	}
}
