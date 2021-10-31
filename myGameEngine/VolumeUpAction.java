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

public class VolumeUpAction extends AbstractInputAction{ 
	private ArrayList<Sound> music;
	private MyGame myGame;
	
	public VolumeUpAction(ArrayList<Sound> s, MyGame g)
	{
        music = s;
		myGame = g;
	} 
	
	public void performAction(float time, Event e)
	{   
		for(int i=0; i < music.size(); i++){
			int v = music.get(i).getVolume();
			if (v >= 0 && v < 10){
				v++;
				music.get(i).setVolume(v);
			}
		}
	} 
} 