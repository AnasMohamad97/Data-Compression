import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

public class AnasMohamad_LZ77 {

	
	public static void decompression()
	{
		 ArrayList<String>Dic = new ArrayList<String>();
		 HashMap<Integer , String> tag = new HashMap();
		 
		 Scanner scan =  new Scanner(System.in);
		int n = scan.nextInt();
		String read ;		

		for(int i = 0 ; i < n ; ++i) {
		   read  = scan.next();
		   tag.put(i,read);  
		}		
		String Decomp = "";

		
		for(Entry<Integer, String> entry: tag.entrySet())
		{
	          
			String str = entry.getValue();
			 
			int first_end = str.indexOf(",");
			int last_end = str.lastIndexOf(",");
			
			String tmp1 = str.substring(1,first_end);
			String tmp2 = str.substring(first_end+1 , last_end);
			String next_char = str.substring(last_end+1 , str.length()-1);
			
			
			int go_back = Integer.parseInt(tmp1);
			int move = Integer.parseInt(tmp2);

			
			if(go_back == 0) {
				Decomp+=next_char;
			}
			else
			{
				char[] text = Decomp.toCharArray();
				for(int i = Decomp.length() - go_back , j=0; j < move; ++j) {
					Decomp = Decomp + text[i+j];
				}
				Decomp+=next_char;
			}
			
			
		}
		System.out.println(Decomp);
		
			
			
			
	}
  


	
	
	public static void main(String[] args) {
		
	
		decompression();


	}

}
