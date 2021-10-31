package GameServer;

import java.io.IOException;
import java.lang.*;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.Vector;

import KittyCatGalactica.*;
import ray.networking.server.GameConnectionServer;
import ray.networking.server.IClientInfo;
import ray.rml.*;

public class GameServerUDP extends GameConnectionServer<UUID> {

  int locolP;
  public int players;
  GameItem[] gameItems;
  NPCcontroller npcCtrl;

  public GameServerUDP(int localPort, NPCcontroller np) throws IOException {
    super(localPort, ProtocolType.UDP);
    print(InetAddress.getLocalHost().getHostAddress());
    locolP = localPort;
    players = 0;
    npcCtrl = np;
    gameItems = new GameItem[7];
    processGameItems();
  }

  @Override
  public void processPacket(Object o, InetAddress senderIP, int senderPort) {
    String message = (String) o.toString();
    String[] messageTokens = message.split(",");
    if (messageTokens.length > 0) {
      // case where server receives a JOIN message
      // format: join,localid
      if (messageTokens[0].compareTo("join") == 0) {
        try {
          System.out.println("Player successfully joined the game.");
          IClientInfo ci;
          ci = getServerSocket().createClientInfo(senderIP, senderPort);
          UUID clientID = (UUID) UUID.fromString(messageTokens[1]);
          addClient(ci, clientID);
          sendItemInfo();
          sendJoinedMessage(clientID, true);
          players++;
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      // case where server receives a CREATE message
      // format: create,localid,x,y,z
      if (messageTokens[0].compareTo("create") == 0) {
        UUID clientID = (UUID) UUID.fromString(messageTokens[1]);
        String[] pos = { messageTokens[2], messageTokens[3], messageTokens[4], messageTokens[5] };
        sendCreateMessages(clientID, pos);
        sendItemInfo();
        sendWantsDetailsMessages(clientID);
      }
      // case where server receives a BYE message
      // format: bye,localid
      if (messageTokens[0].compareTo("bye") == 0) {
        UUID clientID = UUID.fromString(messageTokens[1]);
        sendByeMessages(clientID);
        removeClient(clientID);
        sendItemInfo();
      }

      if (messageTokens[0].compareTo("time") == 0) {
        UUID clientID = UUID.fromString(messageTokens[1]);
        sendFinishTime(clientID, messageTokens[2]);
      }

      // case where server receives a DETAILS-FOR message
      if (messageTokens[0].compareTo("dsfr") == 0) {
        // 1. Look up the sender name
        UUID clientID = UUID.fromString(messageTokens[1]);
        UUID remoteID = UUID.fromString(messageTokens[2]);
        // UUID remoteID = UUID.fromString(messageTokens[2]);
        String[] pos = { messageTokens[3], messageTokens[4], messageTokens[5] };
        String skin = (String) messageTokens[6];
        sndDetailsMsg(clientID, remoteID, pos, skin);
      }

      // case where server receives a MOVE message
      // format: move, localid, x, y, z
      if (messageTokens[0].compareTo("move") == 0) {
        UUID clientID = UUID.fromString(messageTokens[1]);
        String[] pos = { messageTokens[2], messageTokens[3], messageTokens[4], messageTokens[5] };
        sendMoveMessages(clientID, pos);
      }

      if (messageTokens[0].compareTo("needNPC") == 0) {
        UUID clientID = UUID.fromString(messageTokens[1]);
        sendNPCinfo();
      }

      if (messageTokens[0].compareTo("needItems") == 0) {
        UUID clientID = UUID.fromString(messageTokens[1]);
        sendNPCinfo();
      }

      if (messageTokens[0].compareTo("moveItem") == 0) {
        int pos = Integer.parseInt(messageTokens[1]);
        gameItems[pos].randomize();
        sendSingleItemInfo(pos);
      }

      if (messageTokens[0].compareTo("inc") == 0) {
        UUID clientID = UUID.fromString(messageTokens[1]);
        int curPlayerScore = Integer.parseInt(messageTokens[2]);
        if (curPlayerScore >= 500) {
          sendWonMessage(clientID);
          sendLostMessage(clientID);
        }
      }
    }
  }

  private void sendWonMessage(UUID clientID) {
    String message = new String("Winner");
    try {
      sendPacket(message, clientID);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void sendLostMessage(UUID clientID) {
    String message = new String("Loser");
    try {
      forwardPacketToAll(message, clientID);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void sendFinishTime(UUID clientID, String string) {
    String message = new String("finished," + string);

    try {
      forwardPacketToAll(message, clientID);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // format: join, success or join, failure
  public void sendJoinedMessage(UUID clientID, boolean success) {
    try {
      String message = new String("join,");
      if (success)
        message += "success";
      else
        message += "failure";
      sendPacket(message, clientID);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // format: create, remoteId, x, y, z
  public void sendCreateMessages(UUID clientID, String[] position) {
    try {
      // System.out.println("Making Create Message");
      String message = new String("create," + clientID.toString());
      message += "," + position[0];
      message += "," + position[1];
      message += "," + position[2];
      message += "," + position[3];
      forwardPacketToAll(message, clientID);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // format: dsfr, remoteID, x, y, z
  public void sndDetailsMsg(UUID clientID, UUID remoteID, String[] position, String skin) {
    try {
      String message = new String("dsfr," + remoteID.toString());
      message += "," + position[0];
      message += "," + position[1];
      message += "," + position[2];
      message += "," + skin;
      sendPacket(message, clientID);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // format: wsds, clientID
  public void sendWantsDetailsMessages(UUID clientID) {
    try {
      String message = new String("wsds," + clientID.toString());
      forwardPacketToAll(message, clientID);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // format: move, remoteID, x, y, z
  public void sendMoveMessages(UUID clientID, String[] position) {
    try {
      String message = new String("move," + clientID.toString());
      message += "," + position[0];
      message += "," + position[1];
      message += "," + position[2];
      message += "," + position[3];
      forwardPacketToAll(message, clientID);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // format: bye, remoteID
  public void sendByeMessages(UUID clientID) {
    try {
      String message = new String("bye," + clientID.toString());
      forwardPacketToAll(message, clientID);
    } catch (IOException e) {
      e.printStackTrace();
    }
  } // etc…..

  public void startMsg(int t) {
    try {
      String message = new String("time," + 5 + "," + t);
      sendPacketToAll(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendNPCinfo() {
    for (int i = 0; i < npcCtrl.getNumOfNPCS(); i++) {
      try {
        String message = new String("mnpc," + Integer.toString(i));
        message += "," + (npcCtrl.getNPC(i)).getX();
        message += "," + (npcCtrl.getNPC(i)).getY();
        message += "," + (npcCtrl.getNPC(i)).getZ();
        message += "," + (npcCtrl.getNPC(i)).getSizeX();
        message += "," + (npcCtrl.getNPC(i)).getSizeY();
        message += "," + (npcCtrl.getNPC(i)).getSizeZ();
        if (players != 0) {
          // System.out.println("Successfully sent the packet");
          sendPacketToAll(message);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void sendItemInfo() {
    for (int i = 20; i < 27; i++) {
      try {
        String message = new String("mitem," + Integer.toString(i));
        message += "," + gameItems[i - 20].getPosition().x();
        message += "," + gameItems[i - 20].getPosition().y();
        message += "," + gameItems[i - 20].getPosition().z();
        message += "," + gameItems[i - 20].getSkin();
        if (players != 0) {
          sendPacketToAll(message);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void sendSingleItemInfo(int id) {
    try {
      String message = new String("mitem," + Integer.toString(id + 20));
      message += "," + gameItems[id].getPosition().x();
      message += "," + gameItems[id].getPosition().y();
      message += "," + gameItems[id].getPosition().z();
      message += "," + gameItems[id].getSkin();
      if (players != 0) {
        sendPacketToAll(message);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void processGameItems() {
    for (int i = 20; i < 27; i++) {
      gameItems[i - 20] = new GameItem(i);
    }
  }

  public void print(Object o) {
    System.out.println(o);
  }
}
