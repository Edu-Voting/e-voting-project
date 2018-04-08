package voting;

public interface IVoting {
	
	public boolean saveVote() throws Exception;
	public boolean isLoginSuccessful();
	public String getHashOfRegCode();
	public String getHashOfFakeRegCode();
}