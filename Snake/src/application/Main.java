package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Main extends Application {
	
	public static final int snakeSize = 15;
	public static final int windowWidth = 844;
	public static final int windowHeight = 558;
	public static final int boardHeight = 482;
	public static int cycle = 200; // in milli (1000milli = 1sec)
	public static final int minCycle = 30; // minimal cycle
	public static final int numOfSquaresInRow = Main.windowWidth/Main.snakeSize;
    public static final int numOfSquaresInColumn = Main.boardHeight/Main.snakeSize;
    
    public static Color backgroundColor = Color.POWDERBLUE;
    public static Color snakeColor = Color.BLACK;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/MainWindow.fxml"));
			//root.setShape(SnakePoint.generatePoint());
			Scene scene = new Scene(root,windowWidth,windowHeight);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
