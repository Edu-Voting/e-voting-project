package Paillier;

import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;

public class CreatingEvoting implements ICreateElection{
	
	private BigInteger maxCountOfVoter;
	private int candidateCount;
	
	private int maxBitLengthToRepresent;
	//private BigInteger encryptedResult;
	
	private final int certainty = 64;
	private int bit_len = 512;
	
	// pu => (n, g)
	// pr => (lambda, u)
	private BigInteger p, q;
	private BigInteger lambda, n, n_square, g, u;
	
	public CreatingEvoting (BigInteger maxCountOfVoter, int candidateCount) throws Exception{
		if(maxCountOfVoter.compareTo(BigInteger.ZERO) <= 0){
			throw new Exception("Maximum count of voter must be greater than zero!");
		}
		
		this.candidateCount = candidateCount;
		this.maxCountOfVoter = maxCountOfVoter;
		
		maxBitLengthToRepresent = maxCountOfVoter.bitLength();
		//encryptedResult = BigInteger.ONE;
		
		Paillier();
		
		
		
	}
	
	private void Paillier(){
		// Generating p and q 
		//this.p = Math.rPrime(bit_len / 2, certainty);
		this.p = new BigInteger("93258190053368335360689362753326970901156376714515609683660739891440089168229");
		//this.q = Math.independentRPrime(bit_len / 2, certainty, p);
		this.q = new BigInteger("67179543309668099143277329383413232455356339731457773568178761757060960779963");
		
		// Generating lambda and u
		// lambda = lcm(p-1, q-1)
		this.lambda = Math.carmichael(p, q);
		this.n = p.multiply(q);
		this.n_square = n.multiply(n);
		// gcd(L(g^lambda mod n^2), n) must be equal 1
		do{
			//g = Math.random(bit_len * 2, n_square);
			g = new BigInteger("11738903305571125623357275755757996486023846696891322291725888051491778958401872825508696108745305238256712612601774764526182946475828957413791020910054354778828028161359453840988595296293067035051332878398563806666838929962328706202876675246266501939283785729475724608589511297160296153098856792187205327542");
			// Until gcd(L(g^lambda mod n^2), n) is equal 1
		}while(L(g.modPow(lambda, n_square)).gcd(n).intValue() != 1);
		
		// u = (L(g^lambda mod n^2))^{-1} mod n
		u = L(g.modPow(lambda, n_square)).modInverse(n);  
		
		savePr("paillierPr");
		savePu("paillierPu");
		
	}
	// L(x) = (x-1)/n
	private BigInteger L(BigInteger x) {        
        return x.subtract(BigInteger.ONE).divide(n);
    }
	
	private void savePr(String path){
		try{
			
			PrintStream ps = new PrintStream(new File(path));
			//ps.println(this.lambda.toString(16));
			//ps.println(this.u.toString(16));
			ps.println(this.lambda.toString());
			System.out.println("PR1: " + lambda);
			ps.println(this.u.toString());
			System.out.println("PR2: " + u);
			ps.close();
			
			// Removing variables of private key 
			this.lambda = null;
			this.u = null;
			
		}catch(Exception e){
			e.printStackTrace();;
		}
		
	}
	
	private void savePu(String path){
		  try{
		   
		   PrintStream ps = new PrintStream(new File(path));
		   ps.println(this.n.toString(16));
		   ps.println(this.g.toString(16));
		   ps.close();
		   
		   
		  }catch(Exception e){
		   e.printStackTrace();;
		  }
		  
	}
	
	@Override
	public BigInteger getPuN(){
		return this.n;
	}
	
	@Override
	public BigInteger getPuG(){
		return this.g;
	}
	
	@Override
	public int getMaxBitLength(){
		return this.maxBitLengthToRepresent;
	}
	
	@Override
	public BigInteger getMaxCountOfVoter(){
		return this.maxCountOfVoter;
	}
	
	@Override
	public int getCandidateCount(){
		return this.candidateCount;
	}

	

}