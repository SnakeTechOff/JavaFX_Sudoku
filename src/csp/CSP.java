package csp;

import java.util.ArrayList;
import java.util.stream.Collectors;

import environment.Sudoku;

public abstract class CSP {
	public Sudoku sudoku;
	private MinimumRemainingValues mrv;
	private DegreeHeuristic dh;
	private LeastConstrainingValue lcv;
	private AC3 ac3;
	private boolean choice_mrv;
	private boolean choice_dh;
	private boolean choice_lcv;
	private boolean choice_ac3;
	
	public CSP(Sudoku sudoku) {
		this.sudoku = sudoku;
		this.mrv = new MinimumRemainingValues(getInstance());
		this.dh = new DegreeHeuristic(getInstance());
		this.lcv = new LeastConstrainingValue(getInstance());
		this.ac3 = new AC3(getInstance());
	}
	
	public CSP getInstance() {
		return this;
	}
	
	// Init choice in window
	public void initChoice(boolean[] choice) {
		this.choice_mrv = choice[0];
		this.choice_dh = choice[1];
		this.choice_lcv = choice[2];
		this.choice_ac3 = choice[3];
	}
	
	public boolean backtrackingSearch(int[][] grid) {
		return recursiveBacktracking(grid);
	}
	
	private boolean recursiveBacktracking(int[][] grid) {
		if (sudoku.isComplete(grid)) {
			sudoku.grid = grid;
			return true;
		}
		int[] var = select_unassigned_variable(grid);
		for (Integer value: order_domain_values(grid, var)) {
			if (sudoku.isConsistent(grid, var[0], var[1], value)) {
				grid[var[0]][var[1]] = value;
				sudoku.displayGrid(grid);
				sudoku.window.setUpdateGrid(grid);
				// Check constraint
				if (!this.choice_ac3) {
					if (recursiveBacktracking(grid)) {
						return true;
					}
				}
				if (this.choice_ac3 && !ac3.algo(grid)) {		
					if (recursiveBacktracking(grid)) {	
						return true;
					}
				}
			}
		}
		grid[var[0]][var[1]] = sudoku.empty;
		return false;
	}
    
	// Select unassigned variable 
	public int[] select_unassigned_variable(int[][] grid) {
		if (this.choice_mrv == true && this.choice_dh == true) {
			ArrayList<int[]> pos = mrv.algo(grid);
			return dh.algo(grid, pos);
		} else if (this.choice_mrv == true) {
			ArrayList<int[]> pos = mrv.algo(grid);
			return pos.get(0);
		} else if (this.choice_dh == true) {
			ArrayList<int[]> pos = sudoku.getPosEmpty(grid);
			return dh.algo(grid, pos);
		} else if (this.choice_mrv == false && this.choice_dh == false) {
			for (int row = 0; row < sudoku.size; row++) {
				for (int col = 0; col < sudoku.size; col++) {
					if (grid[row][col] == sudoku.empty) {
						return (new int[]{row,col});
					}
				}
			}
		}
		return null;
	}
	
	// Puts the domain in order
	public ArrayList<Integer> order_domain_values(int[][] grid, int[] var) {
		int row = var[0];
		int col = var[1];
		if (this.choice_lcv == false) {
			return (ArrayList<Integer>) sudoku.getDomains(grid, row, col).stream().sorted().collect(Collectors.toList());
		} else if (this.choice_lcv  == true) {
			return lcv.algo(grid, row, col);
		}
		return null;
	}
		
}
