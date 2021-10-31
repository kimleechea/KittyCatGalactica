package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import KittyCatGalactica.*;
import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;
import ray.input.GenericInputManager;

 
 public class RotDolphinCamLeftAction extends AbstractInputAction { 
	private Camera3PController cont;
 
	public RotDolphinCamLeftAction(Camera3PController c){
		cont = c;
	}
	
	// Moves the camera around the target (changes camera azimuth).
	public void performAction(float time, net.java.games.input.Event evt)
	{
		//float rotAmount;
		//float cameraAzimuth = cont.getAzimuth();
		//Node cameraN = cont.getCameraNode();
		Camera camera = cont.getCamera();
		char m = camera.getMode();
		Angle rotationAngleAmt = Degreef.createFrom(1.0f);
		if (m == 'r'){
			//cameraN.yaw(rotationAngleAmt);
			Vector3f u = camera.getFd();
			Vector3f n = camera.getRt();
			Vector3f v = camera.getUp();
			Vector3 newU = (u.rotate(rotationAngleAmt, v)).normalize();
			Vector3 newN = (n.rotate(rotationAngleAmt, v)).normalize();
			camera.setRt((Vector3f)newN);
			camera.setFd((Vector3f)newU);
		}
		
		//cameraAzimuth += rotAmount;
		//cameraAzimuth = cameraAzimuth % 360;
		System.out.println("action initiated " + evt.getValue());
		cont.updateCameraPosition();
		
	}
}