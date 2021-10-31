package GameServer;

import java.util.UUID;

import KittyCatGalactica.*;
import myGameEngine.*;
import java.util.*;
import ray.rage.scene.Entity;
import ray.rage.scene.SceneNode;
import ray.rage.scene.SkeletalEntity;
import ray.rml.Matrix3;
import ray.rml.Matrix3f;
import ray.rml.Vector3;
import ray.rml.Vector3f;

public class GameItem {

  private int id;
  private SceneNode node;
  private Entity entity;
  private Vector3 position;
  private String skin;
  private Random rand = new Random();

  public GameItem(int id) { // constructor
    this.id = id;
    this.position = Vector3f.createFrom((float) (rand.nextInt(80 + 80) - 80), -1f,
        (float) (rand.nextInt(25 + 25) - 25));
    int temp = rand.nextInt(3) + 1;
    if (temp == 1) {
      skin = "crystal.obj";
    } else if (temp == 2) {
      skin = "gem.obj";
    } else {
      skin = "battery.obj";
    }
  }

  public void randomize() {
    this.position = Vector3f.createFrom((float) (rand.nextInt(80 + 80) - 80), -1f,
        (float) (rand.nextInt(25 + 25) - 25));
    int temp = rand.nextInt(3) + 1;
    if (temp == 1) {
      skin = "crystal.obj";
    } else if (temp == 2) {
      skin = "gem.obj";
    } else if (temp == 3) {
      skin = "battery.obj";
    }
  }

  public void setPosition(Vector3 position) {
    this.position = position;
  }

  public void setSkin(String skin) {
    this.skin = skin;
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

  public String getSkin() {
    return this.skin;
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

}
