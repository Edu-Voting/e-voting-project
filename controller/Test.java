package controller;

import java.math.BigInteger;

import multipartyComputation.DBHelper;

public class Test {

	public static void main(String[] args) {	
		DBHelper dbHelper = new DBHelper();
		
		dbHelper.registerVoterCredentials(1, new BigInteger("1959761078934109882742455649792176670654596126695823548069"));
		dbHelper.registerVoterCredentials(2, new BigInteger("5696598224898184795332050964555559190233302174218164702620"));
		dbHelper.registerVoterCredentials(3, new BigInteger("5091649207866623379097096625912768095960216891115846119472"));
		dbHelper.registerVoterCredentials(4, new BigInteger("4228725017373180385874637409096222978554649118144201846203"));
	}
	
}
