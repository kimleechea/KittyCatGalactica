package myGameEngine;
import GameServer.*;
import KittyCatGalactica.*;

import java.util.Random;

import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;

public class SpeedBoostController extends AbstractController { 
	private float cycleTime = 1000.0f;// default cycle time
	private float totalTime;
    private float direction = 1.0f;
    private MyGame myGame;
	private ProtocolClient protClient;
	
	@Override
	protected void updateImpl(float elapsedTimeMillis){
		
		totalTime += elapsedTimeMillis;
		
		if (totalTime > cycleTime){
			//direction = -direction;
			direction = 0.0f;
			totalTime = 0.0f;
		} 
		
		//float transAmt = sinkRate * elapsedTimeMillis * direction;
		//transAmt.translate(0, transAmt, 0);
		for (Node n : super.controlledNodesList){
			Vector3 v = n.getLocalForwardAxis();
			Vector3 p = n.getLocalPosition();
			Vector3 p1 = (Vector3f) Vector3f.createFrom(0.4f*v.x(), 0.4f*v.y(), 0.4f*v.z());
			Vector3 p2 = (Vector3f) p.add((Vector3)p1);
			n.setLocalPosition((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			//n.moveForward(.000000000000000001f);
			Angle rotationAngleAmt = Degreef.createFrom(0.0f);
			protClient.sendMoveMessage(n.getWorldPosition(), rotationAngleAmt);
			//System.out.println("Successfully moved forward");
            myGame.updateVerticalPosition();
		}
	}
} 