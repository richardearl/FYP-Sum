import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Input {
	
	private List<String> textList;
	private int numlines;
	private String fname;
	private int mode;
	public Input() {
		textList = new ArrayList<String>();
    }

	public void setInputsManually(int numl, String filename, int pmode){
		numlines = numl;
		fname = filename;
		pmode = mode;
	}

	public void getUserInputs(){
		Scanner user_input = new Scanner( System.in );
		System.out.println("Final Line amount?: ");
		String lines =user_input.next( );
		numlines = Integer.parseInt(lines);
		System.out.println("Filename?: ");
		fname =user_input.next( );
		System.out.println("Processing mode(1/2): ");
		String mode1 =user_input.next( );
		mode=Integer.parseInt(mode1);
	}
	public void getTextFromFile() throws IOException {
		BufferedReader reader = null;
		//try {
			reader = new BufferedReader(new FileReader(fname));
		//} //catch (FileNotFoundException e) {
			//System.out.println("File not found!");
			//reader = new BufferedReader(new FileReader("default.txt"));
		//}
		try {
		    String line = reader.readLine();
		    while (line != null) {
		    	textList.add(line);
		        line = reader.readLine();
		        
		    }
		} finally {
		    reader.close();
		}
	
	
	}
	
	public int getMode() {
		return mode;
	}

	public int getNumlines() {
		return numlines;
	}

	public List<String> getTextList() {
		return textList;
	}

	public void setTextList(List<String> textList) {
		this.textList = textList;
	}
	
	public void printTextList() {
		for(int i=0; i<textList.size(); i++){
			System.out.println(textList.get(i));
		}
	}
	
}
