package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import application.Window;
import javafx.application.Application;

public class Sudoku extends Thread {

	public int size;
	public int row;
	public int col;
	public int empty;
	private int kill;
	public int square;
	public int[][] grid;
    public Window window;
	private Thread t;
    private Player player;
    private final AtomicBoolean running = new AtomicBoolean(false);
	public HashMap<String, ArrayList<Integer>> domains;
	
	
	public Sudoku(Window window) {
		this.size = 9;
		this.row = this.size;
		this.col = this.size;
		this.empty = 0;
		this.kill = 30;
		this.square = (int) Math.sqrt(this.size);
		this.grid = new int[this.row][this.col];
		this.domains = new HashMap<String, ArrayList<Integer>>();
		this.updateDomains(grid);
    	this.window = window;
        this.player = new Player(getInstance());
	}
	
	public Sudoku() {
		this.size = 9;
		this.row = this.size;
		this.col = this.size;
		this.empty = 0;
		this.kill = 30;
		this.square = (int) Math.sqrt(this.size);
		this.grid = new int[this.row][this.col];
		this.domains = new HashMap<String, ArrayList<Integer>>();
		this.updateDomains(grid);
        this.player = new Player(getInstance());
	}
	
	// Get instance for CSP
	public Sudoku getInstance() {
		return this;
	}
	
	// Execute window
	public void execWindow() throws InterruptedException {
    	this.window = ((Window) this.window);
        this.t = new Thread(() -> Application.launch(Window.class));
        this.t.start();

        Window.awaitFXToolkit();
    }
	
	/// Generate Sudoku ///
    public void generateGrid() { 
    	clearGrid();
        for (int i = 0; i<this.size; i=i+this.square) {
        	fillBox(i, i); 
        }  
        fillOther(0, this.square); 
        removeDigits(); 
    } 
    
    private void fillBox(int row,int col) { 
        int num; 
        for (int i=0; i<this.square; i++) { 
            for (int j=0; j<this.square; j++) { 
                do { 
                    num = (int) Math.floor((Math.random()*this.size+1)); 
                } while (!checkBnumber(this.grid, row, col, num)); 
                this.grid[row+i][col+j] = num; 
            } 
        } 
    } 
    
    private boolean fillOther(int i, int j) { 
        if (j>=this.size && i<this.size-1) { 
            i = i + 1; 
            j = 0; 
        } 
        if (i>=this.size && j>=this.size) {
        	return true; 
        }
        if (i < this.square) { 
            if (j < this.square) 
                j = this.square; 
        } 
        else if (i < this.size-this.square) { 
            if (j==(int)(i/this.square)*this.square) 
                j =  j + this.square; 
        } 
        else { 
            if (j == this.size-this.square) { 
                i = i + 1; 
                j = 0; 
                if (i>=this.size) 
                    return true; 
            } 
        } 
  
        for (int num = 1; num<=this.size; num++) { 
            if (isConsistent(this.grid, i, j, num)) { 
            	grid[i][j] = num; 
                if (fillOther(i, j+1)) {
                    return true; 
                }
                grid[i][j] = 0; 
            } 
        } 
        return false; 
    } 
  
    private void removeDigits() { 
        int count = this.kill; 
        while (count != 0) { 
            int cell = (int) Math.floor((Math.random()*((this.size*this.size)-1)+1));
  
            //System.out.println(cellId); 
            int i = (cell/this.size); 
            int j = (cell%this.size); 
            if (j != 0) 
                j = j - 1; 
  
            //System.out.println(i+" "+j); 
            if (grid[i][j] != 0) { 
                count--; 
                grid[i][j] = 0; 
            } 
        } 
    } 
	///////////////////////
    
    /////// Check ///////
	// Check if complete
	public boolean isComplete(int[][] grid) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Check is consistent
	public boolean isConsistent(int[][] grid, int row, int col, int n) {
		if (!checkRnumber(grid, row, n) || !checkCnumber(grid, col, n) || !checkBnumber(grid, row, col, n)) {
			return false;
		}
		return true;
	}
	
	// Chech Row
	private boolean checkRnumber(int[][] grid, int row ,int n) {
		for (int i = 0; i < this.row; i++) {
			if (grid[row][i] == n) {
				return false;
			}
		}
		return true;
	}
	

	// Check Column
	private boolean checkCnumber(int[][] grid, int col, int n) {
		for (int i = 0; i < this.col; i++) {
			if (grid[i][col] == n) {
				return false;
			}
		}
		return true;
	}
		
	// Check Box
	private boolean checkBnumber(int[][] grid, int row, int col, int n) {
		int box = 3;
		int r = Math.round((row/this.square)*this.square);
		int c = Math.round((col/this.square)*this.square);
		for (int i = 0; i < box; i++) {
			for (int j = 0; j < box; j++) {
				if (grid[r+i][c+j] == n) {
					return false;
				}
			}
		}
		return true;
	}
	///////////////////////
	
	///////////////////////
	
	// Clean grid
	public void clearGrid() {
		this.grid = new int[9][9];
	}
	
	// DeepCopy domains
	public ArrayList<Integer> cloneDomain(ArrayList<Integer> domain){
		ArrayList<Integer> temps = new ArrayList<>();
		for (int x: domain) {
			temps.add(x);
		}
		return temps;
	}
	
	// Print grid
    public void displayGrid(int[][] grid) {
        for (int i = 0; i < this.row; i++) {
        	if (i % 3 == 0){
        		System.out.println("-------------------");
        	}
            for (int j = 0; j < this.col; j++) {
            	if (j % 3 == 0 && j != 0){
            		System.out.print("|");
            	}
            	if (grid[i][j] == 0) {
            		System.out.print("."+" ");
            	} else {
            		System.out.print(grid[i][j]+" ");
            	}
            }
            System.out.println();
        }
        System.out.println("-------------------");
        System.out.println();
    } 

    // Update domains for all vars
    public void updateDomains(int[][] grid) {
		this.domains.clear();
		ArrayList<int[]> vars = this.getPosEmpty(grid);
		for(int[] var : vars){
			this.domains.put(Integer.toString(var[0])+Integer.toString(var[1]), this.getDomains(grid, var));
		}
    }
    
    // Get all variables
	public ArrayList<int[]> getVariables(int[][] grid){
		ArrayList<int[]> variables = new ArrayList<>();
		for (int row = 0; row < this.size; row++) {
			for (int col = 0; col < this.size; col++) {
				variables.add(new int[] {row, col});
			}
		}
		return variables;
	}
	
	// Get domains for var
	public ArrayList<Integer> getDomains(int[][] grid, int row, int col) {
		ArrayList<Integer> values = new ArrayList<>();
			for (int n = 1; n <= this.size; n++) {
				if (this.isConsistent(grid, row, col, n)) {
					values.add(n);
				}
			}
		return values;
	}
	
	public ArrayList<Integer> getDomains(int[][] grid, int[] var) {
		int row = var[0];
		int col = var[1];
		return getDomains(grid, row, col);
	}

	// Get neighbors for var
	public HashMap<int[], ArrayList<Integer>> getNeighbors(int[][] grid, int row, int col) {
		HashMap<int[], ArrayList<Integer>> neighbors = new HashMap<>();
		for (int i = 0; i < this.size; i++) {
			if (grid[row][i] == this.empty) {
				neighbors.put(new int[] {row,i}, getDomains(grid, row, col));
			}
			if (grid[i][col] == this.empty) {
				neighbors.put(new int[] {i,col}, getDomains(grid, row, col));
			}
		}
		int r = Math.round((row/this.square)*this.square);
		int c = Math.round((col/this.square)*this.square);
		for (int i = 0; i < this.square; i++) {
			for (int j = 0; j < this.square; j++) {
				if (grid[r+i][c+j] == this.empty) {
					neighbors.put(new int[] {r+i,c+j}, getDomains(grid, row, col));
				}
			}
		}
		return neighbors;
	}
	
	public HashMap<int[], ArrayList<Integer>> getNeighbors(int[][] grid, int[] var) {
		int row = var[0];
		int col = var[1];
		return getNeighbors(grid, row, col);
	}

	// Get vars empty
	public ArrayList<int[]> getPosEmpty(int[][] grid){
		ArrayList<int[]> pos = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == empty) {
					pos.add(new int[] {i,j});
				}
			}
		}
		return pos;
	}
	
	// Get grid 
	public int[][] getGrid() {
		return this.grid;
	}
	
	
	public int[][] getGridEasy() {
		
        int[][] easy =
        {
                {0, 1, 9, 0, 3, 0, 2, 6, 0},
                {2, 0, 0, 6, 1, 7, 0, 0, 9},
                {6, 0, 0, 8, 0, 2, 0, 0, 3},
                {0, 4, 8, 3, 7, 6, 1, 2, 0},
                {7, 6, 0, 1, 2, 4, 0, 9, 8},
                {0, 2, 3, 5, 8, 9, 7, 4, 0},
                {4, 0, 0, 9, 0, 3, 0, 0, 2},
                {3, 0, 0, 7, 5, 1, 0, 0, 4},
                {0, 7, 6, 0, 4, 0, 9, 3, 0}
        };
		return easy;
	}
	
	
	public int[][] getAverageGrid() {
		
        int[][] average =
        {
                {0, 9, 0, 2, 0, 0, 0, 6, 0},
                {0, 2, 0, 0, 0, 0, 8, 0, 0},
                {1, 0, 7, 8, 0, 0, 5, 9, 0},
                {5, 0, 6, 0, 0, 0, 1, 2, 0},
                {7, 1, 0, 6, 5, 0, 0, 3, 0},
                {0, 0, 0, 4, 0, 0, 0, 0, 0},
                {0, 5, 0, 1, 0, 4, 0, 0, 8},
                {2, 8, 9, 0, 0, 0, 0, 0, 4},
                {0, 0, 0, 0, 8, 0, 0, 0, 0}
        };
		return average;
	}
	
	
	public int[][] getDifficultGrid() {
		
        int[][] difficult =
        {
                {0, 7, 0, 0, 1, 0, 0, 9, 0},
                {9, 0, 0, 8, 0, 0, 0, 0, 7},
                {0, 0, 3, 0, 0, 0, 0, 0, 6},
                {0, 4, 0, 0, 0, 1, 5, 0, 0},
                {0, 3, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 2, 7, 0, 0, 0, 6, 0},
                {5, 0, 0, 0, 0, 0, 6, 0, 0},
                {6, 0, 0, 0, 0, 5, 0, 0, 2},
                {0, 8, 0, 0, 2, 0, 0, 7, 0}
        };
        

        
		return difficult;
	}
	
	// Setter
    public void setRun(boolean b) {
        running.set(b);
    }

	public void run(boolean[] choice) {
		player.initChoice(choice);
		player.backtrackingSearch(this.grid);
	}
}
