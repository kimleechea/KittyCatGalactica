package myGameEngine;
import java.util.Random;

import KittyCatGalactica.*;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;

public class SinkController extends AbstractController { 
	private float sinkRate = 0.000003f;// growth per second
	private float cycleTime = 1000.0f;// default cycle time
	private float totalTime;
	private float direction = 1.0f;
	
	@Override
	protected void updateImpl(float elapsedTimeMillis){
		
		totalTime += elapsedTimeMillis;
		
		if (totalTime > cycleTime){
			//direction = -direction;
			direction = 0.0f;
			totalTime = 0.0f;
		} 
		
		float transAmt = sinkRate * elapsedTimeMillis * direction;
		//transAmt.translate(0, transAmt, 0);
		for (Node n : super.controlledNodesList){
			/*Vector3 curT = n.getLocalPosition();
			curT = Vector3f.createFrom(curT.x(), curT.y()*transAmt, curT.z());
			n.setLocalPosition(curT);*/
			n.moveDown(transAmt);
		}
	}
} 