package myGameEngine;
import GameServer.*;
import KittyCatGalactica.*;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

public class TiltLeftAction extends AbstractInputAction{ 
	private Camera camera;
	private SceneNode dolphinN;
	private Camera3PController cont;
	private ProtocolClient protClient;
	
	public TiltLeftAction(Camera c, SceneNode d, Camera3PController ct, ProtocolClient p)
	{
	camera = c;
	dolphinN = d;
	cont = ct;
	protClient = p;
	} 
	
	public void performAction(float time, Event e)
	{   
		char m = camera.getMode();
		Angle rotationAngleAmt = Degreef.createFrom(1.0f);
		
		Vector3f u = camera.getFd();
		Vector3f n = camera.getRt();
		Vector3f v = camera.getUp();
		if (m == 'c'){
			Vector3 newU = (u.rotate(rotationAngleAmt, v)).normalize();
			Vector3 newN = (n.rotate(rotationAngleAmt, v)).normalize();
			camera.setRt((Vector3f)newN);
			camera.setFd((Vector3f)newU);
		} else if (m == 'r'){
			dolphinN.yaw(rotationAngleAmt);
			protClient.sendMoveMessage(dolphinN.getWorldPosition(), rotationAngleAmt);
		}
		
	} 
} 