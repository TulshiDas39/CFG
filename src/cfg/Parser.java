package cfg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
	
	
	public Parser(String path) throws IOException {
		ArrayList<String> lines = new ArrayList<>();
		FileReader fileReader = 
                new FileReader(path);
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            String line;
            int i=0;
			while((line = bufferedReader.readLine()) != null) {
                lines.add(line);
                System.out.println(i+":"+line);
                i++;
            }   
            // Always close files.
            bufferedReader.close();            
            Graph2 graph = new Graph2(lines);
            System.out.println();
            System.out.println("The graph:");
            graph.buildGraph();
	}
}