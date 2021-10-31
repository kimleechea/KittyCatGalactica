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

public class GetBig extends BTAction {

    NPC npc;

    public GetBig(NPC n) {
        npc = n;
    }

    protected BTStatus update(float elapsedTime) {
        npc.getBig();
        return BTStatus.BH_SUCCESS;
    }

}
