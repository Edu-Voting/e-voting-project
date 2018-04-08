package announcement;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

public class PublicKeyFileParser {
	
	private String publicKeyPath;
	
	public PublicKeyFileParser(String publicKeyPath) {
		this.publicKeyPath = publicKeyPath;
	}
	
	public LinkedHashMap<String,String> parsePublicKeyFile()
	{
		
		LinkedHashMap<String,String> publicKeyMap = new LinkedHashMap<String,String>();
	
	    try
	    {
		   List<String> result = Files.readAllLines(Paths.get(publicKeyPath), StandardCharsets.UTF_8);

		   for( int k=0; k<result.size(); k++){
		     
		      String line = result.get(k);

		      if(k == 0)
		      {
		    	  publicKeyMap.put("n: ", line);
		      }
		      else
		      {
		    	  publicKeyMap.put("g: ", line);
		      }
		   }
		   
		}
	    catch(Exception e)
	    {
		   System.out.println("Could not read the result file! " + e);
		}
	    
	    return publicKeyMap;
	}

}
