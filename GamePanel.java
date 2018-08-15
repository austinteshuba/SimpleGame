//GamePanel.java
//Austin Teshuba
//This is the home to most of the game logic, and is the panel that contains all of the moving parts of the game itself



import java.awt.Color;//imports
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.*;


public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {
	private int ballX, ballY, sliderX, sliderY;//coordinate variables
	private int ballVX, ballVY, sliderVX, sliderVY;//velocity variables
	private ArrayList<Block> blocks;//all of the blocks added
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();// all of the powerups made
	private GameImage slider;//these are the stores images for various elements of the game
	private GameImage background;
	private GameImage ball;
	private GameImage logo;
	private GameImage pauseButton;
	private GameImage healthPowerUp;
	private GameImage longPowerUp;
	private GameImage bonusPointsPowerUp;
	private int score=0;//starting score is 0. 
	private int ballSpeed;//controls how fast the ball moves.
	private double ballSpeedCounter; //this is what will incrment in real time.
	public boolean ready = false;//this is a bool that checks if the game is ready to start
	int gap = 5;//gap for the brick
	private boolean pause = false;
	private Timer pauseTimer;//a timer for the pauses.
	private Sound deathSound = new Sound("src/death.wav", 1);//these are stores sounds which will be used throughout the game
	private Sound hitSound = new Sound("src/hit.wav", 2);
	private Sound backgroundMusic = new Sound("src/music.wav", 1);
	private Sound levelUp = new Sound("src/levelUp.wav", 2);
	private boolean death=false;//this is a bool for losing
	private int h=70;//set height of bottom bar
	private boolean stopped=false;//did the user STOP the game.
	private boolean extended=false;//this means the slider is extended
	private int adjust = 40;//controls how much bigger the slider gets when extended
	private Timer extendTimer = new Timer(5000,this);//timer for extending powerup.
	private int originalSliderWidth, originalSliderHeight;//stores the original dimensions of the slider
	private boolean died = false;//check if the ball is dead.

	
	
	int lives = 3;//start the game with 3 lives
	
	private boolean[] keys;//this is an array that will contain the pressed status of all the buttons.
	
	public GamePanel() {//initializer for the GamePanel class
		addMouseListener(this);//add the listeners
		addMouseMotionListener(this);
		addKeyListener(this);
		
		keys = new boolean[KeyEvent.KEY_LAST+1];//make the keys list 
		
		//GameImage slider = null;
		
		blocks = new ArrayList<Block>();//stores the blocks.
		//image = null;
		
		//load pause button 
		try {
			pauseButton = new GameImage(ImageIO.read(new File("src/pause.png")), 0,0);
			pauseButton.resize(40, 40);//resize to be smaller
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {//load the slider
			slider = new GameImage(ImageIO.read(new File("src/2dplatform.png")), 0, 0);//get the slider image and create a new GameImage instance
			originalSliderWidth = 100;//store the original dimensions and resize accordingly
			originalSliderHeight = 20;
			slider.resize(originalSliderWidth, originalSliderHeight);//resize and position image
			slider.setX(GamePlay.getMiddleX() - slider.getWidth()/2);//set the appropriate x and y
			slider.setY(GamePlay.getScreenHeight() - 100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//load the logo
		try {
			logo = new GameImage(ImageIO.read(new File("src/gameLogo.png")),0,0);
			logo.resize(200, 50);//resize 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {//load the background. Resize.
			background = new GameImage(ImageIO.read(new File("src/b.jpg")), 0, 0);
			background.resize(GamePlay.getScreenWidth(), GamePlay.getScreenHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {//load and resize ball.
			ball = new GameImage(ImageIO.read(new File("src/ball.png")), 0, 0);
			ball.resize(20, 20);
			ball.setX(slider.getX() + slider.getWidth()/2 - ball.getWidth()/2);
			ball.setY(slider.getY()-ball.getHeight()-5);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//load health powerup image
		try {
			healthPowerUp = new GameImage(ImageIO.read(new File("src/healthPowerUp.png")),0,0);
			healthPowerUp.resize(10, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//load long powerup image
		try {
			longPowerUp = new GameImage(ImageIO.read(new File("src/longPowerUp.png")), 0, 0);
			longPowerUp.resize(10, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//load the bonus points
		try {
			bonusPointsPowerUp = new GameImage(ImageIO.read(new File("src/bonusPointsPowerUp.png")), 0,0);
			bonusPointsPowerUp.resize(10, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//music
		backgroundMusic.loop();
		
		
		
		makeBricks(12,8);//make 12 rows of bricks, 8 columns of bricks
	}
		public void makeBricks(int across, int column) {//this makes a new block of bricks. 
			
			int acrossAmount = across;//how many blocks will be across the screen dont make a multiple of 3 and ensure it's even.
			int colAmount = column;//how many blocks will be stacked on top of each other
			int startingBlockX = (GamePlay.getMiddleX()) - (Block.getWidth()*(acrossAmount/2)) - acrossAmount*gap/2; // where the first block will be placed in the x and y directions.
			int startingBlockY = (GamePlay.getMiddleY()/2) - (Block.getHeight()*(colAmount/2)) - acrossAmount*gap/2;
			Random ran = new Random();
			for (int x=0;x<acrossAmount;x++) {//makes the blocks in order. Colors are random 
				for (int y=0; y<colAmount; y++) {
					blocks.add(new Block(ran.nextInt(3), startingBlockX + (x*(Block.getWidth()+gap)), startingBlockY + (y*(Block.getHeight()+gap))));
				}
			}

		}
		
		
		public void respawn() {//respawns the ball once it dies
			
			slider.resize(100, 20);//resize and position image
			slider.setX(GamePlay.getMiddleX() - slider.getWidth()/2);//reset the x to the orignal position
			slider.setY(GamePlay.getScreenHeight() - 100);
			
			ball.setX(slider.getX() + slider.getWidth()/2 - ball.getWidth()/2);//set the ball to the center of the slider
			ball.setY(slider.getY()-ball.getHeight()-5);
			
			ballVX=1;
			ballVY=-1;//reset the velocity to a 45 degree angle up to the right
			
			died=false;//no longer died.
		}
		public void addNotify() {//called when added
	        super.addNotify();
	        ready = true;//if its ready, change the flag
	        ballVY = -1;//Set the ball velocity to 1. Negative VY makes it go up, positive makes it go down.
	        ballVX = 1;//start at a 45 degree angle to the right. Positive to the right. 
	        ballSpeed = 1;//start at speed of 1.
	    }
		public void refresh() {//this is checking for key events
			if(keys[KeyEvent.VK_RIGHT]){//if it is to the right, the sliderVX is 1
				sliderVX = 1;
				//System.out.println("Called");
			}
			else if(keys[KeyEvent.VK_LEFT]){//if it moves to the left, it is -1
				sliderVX =- 1;
			}
			else {
				sliderVX = 0;//not moving = 0
			}
			
		}
		public void death() {//this function handles death (if the ball falls off the bottom of the screen)
			if (died==false) {
				lives-=1;
				deathSound.play();//play the death sound
				
			}
			pauseGame(deathSound.getLength());//pause the game until the death sound is over
			if (lives==0) {//if you have no lives left, you died forever and the flag changes.
				death=true;
			}
			died=true;
		}
		public void levelUp() {//this moves up a level if there are no bricks left
			pauseGame(500);//pause the game, play a nice song, and make more bricks!
			makeBricks(12,8);
			levelUp.play();
		}
		
		public void move() {//this handles all movement and is called every frame
			//make powerups randomly
			Random rand = new Random();//random number generator
			int value = rand.nextInt(500000);//there is a 1 in 500000 chance there will be a new powerup every frame.
			if (value==0) {//if the value is 0
				Random ran = new Random();//make a random number generator and a power up vairable
				PowerUp currentPowerUp = null;
				int type = ran.nextInt(3);//switch the 1 for the amount of types made.
				if (type==0) {//add the powerup depending on the random number. There are 3 types
					currentPowerUp = new PowerUp(healthPowerUp, 0);//this adds a life
				}
				else if (type==1) {
					currentPowerUp = new PowerUp(longPowerUp, 1);//this makes the slider wider
				} else if (type==2) {
					currentPowerUp = new PowerUp(bonusPointsPowerUp, 2);//this just adds points
				}
					
				//set x and y
				//make a random x
				int newX = ran.nextInt(401) - 201 + GamePlay.getScreenWidth()/2; //-200 to 200 variance from center
				int newY = ran.nextInt(201) - 101 + GamePlay.getScreenHeight()/4;//-100 to 100 variance from the 25% of top distance line.
				if (currentPowerUp!=null) {
					currentPowerUp.setX(newX);
					currentPowerUp.setY(newY);
					powerUps.add(currentPowerUp);//add the powerups to an array list
				} else {
					System.out.println("PowerUp error.");//if the variable is null, something went wrong.
				}
			}
			
			//move all powerUps down by ball speed
			for (PowerUp power: powerUps) {
				power.setY(power.getY() + ballSpeed);
			}
			
			//check if the powerups are off the screen. remove if they are. 
			for (int r = powerUps.size()-1; r>=0; r--) {
				if (powerUps.get(r).getY() > GamePlay.getScreenHeight()-h) {
					powerUps.remove(r);
				}
			}
			
			
			
			
			//death of a ball
			if (ball.getY()>GamePlay.getScreenHeight() && pause==false) {
				death();
			}
			
			//no bricks left
			if (blocks.size()==0) {
				//you leveled up!
				levelUp();
			}
			
			
			//wall collision check for ball
			//right wall
			if (ball.rightCollide(GamePlay.getScreenWidth())) {//seperated for later use if wanted.
				ballVX*=-1;
			} else if (ball.leftCollide(0)) {//left wall
				ballVX*=-1;
			}
			
			if (ball.topCollide(0)) {//top wall
				ballVY*=-1;
			} //dont check bottom wall because that is the death of a ball.
			
			//collision with the slider.
			if (ball.getRect().intersects(slider.getRect()) && ball.getY()+ball.getHeight() <= slider.getY()+ballSpeed) {//intersected bool needed
				ballVY*=-1;
				ballVX+=sliderVX;//add the slider velocity to the ball velocity, giving user some control over angle. 
				
			}
			
			//collision with the powerups
			
			for (int a = powerUps.size()-1; a>=0; a--) {//iterate thrpough the powerups
				PowerUp power = powerUps.get(a);
				if (power.getRect().intersects(slider.getRect())) {//if the powerups rect intersects, the slider caught it. Remove from screen
					//THIS MEANS THEY CAUGHT THE POWERUP
					if (power.getType()=="HEALTH" && power.getIntersected()==false) {
						power.setIntersected(true);
						if (lives<5)
							lives+=1;//add a life up to 5
						powerUps.remove(a);
					} else if (power.getType()=="LONG" && power.getIntersected()==false) {
						power.setIntersected(true);
						extended = true;//extended flag to say the slider is extended. extend the slider's width by the adjust amount preset globally
						slider.resize(slider.getWidth()+adjust, slider.getHeight());
						slider.setX(slider.getX()-adjust/2);
						extendTimer.restart();//restart the entend timer
						powerUps.remove(a);
					} else if (power.getType()=="BONUS" && power.getIntersected()==false) {
						power.setIntersected(true);
						score+=100;//add a 100 points.
						powerUps.remove(a);
					}
				}
			}
			
			//collision with blocks
			
			boolean changed = false;//this is a flag to see if any collisions occured this frame
			for (Block block:blocks) {
				Rectangle r = block.getRect();		
				if (r.intersects(ball.getRect())) {//if the rects intersected, something hit something. However, we don't know from what direction
					//since the vy is always going to be by 1, we can check if it is equal. There is no multiples to deal with. 
					
					//this checks if it was colliding from the bottom. Check the bottom edge of block and top of the ball
					if (ball.getY() + 2 >= block.getBottom() && ball.getY() -2 <= block.getBottom() && ball.getIntersected()==false) {
						//bottom collision
						ballVY*=-1;//reflect back
						score += block.downColor();//change the color, adjust score
						ball.setIntersected(true);//set the intersected flag so it will not trigger again for the same stimuli
						ball.setY(ball.getY() + ballVY);//set the y to remove the ball from collision
						changed=true;//set the changed flag to true
						hitSound.play();//play a sound
						
					} else if (ball.getY() + ball.getHeight() + 2 >=block.getTop() && ball.getY() + ball.getHeight() -2 <=block.getTop() && ball.getIntersected()==false) {
						//this checks if it was colliding on the top. Check bottom edge of ball and top of block.
						ballVY*=-1;//reflect back
						score += block.downColor();//change the color, adjust score
						ball.setIntersected(true);//set the intersected flag so it will not trigger again for the same stimuli
						ball.setY(ball.getY() + ballVY);//set the y to remove the ball from collision
						changed=true;//set the changed flag to true
						hitSound.play();//play a sound
					}
					
					if (changed==false && ball.getIntersected()==false) {
						//this must mean it is on the left or right
						//it doesn't matter which one, the change is the exact same!
						ballVX*=-1;//flip the x direction
						score+=block.downColor();//change color, adjust score
						ball.setIntersected(true);//set the intersected flag
						ball.setX(ball.getX() + ballVX);
						changed=true;//set the changed flag
						hitSound.play();//play the sound
					}
					
					
					}
			}//end the for.
			if (changed==false) {//if no changes, reset the flag.
				ball.setIntersected(false);
			}
			
			
			
			//move everything as per velocity
			if (slider.getX() > 10 && sliderVX<0) {
				slider.setX(slider.getX() + sliderVX*ballSpeed*2);//the slider can move twice as fast as the ball.
			} else if ((slider.getX()+slider.getWidth()) < GamePlay.getScreenWidth()-10 && sliderVX>0) {
				slider.setX(slider.getX() + sliderVX*ballSpeed*2);
			}
			if (ready) {
				ball.setX(ball.getX() + ballVX);
				ball.setY(ball.getY() + ballVY);
			}
			
			
		}
		
		public void setKey(int index, boolean b) {//basically a workaround for the keylisteners because they SUCK. This sets the keys array.
			keys[index] = b;
		}
		public void pauseGame(int time) {//pauses for a specified amount of time in milliseconds. 
			if (extended)//if the slider is currently extended stop the timer
				extendTimer.stop();
			pauseTimer = new Timer(time, this);//set the pause timer for the time and then start the timer. set the pause flag to true
			pauseTimer.start();
			pause=true;
		}
		public void stopGame() {//pauses until user says no
			if (extended)
				extendTimer.stop();//stops the extend timer
			pause=true;//waits for other stimuli to unpause.
			stopped = true;//set the stopped flag to true
			repaint();//repaint once to indicate user said to pause
			
		}
		public void resumeGame() {//resumes game after pause 
			if (extended)
				extendTimer.start();//restart timer if thats relevant
			pause=false;//turn off all flags for pause/stop
			stopped = false;
		}
		public boolean getPause() {//gets pause status
			return pause;
		}
		public boolean getReady() {//gets ready status
			return ready;
		}
		public boolean getDeath() {//gets death status
			return death;
		}
		
		public void paintComponent(Graphics g) {//this is where all f the actual painting happens

			if (death==false) {//if we arent dead, the game must be drawn
				if (background!=null) {//draw background
					g.drawImage(background.getImage(), background.getX(), background.getY(), this);
				}
				
				
				
				if (blocks!=null) {//draw the blocks. from the list
					//System.out.println("Yes");
					for (int x = blocks.size()-1; x>= 0; x--) {
						if (blocks.get(x).getDisable()) {//if the blocks are disabled, destroy
							blocks.remove(x);
						}
					}
					for (int t = 0; t<blocks.size(); t++) {//now draw the blocks.
						//System.out.printf("%d %d %d %d\n",blocks.get(t).getX(), blocks.get(t).getY(), blocks.get(t).getWidth(), blocks.get(t).getHeight());
						g.setColor(blocks.get(t).getColor());
						g.fillRect(blocks.get(t).getX(), blocks.get(t).getY(), blocks.get(t).getWidth(), blocks.get(t).getHeight());
					}
				}
				
				if (slider!=null) {//if the slider isnt null, draw it
					//System.out.printf("%d %d\n",slider.getX(), slider.getY());
					g.drawImage(slider.getImage(), slider.getX(), slider.getY(), this);
				}
				
				if (ball!=null) {//draw ball.
					g.drawImage(ball.getImage(), ball.getX(), ball.getY(), this);
				}
				for (PowerUp power: powerUps) {//draw all of the powerups
					g.drawImage(power.getImage(), power.getX(), power.getY(), this);
				}
				
				
				
				
				//draw bottom frame
				
				g.setColor(new Color(0,0,0));
				g.fillRect(0, GamePlay.getScreenHeight()-h, GamePlay.getScreenWidth(), h);//this is the menu bar
				
				//draw score
				g.setColor(new Color(255,255,255));
				g.setFont(new Font("Helvetica", Font.PLAIN, 32));
				g.drawString(String.format("Score: %d", score), GamePlay.getScreenWidth() - 5 - g.getFontMetrics().stringWidth(String.format("Score: %d", score)), GamePlay.getScreenHeight() - 5 - 32);
				
				//paused
				if (stopped) {
					g.drawString("Paused", 10, 42);//upper left corner of the screen. just say paused so user knows
				}
				//lives
				int livesFontSize=15;
				g.setFont(new Font("Helvetica", Font.PLAIN, livesFontSize));
				g.drawString(String.format("Lives: %d",  lives), 20+5+pauseButton.getWidth(), GamePlay.getScreenHeight()-h+(2+livesFontSize));
				
				for (int x=0; x<lives; x++) {//draw one ball for every life they have. 
					//System.out.println("hey");
					g.drawImage(ball.getImage(), 5+pauseButton.getWidth()+20+(5+ball.getWidth())*x, GamePlay.getScreenHeight()-(h/2)-(ball.getHeight())+(livesFontSize/2), this);
				}
				
				//logo
				g.drawImage(logo.getImage(), GamePlay.getScreenWidth()/2-logo.getWidth()/2, GamePlay.getScreenHeight() - (h/2) - (logo.getHeight()/2)-10, this);
				
				//pause button
				g.drawImage(pauseButton.getImage(), 5, GamePlay.getScreenHeight() - (h/2) - pauseButton.getHeight()/2 -10 , this);
				
			} else {//this means the player died.
				
				//draw black background 
				
				//draw logo
				logo.resize(400,100);
				g.setColor(new Color(0,0,0));
				g.fillRect(0, 0, GamePlay.getScreenWidth(), GamePlay.getScreenHeight());
				g.drawImage(logo.getImage(), GamePlay.getScreenWidth()/2 - logo.getWidth()/2, 100, this);
				
				g.setColor(new Color(255,255,255));//draw game over text
				g.setFont(new Font("Helvetica", Font.PLAIN, 60));
				g.drawString("Game Over!", GamePlay.getScreenWidth()/2 - g.getFontMetrics().stringWidth("Game Over!")/2, GamePlay.getScreenHeight()/2 - 30);
				
				//draw score. 
				g.drawString(String.format("Your Score: %d", score), GamePlay.getScreenWidth()/2 - g.getFontMetrics().stringWidth(String.format("Your Score: %d", score))/2, GamePlay.getScreenHeight()/2 + 100);
			}
			
		}
		
		
	
	
	
	
	


	@Override
	public void mouseReleased(MouseEvent e) {//checks if the mouse is released
		// TODO Auto-generated method stub
		int mouseX = e.getX();//check to see if the mouse collided with the mouse button
		int mouseY = e.getY();
		int pauseButtonX = 5;
		int pauseButtonY = GamePlay.getScreenHeight() - (h/2) - pauseButton.getHeight()/2-10;
		Rectangle pauseButtonRect = new Rectangle(pauseButtonX, pauseButtonY, pauseButton.getWidth(), pauseButton.getHeight());//make the button a rect, and make the mouse a point
		System.out.printf("Mouse: %d, %d. Box: %d, %d\n", mouseX, mouseY, pauseButtonX, pauseButtonY);
		//pause button collision
		
		Point mousePoint = new Point(mouseX, mouseY);
		
		if (pauseButtonRect.contains(mousePoint) && died==false) {//if it collided
			if (stopped) {//toggle the pause
				resumeGame();
			} else {
				stopGame();
			}
		}
		//g.drawImage(pauseButton.getImage(), 5, GamePlay.getScreenHeight() - (h/2) - pauseButton.getHeight()/2 -10 , this);
		//i know it looks bad, but these are the parameters of the rect surrounding the button so
			//}
	}
	
	//action listener.
	@Override
	public void actionPerformed(ActionEvent e) {//timer is up
		// TODO Auto-generated method stub
		//System.out.println("Hey");
		if (extended && pause==false) {//if it is the extended timer, make the slider back to normal (or make steps towards it in cases of compounded powerups)
			if (slider.getWidth()>originalSliderWidth)
				slider.resize(slider.getWidth()-adjust, slider.getHeight());//resize to normal
			slider.setX(slider.getX()+adjust/2);
			extendTimer.stop();//stop timer
			extended=false;//reset flag
		}
		if (death==false && pause) {//this means we are paused
			respawn();//reset the things.
			resumeGame(); //restart the game
			pauseTimer.stop();//stop the timer
		} else if (pause) {//we are dead.
			this.repaint();//repaint the end screen
		}
	}
	//unused key listeners
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	//mouse motion listener
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	//mouse listener
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//PAUSE BUTTON IMPLEMENTATION
	
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
