package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class SnakeController implements Initializable{
	
	public enum Directions{
		UP, DOWN, LEFT, RIGHT
	}
	
	int score;
	ArrayList<SnakePoint> board;
	ArrayList<SnakePoint> snake;
	SnakePoint currentFood;
	private boolean isPlaying;
	private boolean isIncreasing;
	Directions direction;
	boolean directionChanged;
	int headInd;
	int tailInd;
	Thread t;
	
	@FXML
    private AnchorPane root;

    @FXML
    private Rectangle gamePlace;

    @FXML
    private Button startBtn;

    @FXML
    private Button pauseBtn;

    @FXML
    private Label scoreLabel;
	
    @FXML
    private VBox menuBox;

    @FXML
    private ComboBox<String> gameSpeedComboBox;
    
    @FXML
    private Pane menuPane;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		snake = new ArrayList<SnakePoint>();
		board = new ArrayList<SnakePoint>();
		gameSpeedComboBox.getItems().addAll("Slow","Normal","Fast","Increasing");
		gameSpeedComboBox.setValue("Normal");
		menuBox.setStyle("-fx-background-color: #07659d");
		gamePlace.setFill(Main.backgroundColor);
		score = 0;
		isPlaying = false;
		direction = Directions.RIGHT;
		directionChanged = false;
		initBoard();
		SnakePoint.AnimateBackgroundColor(menuBox,Main.snakeColor,Main.backgroundColor, Main.cycle*5);
	}

	void initBoard()
	{
		// create board squares
    	for(int i = 0; i < Main.numOfSquaresInColumn; i++)
    	{
    		for(int j = 0; j < Main.numOfSquaresInRow; j++)
        	{
    			SnakePoint point;
				point = new SnakePoint(j*Main.snakeSize, i*Main.snakeSize);
				root.getChildren().add(point);
    	    	board.add(point);
    		}
    	}
    	//create food
    	currentFood = new SnakePoint();
    	root.getChildren().add(currentFood);
    	menuBox.toFront();
	}

    @FXML
    void pauseGame(ActionEvent event) {
    	t.suspend();
    	startBtn.setText("Continue");
    	menuBox.setVisible(true);
    	menuBox.toFront();
    	pauseBtn.setDisable(true);
    }
    
    @FXML
    void startGame(ActionEvent event) {
    	menuBox.setVisible(false);
    	isPlaying = true;
    	pauseBtn.setDisable(false);
    	
    	if(t == null || !t.isAlive()) // create new game
    	{
    		isIncreasing = false;
    		switch(gameSpeedComboBox.getValue().charAt(0))
        	{
    	    	case 'S': //Slow
    	    		Main.cycle = 400;
    	    		break;
    	    	case 'F': //Fast
    	    		Main.cycle = 100;
    	    		break;
    	    	case 'I': //Increasing
    	    		isIncreasing = true;
    	    	case 'N': //Normal
    	    	default:
    	    		Main.cycle = 200;
    	    		break;
        	}
    		gameSpeedComboBox.setDisable(true);
    		for(SnakePoint point : snake)
    		{
    			point.becomeBoard();
    		}
    		snake.clear();
    		headInd = tailInd = (Main.numOfSquaresInColumn/2)*Main.numOfSquaresInRow + Main.numOfSquaresInRow/2;
    		snake.add(board.get(headInd));
    		direction = Directions.RIGHT;
    		snake.get(0).becomeSnake(Directions.RIGHT);
    		currentFood.refreshFood();
    		scoreLabel.setText("0");
		  	score = 0;
	    	startCycles();
    	}
    	else { // game was paused
    		t.resume();
    	}
    }
    
    void startCycles()
    {
    	t = new Thread(){
    	    public void run(){
    	        System.out.println("Thread Running");
    	        while(isPlaying)
    	    	{
    	    		try {
    					Thread.sleep(Main.cycle);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    	    		moveSnake();
    	    	}
    	        System.out.println("Thread Ended");
    	      }
    	    };
    	t.start();
    }
    
    void moveSnake()
    {
    	boolean addPart = false;
    	int prevHeadInd;
    	// check if head reached food
    	if(snake.get(0).getX() == currentFood.getX() && snake.get(0).getY() == currentFood.getY())
    	{
    		score += currentFood.getScore();
    		Platform.runLater(new Runnable() {
				  @Override public void run() {
					  scoreLabel.setText(""+score);                     
				  }
				});
    		if(isIncreasing && Main.cycle > Main.minCycle)
			{
				Main.cycle -= currentFood.getScore();
			}
			currentFood.refreshFood();
			addPart = true;
		}
    	
    	// only the head and the tail will change positions
    	//change head
    	prevHeadInd = headInd;
    	headInd = (headInd + snake.get(0).getxProg() + Main.numOfSquaresInRow*snake.get(0).getyProg());
    	
    	if(snake.size() == 1 && !addPart) // head and tail are the same
		{
    		snake.get(0).becomeBoard();
			snake.remove(0);
			tailInd = headInd;
		}
    	
    	// change tail if necessary
    	if(headInd != tailInd && !addPart) 
    	{
    		snake.get(snake.size()-1).becomeBoard();
    		tailInd = tailInd + snake.get(snake.size()-1).getxProg() + Main.numOfSquaresInRow*snake.get(snake.size()-1).getyProg();
    		snake.remove(snake.size()-1);
    	}
    	if(headInd > 0 && headInd < board.size() // head doesn't exceed the board
    			&& board.get(headInd).getFill() != Main.snakeColor // head doesn't go into the body of the snake
    			&& !((prevHeadInd+1) % Main.numOfSquaresInRow == 0 && headInd % Main.numOfSquaresInRow == 0 && direction == Directions.RIGHT) // exceed board from right
    			&& !(prevHeadInd % Main.numOfSquaresInRow == 0 && (headInd+1) % Main.numOfSquaresInRow == 0 && direction == Directions.LEFT)) //exceed board from left
    	{
    		snake.add(0, board.get(headInd));
    		snake.get(0).becomeSnake(direction);
    	}
    	else { // stop game
    		isPlaying = false;
    		Platform.runLater(new Runnable() {
				  @Override public void run() {
					  	startBtn.setText("Start");
					  	pauseBtn.setDisable(true);
					  	menuBox.setVisible(true);
					  	menuBox.toFront(); 
					  	gameSpeedComboBox.setDisable(false);
				  }
				});
    	}
    }
    
    @FXML
    void arrowPressed(KeyEvent event) {
    	KeyCode key = event.getCode();
    	
    	switch(key)
    	{
	    	case LEFT:
	    		if(direction != Directions.LEFT && direction != Directions.RIGHT)
	    		{
		    		direction = Directions.LEFT;
		    		directionChanged = true;
	    		}
	    		break;
	    	case RIGHT:
	    		if(direction != Directions.RIGHT && direction != Directions.LEFT)
	    		{
		    		direction = Directions.RIGHT;
		    		directionChanged = true;
	    		}
	    		break;
	    	case UP:
	    		if(direction != Directions.UP && direction != Directions.DOWN)
	    		{
		    		direction = Directions.UP;
		    		directionChanged = true;
	    		}
	    		break;
	    	case DOWN:
	    		if(direction != Directions.DOWN && direction != Directions.UP)
	    		{
		    		direction = Directions.DOWN;
		    		directionChanged = true;
	    		}
	    		break;
    		default:
    			
    	}
    }
}
