package myGameEngine;

import GameServer.*;
import KittyCatGalactica.*;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;

public class MoveForwardAction extends AbstractInputAction {
	private Camera camera;
	private SceneNode dolphinN;
	private MyGame myGame;
	private ProtocolClient protClient;

	public MoveForwardAction(Camera c, SceneNode d, MyGame g, ProtocolClient p) {
		camera = c;
		dolphinN = d;
		myGame = g;
		protClient = p;

	}

	public void performAction(float time, Event e) {
		char m = camera.getMode();
		if (m == 'c') {
			Vector3f v = camera.getFd();
			Vector3f p = camera.getPo();
			Vector3f p1 = (Vector3f) Vector3f.createFrom(0.01f * v.x(), 0.01f * v.y(), 0.01f * v.z());
			Vector3f p2 = (Vector3f) p.add((Vector3) p1);
			camera.setPo((Vector3f) Vector3f.createFrom(p2.x(), p2.y(), p2.z()));
		} else if (m == 'r') {
			Vector3 v = dolphinN.getLocalForwardAxis();
			Vector3 p = dolphinN.getLocalPosition();
			Vector3 p1 = (Vector3f) Vector3f.createFrom(myGame.PLAYER_SPEED * v.x(), myGame.PLAYER_SPEED * v.y(),
					myGame.PLAYER_SPEED * v.z());
			Vector3 p2 = (Vector3f) p.add((Vector3) p1);
			dolphinN.setLocalPosition((Vector3f) Vector3f.createFrom(p2.x(), p2.y(), p2.z()));
			// dolphinN.moveForward(.000000000000000001f);
			Angle rotationAngleAmt = Degreef.createFrom(0.0f);
			protClient.sendMoveMessage(dolphinN.getWorldPosition(), rotationAngleAmt);
			myGame.updateVerticalPosition();
		}
	}
}