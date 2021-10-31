package GameServer;

import myGameEngine.*;
import java.util.*;

import java.io.IOException;
import java.lang.*;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.UUID;

import KittyCatGalactica.*;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.ai.behaviortrees.*;

public class NPCcontroller {

  private NPC[] NPClist;
  GameServerUDP server;
  BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
  long thinkStartTime;
  long tickStartTime;
  long lastThinkUpdateTime;
  long lastTickUpdateTime;

  public NPCcontroller() {
    NPClist = new NPC[5];
  }

  public void updateNPCs() {
    for (int i = 0; i < NPClist.length; i++) {
      NPClist[i].updateLocation();
    }
  }

  public void start() {
    thinkStartTime = System.nanoTime();
    tickStartTime = System.nanoTime();
    lastThinkUpdateTime = thinkStartTime;
    lastTickUpdateTime = tickStartTime;
    setupNPCS();
    setupBehaviorTree();
    npcLoop();
  }

  public void npcLoop() {
    while (true) {
      long currentTime = System.nanoTime();
      float elapsedThinkMilliSecs = (currentTime - lastThinkUpdateTime) / (1000000.0f);
      float elapsedTickMilliSecs = (currentTime - lastTickUpdateTime) / (1000000.0f);
      if (elapsedTickMilliSecs >= 50.0f) {
        lastTickUpdateTime = currentTime;
        updateNPCs();
        server.sendNPCinfo();
      }
      if (elapsedThinkMilliSecs >= 500.0f) {
        lastThinkUpdateTime = currentTime;
        bt.update(elapsedThinkMilliSecs);
      }
      Thread.yield();
    }
  }

  public void setupBehaviorTree() {
    for (int i = 0; i < NPClist.length; i++) {
      bt.insertAtRoot(new BTSequence(10 * (i + 1)));
      // bt.insertAtRoot(new BTSequence(20));
      bt.insert(10 * (i + 1), new OneSecPassed(this, getNPC(i), false));
      if (i % 2 == 0)
        bt.insert(10 * (i + 1), new GetSmall(getNPC(i)));
      else
        bt.insert(10 * (i + 1), new GetBig(getNPC(i)));

      bt.insertAtRoot(new BTSequence(100 * (i + 1)));
      bt.insert(100 * (i + 1), new MultSecPassed(this, getNPC(i), false));
      if (i % 2 == 0)
        bt.insert(100 * (i + 1), new GetBig(getNPC(i)));
      else
        bt.insert(100 * (i + 1), new GetSmall(getNPC(i)));
      // bt.insert(20, new AvatarNear(server, this, npc, false));
      // bt.insert(20, new GetBig(npc));
    }
  }

  public int getNumOfNPCS() {
    return NPClist.length;
  }

  public NPC getNPC(int i) {
    return NPClist[i];
  }

  public void setupNPCS() {
    for (int i = 0; i < NPClist.length; i++) {
      NPClist[i] = new NPC();
      // NPClist[i].randomizeLocation();
    }
  }

  public void setServer(GameServerUDP s) {
    server = s;
  }
}
