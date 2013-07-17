package se.kiril.tstest.om.input;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ParseFile {
	private List fileLines = new List();

	public ParseFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;
		while ((line = br.readLine()) != null) {
			fileLines.add(line);
		}
		br.close();
	}

	public List getParsedFile() {
		return fileLines;
	}

	public String getLine(int i) {
		return fileLines.getItem(i);
	}

	public int getNumberOfLines() {
		return fileLines.getItemCount();
	}

}