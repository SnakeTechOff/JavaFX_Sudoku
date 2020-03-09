package file;

import java.io.File;
import java.util.Scanner;

import application.Path;

public class LoadSudoku {
	
	// Load sudoku with file
	public static int[][] load() throws Exception {
		
		int[][] gridLoaded = new int[9][9];
		String path = Path.Load;
		Scanner file = new Scanner(new File(path));
		file.useDelimiter("");
		
		while (file.hasNextLine()) {
			for(int i=0;i<9;i++) {
				String line = file.nextLine();
				String[] numbers = line.split("");
				for(int j=0;j<9;j++) {
					gridLoaded[i][j] = Integer.parseInt(numbers[j]);
				}
			}
        }
		file.close();
		return gridLoaded;
	}
}
