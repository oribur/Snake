package application;

import java.util.Random;

import application.SnakeController.Directions;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SnakePoint extends Rectangle {
	
	int score;
	Directions direction;
	int xProg, yProg;
	FillTransition tr;
	

	private static final Color[] colors = {Color.YELLOW,Color.ORANGE,Color.ORANGERED,Color.RED,Color.PURPLE};
	
	public SnakePoint(int x, int y)
	{
		// create background squares
		setWidth(Main.snakeSize);
		setHeight(Main.snakeSize);
		setFill(Main.backgroundColor);
		direction = null;
		setX(x);
		setY(y);
		tr = new FillTransition();
	    tr.setShape(this);
	    tr.setDuration(Duration.millis(Main.cycle/2));
	}
	
	public SnakePoint()
	{
		// only for initial food
		setWidth(Main.snakeSize);
		setHeight(Main.snakeSize);	
	}
	
	public void becomeSnake(Directions dir)
	{
		direction = dir;
		setProgs();
		setFill(Main.snakeColor);
//		tr.setFromValue(Main.backgroundColor);
//	    tr.setToValue(Main.snakeColor);
//	    tr.play();
	}
	
	public void becomeBoard()
	{
		setFill(Main.backgroundColor);	 
		
//	    tr.setFromValue(Main.snakeColor);
//	    tr.setToValue(Main.backgroundColor);
//	    tr.play();
	}
	
	public static void AnimateBackgroundColor(VBox vbox, Color fromColor,Color toColor,int duration)
	{
	    Rectangle rect = new Rectangle();
	    rect.setFill(fromColor);

	    FillTransition tr = new FillTransition();
	    tr.setShape(rect);
	    tr.setDuration(Duration.millis(duration));
	    tr.setFromValue(fromColor);
	    tr.setToValue(toColor);

	    tr.setInterpolator(new Interpolator() {
	        @Override
	        protected double curve(double t) {
	        	vbox.setBackground(new Background(new BackgroundFill(rect.getFill(), CornerRadii.EMPTY, Insets.EMPTY)));
	            return t;
	        }
	    });

	    tr.play();
	}
	
	public Directions getDirection()
	{
		return direction;
	}
	
	public void setDirection(Directions dir)
	{
		direction = dir;
		setProgs();
	}
	
	public void refreshFood()
	{
		Random rand = new Random();
		setX(rand.nextInt(Main.numOfSquaresInRow)*Main.snakeSize);
		setY(rand.nextInt(Main.numOfSquaresInColumn)*Main.snakeSize);
		score = rand.nextInt(5);
		setFill(colors[score++]);
	}
	
	private void setProgs()
	{
		switch(direction)
		{
			case LEFT:
				xProg = -1;
				yProg=0;
				break;
			case RIGHT:
				xProg = 1;
				yProg=0;
				break;
			case UP:
				yProg = -1;
				xProg=0;
				break;
			case DOWN:
				yProg = 1;
				xProg=0;
				break;
			default:
					
		}
	}
	
	public int getxProg() {
		return xProg;
	}
	
	public void setxProg(int xProg) {
		this.xProg = xProg;
	}
	
	public int getyProg() {
		return yProg;
	}
	
	public void setyProg(int yProg) {
		this.yProg = yProg;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}
