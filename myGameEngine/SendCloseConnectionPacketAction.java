package myGameEngine;
import GameServer.*;
import KittyCatGalactica.*;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;


public class SendCloseConnectionPacketAction extends AbstractInputAction {

    private ProtocolClient protClient;
    private MyGame myGame;

    public SendCloseConnectionPacketAction(MyGame m, ProtocolClient p)
    {
        protClient = p;
        myGame = m;
    }

    
    public void performAction(float time, Event evt)
    { 
        if(protClient != null && myGame.isClientConnected == true)
        { 
            protClient.sendByeMessage();
            System.out.println("shutdown requested");
            SkeletalEntity station1SE = (SkeletalEntity) myGame.getEngine().getSceneManager().getEntity("station1RC");
            SkeletalEntity station2SE = (SkeletalEntity) myGame.getEngine().getSceneManager().getEntity("station2RC");
            station1SE.stopAnimation();
            station2SE.stopAnimation();
            myGame.exit();

        }
        else {

            System.out.println("shutdown requested");
            myGame.exit();
        }
        
    }
}
