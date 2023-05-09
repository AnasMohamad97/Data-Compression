import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class LZ78 {

	static ArrayList<String>Dic = new ArrayList<String>();
	static HashMap<Integer , String> tag = new HashMap();
	
	
	
    public static void compress()
	{
    	Scanner scan =  new Scanner(System.in);
		String read  = scan.next();
		
		char[]text = read.toCharArray();
		
		Dic.add(null);
		int index = 0 , key =0;
		
		
		while(index < text.length)
		{
			
			String cur = "" , pre = "";
			if(Dic.contains(text[index]+"") )
			{
				if(index+1 == text.length ) {
				  tag.put(key++, "<"+0+","+text[index]+">");

				}
			}

			if( Dic.contains(text[index]+"")  )
			{

				cur +=text[index];

				
				for(int i = index+1 ; i < text.length ; ++i)
				{
					
					if( Dic.contains(cur+text[i]) )
					{
						pre = cur;
						cur +=text[i];
					}
						
					else
					{
						Dic.add(cur+text[i]);
						pre = cur;
						index = i;
						tag.put(key++, "<"+Dic.indexOf(pre)+","+text[i]+">");
						break;
					}
				}
			}
			
			else 
			{
				//System.out.println(text[index]+"");

				Dic.add(text[index]+"");
			    tag.put(key++, "<"+0+","+text[index]+">");
			}
			index++;
			
		}
		
		System.out.println("the Compressed text tags : ");		
		for(Entry<Integer, String> entry: tag.entrySet())
		{
			String str = entry.getValue();
			System.out.println(str);
		}
		
		
		
		
	}
    
	public static void decompress() {
		
		String decompressed = "";
	    ArrayList<String>New_Dic = new ArrayList<String>();

		
		for(Entry<Integer, String> entry: tag.entrySet())
		{
			String str = entry.getValue();
			int end =  str.indexOf(",");

			
			String temp = str.substring(1 , end);
			int key_Dic = Integer.parseInt(temp);
			
			if(key_Dic == 0)
				{
				
                  New_Dic.add(str.substring(end+1 , str.length()-1));	
                  decompressed+=str.substring(end+1 , str.length()-1);
				}
			else
			{
				String add ="";
				add = New_Dic.get(key_Dic-1)+str.substring(end+1 , str.length()-1);
				decompressed+= add;
				New_Dic.add(add);
			}
				
		}
		System.out.println("the Decompressed text : ");
		System.out.println(decompressed);
		
		
		
	}
	
	public static void main(String[] args) {
		
		compress();
		decompress();
		
	}

}
