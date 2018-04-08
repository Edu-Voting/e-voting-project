package announcement;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

public class FileParser {
	
	private String resultPath;
	
	public FileParser(String resultPath) {
		this.resultPath = resultPath;
	}
	
	public LinkedHashMap<String,String> parseResultFile()
	{
		
		LinkedHashMap<String,String> resultMap = new LinkedHashMap<String,String>();
	
	    try
	    {
		   List<String> result = Files.readAllLines(Paths.get(resultPath), StandardCharsets.UTF_8);

		   for( int k=0; k<result.size(); k++){
		     
		      String line = result.get(k);
		      if(k < 2)
		      {
		    	  String[] firstLine = line.split(":");
		    	  resultMap.put(firstLine[0], firstLine[1]);
		      }
		      else if(k != 2)
		      {
		    	  String[] newLine = line.split("=>");
		    	  resultMap.put(newLine[0], newLine[1]);
		      }
		   }
		   
		}
	    catch(Exception e)
	    {
		   System.out.println("Could not read the result file! " + e);
		}
	    
	    return resultMap;
	}
}
