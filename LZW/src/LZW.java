import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

public class LZW {
	
	static HashMap<Integer , Integer >tag = new HashMap();
	
	public static void compress()
	{
		ArrayList<String> Dic = new ArrayList<String>();
		
    	Scanner scan =  new Scanner(System.in);
		String read  = scan.next();
		char[]text = read.toCharArray();
		
		
		for(int i = 0 ; i < 128 ; ++i)
		   Dic.add(""+(char)i);
		
		int index = 0 , key = 0;
		
		while(index < text.length)
		{
			if( Dic.contains(text[index]+"" ) )
			{
				String tmp = ""+text[index];
				boolean ok = true;
				int i = index+1 ;
				
				for(; i < text.length ; ++i)
				{
					if(Dic.contains(tmp+text[i]))
					{
		                   tmp+=text[i];
		                   ok = false;
					}
					else
					{
						ok = true;
						Dic.add(tmp+text[i]);
						tag.put(key++ , Dic.indexOf(tmp));
						index = i-1;
						break;
					}
				}
				if(!ok)
				{
					tag.put(key++,Dic.indexOf(tmp));
				}
				
			}
			index++;
			
		}
		
		
		System.out.println("the Compressed text tags : " + tag.size());		
		for(Entry<Integer, Integer> entry: tag.entrySet())
		{
			Integer str = entry.getValue();
			//System.out.println(1);
			System.out.println(str);
		}
		
		
		
		
	}
	
	public static void Decompress()
	{
		ArrayList<String> Dic = new ArrayList<String>();
		
		for(int  i = 0 ; i < 128 ; ++i)
		{
			Dic.add(""+(char)i);
		}
		
		int index = 0  ;
		String prev = "" , current = "" , Decomp = "";
		
		
		for(Entry<Integer , Integer>entry: tag.entrySet())
		{
			Integer v = entry.getValue();
			
			
			if(v < Dic.size())
			{
				current = Dic.get(v) + "";
				Decomp+=current;
				
				if(prev !="")
				{
					prev = prev + current.charAt(0);
					//System.out.println(Dic.size()+" "+prev);

					Dic.add(prev);
				}
				
			
				prev = current;

			}
			
			else
			{
				Dic.add(prev+prev.charAt(0));
				current = Dic.get(v);
				Decomp+=current;
				prev = current;	
			     
			}
		}
		System.out.println("the Decompressed text :");
	    System.out.println(Decomp);

		
		
	}
	
public static void main(String[] args) {
		
		compress();
		Decompress();
		
	}

}
