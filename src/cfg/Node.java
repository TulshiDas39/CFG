package cfg;

import java.util.ArrayList;
import java.util.HashSet;

public class Node {

	public char nodeName;
	public ArrayList<Node>childs = new ArrayList<>();
	public HashSet<Integer>lines = new HashSet<Integer>(); 

	public Node(char c, int line) {
		this.nodeName = c;
		this.lines.add(line);		
	}
	
}