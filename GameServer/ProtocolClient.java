package GameServer;

import java.io.IOException;
import java.lang.*;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;
import javax.vecmath.Tuple3d;

import KittyCatGalactica.*;
import ray.networking.client.GameConnectionClient;
import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rml.Matrix3;
import ray.rml.Matrix3f;
import ray.rml.Vector3;
import ray.rml.Vector3f;
import ray.rml.*;

public class ProtocolClient extends GameConnectionClient {

  private MyGame game;
  private UUID id;
  private Vector<GhostAvatar> ghostAvatars;
  private Vector<GhostNPC> ghostNPCs;
  private Vector<GameItem> ghostItems;

  public ProtocolClient(InetAddress remAddr, int remPort, ProtocolType pType, MyGame game) throws IOException {
    super(remAddr, remPort, pType);
    this.game = game;
    this.id = UUID.randomUUID();
    this.ghostAvatars = new Vector<GhostAvatar>();
    this.ghostNPCs = new Vector<GhostNPC>();
    this.ghostItems = new Vector<GameItem>();

  }

  @Override
  protected void processPacket(Object msg) {
    String strMessage = (String) msg.toString();
    String[] messageTokens = strMessage.split(",");
    if (messageTokens.length > 0) {
      if (messageTokens[0].compareTo("join") == 0) {
        // receive join
        // format: join, success or join, failure
        if (messageTokens[1].compareTo("success") == 0) {
          game.setIsConnected(true);
          System.out.println("Sucessfully Connected To The Game Server.");
          sendCreateMessage(game.getPlayerPosition(), game.getPlayerSkin());
        }
        if (messageTokens[1].compareTo("failure") == 0) {
          game.setIsConnected(false);
        }
      }
      if (messageTokens[0].compareTo("bye") == 0) {
        // format: bye, remoteId
        UUID ghostID = UUID.fromString(messageTokens[1]);
        removeGhostAvatar(ghostID);
      }
      if ((messageTokens[0].compareTo("dsfr") == 0) || // receive dsfr
          (messageTokens[0].compareTo("create") == 0)) {
        // format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z

        UUID ghostID = UUID.fromString(messageTokens[1]);
        Vector3 ghostPosition = Vector3f.createFrom(Float.parseFloat(messageTokens[2]),
            Float.parseFloat(messageTokens[3]), Float.parseFloat(messageTokens[4]));
        String ghostSkin = (String) messageTokens[5];

        try {
          createGhostAvatar(ghostID, ghostPosition, ghostSkin);
        } catch (Exception e) {
        }
      }

      // rec. wants… // etc…..
      // Gets the details for the specific UUID the server is looking for
      // Gets the position and sends both the UUID and the pos
      if (messageTokens[0].compareTo("wsds") == 0) {
        UUID ghostID = UUID.fromString(messageTokens[1]);
        sendDetailsForMessage(ghostID, game.getPlayerPosition(), game.getPlayerSkin());
      }

      // rec. move... // etc…..
      if (messageTokens[0].compareTo("move") == 0) {
        // format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z
        UUID ghostID = UUID.fromString(messageTokens[1]);
        Vector3 ghostPosition = Vector3f.createFrom(Float.parseFloat(messageTokens[2]),
            Float.parseFloat(messageTokens[3]), Float.parseFloat(messageTokens[4]));
        Angle ghostAngle = Degreef.createFrom(Float.parseFloat(messageTokens[5]));
        try {
          moveGhostAvatar(ghostID, ghostPosition, ghostAngle);
        } catch (Exception e) {
        }
      }

      if (messageTokens[0].compareTo("mnpc") == 0) {
        int ghostID = Integer.parseInt(messageTokens[1]);
        Vector3 ghostPosition = Vector3f.createFrom(Float.parseFloat(messageTokens[2]),
            Float.parseFloat(messageTokens[3]), Float.parseFloat(messageTokens[4]));
        Vector3 ghostSize = Vector3f.createFrom(Float.parseFloat(messageTokens[5]), Float.parseFloat(messageTokens[6]),
            Float.parseFloat(messageTokens[7]));
        try {
          updateGhostNPC(ghostID, ghostPosition, ghostSize);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (messageTokens[0].compareTo("mitem") == 0) {
        int ghostID = Integer.parseInt(messageTokens[1]);
        Vector3 ghostPosition = Vector3f.createFrom(Float.parseFloat(messageTokens[2]),
            Float.parseFloat(messageTokens[3]), Float.parseFloat(messageTokens[4]));
        String ghostSkin = (String) messageTokens[5];
        try {
          updateGhostItem(ghostID, ghostPosition, ghostSkin);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (messageTokens[0].compareTo("Winner") == 0) {

        updateWinMessage();
      }

      if (messageTokens[0].compareTo("Loser") == 0) {
        updateLoseMessage();
      }
    }
  }

  public void updateWinMessage() {
    this.game.winnerUpdate();
  }

  public void updateLoseMessage() {
    this.game.loserUpdate();
  }

  // format: join, localId
  public void sendJoinMessage() {
    try {
      sendPacket(new String("join," + id.toString()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // format: (create, localId, x,y,z, skin.png)
  public void sendCreateMessage(Vector3 pos, String skin) {
    try {
      String message = new String("create," + this.id.toString());
      message += "," + pos.x() + "," + pos.y() + "," + pos.z();
      message += "," + skin;
      sendPacket(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // format: (bye, localId)
  public void sendByeMessage() {
    try {
      String message = new String("bye," + id.toString());
      sendPacket(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // format: (dsfr, remoteID, localID, x,y,z)
  // This is saying, I want to send the details to remoteID
  // The details that I'm sending are my LocalID and my coordinates.
  public void sendDetailsForMessage(UUID remid, Vector3 pos, String skin) {
    try {
      String message = new String("dsfr," + remid.toString() + "," + this.id.toString());
      message += "," + pos.x() + "," + pos.y() + "," + pos.z();
      message += "," + skin;
      sendPacket(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendMoveMessage(Vector3 pos, Angle ang) {
    try {
      String message = new String("move," + id.toString());
      message += "," + pos.x() + "," + pos.y() + "," + pos.z();
      message += "," + ang.valueDegrees();
      sendPacket(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendCollidedItem(int id) {
    try {
      String message = new String("moveItem," + Integer.toString(id));
      sendPacket(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void askForNPCinfo() {
    try {
      sendPacket(new String("needNPC," + id.toString()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void askForItemInfo() {
    try {
      sendPacket(new String("needItems," + id.toString()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendIncrementScore(int score) {
    try {
      String message = new String("inc," + id.toString());
      message += "," + Integer.toString(score);
      sendPacket(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method to locate the GhostAvatar object with the specific GhostID
  public GhostAvatar getGhost(UUID GhostID) {
    GhostAvatar temp = null;
    if (this.ghostAvatars.isEmpty()) {
    } else {
      for (int i = 0; i < this.ghostAvatars.size(); i++) {
        if (GhostID == this.ghostAvatars.get(i).getID()) {
          temp = this.ghostAvatars.get(i);
        }
      }
    }
    return temp;
  }

  public void createGhostAvatar(UUID ghostID, Vector3 ghostPosition, String skin) throws IOException {
    this.ghostAvatars.add(new GhostAvatar(ghostID, ghostPosition, skin));
    this.game.addGhostAvatarToGameWorld(this.ghostAvatars.get(ghostAvatars.size() - 1));
  }

  public void removeGhostAvatar(UUID ghostID) {
    if (this.ghostAvatars.isEmpty()) {
    } else {
      for (int i = 0; i < this.ghostAvatars.size(); i++) {
        if (ghostID == this.ghostAvatars.get(i).getID()) {
          this.game.removeGhostAvatarFromGameWorld(this.ghostAvatars.get(i));
          this.ghostAvatars.remove(i);
          break;
        }
      }
    }
  }

  public void moveGhostAvatar(UUID ghostID, Vector3 pos, Angle ang) throws IOException {
    if (this.ghostAvatars.isEmpty()) {
    } else {
      for (int i = 0; i < this.ghostAvatars.size(); i++) {
        if (ghostID.toString().equals(this.ghostAvatars.get(i).getID().toString())) {
          this.ghostAvatars.get(i).setPosition(pos);
          this.ghostAvatars.get(i).setAngle(ang);
          this.game.moveGhostAvatarAroundGameWorld(this.ghostAvatars.get(i), pos, ang);
        }
      }
    }
  }

  private void createGhostNPC(int id, Vector3 position, Vector3 size) throws IOException {
    GhostNPC newNPC = new GhostNPC(id, position, size);
    ghostNPCs.add(newNPC);
    game.addGhostNPCtoGameworld(newNPC);
  }

  private void updateGhostNPC(int id, Vector3 position, Vector3 size) throws IOException {
    // If the vector contains the element, adjust it. Otherwise, make it.
    if (!(ghostNPCs.size() < 5)) {
      ghostNPCs.get(id).setPosition(position);
      ghostNPCs.get(id).setSize(size);
      game.moveGhostNPCAroundGameWorld(ghostNPCs.get(id), position, size);
    } else {
      createGhostNPC(id, position, size);
    }
  }

  private void createGhostItem(int id, Vector3 position, String skin) throws IOException {
    GameItem item = new GameItem(id);
    item.setPosition(position);
    item.setSkin(skin);
    ghostItems.add(item);
    game.addGhostItemToGameWorld(ghostItems.get(ghostItems.size() - 1));
  }

  private void updateGhostItem(int id, Vector3 position, String skin) throws IOException {
    // If the vector contains the element, adjust it. Otherwise, make it.
    if (!(ghostItems.size() < 7)) {
      ghostItems.get(id - 20).setPosition(position);
      ghostItems.get(id - 20).setSkin(skin);
      game.updateItemPositions(ghostItems.get(id - 20));
    } else {
      createGhostItem(id, position, skin);
    }
  }

  public void print(Object o) {
    System.out.println(o);
  }
}
