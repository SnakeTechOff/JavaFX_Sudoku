package csp;

import java.util.ArrayList;

public class DegreeHeuristic {

	private CSP csp;

	public DegreeHeuristic(CSP csp) {
		this.csp = csp;
	}
	
	// Extracts variables with max degree 
	public int[] algo(int[][] grid, ArrayList<int[]> pos) {
		int max_degree = -1;
		int[] position = null;
		if (pos.isEmpty()) {
			 pos = csp.sudoku.getPosEmpty(grid);
		} else if (pos.size() == 1) {
			return pos.get(0);
		}
		for (int[] p: pos) {
			int degree = 0;
			int row = p[0];
			int col = p[1];
			for (int i = 0; i < csp.sudoku.size; i++) {
				if (grid[row][i] == csp.sudoku.empty) {
					degree++;
				}
				if (grid[i][col] == csp.sudoku.empty) {
					degree++;
				}
			}
			int box = 3;
			int r = Math.round((row/csp.sudoku.square)*csp.sudoku.square);
			int c = Math.round((col/csp.sudoku.square)*csp.sudoku.square);
			for (int i = 0; i < box; i++) {
				for (int j = 0; j < box; j++) {
					if (grid[r+i][c+j] == csp.sudoku.empty) {
						degree++;
					}
				}
			}
			if (degree > max_degree) {
				max_degree = degree;
				position = p;
			}
		}
		return position;
	}
}
