package myGameEngine;
import ray.rage.*;
import ray.rage.game.*;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import KittyCatGalactica.*;
import net.java.games.input.Event;

 public class MoveYControllerAction extends AbstractInputAction {
	private Camera camera2;
	private SceneNode dolphin2N;
	private MyGame myGame;

	public MoveYControllerAction(Camera c, SceneNode d, MyGame g){
		dolphin2N = d;
		camera2 = c;
		myGame = g;
	}

	public void performAction(float time, net.java.games.input.Event evt){
        //Vector3f v = camera2.getFd();
        //Vector3f p = camera2.getPo();
		
		/*char m = camera2.getMode();
		if (m == 'c'){
			if (evt.getValue() < 0.02){
				Vector3f p1 = (Vector3f) Vector3f.createFrom(0.01f*v.x(), 0.01f*v.y(), 0.01f*v.z());
				Vector3f p2 = (Vector3f) p.add((Vector3)p1);
				camera2.setPo((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			}
			else if(evt.getValue() > -0.02){
				Vector3f p1 = (Vector3f) Vector3f.createFrom(0.01f*v.x(), 0.01f*v.y(), 0.01f*v.z());
				Vector3f p2 = (Vector3f) p.sub((Vector3)p1);
				camera2.setPo((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			}
		} else if (m == 'r'){*/
			/*if (evt.getValue() < 0.02){
				Vector3 dv = dolphin2N.getLocalForwardAxis();
				Vector3 dp = dolphin2N.getLocalPosition();
				Vector3 p1 = (Vector3f) Vector3f.createFrom(0.1f*dv.x(), 0.1f*dv.y(), 0.1f*dv.z());
				Vector3 p2 = (Vector3f) dp.add((Vector3)p1);
				dolphin2N.setLocalPosition((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			}
			else if(evt.getValue() > -0.02){
				Vector3 dv = dolphin2N.getLocalForwardAxis();
				Vector3 dp = dolphin2N.getLocalPosition();
				Vector3 p1 = (Vector3f) Vector3f.createFrom(0.1f*dv.x(), 0.1f*dv.y(), 0.1f*dv.z());
				Vector3 p2 = (Vector3f) dp.sub((Vector3)p1);
				dolphin2N.setLocalPosition((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			}*/
		//}
		
		if (evt.getValue() < 0.02){
				dolphin2N.moveForward(0.1f);
				myGame.updateVerticalPosition();
		}else if(evt.getValue() > -0.02){
				dolphin2N.moveBackward(0.1f);
				myGame.updateVerticalPosition();
		}
	}
}