import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Arithmatic_Coding {
	
	static HashMap<Character , CharProbability> tag = new HashMap();

	public static int[]  buildFreqTable(String str)
	{
		int[] freq = new int[256];
		for(char c : str.toCharArray())
		{
			if(c != ' ')freq[c]++;
		}
		return freq; 
	}
	
	
	
	
	static class CharProbability
	{
		public char c ;
		public double Low ;
		public double High;
		
		public void setChar(char c )
		{
		   this.c = c;	
		}
		public void setHigh(double High )
		{
			this.High = High;
		}
		public void setLow(double Low )
		{
			this.Low = Low;
		}
		CharProbability(char c ,double d , double e)
		{
			this.c = c;
			this.High = e;
			this.Low = d;
		}
		
	}
	
	
	
	
	static double Compress( String Data) throws FileNotFoundException 
	{
		int[]freq = buildFreqTable(Data);
		int total = 0 , cnt = 0;
		for(int  i = 0 ; i < 256 ; ++i)
		{
			total+=freq[i];
			if(freq[i] == 0)cnt++;
			
		}
		//System.out.println(total);
		ArrayList<CharProbability>P = new ArrayList<CharProbability>();

		//CharProbability a = new CharProbability('A' , new BigDecimal("0.0") ,new BigDecimal("0.8") );
		//CharProbability b = new CharProbability('B' ,new BigDecimal( "0.8") ,new BigDecimal ("0.82") );
		//CharProbability cc = new CharProbability('C' ,new BigDecimal( "0.82") , new BigDecimal("1.0") );

		CharProbability a = new CharProbability('A' , 0.0 ,0.8 );
		CharProbability b = new CharProbability('B' , 0.8 ,0.82 );
		CharProbability cc = new CharProbability('C' , 0.82 ,1 );


		tag.put('A', a);
		tag.put('B', b);
		tag.put('C', cc);

		

		
		/*int j = 0;
		for(int i = 0 ; i < 256 ; ++i)
		{
			if(freq[i]!=0)
			{
				double H = (double)freq[i]/(double)total;

				
				if(j == 0)
				{
					CharProbability p = new CharProbability((char)i , 0.0 , H);
					P.add(p);
					tag.put((char)i, p);
				}
				else
				{
					CharProbability x = P.get(j-1);
					CharProbability p = new CharProbability((char)i , x.High , x.High+H);
					P.add(p);
					tag.put((char)i, p);

				}
				j++;
			}
			
		}
		
		
		/*for(int  i = 0 ; i  <  j ; ++i) {
			CharProbability x = P.get(i);
			System.out.println(x.c + " : " + x.Low + " : " + x.High);
		}*/
		
		
	   /* BigDecimal l = new BigDecimal( "0.0");
	    BigDecimal u = new BigDecimal( "1.0");
	    BigDecimal compression_code =new BigDecimal( "0.0");
	    BigDecimal old_u = new BigDecimal( "1.0");*/
		
		double l = 0.0;
	    double u =1.0;
	    double compression_code = 0.0;
	    double old_u =  1.0;
		for(char c : Data.toCharArray())
		{
		    
			CharProbability x = tag.get(c);
			
			 old_u = u;
			// u = l.add(u.subtract(l).multiply(x.High));
			 //l = l.add(old_u.subtract(l).multiply(x.Low));
			 
		   //System.out.println(u + " " + l + " ");

		    u = l + ((u-l)*x.High);
		    l = l + ((old_u-l)*x.Low);
		    
		}		
		compression_code = l;
		//System.out.println(l);
		
		File file = new File("/Users/anasmohamadsobhi/Java_WorkSpace/ArithmaticEncoding/Compressed.txt");
		try
		{
			FileWriter fileWriter = new FileWriter("Compressed.txt");
			BufferedWriter bufferedWriter =
	                new BufferedWriter(fileWriter);
			String s = Double.toString(l);
			bufferedWriter.write(s);
            bufferedWriter.close();
		}
		catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + "Compressed.txt" + "'");
            // Or we could just do this:
            // ex.printStackTrace();

   }
	       return 	compression_code;	

		
}
	
	
	
	
   static void Decompress(String FileName) throws IOException
   {
		String line = null;
		String Data = "";

	   try
		{
			FileReader fileReader = new FileReader(FileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null)
			{
				Data += line;
				//System.out.println(line);
			}
			bufferedReader.close();
		}catch(FileNotFoundException ex)
		{
			System.out.println("Error Reading File " + FileName + "'");
		}
        
	   double code = Double.parseDouble(Data);

	   
	   double l = 0.0;
	   double u = 1.0;
	   double interval = code;
;
	   String text = ""; 
	   int cnt = 0;
	   for( ; ; )
	   {
			for(Entry<Character, CharProbability> entry: tag.entrySet())
			{
				CharProbability x = entry.getValue();
				
				//System.out.println(interval);
				
				if(x.Low <= interval && interval <= x.High)
				{
					
					text+=x.c;
				//	System.out.print(x.c);
				//	System.out.println(interval);
                   double old_u = u;
					u = l + ((u-l)*x.High);
					l = l + ((old_u-l)*x.Low);
					interval = (code-l)/(u-l);
					//System.out.println(interval);
				//	System.out.print(interval);
					cnt++;
					break;
					
				}
			}
			if(cnt == 4)break;
   
	   }
	   System.out.println(text);
	   
	   
	   File file = new File("/Users/anasmohamadsobhi/Java_WorkSpace/ArithmaticEncoding/DeCompressed.txt");
		try
		{
			FileWriter fileWriter = new FileWriter("DeCompressed.txt");
			BufferedWriter bufferedWriter =
	                new BufferedWriter(fileWriter);
			//String s = Double.toString(l);
			bufferedWriter.write(text);
           bufferedWriter.close();
		}
		catch(IOException ex) {
           System.out.println(
               "Error writing to file '"
               + "Compressed.txt" + "'");
           // Or we could just do this:
           // ex.printStackTrace();

  }
	   
	   
	   
   }
		
	
		

	public static void main(String[] args) throws IOException  {
		
		
		String fileName = "temp.txt";
		String line = null;
		String Data = "";
		
		File file = new File("/Users/anasmohamadsobhi/Java_WorkSpace/ArithmaticEncoding/temp.txt");
		  
		//Create the file
		/*if (file.createNewFile())
		{
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}*/
		
		try
		{
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null)
			{
				Data += line;
				//System.out.println(line);
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex)
		{
			System.out.println("Error Reading File " + fileName + "'");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		Compress(Data);
		Decompress("Compressed.txt");

	}

}
