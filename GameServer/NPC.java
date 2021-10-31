package GameServer;

import java.util.*;

import KittyCatGalactica.*;
import myGameEngine.*;

public class NPC {

  double locX, locY, locZ, sizX, sizY, sizZ;
  boolean turnBack = false;
  Random random = new Random();
  int numMoves = 0;

  public NPC() {
    // return random.nextInt(max - min) + min;
    // //Vector3f(14.874405, -1.6408281, 85.32519)
    // Vector3f(-14.558899, -1.73, -84.28119)
    int temp = random.nextInt(2);
    if (temp == 0) {
      locX = random.nextInt(20 - 10) + (double) 10;
      locY = -1;
      locZ = random.nextInt(80 - 40) + (double) 40;

      sizX = .05;
      sizY = .05;
      sizZ = .05;
    } else if (temp == 1) {
      locX = -random.nextInt(20 - 10) - (double) 10;
      locY = -1;
      locZ = -random.nextInt(80 - 40) - (double) 40;

      sizX = .05;
      sizY = .05;
      sizZ = .05;
    }

  }

  public double getX() {
    return locX;
  }

  public double getY() {
    return locY;
  }

  public double getZ() {
    return locZ;
  }

  public void setX(double x) {
    locX = x;
  }

  public void setY(double y) {
    locY = y;
  }

  public void setZ(double z) {
    locZ = z;
  }

  public double getSizeX() {
    return this.sizX;
  }

  public double getSizeY() {
    return this.sizY;
  }

  public double getSizeZ() {
    return this.sizZ;
  }

  public double getSize() {
    return this.sizX;
  }

  public void setSizeX(double x) {
    this.sizX = x;
  }

  public void setSizeY(double y) {
    this.sizY = y;
  }

  public void setSizeZ(double z) {
    this.sizZ = z;
  }

  public void updateLocation() {
    // Get NPC location x, y z
    turnCondition();
    // System.out.println("Num moves is: " + numMoves);
  }

  public void getSmall() {
    this.sizX = .025;
    this.sizY = .025;
    this.sizZ = .025;
  }

  public void getBig() {
    this.sizX = .3;
    this.sizY = .3;
    this.sizZ = .3;
  }

  // public void moveConditions(boolean bool, numMoves)
  public void turnCondition() {
    if (numMoves == 0) {
      setX(getX() + .01 + .01 * (random.nextInt(4) + 1));
      if (getX() >= 10 * 2) {
        numMoves++;
      }
    } else if (numMoves == 1) {
      setZ(getZ() - .01 + .01 * (random.nextInt(4) + 1));
      if (getZ() >= 40 * 2) {
        numMoves++;
      }
    } else if (numMoves == 2) {
      setX(getX() - .01 - .01 * (random.nextInt(4) + 1));
      if (getX() <= -10 * 2) {
        numMoves++;
      }
    } else if (numMoves == 3) {
      setZ(getZ() - .01 - .01 * (random.nextInt(4) + 1));
      if (getZ() <= -40 * 2) {
        numMoves = 0;
      }
    }
  }

  public void randomizeLocation() {
    setX(random.nextInt(30));
    setY(random.nextInt(30));
    setZ(1);
  }
}
