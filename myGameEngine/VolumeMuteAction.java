package myGameEngine;
import GameServer.*;
import KittyCatGalactica.*;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import ray.audio.*;
import com.jogamp.openal.ALFactory;
import java.util.*;

public class VolumeMuteAction extends AbstractInputAction{ 
	private ArrayList<Sound> music;
	private MyGame myGame;
	private int count;
	
	public VolumeMuteAction(ArrayList<Sound> s, MyGame g)
	{
        music = s;
		myGame = g;
	} 
	
	public void performAction(float time, Event e)
	{   
		count = myGame.getRadioCount();
		boolean isPlaying = music.get(count).getIsPlaying();
		if(isPlaying == true){
			music.get(count).pause();
		}else if(isPlaying == false){
			music.get(count).resume();
		}
		
	} 
} 