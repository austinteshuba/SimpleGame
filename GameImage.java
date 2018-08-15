//GameImage.java
//Austin Teshuba
//This class is a class for all images in the game.
// it includes vital information that makes it easier to place, organize, and manage images in the arkanoid game.
//Fields: 
// x stores the x coordinate
// y stores the y coordinate
// width stores the width
//height stores the height
//intersected says if the object has been collided in the y direction. This is a flag to prevent repeat triggers
//image stores the BufferedImage (drawable) of the actual image.
//Methods:
// getX returns the x field
//getY returns the y field
//getWidth returns the width
//getHeight returns the height
//getImage returns the image
//setX sets the x field 
//setY sets the y field
//getCenterX gets the x coordinate of the center of the image
//getCenterY gets the y coordinate of the center of the image
//Resize resizes an image to an x and y
//collideRight, collide Left, collideTop, and collideBottom check the collision status of the right, left, top, and bottom edges of the image, respectively
//getIntersected gets the intersected field
//setIntersected sets the intersected field



import java.awt.Graphics2D;//imports
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
//this is made for the sole purpose of organizing the x and y coordinates of moving image objects (not built into BufferedImage)
public class GameImage {
	private BufferedImage image;//stores image
	private int x;//stores x
	private int y;//stores y
	private int width;//stores width
	private int height;//stores height
	private boolean intersected = false;//this tracks if the thing has already been intersected.
	
	public GameImage(BufferedImage i, int x1, int y1) {//initializes the class. takes in an image, an x and a y
		image = i;//store the image, x, and the y
		x = x1;
		y = y1;

		width = i.getWidth();//set the original width and height depending on the image
		height = i.getHeight();
	}
	
	public int getX() {//returns the x
		return x;
	}
	
	public int getY() {//returns the y
		return y;
	}
	public int getWidth() {//returns the width
		return width;
	}
	public int getHeight() {//returns the height
		return height;
	}
	public BufferedImage getImage() {//returns the image
		return image;
	}
	public void setX(int x) {//sets the x
		this.x = x;
	}
	public void setY(int y) {//sets the y
		this.y = y;
	}
	public int getCenterY() {//calculates and returns the y coordinate of the center
		return y + height/2;
	}
	public int getCenterX() {//calculates and returns the x coordinate of the center 
		return x + width/2;
	}
	public BufferedImage resize(int newWidth, int newHeight) {//resizes an image based on a new width and height
		Image temporary = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);//resizes image as a Type Image.
		
		
		BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);//make a transparent Buffered IMage
		
		Graphics2D t = resized.createGraphics();//Paste Image onto Buffered Image
		t.drawImage(temporary, 0,0, null);//draw the image
		t.dispose();//disable the graphics variable
		
		image = resized;//the new image is now your image
		width = newWidth;//set the new width and height to the values passed in
		height = newHeight;
	
		return image;//return the new image (if you want it)
	}
	public boolean rightCollide(int x1) {//checks if the right is colliding with the x
		return x+width>x1;
	}
	public boolean leftCollide(int x1) {//checks if the left is colliding with the x
		return x<x1;
	}
	public boolean topCollide(int y1) {//checks if the top is colliding with the y
		return y<y1;
	}
	public boolean bottomCollide(int y1) {//checks if the bottom is colliding with the y
		return y+height>y1;
	}
	public Rectangle getRect() {//returns the rectangle of the image
		Rectangle tempRect = new Rectangle(x, y, width, height);
		return tempRect;
	}
	public boolean getIntersected() {//gets the intersected status
		return intersected;
	}

	public void setIntersected(boolean t) {//sets the intersected status
		intersected = t;
	}

}


