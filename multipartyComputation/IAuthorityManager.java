package multipartyComputation;

public interface IAuthorityManager {
	public void shareSecret();
	public void reconstructSecret() throws Exception;
}
