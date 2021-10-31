package myGameEngine;
import ray.rage.*;
import ray.rage.game.*;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import KittyCatGalactica.*;
import net.java.games.input.Event;

 public class MoveXControllerAction extends AbstractInputAction {
	private Camera camera2;
	private SceneNode dolphin2N;

	public MoveXControllerAction(Camera c, SceneNode d){
		dolphin2N = d;
		camera2 = c;
	}

	public void performAction(float time, net.java.games.input.Event evt){
        //Vector3f v = camera2.getRt();
        //Vector3f p = camera2.getPo();
		
		/*char m = camera2.getMode();
		if (m == 'c'){
			if (evt.getValue() > 0.02){
				Vector3f p1 = (Vector3f) Vector3f.createFrom(0.01f*v.x(), 0.01f*v.y(), 0.01f*v.z());
				Vector3f p2 = (Vector3f) p.add((Vector3) p1);
				camera2.setPo((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			}
			else if(evt.getValue() < -0.02){
				Vector3f p1 = (Vector3f) Vector3f.createFrom(0.01f*v.x(), 0.01f*v.y(), 0.01f*v.z());
				Vector3f p2 = (Vector3f) p.sub((Vector3) p1);
				camera2.setPo((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			}
		} else if (m == 'r'){
			if (evt.getValue() > 0.02){
				Vector3 dv = dolphin2N.getLocalRightAxis();
				Vector3 dp = dolphin2N.getLocalPosition();
				Vector3 p1 = (Vector3f) Vector3f.createFrom(0.1f*dv.x(), 0.1f*dv.y(), 0.1f*dv.z());
				Vector3 p2 = (Vector3f) dp.sub((Vector3)p1);
				dolphin2N.setLocalPosition((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			}
			else if(evt.getValue() < -0.02){
				Vector3 dv = dolphin2N.getLocalRightAxis();
				Vector3 dp = dolphin2N.getLocalPosition();
				Vector3 p1 = (Vector3f) Vector3f.createFrom(0.1f*dv.x(), 0.1f*dv.y(), 0.1f*dv.z());
				Vector3 p2 = (Vector3f) dp.add((Vector3)p1);
				dolphin2N.setLocalPosition((Vector3f)Vector3f.createFrom(p2.x(),p2.y(),p2.z()));
			}
		} */
		
		Angle negAng = Degreef.createFrom(0-1.0f);
		Angle posAng = Degreef.createFrom(1.0f);
		Angle rotAngleAmt;
        float rotAmount;
		
		if (evt.getValue() > 0.02){
			rotAmount = evt.getValue();
            rotAngleAmt = Degreef.createFrom(0.0f - rotAmount);
			//dolphin2N.yaw(negAng);
			dolphin2N.yaw(rotAngleAmt);

		}else if(evt.getValue() < -0.02){
			rotAmount = evt.getValue();
            rotAngleAmt = Degreef.createFrom(0.0f - rotAmount);
			//dolphin2N.yaw(posAng);
			dolphin2N.yaw(rotAngleAmt);

		}
	}
}
