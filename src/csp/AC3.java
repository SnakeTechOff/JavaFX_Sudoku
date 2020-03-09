package csp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;

import environment.Tuple;

public class AC3 {

	private CSP csp;

	public AC3(CSP csp) {
		this.csp = csp;
	}
	
	public boolean algo(int[][] grid) {
		csp.sudoku.updateDomains(grid);
		Queue<Tuple<int[], int[]>> queue = getArc(grid);
		while (!queue.isEmpty()) {
			Tuple<int[], int[]> first = queue.remove();
			int[] xi = first.getI();
			int[] xj = first.getJ();

			if (removeInconsistentValues(grid, xi, xj)) {
				if (csp.sudoku.domains.get(Integer.toString(xi[0])+Integer.toString(xi[1])).size() == csp.sudoku.empty){
					return false;
				} else {
					for (Entry<int[], ArrayList<Integer>> xk: csp.sudoku.getNeighbors(grid, xi).entrySet()) {
						if (xk.getKey() != xi) {
							queue.add(new Tuple<int[], int[]>(xk.getKey(), xi));
						}
					}
				}
			}
			
		}
		return true;
	}
	
	private Queue<Tuple<int[], int[]>> getArc(int[][] grid) {
		Queue<Tuple<int[], int[]>> arcs = new LinkedList<>();
		for (int[] var: csp.sudoku.getPosEmpty(grid)) {
			for (Entry<int[], ArrayList<Integer>> xk: csp.sudoku.getNeighbors(grid, var).entrySet()) {
				arcs.add(new Tuple<int[], int[]>(var, xk.getKey()));
			}
		}
		return arcs;
	}
	
	public boolean removeInconsistentValues(int[][] grid, int[] xi, int[] xj) {
		boolean removed = false;
		ArrayList<Integer> temp = csp.sudoku.cloneDomain(csp.sudoku.domains.get(Integer.toString(xi[0])+Integer.toString(xi[1])));
		//System.out.println(temp);
		for (int x: csp.sudoku.domains.get(Integer.toString(xi[0])+Integer.toString(xi[1]))) {
			if (checkInconsistent(x, csp.sudoku.domains.get(Integer.toString(xj[0])+Integer.toString(xj[1])))) {
				temp.remove(temp.indexOf(x));
				removed = true;
			}
			csp.sudoku.domains.put(Integer.toString(xi[0])+Integer.toString(xi[1]), temp);
		}
		return removed;
	}
	
    private static boolean checkInconsistent(int i, ArrayList<Integer> domain){
        for(int j: domain){
            if(i != j){
                return false;
            }
        }
        return true;
    }
	
}
