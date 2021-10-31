package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rage.game.*;
import ray.rml.*;
import KittyCatGalactica.*;
import net.java.games.input.Event;

public class JumpAction extends AbstractInputAction{ 
	private SceneNode dolphinN;
	private SceneManager sm;
	private Tessellation tess;
	private JumpController cont;
	private float sinkRate = 0.000003f;// growth per second
	private float cycleTime = 1000.0f;// default cycle time
	private float totalTime;
	private float direction = 1.0f;
	
	public JumpAction(SceneNode d, Tessellation t, JumpController c, SceneManager m)
	{
		dolphinN = d;
		tess = t;
		cont = c;
		sm = m;
	} 
	
	public void performAction(float time, Event e)
	{   
		float impulse = 0.0f;
		float jumpHeight = 1.0f;
		Vector3 n = dolphinN.getLocalUpAxis();
		Vector3 p = dolphinN.getLocalPosition();
		final Vector3 worldPosition = dolphinN.getWorldPosition();
		final Vector3 localPosition = dolphinN.getLocalPosition();
		/*while(impulse < maxVerticalLeap){
			impulse += 0.1f;
			final Vector3 newLocalPosition = Vector3f.createFrom(
					// Keep the X coordinate
					localPosition.x(),
					// The Y coordinate is the varying height
					tess.getWorldHeight(worldPosition.x(), worldPosition.z())+impulse,
					// Keep the Z coordinate
					localPosition.z()
				);
		dolphinN.setLocalPosition(newLocalPosition);
		}*/

		float transAmt = tess.getWorldHeight(worldPosition.x(), worldPosition.z())+0.27f;
		//dolphinN.translate(localPosition.x(), transAmt+jumpHeight, localPosition.z());

		cont.addNode(dolphinN);
		sm.addController(cont);
	} 
} 