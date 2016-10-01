package Astar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MapReader {

	private String map = "";
	private int boardHeight = 0;
	private int boardWidht = 0;

	public String getMap(){
		return map;
	}

	public int getIndex(char c){
		return getMap().indexOf(c);
	}

	public int getYPosOfA(){
		int ypos = (int)Math.floor(getIndex('A')/(boardWidht/boardHeight));
		return ypos;
	}
	public int getXPosOfA(){
		int xpos = (int) getIndex('A')%21;
		return xpos;
	}

	public int getYPosOfB(){
		int ypos = (int)Math.floor(getIndex('B')/(boardWidht/boardHeight));
		return ypos;
	}
	public int getXPosOfB(){
		int xpos = (int) getIndex('B')%(boardWidht/boardHeight);
		return xpos;
	}
	public int getBoardHeight(){
		return boardHeight;
	}
	public int getBoardWidth(){
		return boardWidht;
	}

	public void readFromFile(String path) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		int line;
		int counter = 0;
		while ((line = reader.read()) != -1) {
			counter +=1;
			if ((char)line == '\n'){
				boardHeight +=1;
				boardWidht = counter;
			}
			map += (char)line;
		}

		reader.close();
	}

}
