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

public class MusicUpAction extends AbstractInputAction{ 
    private MyGame myGame;
    private SceneNode dolphinN;
    private IAudioManager audioMgr; 
    private ArrayList<Sound> radio = new ArrayList<Sound>();
    private int count;
    private Sound sound;
	
	public MusicUpAction(MyGame g, SceneNode d, IAudioManager a, ArrayList<Sound> s, int c)
	{
        myGame = g;
        dolphinN = d;
        audioMgr = a;
        radio = s;
        count = c;
	} 
	
	public void performAction(float time, Event e)
	{   
        count = myGame.getRadioCount();
        sound = radio.get(count);
        int v = radio.size();
        if(sound.getIsPlaying() == true){
            sound.stop();
        }
        if (count >= 0 && count < v-1){
            count++;
            sound = radio.get(count);
            sound.setMaxDistance(10.0f);
            sound.setMinDistance(0.1f);
            sound.setRollOff(5.0f);
            sound.setLocation(dolphinN.getWorldPosition());
            sound.play();
            myGame.incrementRadioCount();
        }else if(count == v-1){
            sound = radio.get(count);
            sound.setMaxDistance(10.0f);
            sound.setMinDistance(0.1f);
            sound.setRollOff(5.0f);
            sound.setLocation(dolphinN.getWorldPosition());
            sound.play();
        }
	} 
} 