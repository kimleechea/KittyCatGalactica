package myGameEngine;
import GameServer.*;
import KittyCatGalactica.*;
import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rage.game.*;
import ray.rml.*;
import net.java.games.input.Event;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color.*;
import java.io.*;
import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.rendersystem.states.*;
import ray.rage.rendersystem.*;
import ray.rage.asset.texture.*;
import ray.rage.asset.AbstractAsset.*;
import ray.rage.asset.material.Material;

public class LightDownAction extends AbstractInputAction
{
    private MyGame game;

    public LightDownAction(MyGame g)
    {
        game = g;
    }

    public void performAction(float time, Event e)
	{   
        game.playerLighter.setAmbient(new Color(.1f, .1f, .1f));
        game.playerLighter.setDiffuse(new Color(.3f, .4f, .5f));
        game.playerLighter.setSpecular(new Color(.5f, .5f, .5f));
        game.playerLighter.setRange(9.0f);

	}
}
