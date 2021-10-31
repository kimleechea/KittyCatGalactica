package myGameEngine;
import GameServer.*;
import KittyCatGalactica.*;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

public class SpeedBoostAction extends AbstractInputAction{ 
	private Camera camera;
	private SceneNode dolphinN;
	private MyGame myGame;
    private ProtocolClient protClient;
    private SpeedBoostController cont;
    private float elapsTime;
    private float boostTime;
    private float speed;
    private boolean hasBattery;
	
	public SpeedBoostAction(Camera c, SceneNode d, MyGame g, ProtocolClient p, SpeedBoostController sbc, float t, float sp, boolean hb)
	{
		camera = c;
		dolphinN = d;
		myGame = g;
        protClient = p;
        cont = sbc;
        elapsTime = t;
        speed = sp;
        hasBattery = hb;
	} 
	
	public void performAction(float time, Event e)
	{   
        //boostTime = elapsTime + 50f;
       // while(boostTime > elapsTime && hasBattery = true){
            /*Vector3 v = dolphinN.getLocalForwardAxis();
			Vector3 p = dolphinN.getLocalPosition();
			Vector3 p1 = (Vector3f) Vector3f.createFrom(speed*v.x(), speed*v.y(), speed*v.z());
			Vector3 p2 = (Vector3f) p.add((Vector3)p1);
			dolphinN.setLocalPosition((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			//dolphinN.moveForward(.000000000000000001f);
			Angle rotationAngleAmt = Degreef.createFrom(0.0f);
			protClient.sendMoveMessage(dolphinN.getWorldPosition(), rotationAngleAmt);
			//System.out.println("Successfully moved forward");
            myGame.updateVerticalPosition();*/
            
            //cont.addNode(dolphinN);
		    //sm.addController(cont);
       // }
	} 
} 