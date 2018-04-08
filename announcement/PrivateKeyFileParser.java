package announcement;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

public class PrivateKeyFileParser {
	
	private String privateKeyPath;
	
	public PrivateKeyFileParser(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}
	
	public LinkedHashMap<String,String> parsePrivateKeyFile()
	{
		
		LinkedHashMap<String,String> privateKeyMap = new LinkedHashMap<String,String>();
		
	    try
	    {
		   List<String> result = Files.readAllLines(Paths.get(privateKeyPath), StandardCharsets.UTF_8);

		   for( int k=0; k<result.size(); k++){
		     
		      String line = result.get(k);

		      if(k == 0)
		      {
		    	  privateKeyMap.put("lambda: ", line);
		      }
		      else
		      {
		    	  privateKeyMap.put("u: ", line);
		      }
		   }
		   
		}
	    catch(Exception e)
	    {
		   System.out.println("Could not read the private key file! " + e);
		}
	    
	    return privateKeyMap;
	}

}
