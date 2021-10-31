package myGameEngine;
import GameServer.*;
import KittyCatGalactica.*;
import net.java.games.input.Event;
import ray.input.action.AbstractInputAction;
import ray.rage.game.*;
import ray.rage.scene.*;
import ray.rml.*;

public class TiltRightAction extends AbstractInputAction {

  private Camera camera;
  private SceneNode dolphinN;
  private ProtocolClient protClient;

  public TiltRightAction(Camera c, SceneNode d, ProtocolClient p) {
    camera = c;
    dolphinN = d;
    protClient = p;
  }

  public void performAction(float time, Event e) {
    char m = camera.getMode();
    Angle rotationAngleAmt = Degreef.createFrom(0 - 1.0f);
    if (m == 'c') {
      Vector3f u = camera.getFd();
      Vector3f n = camera.getRt();
      Vector3f v = camera.getUp();
      Vector3 newU = (u.rotate(rotationAngleAmt, v)).normalize();
      Vector3 newN = (n.rotate(rotationAngleAmt, v)).normalize();
      camera.setRt((Vector3f) newN);
      camera.setFd((Vector3f) newU);
    } else if (m == 'r') {
      dolphinN.yaw(rotationAngleAmt);
      protClient.sendMoveMessage(dolphinN.getWorldPosition(), rotationAngleAmt);
    }
  }
}
