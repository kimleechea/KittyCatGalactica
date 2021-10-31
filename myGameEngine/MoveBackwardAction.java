package myGameEngine;
import GameServer.*;
import KittyCatGalactica.*;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

public class MoveBackwardAction extends AbstractInputAction{ 
	private Camera camera;
	private SceneNode dolphinN;
	private MyGame myGame;
	private ProtocolClient protClient;
	
	public MoveBackwardAction(Camera c, SceneNode d, MyGame g, ProtocolClient p)
	{
		camera = c;
		dolphinN = d;
		myGame = g;
		protClient = p;
	} 
	
	public void performAction(float time, Event e)
	{   
		char m = camera.getMode();
		if (m == 'c'){
			Vector3f v = camera.getFd();
			Vector3f p = camera.getPo();
			Vector3f p1 = (Vector3f) Vector3f.createFrom(0.01f*v.x(), 0.01f*v.y(), 0.01f*v.z());
			Vector3f p2 = (Vector3f) p.sub((Vector3)p1);
			camera.setPo((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
		}else if (m == 'r'){
			Vector3 v = dolphinN.getLocalForwardAxis();
			Vector3 p = dolphinN.getLocalPosition();
			Vector3 p1 = (Vector3f) Vector3f.createFrom(0.1f*v.x(), 0.1f*v.y(), 0.1f*v.z());
			Vector3 p2 = (Vector3f) p.sub((Vector3)p1);
			Angle rotationAngleAmt = Degreef.createFrom(0.0f);
			dolphinN.setLocalPosition((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			protClient.sendMoveMessage(dolphinN.getWorldPosition(), rotationAngleAmt);
			myGame.updateVerticalPosition();
		}
	} 
} 