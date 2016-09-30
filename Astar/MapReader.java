package Astar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MapReader {

	private String map = "";

	public String getMap(){
		return map;
	}

	public int getIndex(char c){
		return getMap().indexOf(c);
	}

	public int getYPosOfA(){
		int ypos = (int)Math.floor(getIndex('A')/21);
		return ypos;
	}
	public int getXPosOfA(){
		int xpos = (int) getIndex('A')%21;
		return xpos;
	}

	public int getYPosOfB(){
		int ypos = (int)Math.floor(getIndex('B')/21);
		return ypos;
	}
	public int getXPosOfB(){
		int xpos = (int) getIndex('B')%21;
		return xpos;
	}

	public void readFromFile(String path) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		int line;
		while ((line = reader.read()) != -1) {
			map += (char)line;
		}
		reader.close();
	}

}
