package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.LinkedList;
import java.util.Map;
import java.util.ArrayList;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//TODO

//Fix protection MODE
//ADD pcount to it.

public class Display extends JPanel{
	LinkedList<Integer> scores = new LinkedList<Integer>();
	int score;

	//These are in squares ie the board is 10 squares wide
	public final int width;
	public final int height;
	
	//This is the jframe
	public final int window_width = 600;
	public final int window_height = 600;  
	
	
	//size of square width
	public final int wSize;
	public final int hSize;

	//This will be the board
	//0 value for blank
	//1 for snake
	//2 for head
	//3 for food
	//4 for wall
	public int [] [] squares;
	
	//Cordinates of snake
	public int [] xSnake;
	public int [] ySnake;
	
	
	
	
	//make the ai
	RandomAi randa;
	Ai ai = new Ai();
	
	//for timing
	private long current = System.currentTimeMillis();
	private long endTime = System.currentTimeMillis();
	private long delta = 200;

	//snake
	//public Snake snake;
	int direction = 1;
	public int size = 3;
	
	
	int nextDirection =1;
	//apple defintion
	int xApple;
	int yApple;
	public Display() {
		//set the ai
		randa = new RandomAi();
		//set background stuff
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(window_width, window_height));
		this.setFocusable(true);
		this.addKeyListener(new myKeyAdapter());
        //set matrix + width and stuff
		width = 10;
		height = 10;
		squares = new int[width][height];
		wSize = (int)(window_width/width); 
		hSize = (int)(window_width/height);
		int mw = (int)(width/2);
		int my = (int)(height/2);
		
		//snake = new Snake(mw,my);
		xSnake = new int[width*height];
		ySnake = new int[width*height];
		
		xSnake[0]=mw;
		xSnake[1]=mw-1;
		xSnake[2]=mw-2;
		
		ySnake[0]=my;
		ySnake[1]=my;
		ySnake[2]=my;
		
		xApple = (width/2) + 3;
		yApple = height/2;
		squares[xApple][yApple] =3;
		

		//squares[1][1] =4;
		score = 0;
	}
	public LinkedList<Integer> getScores() {
		return scores;
		
	}
	//Just reset everything
	public void reset() {
		for (int i =0; i < squares.length; i ++) {
			for (int j =0; j < squares[i].length; j ++) {
				if (squares[i][j] ==4) {
					squares[i][j]=0;
				}
				
			}
		}
		direction =1;
		size = 3;
		int mw = (int)(width/2);
		int my = (int)(height/2);
		
		//snake = new Snake(mw,my);
		xSnake = new int[width*height];
		ySnake = new int[width*height];
		
		xSnake[0]=mw;
		xSnake[1]=mw-1;
		xSnake[2]=mw-2;
		
		ySnake[0]=my;
		ySnake[1]=my;
		ySnake[2]=my;

		squares[xApple][yApple] = 0;
		xApple = (width/2) + 3;
		yApple = height/2;
		squares[xApple][yApple] =3;
		squares[1][1] =4;
		scores.add(score);
		score =0;
		
	}
	public void move() {
		//Moves the snake foward besides the head which is updated later
		for (int i = size; i > 0; i --) {
			xSnake[i] = xSnake[i-1];
			ySnake[i] = ySnake[i-1];
		}
		
		//Updates head position
		switch (direction) {
		case 1:
			xSnake[0] = xSnake[0] +1;
			break;
		case -1:
			xSnake[0] = xSnake[0] -1;
			break;
		case 2:
			ySnake[0] = ySnake[0] +1;
			break;
		case -2:
			ySnake[0] = ySnake[0] -1;
			break;
		}
		
		//Redraws the snake
		if (checkColl()) {
			for (int i =0; i < squares.length; i ++) {
				for (int j =0; j < squares[i].length; j ++) {
					if (squares[i][j] == 1 || squares[i][j] == 2) {
						squares[i][j] = 0;
					}
					
		
				}
			}
			squares[xSnake[0]] [ySnake[0]] = 2;
			for (int i = 1; i < size; i ++) {
				squares[xSnake[i]] [ySnake[i]] = 1;
				
			}
		}
		
		
		
	}
	//returns true if no hit occured
	public Boolean checkColl() {
		//hits the outside
		if (xSnake[0] >= width || xSnake[0] < 0 || ySnake[0] >= height || ySnake[0] < 0 || squares[xSnake[0]] [ySnake[0]] == 4){
			reset();
			return false;
		}
		//Checks the snake
		for (int i = size; i > 0; i--) {
			if (xSnake[0] == xSnake[i] && ySnake[0] ==ySnake[i]) {
				reset();
				return false;
			}
		}
		//Checks if the apple was hit
		if (xSnake[0] == xApple && ySnake[0] == yApple) {
			size ++;
			score++;
			
			xApple = (int) ( Math.random()*(width-1));
			yApple = (int) ( Math.random()*(height-1));
			while(squares[xApple][yApple]!= 0) {
				xApple = (int) ( Math.random()*(width-1));
				yApple = (int) ( Math.random()*(height-1));
				
			}
			squares[xApple][yApple] = 3;
			int tx = (int) ( Math.random()*(width-1));
			int ty = (int) ( Math.random()*(height-1));

		
			while(squares[tx][ty]!= 0) {
				tx = (int) ( Math.random()*(width-1));
				ty = (int) ( Math.random()*(height-1));
				
			}
			//squares[tx][ty] = 4;
				
			
		}
		return true;
	}
	
	public void drawR(Graphics g,int x, int y,int value) {
		if (value == 0) {
			g.setColor(Color.WHITE);
		}
		else if (value == 1) {
			g.setColor(Color.GREEN);
		}
		else if (value == 2) {
			g.setColor(Color.BLUE);
		}
		else if (value == 3) {
			g.setColor(Color.RED);
		}
		else if (value == 4) {
			g.setColor(Color.BLACK);
			
		}
		else if (value == 5) {
			g.setColor(Color.YELLOW);
			
		}
		else if (value == 6) {
			g.setColor(Color.GRAY);
			
		}

		else {
			//System.out.println("Unknown Value " + (value) + "  Setting to yellow");
			g.setColor(Color.yellow);
		}
		g.fillRect(x, y, wSize, hSize);
	}

	public void paint(Graphics g)

	{
		
		current = System.currentTimeMillis();

		if (current - delta > endTime) {
			//get ai move
			
			//nextDirection =randa.setMove(direction);
			nextDirection = ai.setMove(direction);
			//set direction
			direction = nextDirection;
			move();
			//delta timeing
			endTime = System.currentTimeMillis();
		}
	//draw squares
		for (int i =0; i < squares.length; i ++) {
			for (int j =0; j < squares[i].length; j ++) {
				drawR(g,i*wSize, j*hSize, squares[i][j]);	
	
			}
		}
		repaint();
	}
	public class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            
        	switch(e.getKeyCode()) {
            
            	
                case KeyEvent.VK_LEFT:
                    if(direction != 1) {
                        nextDirection = -1;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != -1) {
                    	nextDirection = 1;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 2) {
                    	nextDirection = -2;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction !=-2) {
                    	nextDirection = 2;
                    }
                    break;
            }
            
        }
	
	}

	public class Ai{
		Queue path = new Queue();
		int [] [] temp_squares;
		boolean flag = false;
		boolean flag2 = false;
		boolean protectionMode = false;
		int pcount;
		public Ai() {
			
			
		}
		public boolean setMoveHelper(int d) {
			/*
			System.out.println(protectionMode);
			if (protectionMode) {
				System.out.println("PROTECTION MODE");
				int hx = xSnake[0]; 
				int hy = ySnake[0];
				//left
				if (d == -1) {
					while (isValid(hx,hy)) {
						Cord temp = new Cord(hx,hy);
						path.add(temp);
						hx --;
					}
					//Go through until we find a place to turn
					while (!isValid(hx,hy+1) && !isValid(hx,hy-1)) {
						//if none are valid remove from the end and just go straight
						if(path.removeFromEnd()== null) {
							while (isValid(hx,hy)) {
								Cord temp = new Cord(hx,hy);
								path.add(temp);
								hx --;
								
							}
							return true;
						}
					}
					if (isValid(hx,hy+1)) {
						path.add(new Cord(hx,hy+1));
						return true;
					}
					else if(isValid(hx,hy-1)){
						path.add(new Cord(hx,hy-1));
						return true;
						
					}
					else {
						System.out.println("IDK");
						return false;
					}
				
				}
				
				
				//right
			if (d == 1) {
					
					while (isValid(hx,hy)) {
						Cord temp = new Cord(hx,hy);
						path.add(temp);
						hx ++;
						
					}
					//Go through until we find a place to turn
					while (!isValid(hx,hy+1) && !isValid(hx,hy-1)) {
						//if none are valid remove from the end and just go straight
						if(path.removeFromEnd()== null) {
							while (isValid(hx,hy)) {
								Cord temp = new Cord(hx,hy);
								path.add(temp);
								hx --;
								
							}
							return true;
						}
					}
					if (isValid(hx,hy+1)) {
						path.add(new Cord(hx,hy+1));
						return true;
					}
					else if(isValid(hx,hy-1)){
						path.add(new Cord(hx,hy-1));
						return true;
						
					}
					else {
						System.out.println("IDK");
						return false;
					}
				
				}
			
			
			//Down
			if (d == -2) {
				
				while (isValid(hx,hy)) {
					Cord temp = new Cord(hx,hy);
					path.add(temp);
					hy --;
					
				}
				//Go through until we find a place to turn
				while (!isValid(hx+1,hy) && !isValid(hx-1,hy)) {
					//if none are valid remove from the end and just go straight
					if(path.removeFromEnd()== null) {
						while (isValid(hx,hy)) {
							Cord temp = new Cord(hx,hy);
							path.add(temp);
							hx --;
							
						}
						return true;
					}
				}
				if (isValid(hx+1,hy)) {
					path.add(new Cord(hx+1,hy));
					return true;
				}
				else if(isValid(hx-1,hy)){
					path.add(new Cord(hx-1,hy));
					return true;
					
				}
				else {
					System.out.println("IDK");
					return false;
				}
			
			}
			//up
			if (d == 2) {
				
				while (isValid(hx,hy)) {
					Cord temp = new Cord(hx,hy);
					path.add(temp);
					hx --;
					
				}
				//Go through until we find a place to turn
				while (!isValid(hx+1,hy) && !isValid(hx-1,hy)) {
					//if none are valid remove from the end and just go straight
					if(path.removeFromEnd()== null) {
						while (isValid(hx,hy)) {
							Cord temp = new Cord(hx,hy);
							path.add(temp);
							hx --;
							
						}
						return true;
					}
				}
				if (isValid(hx+1,hy)) {
					path.add(new Cord(hx+1,hy));
					return true;
				}
				else if(isValid(hx-1,hy)){
					path.add(new Cord(hx-1,hy));
					return true;
					
				}
				else {
					System.out.println("IDK");
					return false;
				}
			
			}
				
			}
			*/
			
			boolean keep = true;
			int count = 0;
			protectionMode = false;
			temp_squares = copy2d(squares);
			int [] [] temp2_squares = copy2d(squares);
			while(keep) {
				count ++;
				
				if (count > 100) {
				
					temp_squares = copy2d(squares);
					path = bfs(xApple,yApple,xSnake[0],ySnake[0]);
					System.out.print("\n\n");
					System.out.println("Head: "+toStr(xApple, yApple));
					System.out.println("Goal: "+toStr(xSnake[size-1], ySnake[size-1]));
					System.out.print("Going straight");
					System.out.print("\n\n");
					//squares = temp_squares;
					
					pcount = 0;
					protectionMode = true;
					return false;
				}
				flag2 = false;;
				path = bfs(xSnake[0],ySnake[0],xApple,yApple);
				//if the path is empty find a random empty square that the snake can go to
				//But this means it still can trap itself
				/*
				if (path.isEmpty()) {
					pcount = 0;

					protectionMode = true;

					return true;
				}
				*/

				temp2_squares = temp_squares;
				temp_squares = copy2d(squares);
				flag2 = true;
				if(bfs(xApple,yApple,xSnake[size-1],ySnake[size-1]).isEmpty()) {
					for(int i = 0; i < path.size; i ++) {
						
						temp2_squares[path.get(i).getX()][path.get(i).getY()] = 4;
					}
			
				}
				else {
					keep = false;
				}
				for (int i = 0; i < temp2_squares.length; i ++) {
					for (int j = 0; j < temp2_squares[i].length; j ++) {
						if (temp2_squares[i][j]!= 5 || temp2_squares[i][j] != 6) {
							temp_squares[i][j] = temp2_squares[i][j];
						}
					}
				}
			}
			

			path.pop();
			return true;
		}
		public int setMove(int d) {
			if(flag) {
				throw new ArithmeticException();
			}
			if(path.isEmpty())
			{
				System.out.println("New path getting made");
				
				if(!setMoveHelper(d)) {
					//flag = true;
					return d;
					
				}
				if (path.isEmpty()) {
					return d;
				}
			}
			path = new Queue();
			if(!setMoveHelper(d)) {
				return d;

				
			}
			
			Cord nextSquare = path.pop();

			if (nextSquare.getX() > xSnake[0]) {
				return 1;
			}
			if (nextSquare.getX() < xSnake[0]) {
				return -1;
			}
			if (nextSquare.getY() > ySnake[0]) {
				return 2;
			}
			if (nextSquare.getY() < ySnake[0]) {
				return -2;
			}
			

			
			
			
			//temp_squares = copy2d(squares);
			//Check if we can get back to the tail.  If s
			/*
			while(bfs(xApple,yApple,xSnake[0],ySnake[0]).isEmpty()) {
				temp_squares = copy2d(squares);
				for(int i =0; i < path.size; i++) {
					temp_squares[path.get(i).getX()][path.get(i).getY()]
				}
				
				
			}
			*/
			return d;
		}
		private Queue bfs(int x, int y,int goalX,int goalY) {
			
			Queue q = new Queue();
			Queue turns = new Queue();
			Cord head = new Cord(x,y);
			q.add(head);
			Cord current;
			
			while (!q.isEmpty()) {
				current = q.pop();
				
				//if its the goal set the path to be correct
				if (temp_squares[current.getX()][current.getY()] == 6) {
					continue;
				}
				if(current.getX()==goalX && current.getY() == goalY) {
					while(current.getPrev()!=null) {
						current.getPrev().setNext(current);
						current = current.getPrev();
					}
					current = head;
					while(current != null) {
						turns.add(current);
						current = current.getNext();
					}
					
					return turns;
				}
				
				//Check if square above is good if so add it to the queue and mark it as discovered int value (5)
				if (isValid(current.getX(),current.getY()+1)) {
					Cord c = new Cord(current.getX(),current.getY()+1);
					c.setPrev(current);
					temp_squares[current.getX()][current.getY()+1] = 5;
					q.add(c);
				}
	
				//below
				if (isValid(current.getX(),current.getY()-1)) {
					Cord c = new Cord(current.getX(),current.getY()-1);
					c.setPrev(current);				
					temp_squares[current.getX()][current.getY()-1] = 5;
					q.add(c);
				}
				//left
				if (isValid(current.getX()-1,current.getY())) {
					Cord c = new Cord(current.getX()-1,current.getY());
					c.setPrev(current);
					temp_squares[current.getX()-1][current.getY()] = 5;
					q.add(c);
				}
				//right
				if (isValid(current.getX()+1,current.getY())) {
					Cord c = new Cord(current.getX()+1,current.getY());
					c.setPrev(current);
					temp_squares[current.getX()+1][current.getY()] = 5;
					q.add(c);
				}
				temp_squares[current.getX()] [current.getY()] = 6;
			}

			return new Queue();
		}
		private boolean isValid(int x, int y) {
			//special case if its the last square of snake and flag2 = true then do it
			if(flag2) {
				if (x == xSnake[size-1] && y== ySnake[size-1]) {
					
					return true;
				}
			}
			if (x >= temp_squares.length || x < 0) {
				return false;
			}
			if (y >= temp_squares[x].length || y < 0) {
				return false;
			}
			if (temp_squares[x][y] == 0 || temp_squares[x][y] == 3) {
				return true;
			}

			return false;
		}

		
	}
//Queue
	public class Queue{
		int size = 0;
		ArrayList<Cord> arr;
		public Queue() {
			arr = new ArrayList<Cord>();
		}
		public void add(Cord c) {
			this.arr.add(c);
			this.size ++;
		}
		public Cord pop() {
			if (this.size == 0) {
				return null;
			}
			
			Cord temp = arr.get(0);
			arr.remove(0);
			this.size--;
			return temp;
		}
		public boolean isEmpty() {
			return this.size == 0;
		}
		public String toString() {
			String result = "";
			for (Cord i : this.arr) {
				result = result + "("+ i.getX() + ", "+i.getY() + ") ";
 			}
			return result;
		}
		public Cord get(int i) {
			return this.arr.get(i);

		}
		public Cord removeFromEnd() {
			if (this.size == 0) {
				return null;
			}
			
			Cord temp = arr.get(size-1);
			arr.remove(size-1);
			this.size--;
			return temp;
		}

	}
	//cord i know this is confusing bad code what ever.
	public class Cord{
		int x;
		int y;
		public Cord prev;
		public Cord next;
		public Cord(int x, int y) {
			this.x = x;
			this.y = y;
			this.prev=null;
			this.next=null;
		}
		public int getX() {
			return this.x;
		}
		public int getY() {
			return this.y;
		}
		public Cord getPrev() {
			return this.prev;
		}
		public void setPrev(Cord c) {
			this.prev = c;
		}
		public Cord getNext() {
			return this.next;
		}
		public void setNext(Cord c) {
			this.next = c;
		}
		public String toString() {

			return("("+ this.x+", "+this.y+")");
		}
	}
	public class RandomAi{

		

		int best_score;
		public RandomAi() {
			best_score = 0;
		}
		public int setMove(int d) {
			boolean a = true;
			int move = 0;
			while (move == 0) {
				move = (int)(Math.random()*4);
				if (move == 0) {
					if (d != 1) {
						//nextDirection = -1;
						a = false;
						return -1;
					}
					else {
						continue;
					}
				}
				else if (move == 1) {
					if (d != -1) {
						nextDirection = 1;
						a = false;
						return 1;
					}
					else {
						continue;
					}
				}
				else if (move == 2) {
					if (d != 2) {
						//nextDirection = -2;
						a = false;
						return -2;
					}
					else {
						continue;
					}
				}
				if (move == 4) {
					if (d != -2) {
						nextDirection = 2;
						a = false;
						return 2;
					}
					else {
						continue;
					}
				}
			}
			return d;
		}
	}
	
	//static methods
	public static String toStr(int a, int b) {
		return(a + ", " + b);
	}
	public static int [] [] copy2d(int [] [] arr){
		int [] [] temp = new int[arr.length][arr[0].length];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				temp[i][j]= arr[i][j];
			}
		}
		return temp;
		
	}
}
