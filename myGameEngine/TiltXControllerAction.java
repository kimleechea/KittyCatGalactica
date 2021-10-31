package myGameEngine;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import KittyCatGalactica.*;
import net.java.games.input.Event;

public class TiltXControllerAction extends AbstractInputAction{
	
    private Camera camera;
	private SceneNode dolphinN;
	
    public TiltXControllerAction(Camera c, SceneNode d){
		dolphinN = d;
        camera = c;
    }
	
    public void performAction(float time, net.java.games.input.Event evt){
        Angle rotAngleAmt;
        float rotAmount;
        Vector3f u = camera.getFd();
        Vector3f n = camera.getRt();
        Vector3f v = camera.getUp();
		char m = camera.getMode();
		Angle rotationAngleAmt = Degreef.createFrom(1.0f);
		Angle negRotationAngleAmt = Degreef.createFrom(0-1.0f);
		if (m == 'c'){
			if (evt.getValue() < -0.2){
				rotAmount = evt.getValue() ;
				rotAngleAmt = Degreef.createFrom(0.0f - rotAmount);
				Vector3 newU = (u.rotate(rotAngleAmt, v)).normalize();
				Vector3 newN = (n.rotate(rotAngleAmt, v)).normalize();
				camera.setRt((Vector3f)newN);
				camera.setFd((Vector3f)newU);
			}
			else if (evt.getValue() > 0.2){
				rotAmount = evt.getValue();
				rotAngleAmt = Degreef.createFrom(0.0f - rotAmount);
				Vector3 newU = (u.rotate(rotAngleAmt, v)).normalize();
				Vector3 newN = (n.rotate(rotAngleAmt, v)).normalize();
				camera.setRt((Vector3f)newN);
				camera.setFd((Vector3f)newU);
			}
		} else if (m == 'r'){
			if (evt.getValue() < -0.2){
				dolphinN.yaw(rotationAngleAmt);
			}else if (evt.getValue() > 0.2){
				dolphinN.yaw(negRotationAngleAmt);
			}
		}
	}
}
