import java.util.ArrayList;
import java.util.Scanner;

public class ScalerQuantization {
	
	
	static class Quantity
	{
		public int l;
		public int h;
		public int q;
		public int q_;
		
		public Quantity(int l , int h , int q ,int q_)
		{
			this.l = l;
			this.h = h;
			this.q = q;
			this.q_ =q_;
		}
		
		
	}
	
	public static Quantity[] Quantaize( int levels)
	{
		Quantity[] m = new Quantity[levels];
	    int inc = 128/levels;
	    
		for(int  i = 0 ; i < levels ; ++i)
		{
			  if(i!=0)
			  {
				  int l = m[i-1].h;
				  int h = l+inc;
				  int Q_ = (l+h)/2;
				  m[i] = new Quantity(l , h , i , Q_);
				  
			  }
			  else
			  {
				  int l = 0;
				  int h = inc;
				  m[i] = new Quantity(l , h , i , (l+h)/2 );
				  
			  }
		}
		
		   return m;
	}
	public static void encode(int[] stream , int levels)
	{
		Quantity[] m = Quantaize( levels);
		
		/*for(int  i = 0 ; i < m.length ; ++i)
		{
			//System.out.println(m[i].l +" " + m[i].h + " " + m[i].q + " " + m[i].q_);
			//System.out.println( m[i].q );

		}*/
		ArrayList<Integer> out = new ArrayList<Integer>();
		
		double error = 0.0;
		
		for(int  i = 0 ; i < stream.length ; ++i)
		{
			for(int  j = 0 ; j < m.length ; ++j)
			{
				if(m[j].l <= stream[i] && m[j].h >= stream[i])
				{
					//System.out.print(m[j].q);

					out.add(m[j].q);
					error += (m[j].q_-stream[i]);
					break;
				}
			}
		}
		
		System.out.println( "Mean square error" +  Math.pow(error, 2.0)/10.0   );

		for(int  i = 0 ; i < out.size() ; ++i)
		{
			System.out.print( Integer.toBinaryString(out.get(i))   );
		}

		
		
	}

	public static void main(String[] args) {
		
		
		Scanner scan = new Scanner(System.in);
		
		int Bits = scan.nextInt();
		int levels = (int) Math.pow(2.0, (double)Bits);
		
        int[] stream = new int[10];
		for(int i = 0 ; i < 10 ;i++)
		{
			stream[i] = scan.nextInt();
		}
		encode(stream,levels);
		
		

	}

}
