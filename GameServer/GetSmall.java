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

public class GetSmall extends BTAction {

    NPC npc;

    public GetSmall(NPC n) {
        npc = n;
    }

    protected BTStatus update(float elapsedTime) {
        npc.getSmall();
        return BTStatus.BH_SUCCESS;
    }

}
