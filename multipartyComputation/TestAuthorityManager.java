package multipartyComputation;

import controller.Controller;

/*
 * Author : Leyla
 * Date   : January 05, 2017
 */
public class TestAuthorityManager {
	
	public static void main(String[] args) throws Exception{
		
		Controller controller = Controller.getInstance();
	
		AuthorityManager authorityManager = controller.getAuthorityManager();
		authorityManager.shareSecret();
		authorityManager.reconstructSecret();
	}
}

