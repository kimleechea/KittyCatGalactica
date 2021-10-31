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

public class MultSecPassed extends BTCondition {

    NPCcontroller npcCtrl;
    NPC npc;
    long lastUpdateTime;

    public MultSecPassed(NPCcontroller c, NPC n, boolean toNegate) {
        super(toNegate);
        npcCtrl = c;
        npc = n;
        lastUpdateTime = System.nanoTime();
    }

    protected boolean check() {
        float elapsedMilliSecs = (System.nanoTime() - lastUpdateTime) / (1000000.0f);
        if ((elapsedMilliSecs >= 15000.0f)) {
            lastUpdateTime = System.nanoTime();
            // npcc.setNearFlag(false);
            return true;
        } else
            return false;
    }
}
