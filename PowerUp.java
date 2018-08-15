//PowerUp.java
//Austin Teshuba
//This is a subclass of GameImage specifically for powerup, that contains its special type abilities

import java.awt.image.BufferedImage;

public class PowerUp extends GameImage {//inherits from GameImage class
	private int type;//stores type
	
	
	public PowerUp(BufferedImage i, int x1, int y1, int type) {//init the power up
		super(i, x1, y1);//call the init from Game Image
		this.type=type;//store the type
		
	}
	public PowerUp(GameImage i, int type) {//another init that alloww for a GameImage to be passed in instead of individual properties
		super(i.getImage(), i.getX(), i.getY());
		this.type = type;
	}
	
	public String getType() {//returns the type in string format
		if (type==0) {//if 0, health
			return "HEALTH";
		} else if (type==1) {//if 1, it is long
			return "LONG";
		} else if (type==2) {//if 2 it is bonus
			return "BONUS";
		}
		return "NONE";//otherwise, it is nothing and likely an error. 
		
	}
}