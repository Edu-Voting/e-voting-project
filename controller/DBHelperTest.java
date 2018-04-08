package controller;

import java.math.BigInteger;

import multipartyComputation.DBHelper;



public class DBHelperTest {
	
	public static void main(String[] args) throws Exception {
		
		Controller controller = Controller.getInstance();
		
		DBHelper dbHelper = controller.getDbHelper();
		/*
		//leyla
		dbHelper.registerAuthorityCredentials(1, new BigInteger("1959761078934109882742455649792176670654596126695823548069"));
		BigInteger pr = dbHelper.getAuthorityPrivateKey(1);
		System.out.println(pr);
		
		//arzum
		dbHelper.registerAuthorityCredentials(2, new BigInteger("986370399228543641715248996094950550391312487125653523373"));
		dbHelper.registerAuthorityCredentials(3, new BigInteger("4228725017373180385874637409096222978554649118144201846203"));
		dbHelper.registerAuthorityCredentials(4, new BigInteger("5696598224898184795332050964555559190233302174218164702620"));

		//dbHelper.registerAuthorityCredentials(5, new BigInteger("5091649207866623379097096625912768095960216891115846119472"));
		//dbHelper.registerAuthorityCredentials(6, new BigInteger("1579541408981564109960874567797314686343928376567610765081"));
		//dbHelper.registerAuthorityCredentials(7, new BigInteger("2021698455173514572316857735829360569599395889707708362268"));
		
		//dbHelper.registerAuthorityCredentials(8, new BigInteger("1131499832774803449398131778966727608531154155836124325782"));
		//dbHelper.registerAuthorityCredentials(9, new BigInteger("933278972158557448177913351189150605577407326519890853506"));
		*/
		
		dbHelper.updateCandidateElectionId(15);
		
	}
}
