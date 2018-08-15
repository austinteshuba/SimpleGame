//GamePlay.java
//Austin Teshuba
//This is the file which controls the window itself, and the game on it's highest level.

import java.awt.*;//imports
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.*;
public class GamePlay extends JFrame implements ActionListener, KeyListener {
	private Timer myTimer;//this is the timer for each frame. Controls game loop
	private GamePanel game = null;//this is the game itself (the game panel)
	static int screenWidth = 800;//screenwidth and height stored here
	static int screenHeight = 650;


	public GamePlay() {//initialize the new game play
		super("Arkanoid");//call the super. name the window Arkanoid
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//exit when the window is closed
		setSize(screenWidth, screenHeight);//set the screen
		addKeyListener(this);//add the listener
		myTimer = new Timer(3, this);//make the timer trigger every 3ms. New frame every 3ms. THat's fast. 
		myTimer.start();//start timer
		game = new GamePanel();//make the game panel and add it to the screen
		add(game);
		
		//restart.addActionListener(this);
		
		setVisible(true);//make the window visible
		
	}
	public static void main (String[]args) {//this is the main called at launch. init the class and the rest is history!
		new GamePlay();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {//listens for the timer
		if (game!=null && game.getReady() && game.getPause()==false && game.getDeath()==false) {//if the game is ready, and we arent paused or dead
			//System.out.println("Happened");
			game.refresh();//key listener
			game.move();//move things
			game.repaint();//paint things
		} else if (game!=null && game.getReady() && game.getDeath()==true) {//if we are not null and ready, but dead, paint for the end screen.
			game.repaint();
			//add(restart, BorderLayout.SOUTH);
		}
		
	}
	public static int getScreenWidth() {//returns the screen width
		return screenWidth;
	}
	public static int getScreenHeight() {//returns the screen height
		return screenHeight;
	}
	public static int getMiddleX() {//returns the middle x
		return screenWidth/2;
	}
	public static int getMiddleY() {//returns the middle y
		return screenHeight/2;
	}
	@Override
	public void keyPressed(KeyEvent e) {//every time the key is pressed, this updates the key array in the game
		// TODO Auto-generated method stub
		game.setKey(e.getKeyCode(), true);
	//System.out.printf("%d\n", e.getKeyCode());
	}
	@Override
	public void keyReleased(KeyEvent e) {//every time a key is released, this updates the key array in the game
		// TODO Auto-generated method stub
		game.setKey(e.getKeyCode(), false);
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {//unused listener.
		// TODO Auto-generated method stub
		
	}

}
