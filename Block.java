//Block.java
//Austin Teshuba
//This is the block class that stores the blocks added for arkanoid
//Fields: 
// Color is the integer that grabs the correct color from the array
// boxX and boxY is the x and y coordinates of the box
// width and height control the width and height of the box
// this disable checks to see if the box has been disabled. if so, it will appear invisible until removed. 
//Methods:
//getWidth gets the width field
//getHeight gets the height field
//getX returns the x field
//get y returns the y field
//getBottom gets the bottom edge y coordinate
//getTop gets the top edge y coordinate
//getLeft gets the left edge x coordinate
//getRight gets the right edge x coordinate

import java.awt.Color;//imports
import java.awt.Rectangle;
import java.util.Arrays;
public class Block {
	private int color;//index for color array
	private int boxX, boxY;//the x and y coordinates
	//private int previousX, previousY;
	private final Color[] colors = {new Color(255,0,0), new Color(255,255,255), new Color(0,255,0)};//this is an array of the colors used. 
	static int width = 50;//controls width
	static int height = 20;//controls height
	private boolean disable = false;//if this is true, the block has been hit to the point of death.
	
	public Block (Color startingColor, int bX, int bY) {//inits with a starting color
		color = Arrays.asList(colors).indexOf(startingColor);//stores the color int as the corresponding index to the color
		boxX = bX;//stores the inputted x and y 
		boxY = bY;
		
	}
	public Block (int col, int bX, int bY) {//convinenice initializer
		color = col;//stores the inputted color index, x and y.
		boxX = bX;
		boxY = bY;
	}
	public static int getWidth() {//returns width
		return width;
	}
	public static int getHeight() {//returns height
		return height;
	}
	public int getX() {//returns x
		return boxX;
	}
	public int getY() {//returns y
		return boxY;
	}
	public Color getColor() {//get color returns the actual color of the block.
		if (disable) //if the block is disabled, send back a black transparent color. 
			return new Color(0,0,0,0);//IN CASE OF ANY ISSUES WITH TIMING. NOT USED AS REGULAR IMPLEMENTATION.
		return colors[color];//return the color from the final color array
	}
	public int getBottom() {//calculates the bottom edge
		return boxY+height;
	}
	public int getRight() {//calculates the right edge
		return boxX+width;
	}
	public int getLeft() {//returns left edge
		return boxX;
	}
	public int getTop() {//returns top edge
		return boxY;
	}
	public boolean getDisable() {//gets disbaled status
		return disable;
	}
	public Rectangle getRect() {//gets the rectangle of the block via the built in rectangle class. 
		return new Rectangle(boxX,boxY,width,height);
	}
	
	public int downColor() {//changes the color of the brick down one in the hierarchy. returns increase in score. 
		
		color-=1;
		if (color<0) {//if the color index is now less than one, the block has been destroyed
			disable=true;
		}
		return (color+2)*10;//10 points for first, 20 for second, etc.
	}
}
