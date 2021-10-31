package GameServer;

import java.util.UUID;

import KittyCatGalactica.*;
import myGameEngine.*;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rml.Matrix3;
import ray.rml.Matrix3f;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class GhostNPC {

  private int id;
  private SceneNode node;
  private Entity entity;
  private Vector3 position;
  private Vector3 size;

  public GhostNPC(int id, Vector3 position, Vector3 size) { // constructor
    this.id = id;
    this.position = position;
    this.size = size;

  }

  public void setPosition(Vector3 position) {
    node.setLocalPosition(position);
  }

  public int getID() {
    return id;
  }

  public Vector3 getPosition(Vector3 position) {
    return node.getLocalPosition();
  }

  public Vector3 getPosition() {
    return this.position;
  }

  public Vector3 getSize() {
    return this.size;
  }

  public SceneNode getSceneNode() {
    return this.node;
  }

  public Entity getEntity() {
    return this.entity;
  }

  public void setNode(SceneNode ghostN) {
    node = ghostN;
  }

  public void setEntity(Entity ghostE) {
    entity = ghostE;
  }

  public void setSize(Vector3 size) {
    this.size = size;
  }
}
