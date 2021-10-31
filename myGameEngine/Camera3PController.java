package myGameEngine;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.util.ArrayList;

import KittyCatGalactica.*;

import java.lang.Object;

import ray.rage.*;
import ray.rage.game.*;

import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;

import ray.rml.*;

import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;

import ray.input.*;
import ray.input.action.*;
import ray.input.action.Action;
import ray.input.action.AbstractInputAction;
import ray.input.GenericInputManager;
import ray.input.InputManager;
import ray.input.InputManager.INPUT_ACTION_TYPE;

import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Controller;
import net.java.games.input.Event;

public class Camera3PController{
	private Camera camera;
	//the camera being controlled
	private SceneNode cameraN;
	//the node the camera is attached to
	private SceneNode target;
	//the target the camera looks at
	private float cameraAzimuth;
	//rotation of camera around Y axis
	private float cameraElevation;
	//elevation of camera above target
	private float radias;
	//distance between camera and target
	private Vector3 targetPos;
	//target’s position in the world
	private Vector3 worldUpVec;
	
	private InputManager im; 
	
	private static final float MAX_ZOOM_OUT = 100.0f;
	private static final float MAX_ZOOM_IN = 50.0f;
	private static final float ORBIT_SPEED = 0.75f;
	
	public Camera3PController(Camera cam, SceneNode camN,SceneNode targ, String controllerName, InputManager im)
	{ 
		super();
		camera = cam;
		cameraN = camN;
		target = targ;
		cameraAzimuth = 180.0f;
		// start from BEHIND and ABOVE the target
		cameraElevation = 20.0f;
		// elevation is in degrees
		radias = 2.0f;
		worldUpVec = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
		
		setupInput(im, controllerName);
		updateCameraPosition();
		
	} 
	
	// Updates camera position: computes azimuth, elevation, and distance
    // relative to the target in spherical coordinates, then converts those
	// to world Cartesian coordinates and setting the camera position
	public void updateCameraPosition()
	{ 
		double theta = Math.toRadians(cameraAzimuth);
		// rot around target
		double phi = Math.toRadians(cameraElevation);
		// altitude angle
		double x = radias * Math.cos(phi) * Math.sin(theta);
		double y = radias * Math.sin(phi);
		double z = radias * Math.cos(phi) * Math.cos(theta);
		cameraN.setLocalPosition(Vector3f.createFrom ((float)x, (float)y, (float)z).add(target.getWorldPosition()));
		cameraN.lookAt(target, worldUpVec);
	}   
	
	public void setupInput(InputManager im, String cn){	
		
		if(cn.contains("mouse"))
        {
            Action orbitAAction = new OrbitAroundAction();
            im.associateAction(cn,
            net.java.games.input.Component.Identifier.Axis.X, orbitAAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

            Action orbitElevationAction = new OrbitElevationAction();
            im.associateAction(cn,
            net.java.games.input.Component.Identifier.Axis.Y, orbitElevationAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        }
        else if(cn != null)
        {
            Action orbitAAction = new OrbitAroundAction();
			im.associateAction(cn, net.java.games.input.Component.Identifier.Axis.RX, orbitAAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
    
            Action orbitElevationAction = new OrbitElevationAction();im.associateAction(cn,net.java.games.input.Component.Identifier.Axis.RY, orbitElevationAction,
            InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        }

        Action orbitRadiansAction = new OrbitRadiansAction();im.associateAction(cn,net.java.games.input.Component.Identifier.Axis.Z, orbitRadiansAction,
        InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}
	
	private class OrbitAroundAction extends AbstractInputAction {
		@Override
		public void performAction(float time, Event evt) {
			float rotAmount;
			if (evt.getValue() < -0.2f) {
				rotAmount = -1.0f * ORBIT_SPEED;
			} else if (evt.getValue() > 0.2f) {
				rotAmount = ORBIT_SPEED;
			} else {
				rotAmount = 0.0f;
			}
			cameraAzimuth += rotAmount;
			cameraAzimuth = cameraAzimuth % 360;
			updateCameraPosition();
		}
	}
	
	private class OrbitRadiansAction extends AbstractInputAction {
		@Override
		public void performAction(float time, Event evt) {
			float zoomAmount;
			if (evt.getValue() > 0.2f){
				if (evt.getComponent().getName().toLowerCase().contentEquals("z axis")) {

					zoomAmount = 0.01f;
				} else {
					zoomAmount = -1.0f * ORBIT_SPEED;
				}
			} else if (evt.getValue() < -0.2f){
				if (evt.getComponent().getName().toLowerCase().contentEquals("z axis")) {

					zoomAmount = -1.0f * 0.01f;
				} else {
					zoomAmount = ORBIT_SPEED;
				}
			} else {
				zoomAmount = 0.0f;
			}
			
			final float chk = radias + zoomAmount;
			//if ((chk < MAX_ZOOM_IN) || (chk > MAX_ZOOM_OUT)) return;
			if(chk >= 0){
				radias = chk;
				radias = radias % 360;
				updateCameraPosition();
			}
		}
	}
	
	private class OrbitElevationAction extends AbstractInputAction {
		@Override
		public void performAction(float time, Event evt) {
			float elevateAmount;
			if (evt.getValue() > 0.2f) {
				elevateAmount = -1.0f * ORBIT_SPEED;
			} else if (evt.getValue() < -0.2f) {
				elevateAmount = ORBIT_SPEED;
			} else {
				elevateAmount = 0.0f;
			}
			
			final float chk = cameraElevation + elevateAmount;
			if ((chk < 0.0f) || (chk > 75.0f)) return;
			
			cameraElevation = chk;
			cameraElevation = cameraElevation % 360;
			updateCameraPosition();
		}
	}
	
	public float getAzimuth(){
		return cameraAzimuth;
	}
	
	public Node getCameraNode(){
		return cameraN;
	}
	
	public Camera getCamera(){
		return camera;
	}
	
} 

	