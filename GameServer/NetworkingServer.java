package GameServer;

import java.io.IOException;

import KittyCatGalactica.*;
import ray.networking.IGameConnection.ProtocolType;

public class NetworkingServer {

  private NPCcontroller npcCtrl;
  private GameServerUDP thisUDPServer;
  // private GameServerTCP thisTCPServer;

  private long lastUpdateTime;
  private long lastUpdateTim;

  int count;

  public NetworkingServer(int serverPort, String protocol) throws IOException {
    // System.out.println("ServerPort: " + serverPort + "\nProtocol: " + protocol);
    long startTim = System.nanoTime();
    lastUpdateTim = startTim;
    long startTime = System.nanoTime();
    lastUpdateTime = startTime;
    npcCtrl = new NPCcontroller();

    try {
      thisUDPServer = new GameServerUDP(serverPort, npcCtrl);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // npcCtrl.setupNPCS();
    npcCtrl.setServer(thisUDPServer);
    npcCtrl.start();
    npcLoop();
  }

  public void npcLoop() { // NPC control loop
    while (true) {
      long frameStartTime = System.nanoTime();
      float elapMilSecs = (frameStartTime - lastUpdateTime) / (1000000.0f);
      if (elapMilSecs >= 50.0f) {
        lastUpdateTime = frameStartTime;
        npcCtrl.updateNPCs();
        thisUDPServer.sendNPCinfo();
      }
      Thread.yield();
    }
  }

  public void lobby() {
    while (count > -1) {
      long frameStartTim = System.nanoTime();
      float elapMilSec = (frameStartTim - lastUpdateTim) / (1000000.0f);
      if (elapMilSec >= 1000.0f) {
        lastUpdateTim = frameStartTim;
        thisUDPServer.startMsg(count);
        count--;
      }
      // Thread.yield();
    }

    System.out.println("lobby time ended");
  }

  public static void main(String[] args) throws NumberFormatException, IOException {
    if (args.length > 1) {
      NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
    }
  }
}
