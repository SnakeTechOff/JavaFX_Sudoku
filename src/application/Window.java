package application;


import java.util.concurrent.CountDownLatch;

import environment.Sudoku;
import file.LoadSudoku;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
//import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Window extends Application {
    
    private AnchorPane ap_scene;
    private BorderPane bp_all;
    private static final CountDownLatch latch = new CountDownLatch(1);
    private static boolean closed;
    private CheckBox cb_mrv;
	private CheckBox cb_dh;
	private CheckBox cb_lcv;
	private CheckBox cb_ac3;
	private Button bt_start;
	private Button bt_stop;
	private Button bt_generate;
	private Button bt_clean;
	private Button bt_file;
	private Text tt_info;
	private Rectangle rt_menu;
	private GridPane gp_grid;
	private StackPane sp_menu;
	private GridPane gp_cb;
	private GridPane gp_bt;
	private VBox vb_menu;
	private Label lb_sudoku;
	private Sudoku sudoku;
	private Text[][] sudokuText;
	private int[][] sudokuCopy;
    
    
    // Init interface
    public Window(){
         this.create_widgets();
         this.modify_widgets();
         this.create_layouts();
         this.modify_layouts();
         this.add_widgets_to_layouts();
         this.add_layouts_to_layouts();
         this.setup_connections();
         this.sudoku = new Sudoku(getInstance());
         this.sudokuText = new Text[9][9];
         this.sudokuCopy = new int[9][9];
         this.initColor();
         this.initGrid();
    }
    
	public Window getInstance() {
		return this;
	}
    
    public static void awaitFXToolkit() throws InterruptedException {
        latch.await();
     }
    
    @Override
    public void init() {
        latch.countDown();
    }
    
    // Create widgets
    private void create_widgets() {
    	
        // Rectangle
        this.rt_menu = new Rectangle(550.00, 100.00);
        
        // Label
        this.lb_sudoku = new Label("Sudoku");
        
        // Button
        this.bt_start = new Button("Start resolution");
        this.bt_stop = new Button("Stop resolution");
        this.bt_generate = new Button("Random generation");
        this.bt_clean = new Button("Clean case");
        this.bt_file = new Button("Load File");
        
        // CheckBox
        this.cb_mrv = new CheckBox("MRV");
        this.cb_dh = new CheckBox("DH");
        this.cb_lcv = new CheckBox("LCV");
        this.cb_ac3 = new CheckBox("AC3");
        
        // Text
        this.tt_info = new Text("Research in progress ...");
    }
    
    // Modify widgets
    private void modify_widgets() { 
    	
        // Rectangle
        this.rt_menu.setArcHeight(40.00);
        this.rt_menu.setArcWidth(40.00);
        
        // Label
        this.lb_sudoku.setFont(Font.loadFont(getClass().getResourceAsStream(Path.Font), 60));
        
        // CheckBox
        this.cb_mrv.setSelected(true);
        this.cb_dh.setSelected(true);
        this.cb_lcv.setSelected(true);
        this.cb_ac3.setSelected(true);
        
    }
    
    // Create layouts
    private void create_layouts() {
    	
        this.ap_scene = new AnchorPane();
        this.bp_all = new BorderPane(); 
        this.gp_grid = new GridPane();
        this.gp_cb = new GridPane();
        this.gp_bt = new GridPane();
        this.sp_menu = new StackPane();
        this.vb_menu = new VBox();
        
    }
 
    // Modify layouts
    @SuppressWarnings("static-access")
	private void modify_layouts() {
    	
        // BorderPane
        this.bp_all.setLayoutX(10);
        this.bp_all.setLayoutY(10);
        this.bp_all.setAlignment(this.lb_sudoku, Pos.CENTER);
        this.bp_all.setAlignment(this.sp_menu, Pos.CENTER);
        
        // VBox
        this.vb_menu.setSpacing(10.00);
        this.vb_menu.setPadding(new Insets(20));
        
        // GridPane
        this.gp_grid.setAlignment(Pos.CENTER);
        this.gp_cb.setHgap(10.00);
        this.gp_cb.setId("grid");
        this.gp_bt.setHgap(10.00);
        this.gp_bt.setId("grid");
        
        // StackPane
        this.sp_menu.setPadding(new Insets(20));
   
    }   
    
    // Add widgets to layouts
    private void add_widgets_to_layouts() {
        
        // Init Board
        for(int i = 0; i < 9; i++) {
        	RowConstraints row = new RowConstraints(54);
            ColumnConstraints column = new ColumnConstraints(54);
            row.setValignment(VPos.CENTER);
            column.setHalignment(HPos.CENTER);
            gp_grid.getColumnConstraints().add(column);
            gp_grid.getRowConstraints().add(row);
        }
        
        // Menu
        this.gp_cb.add(cb_ac3, 0, 0);
        this.gp_cb.add(cb_mrv, 1, 0);
        this.gp_cb.add(cb_dh, 2, 0);
        this.gp_cb.add(cb_lcv, 3, 0);
        this.gp_bt.add(bt_start, 0, 1);
        this.gp_bt.add(bt_stop, 1, 1);
        this.gp_bt.add(bt_generate, 2, 1);
        this.gp_bt.add(bt_clean, 3, 1);
        this.gp_cb.add(bt_file, 20, 0);
        // BorderPane
        this.bp_all.setTop(lb_sudoku);
        // StackPane
        this.sp_menu.getChildren().add(rt_menu);
    }
    
    // Add layouts to layouts
    private void add_layouts_to_layouts() {
    	
        // AnchorPane
        this.ap_scene.getChildren().add(bp_all);
        
        // BorderPane
        this.bp_all.setCenter(gp_grid);
        this.bp_all.setBottom(sp_menu);
        
        // VBox
        this.vb_menu.getChildren().addAll(gp_cb,gp_bt);
        
        // StackPane
        this.sp_menu.getChildren().add(vb_menu);
        
    }
  
    private void setup_connections() {
    	bt_start.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent actionEvent) {
    	    	boolean[] choice = {cb_mrv.isSelected(), cb_dh.isSelected(), cb_lcv.isSelected(), cb_ac3.isSelected()};
    	    	sudoku.run(choice);
    	    	
    	    }
    	});
    	
    	bt_stop.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent actionEvent) {
    	    	sudoku.interrupt();
    	        /*try { 
    	            sudoku.interrupt(); 
    	        } 
    	        catch (Exception e) { 
    	            System.out.println("Exception handled"); 
    	        } */
    	    }
    	});
    	
    	bt_generate.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent actionEvent) {
    	    	sudoku.clearGrid();
    	    	clearGrid();
    	    	sudoku.generateGrid();
    	    	sudokuCopy();
    	    	setInitGrid();
    	    }
    	});
    	
    	bt_clean.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent actionEvent) {
    	    	sudoku.clearGrid();
    	    	clearGrid();
    	    }
    	});
    	
    	bt_file.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent actionEvent) {
    	    	sudoku.clearGrid();
    	    	clearGrid();
    	    	try {
					sudoku.grid = LoadSudoku.load();
				} catch (Exception e) {
					e.printStackTrace();
				}
    	    	sudokuCopy();
    	    	setInitGrid();
    	    }
    	});
    }
    
    // Copy for print
    public void sudokuCopy() {
		for (int row = 0; row < sudoku.size; row++) {
			for (int col = 0; col < sudoku.size; col++) {
				this.sudokuCopy[row][col] = this.sudoku.grid[row][col];
			}
		}
    }
    
    // Clear grid
    public void clearGrid() {
        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
            	this.sudokuText[row][col].setText("");         	
            }
        }
    }
    
    // Initialize color board
    private void initColor() {
        // Init Color
        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
            	Rectangle rt = new Rectangle(54.00, 54.00);
            	if (row == 3 || row == 4 || row == 5 || col == 3 || col == 4 || col == 5 ) {
	            	rt.setFill(Color.rgb(255, 255, 255));
            	} else {
	            	rt.setFill(Color.rgb(191, 191, 191));
            	}
            	gp_grid.add(rt, row,col);
            }
        }
        int x = Math.round((4/3)*3);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Rectangle rt = new Rectangle(54.00, 54.00);
				rt.setFill(Color.rgb(191, 191, 191));
				gp_grid.add(rt, x+i,x+j);
			}
		}
    }
    
    // Initialize GridPane
    public void initGrid() {
    	for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
		    	this.sudokuText[row][col] = new Text("");
		    	gp_grid.add(this.sudokuText[row][col], col,row);
            }
    	}
    }

    //// Getter 
    public boolean getClose() {
    	return closed;
    }

    //// Setter
    // For print timer
    public void setIntTimer(int i) {
    	
    	tt_info.setText("Resolved in " + i + "s");
    	
    }
    
    // Initialize grid
    public void setInitGrid() {
		for (int row = 0; row < sudoku.size; row++) {
			for (int col = 0; col < sudoku.size; col++) {
				if(sudoku.grid[row][col] != 0) {
					String v = Integer.toString(sudoku.grid[row][col]);
			    	this.sudokuText[row][col].setId("initText");
			    	this.sudokuText[row][col].setText(v);
				}
			}
		}
    }
    
    // Update grid
    public void setUpdateGrid(int[][] grid) {
    	
		for (int row = 0; row < sudoku.size; row++) {
			for (int col = 0; col < sudoku.size; col++) {
				if(grid[row][col] != 0 && sudokuCopy[row][col] != grid[row][col]) {
					String v = Integer.toString(grid[row][col]);
			    	sudokuText[row][col].setId("addText");
			    	sudokuText[row][col].setText(v);
				} else if (grid[row][col] == 0) {
			    	this.sudokuText[row][col].setId("initText");
			    	this.sudokuText[row][col].setText("");
				}
			}
		}
    }
    
    // For test
    public void p() {
    	System.out.println(this.cb_ac3.isAllowIndeterminate());
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(this.ap_scene,610,690);
        scene.getStylesheets().add(getClass().getResource(Path.CSS).toExternalForm());
        stage.getIcons().add(new Image(Path.Sudoku));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> closed = true);
        stage.setTitle("Sudoku");
        stage.show();
       // setInitGrid();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

