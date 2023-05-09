import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;


public class LBG {
	
	static int Imagewidth;
	static int Imageheight;

	
	
	
	

	static int[][]  ReadImage(String filename)
	{
		File dir = new File(filename);
		int[][] imgMatrix = null;
		
		
		try {
			
			
	         BufferedImage img = ImageIO.read(dir);
	         
	         
             Imagewidth = img.getWidth();
             Imageheight = img.getHeight();
            
           // System.out.println(width);
           // System.out.println(height);


            imgMatrix=new int[Imageheight][Imagewidth];

            for (int y = 0; y < Imageheight; y++)
            {
                for (int x = 0; x < Imagewidth; x++)
                {
                    int p = img.getRGB(x,y);
                    int a = (p>>24)&0xff;
                    int r = (p>>16)&0xff;
                    int g = (p>>8)&0xff;
                    int b = p&0xff;

                    //because in gray image r=g=b  we will select r

                    imgMatrix[y][x]=r;


                    //set new RGB value
                    p = (a<<24) | (r<<16) | (g<<8) | b;
                    img.setRGB(x, y, p);
                }
            }
        }
		catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		return imgMatrix;
		
	}
	
	 public static void WriteImage(int[][] imageMatrix,String imageoutPath){

	        int height=imageMatrix.length;
	        int width=imageMatrix[0].length;
	        BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);

	        for (int y = 0; y < height; y++)
	        {
	            for (int x = 0; x < width; x++){

	                int a=255;
	                int pix=	imageMatrix[y][x];
	                int p=  (a<<24)	| (pix<<16) | (pix<<8) | pix;

	                img.setRGB(x, y, p);

	            }
	        }

	        File f = new File("k.jpg");

	        try {
	            ImageIO.write(img, "jpg",f);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	
	public static void Encoder(int[][] img ,int levels  , int height , int width )
	{
	    Vector<int[]> vv = new Vector<int[]>(); // splited vectors

		HashMap< Integer ,Vector<int[]> >intial = new HashMap();
		
		Vector<int []> Blocks = Divide_into_Blocks(img  , height , width ); // 1st -> divide into blocks
		
		intial.put(0, Blocks);
		vv = split( intial , height , width);
		
		HashMap< Integer ,Vector<int[]> >Near = new HashMap();
		Near = cluster(Blocks , vv);

		

       while(true)
       {
    	  

    	  if(vv.size() < Math.pow(2,levels))
    	  {
    		//  vv.clear();
    		  vv =  split(Near , height , width);  // puts in v
    		//  Near.clear();
    		  Near = cluster(Blocks , vv);
    		  

    	  }	
    	  
    	  else
    	  {
    		 Vector<int[]> flag = new Vector<int[]>();
    		 flag = Get_Avarge(Near , height , width);
    
    		 if( !check(vv , flag  ) )
    		  {
    			 break;
    		  }
    		//  vv.clear();
    		  vv = flag;
    		//  Near.clear();
    		  Near = cluster(Blocks , vv);
    		  
    	  }
    	  
       }
     
       
       Vector<int []> NEW_Blocks = Map(Blocks , vv , Near);
       
       WriteFiles(vv , NEW_Blocks );
       
      int[][]compressedImage = Fill_The_Image(NEW_Blocks , height , width);
       
      System.out.print("write");
      WriteImage(compressedImage , "/Users/anasmohamadsobhi/Java_WorkSpace/Vector_Quantization");
       
       		
		
	}
	
	
	
	
	public static Vector<int[]> Divide_into_Blocks(int [][]img , int n , int m)
	{
		Vector<int[]>Blocks = new Vector<int[]>();
		

        for (int i = 0; i < Imageheight; i += n) {
            for (int j = 0; j < Imagewidth; j += m) {
                int[] arr = new int[n * m];
                int idx2 = 0;
                for (int k = 0, z = i; k < n; ++k, ++z) {
                    for (int l = 0, zz = j; l < m; ++l, ++zz) {
                        if (z >= Imageheight|| zz >= Imagewidth)
                            arr[idx2++] = 0;
                        else
                            arr[idx2++] = img[z][zz];
                    }
                }
                Blocks.add(arr);
            }
        }
	
		
		return Blocks;
		
	}
	
	public static Vector<int[]> split(HashMap< Integer ,Vector<int[]> >clusters , int h , int w )
	{
	   Vector<int[]> v = new Vector<int[]>(); // splited vectors
		
		for(int c = 0 ; c < clusters.size() ; ++c) 
	    {
			
			
			int []  arr = new int[h*w];	
		Vector<int[]>Blocks = clusters.get(c);
        double  total = Blocks.size();

		for(int i = 0 ; i < h*w ; ++i )
		{
			for(int j = 0 ; j < Blocks.size() ; ++j)
			{
				arr[i] += Blocks.get(j)[i];
			}
		}
	
		int []  arr1 = new int[h*w];
		int []  arr2 = new int[h*w];	


		for(int  i = 0 ; i < h*w ; ++i)
		{
			arr1[i] = (int)Math.floor((double)arr[i]/total );
			arr2[i] = (int)Math.ceil((double)arr[i]/total );

		}
		v.add(arr1);
		v.add(arr2);
		
		
	 }
		
		return v;
		
   }
	
	public static HashMap< Integer ,Vector<int[]> > cluster(Vector<int[]>Blocks ,Vector<int[]> v )
	{
	 HashMap< Integer ,Vector<int[]> >Near = new HashMap<Integer ,Vector<int[]>>(v.size());
	 
	 for(int i  = 0  ; i < v.size() ; ++i)
	 {
		 Vector<int[]>tmp = new Vector();
		 Near.put(i, tmp );
	 }

		for(int i = 0  ; i < Blocks.size() ; ++i )
		{
			int mn = 999999999 , index = 0;
			for(int  j = 0 ; j < v.size() ; ++j)
			{
				int dis = find_Dis(Blocks.get(i) , v.get(j));
				if(dis <  mn )
				{
					mn = dis;
					index = j; // the quantizer at the j-nth position
				}
			}
			
			Near.get(index).add(Blocks.get(i));
		}
		
		return Near;
		
	}
	
	public static int find_Dis(int[]a ,  int[]b)
	{
		int dis = 0;
		for(int  i = 0 ; i < a.length ; ++i)
		{
			dis += Math.abs((a[i] - b[i]));
		}
		return dis;
	}
	
	
	public static Vector<int[]>  Get_Avarge( HashMap< Integer ,Vector<int[]> > clusters , int h , int w  )
	{
		 Vector<int[]> v = new Vector<int[]>(); //

		for(int  c = 0 ; c < clusters.size() ; ++c )
		{
			int []  arr = new int[h*w];	
			Vector<int[]>Blocks = clusters.get(c);
	        double  total = Blocks.size();
	        
	        for(int i = 0 ; i<h*w ; ++i)
	        {
	        	for(int  j = 0 ; j<total ; ++j)
	        	{
	        		arr[i] += Blocks.get(j)[i];
	        	}
	        	arr[i]= (int)Math.floor((double)arr[i]/total);
	        }
	        v.add(arr);
		}
		return v;
	}
	
	
	public static boolean check(Vector<int[]> a , Vector<int[]> b)
	{
		boolean bool = false;
		
		for(int  i = 0 ; i < a.size() ; ++i)
		{
			for(int j = 0 ; j < a.get(i).length ; ++j)
			{

				if(a.get(i)[j] != b.get(i)[j])
					return true;
			}
		}
		
		return bool;
	}
	
	public static Vector<int[]> Map (Vector<int[]>Blocks ,Vector<int[]>vv , HashMap< Integer ,Vector<int[]> > clusters )
	{
		
		for(int i = 0 ; i < Blocks.size() ; ++i)
		{
			int[] b = Blocks.get(i);
			for(int j = 0  ; j < clusters.size() ; ++j)
			{
                    for(int k = 0 ; k < clusters.get(j).size(); ++k)
                    {
                    	if(equal(b , clusters.get(j).get(k)))
                    		
                    	{
                    		//System.out.println("here");
                    		//Blocks.set(i, vv.elementAt(j)); // lw 3alt b2a yb2 hena
                    		Blocks.setElementAt(vv.elementAt(j), i);
                    	}
                    }
			}
		}
		
		
		return Blocks;
		
	}
	
	
	public static boolean equal(int a[],int b[])
	{
		for(int i = 0 ; i < a.length ; ++i)
		{
			if(a[i]!=b[i])return false;
		}
		return true;
	}
	
	
	
	public static void WriteFiles(Vector<int[]> v ,Vector<int[]> blocks )
	{
       
        File file = new File("/Users/anasmohamadsobhi/Java_WorkSpace/VectorQuantization/CodeBook.txt");
        try
        {
       	 FileWriter fileWriter = new FileWriter("CodeBook.txt");
			BufferedWriter bufferedWriter =
	                new BufferedWriter(fileWriter);
			
			for(int  i = 0 ; i < v.size(); ++i)
			{
				String s = Integer.toString(i);
				bufferedWriter.write(s+"\n");
				
				for(int j = 0 ; j < v.get(i).length ; ++j)
				{
					String ss = Integer.toString(v.get(i)[j]);
					bufferedWriter.write(ss+" ");

				}
	 				bufferedWriter.write("\n");	

			}
			
			bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + "Compressed.txt" + "'");
            // Or we could just do this:
            // ex.printStackTrace();

              }
        
        
        File f = new File("/Users/anasmohamadsobhi/Java_WorkSpace/VectorQuantization/compressed.txt");
        try
        {
       	 FileWriter fileWriter = new FileWriter("compressed.txt");
			BufferedWriter bufferedWriter =
	                new BufferedWriter(fileWriter);
			
			for(int  i = 0 ; i < blocks.size(); ++i)
			{
				
				for(int j = 0 ; j < blocks.get(i).length ; ++j)
				{
					String ss = Integer.toString(blocks.get(i)[j]);
					bufferedWriter.write(ss+" ");

				}
	 				bufferedWriter.write("\n");	

			}
			
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
	
	
	
     public static int[][] Fill_The_Image(Vector<int[]> Blocks , int h , int w)
     {
    	 int[][] img = new int[Imageheight][Imagewidth];
    	 int b = 0;
    	 for(int  i = 0 ; i < Imageheight ; i+=h)
    	 {
    		 for(int  j = 0 ; j < Imagewidth ; j+=w)
    		 {
                         			 
    				 int[] arr = Blocks.elementAt(b++);
    				 int idx = 0;
    				 
    				 for(int k = 0 , ix = i ; k < h ; ++k , ++ix)
    				 {
    					 for(int l = 0 , jx = j ; l < w ; ++l ,++jx)
    					 {
							// System.out.print(idx+" "+ix+" "+jx+" ");      

    					   if(ix >= h || ix<0 || jx >= w || jx<0 || idx >= h*w);
    						 
    					     else img[ix][jx] = arr[idx++];
    					 }
    					 //System.out.println("");
    				 }
    			 
    		 }
    	 }
		return img;
    	 
     }
	
	
	
	public static void main(String[] args) {
		
	   int [][] img = ReadImage("1.jpg");
	   
	   
	   
	    Scanner scan = new Scanner(System.in);
	    System.out.print("Enter the number of levels : ");
	    int l = scan.nextInt();
	    System.out.print("Enter the vector codebook height : ");
	    int h = scan.nextInt();
	    System.out.print("Enter the vector codebook width : ");
	    int w = scan.nextInt();



	   Encoder(img , l , h , w);
		

	}

}
