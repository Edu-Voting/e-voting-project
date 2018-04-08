package multipartyComputation;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/*
 * Author : Leyla
 * Date   : January 05, 2017
 */
public class SecretFileHelper {
	
	public SecretFileHelper(){
	}
	
	public static BigInteger readBigIntegerFromFile(String path){
		BigInteger value = null;
		
        try {
            List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            value = new BigInteger(lines.get(0));
        } catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

	public static void saveBigIntegerToFirstLine(String path, BigInteger value){
		List<String> lines=null;
		try {
			lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
			lines.set(0, value.toString());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Files.write(Paths.get(path), lines);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	public static void saveBigIntegerToFile(String path, BigInteger value){
		try{
			PrintStream ps = new PrintStream(new File(path));
			ps.println(value.toString());
			ps.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
