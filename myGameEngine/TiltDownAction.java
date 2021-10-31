package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import KittyCatGalactica.*;
import net.java.games.input.Event;

public class TiltDownAction extends AbstractInputAction{ 
	private Camera camera;
	private SceneNode dolphinN;
	
	public TiltDownAction(Camera c, SceneNode d)
	{camera = c;
	dolphinN = d;
	} 
	
	public void performAction(float time, Event e)
	{   
		char m = camera.getMode();
		Angle rotationAngleAmt = Degreef.createFrom(0-1.0f);
		if (m == 'c'){
			Vector3f u = camera.getFd();
			Vector3f n = camera.getRt();
			Vector3f v = camera.getUp();
			Vector3 newU = (u.rotate(rotationAngleAmt, n)).normalize();
			Vector3 newV = (v.rotate(rotationAngleAmt, n)).normalize();
			camera.setFd((Vector3f)newU);
			camera.setUp((Vector3f)newV);
		} else if (m == 'r'){
			dolphinN.pitch(rotationAngleAmt);
		}
		
	} 
} 