package csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class LeastConstrainingValue {

	private CSP csp;

	public LeastConstrainingValue(CSP csp) {
		this.csp = csp;
	}
	
	// Organizes the domains of a variable with respect to the constraint
	public ArrayList<Integer> algo(int[][] grid, int row, int col) {
		ArrayList<Integer> sort = new ArrayList<>();
		HashMap<Integer, Integer> values = new HashMap<>();
		ArrayList<Integer> keys = csp.sudoku.getDomains(grid, row, col);
		for (int k: keys) {
			values.put(k, 0);
		}
		for (HashMap.Entry<int[], ArrayList<Integer>> neighbor : csp.sudoku.getNeighbors(grid, row, col).entrySet()) {
		    if (grid[neighbor.getKey()[0]][neighbor.getKey()[1]] == csp.sudoku.empty) {
		    	for (int v : neighbor.getValue()) {
		    		if (values.containsKey(v)) {
		    			values.put(v, values.get(v) + 1);
		    		}
		    	}
		    }
		}
		Map<Integer, Integer> sorted = values.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));;
		for (Entry<Integer, Integer> s : sorted.entrySet()) {
			sort.add(s.getKey());
		}
		return sort;
	}
}
