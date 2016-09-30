package Astar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class AStar {
	//FRAMEWORK FOR SETTING UP THE A* ALGORITHM
	//----------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------
	public List<List<String>> OPEN = new ArrayList<List<String>>();
	public List<List<String>> CLOSE = new ArrayList<List<String>>();
	public List<List<String>> CHILDREN = new ArrayList<List<String>>();
	public List<List<String>> KIDS = new ArrayList<List<String>>();
	public List<List<String>> SHORTESTPATH = new ArrayList<List<String>>();

	//Position of current node in STRING LIST
	public int stringPos;

	//Position(X,Y) of current node
	public int currentX;
	public int currentY;
	//Probably won't need these
	public int oldX;
	public int oldY;

	private String map;


//Initializes the program
	public void init(MapReader mr){

		//calulates current X and Y pos
		currentX = mr.getXPosOfA();
		currentY = mr.getYPosOfA();
		stringPos = mr.getIndex('A');

		//adding start node to OPEN
		addToOpen(generateList(mr, currentX, currentY, currentX, currentY));

	}
	//end framework----------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------

//Checks if a childNode is in OPEN/CLOSE/KIDS and so on..
	public boolean isInOpen(List<String> childNode){
		for(List<String> li:OPEN){
			if(Integer.parseInt((String)li.get(0)) == Integer.parseInt((String)childNode.get(0))  && Integer.parseInt((String)li.get(1)) == Integer.parseInt((String)childNode.get(1))){
				return true;
			}
		}
		return false;
	}
	public boolean isInClose(List<String> childNode){
		for(List<String> li:CLOSE){
			if(Integer.parseInt((String)li.get(0)) == Integer.parseInt((String)childNode.get(0))  && Integer.parseInt((String)li.get(1)) == Integer.parseInt((String)childNode.get(1))){
				return true;
			}
		}
		return false;
	}
	public boolean isInKids(List<String> childNode){
		for(List<String> li:KIDS){
			if(Integer.parseInt((String)li.get(0)) == Integer.parseInt((String)childNode.get(0))  && Integer.parseInt((String)li.get(1)) == Integer.parseInt((String)childNode.get(1))){
				return true;
			}
		}
		return false;
	}

	public List<String> findKid(List<String> childNode){
		for(List<String> li:KIDS){
			if(Integer.parseInt((String)li.get(0)) == Integer.parseInt((String)childNode.get(0))  && Integer.parseInt((String)li.get(1)) == Integer.parseInt((String)childNode.get(1))){
				return li;
			}
		}
		return null;
	}
	public boolean isInShortest(List<String> childNode){
		for(List<String> li:SHORTESTPATH){
			if(Integer.parseInt((String)li.get(0)) == Integer.parseInt((String)childNode.get(0))  && Integer.parseInt((String)li.get(1)) == Integer.parseInt((String)childNode.get(1))){
				return true;
			}
		}
		return false;
	}


//Main LOOP of the A* Algorithm
	public void start(MapReader mr){
		int x = 0;
		//While loop until program terminates
		while (!OPEN.isEmpty()){
			System.out.println("Iteration "+x);
			x+= 1;
			//Pops best OPEN list
			List<String> currentPosition = OPEN.get(0);
			OPEN.remove(0);
			CLOSE.add(currentPosition);
			updatePosition(Integer.parseInt((String) currentPosition.get(0)), Integer.parseInt((String) currentPosition.get(1)));
			if(currentX == mr.getXPosOfB() && currentY == mr.getYPosOfB()){
				System.out.println("\nSUCCESS!");
				shortestPath(CLOSE);
				System.out.println("Length: "+SHORTESTPATH.size());
				printShortestPath(SHORTESTPATH, mr);
//				findPath(mr);
				break;
			}

			findChildren(mr); //Finds all children of current position and loops them
			for (List<String> children : CHILDREN){
				if (isInKids(children)){
					children = findKid(children);
				}
				KIDS.add(children);

				//If the child node is NEW (Not in OPEN or CLOSE)
				if(!isInOpen(children) && !isInClose(children)){
					//attach_and_eval(children, currentPosition);
					addToOpen(children);
					sortOpen(OPEN);
				}
			}
			CHILDREN.clear();
		}
	}

//visualizes the shortest path by printing the route on the map, 'O' means traversed
	private void printShortestPath(List<List<String>> shortestpath, MapReader mr) {
		String shortestPathMap = map;
		for(List<String> li: shortestpath){
			int x = Integer.parseInt(li.get(0));
			int y = Integer.parseInt(li.get(1));
			int sPos = x+(y*21);
			if(!(sPos == mr.getIndex('A') || sPos == mr.getIndex('B'))){
				char[] mapChars = shortestPathMap.toCharArray();
				mapChars[sPos] = 'O';
				shortestPathMap = String.valueOf(mapChars);
			}
		}
		System.out.println(shortestPathMap);
	}

// calculates the shortest path from the CLOSE list and genereates the SHORTESTPATH  list
	private void shortestPath(List<List<String>> close) {
		//traverse from goal to start -> so reversing CLOSE list
		Collections.reverse(close);
		//adding goal position to SHORTESTPATH
		List<String> goal = CLOSE.get(0);
		SHORTESTPATH.add(goal);

		//iteratig through CLOSE using parents

		for(List<String> li: CLOSE){
			if(!isInShortest(li)){
				List<String> prev = SHORTESTPATH.get(SHORTESTPATH.size()-1);
				if(prev.get(2).equals(li.get(0)) && prev.get(3).equals(li.get(1))){
					SHORTESTPATH.add(li);
				}
			}
		}
		for(List<String> li: SHORTESTPATH){
			li.remove(2);
			li.remove(2);
			li.remove(2);
			li.remove(2);
			li.remove(2);
		}
		Collections.reverse(SHORTESTPATH);
		System.out.println("SHORTESTPATH "+SHORTESTPATH);

	}

	public void attach_and_eval(List<String> children1, List<String> currentPosition1){
		String parentX = currentPosition1.get(2);
		String parentY = currentPosition1.get(3);

		CHILDREN.get(CHILDREN.indexOf(children1)).set(2,parentX);
		CHILDREN.get(CHILDREN.indexOf(children1)).set(3,parentY);

	}
//Iterate through KIDS and find best parents, return whole list as "bestPath"
//	public List<List<String>> findPath(MapReader mr){
//		List<List<String>> bestPath = new ArrayList<List<String>>();
//		//Iterate through KIDS and find best parents
//		while (Integer.parseInt((String) CHILDREN.get(0).get(2)) != mr.getXPosOfA() && Integer.parseInt((String) CHILDREN.get(0).get(3)) != mr.getYPosOfA()){
//
//		}
//
//		return bestPath;
//	}



//Implement functions for ADD to OPEN, CLOSE, CHILDREN and REMOVE from OPEN/CLOSE here:
	public List<String> generateList(MapReader mr, int x, int y, int parentX, int parentY){
		List<String> liste = new ArrayList<String>();
		int x1 = Math.abs(x - mr.getXPosOfB());
		int y1 = Math.abs(y - mr.getYPosOfB());
		//heuristics
		int toBcost = Math.abs(x1+y1);

		int x2 = Math.abs(mr.getXPosOfA() - x);
		int y2 = Math.abs(mr.getYPosOfA() - y);
		//cost
		int fromAcost =  Math.abs(x2+y2);

		//toaltcost = cost+heuristic
		int totalCost = fromAcost + toBcost;

		liste.add(""+x);
		liste.add(""+y);
		liste.add(""+parentX);
		liste.add(""+parentY);
		liste.add(""+fromAcost);
		liste.add(""+toBcost);
		liste.add(""+totalCost);

		return liste;

	}

	public void addToOpen(List<String> liste){
		OPEN.add(liste);
	}
	public void addToClosed(List<String> liste){
		CLOSE.add(liste);
	}
	public void addToChildren(List<String> liste){
		CHILDREN.add(liste);
	}

	public List<List<String>> sortOpen(List<List<String>> list1){
		Collections.sort(list1, Comparator.comparing(e -> Integer.valueOf(e.get(6))));
		return list1;
	}


	//--------------------------------------------------------------------------------------
	//Function for calculating distance from start->node, node->end and total


//SEARCH ALL CHILD NODES of StringPos (current position)
	public void findChildren(MapReader mr){
		//Iteration 5: x= -1
		if (currentY > 0){
			char up = map.charAt((stringPos)-21);
			if(up!='#'){
//				System.out.println("Node UP");
				addToChildren(generateList(mr, currentX,currentY-1, currentX,currentY));
			}
		}

		if ( currentY < 6){
			char down = map.charAt((stringPos)+21);
			if(down!='#'){
//				System.out.println("Node DOWN");
				addToChildren(generateList(mr, currentX,currentY+1, currentX,currentY));
			}
		}

		if (currentX < 19){
			char rigth = map.charAt((stringPos)+1);
			if(rigth!='#'){
//				System.out.println("Node RIGHT");
				addToChildren(generateList(mr, currentX+1,currentY, currentX,currentY));
			}
		}

		if (currentX > 0){
			char left = map.charAt((stringPos)-1);
			if(left!='#'){
//				System.out.println("Node LEFT");
				addToChildren(generateList(mr, currentX-1,currentY, currentX,currentY));
			}
		}

	}

//UPDATE STRING POSITION with X,Y from the most recent popped position
	public void updatePosition(int nextX, int nextY){
		//oldX & oldY are probably pointless variables
		oldX = currentX;
		oldY = currentY;
		this.currentX = nextX;
		this.currentY = nextY;
		stringPos = currentX + currentY*21;
		//Prints out stringPos and actual current position
		System.out.println("x:"+currentX + " y:"+currentY);
	}



//MAIN FUNCTION
	public static void main(String[] args) throws IOException {
		AStar astar = new AStar();

		//Runs MapReader, reads text file and sets up map.
		MapReader mr = new MapReader();
		mr.readFromFile("C:/eclipse/Prosjekter/tdt4136/src/Astar/board-1-1.txt");
		System.out.println(mr.getMap());
		astar.map = mr.getMap();
		System.out.println("A(y,x): "+ mr.getXPosOfA()+" "+mr.getYPosOfA());
		System.out.println("B(y,x): "+ mr.getXPosOfB()+" "+mr.getYPosOfB() + "\n");

		//Initialize the program and put A node in OPEN
		astar.init(mr);

		//Run the A* ALGORITHM LOOP
		astar.start(mr);
	}
}

