package cfg;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph2 {
	
	public ArrayList<String>lines;
	public static char nodeName = 'A';
	public static int lineNo =0;
	
	public Graph2(ArrayList<String>lines) {
		this.lines = lines;
	}
	
	private HashMap<Character, ArrayList<Character>> visitTree(Node root,ArrayList<Node>visited, HashMap<Character, ArrayList<Character>> table) {
		visited.add(root);
		ArrayList<Node>childs = root.childs;
		Node current;
		
		table.put(root.nodeName, printChild(root));
			for(int i=0;i<childs.size();i++){
					current = childs.get(i);
					if(!visited.contains(current)) visitTree(current,visited,table);
			}
			
		return table;
	}
	
	public ArrayList<Character> printChild(Node node){
		ArrayList<Character> nodes = new ArrayList<>(); 
		System.out.print(node.nodeName+"-->(");
		for(int i=0;i<node.childs.size();i++){
			System.out.print(node.childs.get(i).nodeName);
			if(node.childs.size() - i >1)System.out.print(",");
			nodes.add(node.childs.get(i).nodeName);
		}
		System.out.println(")");
		System.out.print(node.nodeName);
		System.out.println(node.lines);
		return nodes;
	}

	public Node buildGraph(){
 		Node root = new Node(Graph2.nodeName,0);
		Graph2.nodeName++;
		buildChild(root, true);
		
		ArrayList<Node>visited = new ArrayList<>();
		HashMap<Character, ArrayList<Character>> table = new HashMap<>();
		visitTree(root,visited,table);
		int size = table.keySet().size();
		int arr[][] = new int[size][size];
		create2Darray(table,arr);
		System.out.println();
		System.out.println("The graph matrix:");
		printMatrix(arr);
		int cyclomaticComplexity = 0;
		System.out.println();
		System.out.println("Indepent paths:");
		cyclomaticComplexity = findPaths(root,"",cyclomaticComplexity);
		
		System.out.println();
		System.out.println("The cyclomatic complexity: "+cyclomaticComplexity);
		
		return root;
	}

	private int findPaths(Node root, String string, int cyclomaticComplexity) {
		boolean isLoop = false;
		if(string.contains(String.valueOf( root.nodeName))) isLoop = true;
		string =string.concat(String.valueOf(root.nodeName));
		if(root.childs.isEmpty()){
			System.out.println(string);
			cyclomaticComplexity++;
		}
		else string = string.concat("-->");
		ArrayList<Node>childs = root.childs;
		Node current;
		
			for(int i=0;i<childs.size();i++){
					current = childs.get(i);
					if(!string.contains(String.valueOf( current.nodeName)) || !isLoop)
						cyclomaticComplexity = Math.max(cyclomaticComplexity, findPaths(current,string,cyclomaticComplexity));
			}
			return cyclomaticComplexity;
	}


	private void printMatrix(int[][] arr) {
		System.out.print("\t");
		char c = 'A';
		for(int i =0;i<arr.length;i++){
			c = (char) ('A' + i);
			System.out.print(c);
			System.out.print("\t");
		}
		System.out.println();
		for(int i = 0;i<arr.length;i++){
			char ch = (char) ('A' +i);
			System.out.print(ch);		
			for(int j =0;j<arr.length;j++){
				System.out.print("\t"+arr[i][j]);
			}
			System.out.println();
		}
		
	}

	private void create2Darray(HashMap<Character, ArrayList<Character>> table, int[][] arr) {
		for(int i = 0;i<arr.length;i++){
			for(int j = 0;j<arr.length;j++){
				arr[i][j] = 0;
			}
		}
		char ch = 'A';
		for(int i =0;i<table.keySet().size();i++){
			for(int j =0; j<table.get(ch).size(); j++){
				arr[i][(char)(table.get(ch).get(j) - 'A')] = 1;
			}
			ch++;
		}
		
		
	}

	private Node buildChild(Node parent, boolean isMultipleLine) {
		Node statement_node = null ;
		ArrayList<Node> lastNodesInBranch = new ArrayList<>();
		lineNo++;
		
		while(true){
			if(lineNo>=lines.size())return null;
			
			if(this.lines.get(lineNo).contains("else if")){
				
				Node newNode = new Node(nodeName,lineNo);
				nodeName+=1;
				parent.childs.add(newNode);
				newNode = buildChild(newNode, isBlock(newNode));
				if(newNode == null) return null;
				lastNodesInBranch.add(newNode);
			}
			
			else if(this.lines.get(lineNo).contains("if")){
				Node newNode = new Node(nodeName,lineNo);
				nodeName+=1;
				
				if(!lastNodesInBranch.isEmpty()){
					addParents(newNode, lastNodesInBranch);
					lastNodesInBranch.clear();
				}
				else{
					parent.childs.add(newNode);
				}
				parent = newNode;
				newNode = buildChild(newNode, isBlock(newNode));
				if(newNode == null) return null;
				lastNodesInBranch.add(newNode);
				
				statement_node = null;
				
			}
			
			else if(this.lines.get(lineNo).contains("else")){
				Node newNode = new Node(nodeName,lineNo);
				nodeName+=1;
				parent.childs.add(newNode);
				newNode = buildChild(newNode, isBlock(newNode));
				if(newNode == null) return null;
				lastNodesInBranch.add(newNode);
				
			}
			
			else if(this.lines.get(lineNo).contains("for") ||
					( this.lines.get(lineNo).contains("while") && !this.lines.get(lineNo).contains("}while"))){
				Node newNode = new Node(nodeName,lineNo);
				nodeName+=1;
				
				if(!lastNodesInBranch.isEmpty()){
					addParents(newNode, lastNodesInBranch);
					lastNodesInBranch.clear();
				}
				else{
					parent.childs.add(newNode);
				}
				parent = newNode;
				lastNodesInBranch.add(parent);
				newNode = buildChild(newNode, isBlock(newNode));
				if(newNode == null) return null;
				newNode.childs.add(parent);
				/*lastNodesInBranch.add(newNode);*/
				parent = newNode;
				statement_node = null;
				
			}
			
			else if(this.lines.get(lineNo).contains("do")){
				Node newNode = new Node(nodeName,lineNo);
				nodeName+=1;
				
				if(!lastNodesInBranch.isEmpty()){
					addParents(newNode, lastNodesInBranch);
					lastNodesInBranch.clear();
				}
				else{
					parent.childs.add(newNode);
					
				}
				parent = newNode;
				newNode = buildChild(newNode, isBlock(newNode));
				if(newNode == null) return null;
				newNode.childs.add(parent);
				parent = newNode;
				statement_node = null;
				
			}
			
			else{
				if(lines.get(lineNo).trim().isEmpty()){
					lineNo++; continue;
				}
				if(statement_node == null){
					statement_node = new Node(Graph2.nodeName, lineNo);
					Graph2.nodeName +=1;
					if(!lastNodesInBranch.isEmpty()){
						addParents(statement_node, lastNodesInBranch);
						lastNodesInBranch.clear();
					}
					else parent.childs.add(statement_node);
					parent = statement_node;
					
				}
				else{
					statement_node.lines.add(lineNo);
				}
				
				if(lines.get(lineNo).contains("}") || ! isMultipleLine) return statement_node;
				
			}
		lineNo++;
		}
	}

	private void addParents(Node newNode , ArrayList<Node> lastNodesInBranch) {
		
		for(int i=0; i<lastNodesInBranch.size();i++){
			 lastNodesInBranch.get(i).childs.add(newNode);
		}
		
	}
	
	private boolean isBlock(Node newNde){
		if(this.lines.get(lineNo).endsWith("{")) return true;
		lineNo++;
		while(true){
			if(this.lines.get(lineNo).endsWith("{")) return true;
			else if(this.lines.get(lineNo).endsWith(";")){
				lineNo--;
				return false; 
			}
			else newNde.lines.add(lineNo);
			lineNo++;
			
		}
	}
		
		
}