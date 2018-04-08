package announcement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileParser parser = new FileParser("countingResults.txt");
		
		LinkedHashMap<String,String> map = parser.parseResultFile();
		Map<String,String> scoreMap = new HashMap<String,String>();
		
		int size = map.size();
		Set<String> keySet = map.keySet();
		Object[] keyArray = keySet.toArray();
		
		for(int i = 0; i < size; i++)
		{
			
			String value = (new ArrayList<String>(map.values())).get(i);
			System.out.println("value once: " + value);
			value = value.trim();
			System.out.println("value: " + value);
			if(i < 2)
			{
				if(i == 0)
				{
					System.out.println("number of valid votes: " + Integer.parseInt(value));
				}
				else
				{
					System.out.println("number of candidates: " + Integer.parseInt(value));
				}
			}
			else
			{
				scoreMap.put(keyArray[i].toString(), value);
				System.out.println("scoreMap: " + scoreMap.toString());
			}
		}

	}

}
