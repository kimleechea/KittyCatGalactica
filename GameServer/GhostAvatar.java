package GameServer;

import java.util.UUID;

import KittyCatGalactica.*;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rml.Matrix3;
import ray.rml.Matrix3f;
import ray.rml.Vector3;
import ray.rml.Vector3f;
import ray.rml.*;

public class GhostAvatar {

  private UUID id;
  private SceneNode node;
  private Entity entity;
  private Vector3 position;
  private Angle angle;
  private String skin;

  public GhostAvatar(UUID id, Vector3 position, String skin) {
    this.id = id;
    this.position = position;
    this.skin = skin;
  }

  public UUID getID() {
    return this.id;
  }

  public String getSkin() {
    return this.skin;
  }

  public Angle getAngle() {
    return this.angle;
  }

  public Vector3 getPosition() {
    return this.position;
  }

  public SceneNode getSceneNode() {
    return this.node;
  }

  public Entity getEntity() {
    return this.entity;
  }

  public void setUUID(UUID newID) {
    this.id = newID;
  }

  public void setPosition(Vector3 ghostN) {
    node.setLocalPosition(ghostN);
  }

  public void setNode(SceneNode ghostN) {
    node = ghostN;
  }

  public void setEntity(Entity ghostE) {
    entity = ghostE;
  }

  public void setAngle(Angle angleE) {
    this.angle = angleE;
  }

  public void setSkin(String skin) {
    this.skin = skin;
  }
}
