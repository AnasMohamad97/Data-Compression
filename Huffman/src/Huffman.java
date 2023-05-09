import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


public class Huffman {
	
	
	public static int[] buildFreqTable(String data)
	{
		int[] freq = new int[256];
		for(char c : data.toCharArray())
		{
			freq[c]++;
		}
		return freq;
	}
	
	
	 static Node BuildHuffmanTree(int[] freq)
	{
		 PriorityQueue<Node>PQ = new PriorityQueue<>();
		 
		 for(char  i = 0 ; i < 256 ;++i)
		 {
			 if(freq[i] > 0)
			 {
				 PQ.add( new Node(i , freq[i] , null , null));
			 }
		 }
		 
		 if (PQ.size() == 1)
		 {
			 PQ.add(new Node('\0' , 1 , null ,null));
		 }
		 while(PQ.size() > 1)
		 {
			 Node l = PQ.poll();
			 Node r = PQ.poll();
			 Node parent = new Node('\0' , l.freq + r.freq , l , r);
			 PQ.add(parent);
			
		 }
		 return PQ.poll(); // will return the root node
		
	}
	
	
	static class Node implements Comparable<Node>
	{
		private char character ;
		private int freq;
		private  Node left;
		private  Node right;
		
		private Node( char character , int freq , Node left , Node right)
		{
			this.character = character;
			this.freq  = freq;
			this.left = left;
			this.right = right;
			
		}
		
		boolean isleaf()
		{
			return this.left == null && this.right == null;
		}
		public int compareTo(Node n)
		{
			int freqComparison =  Integer.compare(this.freq, n.freq);
			//if(freqComparison!=0){
				return freqComparison; 
		     //}
			//else return Integer.compare(this.character, n.character);
		}
		
		
	}
	
	
	
	private static Map<Character ,String> BuildLookUpTable(Node root)
	{
		Map<Character , String >lookupTable = new HashMap<>();
		
		buildTable(root  , "" ,lookupTable);
		return lookupTable;
	}
	private static void buildTable(Node node , String s ,Map<Character , String >lookupTable)
	{
		if(!node.isleaf())
		{
			buildTable(node.left , s+'0', lookupTable);
			buildTable(node.right , s+'1', lookupTable);

		}
		else
		{
			lookupTable.put(node.character , s);  
		}
	}
	
	
	public static HuffmanData Compress(String s)
	{
		int[] freq = buildFreqTable(s);
		Node root = BuildHuffmanTree(freq);
		Map<Character , String > lookupTable = BuildLookUpTable(root); 
		
		
		for(Map.Entry<Character, String> entry : lookupTable.entrySet())
		{
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		String encodedBinary = "";
		for(char c : s.toCharArray())
		{
			encodedBinary += lookupTable.get(c);
		}
		System.out.println("");
		System.out.println("the binary encoded message : ");
		System.out.println(encodedBinary);
		
		HuffmanData H = new HuffmanData(encodedBinary , root);
		return H;
        
		
	}
	
	
	public static void decompress(HuffmanData H)
	{
		String str = "";
		String encoded = H.BinaryEncoded;
		Node current = H.getRoot();
		int  i = 0;
		while(i < encoded.length())
		{
			while(!current.isleaf())
			{
				char bit = encoded.charAt(i);
				
				if(bit == '1') {
					current = current.right;
				}
				else if(bit == '0') current = current.left;
				i++;

			}
			str+= current.character;
			current = H.getRoot();
		}
		System.out.println("the decocde message : ");
		System.out.println(str);
	}

	
	static class HuffmanData
	{
		Node root;
		String BinaryEncoded;
		HuffmanData(String Encoded , Node root)
		{
			this.BinaryEncoded = Encoded;
			this.root = root;
		}
		public Node getRoot()
		{
			return this.root;
		}
	}


	

	public static void main(String[] args) 
	{
		String s = "ABBBACDA";
	    Compress(s);
		
	}

}
