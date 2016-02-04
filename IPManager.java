import java.util.*;
import java.io.*;
public class IPManager{
	
	static HashMap<String,Boolean> map = new HashMap<>();
		
	static String str= null;
	public static void initializeMap() throws Exception{
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the first three bytes of the  DHCP server : ");
		str = input.readLine();
		for(int i=1;i<=254;i++){
			if(i>99)
				map.put(str+i,new Boolean(false));
			else if(i>9)
				map.put(str+"0"+i,new Boolean(false));
			else
				map.put(str+"00"+i,new Boolean(false));		
		}
		
	}
	
	public static void freeEntry(String key){
		map.remove(key);
		map.put(key,new Boolean(false));
	}
	
	public static String getRandomFreeIP(){
	
		while(true){
				int randomNum = (int) (Math.random()*254);
				String key = generateKey(randomNum);
			if(!map.get(key).booleanValue()){
				map.remove(key);
				map.put(key,new Boolean(true));		
				return key;
			}
		}
		//return "error";
		
	}
	
	public static String generateKey(int number){
		
		String str = IPManager.str;		
		if(number>99)
			str = str+number;
		else if(number>9)
			str = str+"0"+number;
		else
			str = str+"00"+number;
		
		return str;
			
	}
	
/*	public static void main(String [] args) throws Exception{
	
		initializeMap();
		String str = getRandomFreeIP();
		System.out.println(str);
		freeEntry(str);
	}
	*/
}



