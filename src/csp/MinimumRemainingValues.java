package csp;

import java.util.ArrayList;
import java.util.HashMap;

public class MinimumRemainingValues {
	
	private CSP csp;

	public MinimumRemainingValues(CSP csp) {
		this.csp = csp;
	}

	// Extracts all variables with the smallest domains
	public ArrayList<int[]> algo(int[][] grid) {
		HashMap<int[], Integer> domains = new HashMap<int[], Integer>();
		int value = csp.sudoku.size;
		int min = csp.sudoku.size;
		ArrayList<int[]> min_domains = new ArrayList<>();
		for (int row = 0; row < csp.sudoku.size; row++) {
			for (int col = 0; col < csp.sudoku.size; col++) {
				if (grid[row][col] == 0) {
					value = csp.sudoku.getDomains(grid, row, col).size();
					int[] pos = {row, col};
					domains.put(pos, value);
					min = (int) Math.min(min, value);
				}
			}
		}
		for (int[] k : domains.keySet()) {
			  if (min == domains.get(k)) {
				  min_domains.add(k);
			  }
			}
		return min_domains;
	}
}
