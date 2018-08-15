//Sound.java
//Austin Teshuba
//This is a class for all sounds, and contains utilities to manage, play, restart, and loop sounds.

import java.io.File;

import javax.sound.sampled.*;
public class Sound {
	private Clip soundClip;//store the sound clip
	private double volume = 1;//this controls the volume
	public Sound (String fileName, double volume) {//init the class
		try {//try to load the song.
			File file = new File(fileName);//get the file
			if (file.exists()) {
				//soundClip = AudioSystem.getClip();
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);//get the clip as an audiostream
				soundClip = AudioSystem.getClip();//change to sound clip
                	soundClip.open(sound);//open the sound
			} else {//if it doesnt exist, say that in console
				System.out.println("Sound file not working.");
			}
		} catch (Exception e) {//might not work. print error. 
			e.printStackTrace();
		}
		this.volume = volume;//store volume
	}
	public void play() {//plays the song
		soundClip.setFramePosition(0);//start from beginning
		
		FloatControl soundControl = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);//this controls volume
		soundControl.setValue((float) Math.max(0, Math.max(6, volume)));//max of 6, min of 0 so make sure we stay in that. 
		soundClip.start();//start the sound
	}
	public void stop() {//stops the song
		soundClip.stop();
	}
	public void loop() {//loops the song indefinitely
		soundClip.setFramePosition(0);
		soundClip.loop(Clip.LOOP_CONTINUOUSLY);//just do it forever.
	}
	public int getLength() {//get the length of the clip in milliseconds. 
		return (int) (soundClip.getMicrosecondLength()/1000);
	}

}
