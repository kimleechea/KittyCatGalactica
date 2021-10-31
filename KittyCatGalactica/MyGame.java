package KittyCatGalactica;
import myGameEngine.*;
import GameServer.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.Color.*;
import java.io.*;
import java.nio.FloatBuffer;	
import java.nio.IntBuffer;
import java.nio.Buffer.*;
import java.lang.Object.*;
import java.util.*;
import java.io.IOException;
import java.awt.geom.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import ray.rage.util.Configuration;
import java.util.List;

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
import ray.input.*;
import ray.input.action.*;
import ray.input.action.Action;
import ray.input.action.AbstractInputAction;
import ray.input.GenericInputManager;
import ray.input.InputManager;
import ray.input.InputManager.INPUT_ACTION_TYPE;
import ray.rage.util.BufferUtil.*;
import ray.rage.rendersystem.shader.*;
import ray.rage.util.*;
import ray.rage.rendersystem.Renderable.Primitive;
import ray.physics.*;
import ray.networking.IGameConnection.ProtocolType;
import static ray.rage.scene.SkeletalEntity.EndType.*;
import ray.audio.*;
import com.jogamp.openal.ALFactory;

public class MyGame extends VariableFrameRateGame implements MouseListener, MouseMotionListener{

	// to minimize variable allocation in update()
	GL4RenderSystem rs;
	float elapsTime = 0.0f;
	float elapsTime2 = 0.0f;
	String elapsTimeStr, counterStr, dispStr, dispStr2, dispStr3;
	int elapsTimeSec, counter = 0, score1, score2;
	public InputManager im;
	String winStatus = "";
	SceneManager sm;
	private Camera3PController orbitController1, orbitController2;
	private Action 	quitGameAction, incrementCounterAction, moveForwardAction, moveBackwardAction
					,tiltLeftAction, tiltRightAction, jumpAction, volumeDownAction, volumeUpAction 
					,volumeMuteAction, speedBoostAction, LightUpAction, LightDownAction, musicDownAction
					,musicUpAction;
					
	private PhysicsEngine physicsEng;
	private PhysicsObject groundPhysicsObject, station1PhysicsObject, station2PhysicsObject, playerPhysicsObject;
	private SceneNode tessellationN;

	ArrayList <Integer> physicObjects= new ArrayList<Integer>();
	ArrayList <String> list = new ArrayList<String>();
	ArrayList <String> listSkins = new ArrayList<String>();

	ArrayList <Entity> itemEntityList = new ArrayList<Entity>();
	ArrayList <Light> spotLampList = new ArrayList<Light>();
	ArrayList <SceneNode> itemNodeList = new ArrayList<SceneNode>();
	ArrayList<String> itemName = new ArrayList<String>();
	ArrayList<String> itemType = new ArrayList<String>();

	ArrayList<AudioResource> radioResourceList = new ArrayList<AudioResource>();
	ArrayList<Sound> radioSoundList = new ArrayList<Sound>();
	List<String> musicList;
	List<String> authorList;

	private static final String SKYBOX_NAME = "SkyBox";
	private boolean skyBoxVisible = true;
	public Light playerLighter;

	IAudioManager audioMgr;  
	Sound rocketSound, itemSound, bgSound, meowSound;
	
	int ch = 1;
	int ct = 1;
	int count1 = 0;
	int count2 = 0;
	int count3 = 0;
	int count4 = 0;
	int radioCount;
	int musicVolume = 0;

	public float x;
	public float y;
	public float z;
	public float nextX;
	public float nextY;
	public float nextZ;

	public float batteryTime = 0.0f;
	public float PLAYER_SPEED = 0.1f;
	public float BOOST_SPEED;
	public boolean hasBattery;

	public boolean isClientConnected;
	protected SceneNode ghostN;
	protected SceneNode ghostNPC_N, ghostItem_N;
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	public ProtocolClient protClient;
	private Vector<UUID> gameObjectsToRemove;
	private static String catThemeChooser = "";
	
	protected SceneNode check;
	
	

    public MyGame(String serverAddr, int sPort){
		super();
		this.serverAddress = serverAddr;
		this.serverPort = sPort;
		this.serverProtocol = ProtocolType.UDP;	  
		setupNetworking();
	}
	
	public MyGame(){
		super();
	}

    public static void main(String[] args) {
		Scanner src = new Scanner(System.in);
		while(catThemeChooser.length() == 0)
		{
			System.out.println("Which cat character would you like?");
			System.out.println("Enter 1 for Hero Cat");
			System.out.println("Enter 2 for Villain Cat");
			int choice = src.nextInt(); 
			if(choice == 1)
			{
				catThemeChooser = "pirate_cat_texture_color.png";
			}
			else if(choice == 2)
			{
				catThemeChooser = "pirate_cat_enemy_texture_color.png";
			}
			else{
				System.out.println("Incorrect input. Game cannot start. Please try again.");
			}

		}
		MyGame game;
		
		if(args.length == 0)
		{
			game = new MyGame();
		}
		else
		{
			game = new MyGame(args[0], Integer.parseInt(args[1]));
		}
		// Make the factory and pull the script
		ScriptEngineManager factory = new ScriptEngineManager();

		// Get the JavaScript engine
		ScriptEngine jsEngine = factory.getEngineByName("js");

        try {
			
            game.startup();
            game.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            game.shutdown();
            game.exit();
		}
	
	}
	
	private void executeScript(ScriptEngine engine, String scriptFileName){
 		try
 		{ 
			FileReader fileReader = new FileReader(scriptFileName);
 			engine.eval(fileReader); //execute the script statements in the file
 			fileReader.close();
 		}
 		catch (FileNotFoundException e1)
 		{ System.out.println(scriptFileName + " not found " + e1); }
 		catch (IOException e2)
 		{ System.out.println("IO problem with " + scriptFileName + e2); }
 		catch (ScriptException e3)
 		{ System.out.println("ScriptException in " + scriptFileName + e3); }
 		catch (NullPointerException e4)
 		{ System.out.println ("Null ptr exception in " + scriptFileName + e4); }
 	}

	@Override
	protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
		rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
	}
	
	

    @Override
    protected void setupCameras(SceneManager sm, RenderWindow rw) {
        SceneNode rootNode = sm.getRootSceneNode();
		//RenderSystem rsy = sm.getRenderSystem();
        Camera camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
        rw.getViewport(0).setCamera(camera);

        SceneNode cameraN = rootNode.createChildSceneNode("MainCameraNode");
        cameraN.attachObject(camera);
		camera.setMode('r');
		camera.getFrustum().setFarClipDistance(1000.0f);
		
		camera.setRt((Vector3f)Vector3f.createFrom(1.0f, 0.0f, 0.0f));
		camera.setUp((Vector3f)Vector3f.createFrom(0.0f, 1.0f, 0.0f));
		camera.setFd((Vector3f)Vector3f.createFrom(0.0f, 0.0f, -1.0f));
		camera.setPo((Vector3f)Vector3f.createFrom(0.0f, 0.0f, 0.0f));
    }
		
	private void setupPhysicsEngine(){
		String engine = "ray.physics.JBullet.JBulletPhysicsEngine";
		float[] gravity = {0, -3f, 0};
		physicsEng = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEng.initSystem();
		physicsEng.setGravity(gravity);
    }
	
	private void setupPhysicsObjects(){
		
		// Prepare the Script Engine
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine jsEngine = factory.getEngineByName("js");
		String physicObjectsInit = "PhysicObjectsInit.js";
		
		// Get the proper Scene Nodes to modify
		SceneNode station1N = this.getEngine().getSceneManager().getSceneNode("station1RCNode");
		jsEngine.put("station1N", station1N);
		SceneNode station2N = this.getEngine().getSceneManager().getSceneNode("station2RCNode");
		jsEngine.put("station2N", station2N);
		SceneNode groundNode = this.getEngine().getSceneManager().getSceneNode("GroundN");
		jsEngine.put("groundNode", groundNode);
		//SceneNode dolphinN = this.getEngine().getSceneManager().getSceneNode("dolphinNode");
		//jsEngine.put("dolphinN", dolphinN);

		// Set up the physics paramaters + a temporary array for doubles
		float up[] = {0,1,0};
		float mass = 1.0f;
		double[] TemporaryDoubleArray;

		// Instantiates the planet to the physics world
		TemporaryDoubleArray = toDoubleArray(station1N.getLocalTransform().toFloatArray());
		station1PhysicsObject = physicsEng.addSphereObject(physicsEng.nextUID(), mass , TemporaryDoubleArray , 2.0f);
		jsEngine.put("station1PhysicsObject", station1PhysicsObject);
		station1N.setPhysicsObject(station1PhysicsObject);

		TemporaryDoubleArray = toDoubleArray(station2N.getLocalTransform().toFloatArray());
		station2PhysicsObject = physicsEng.addSphereObject(physicsEng.nextUID(), mass , TemporaryDoubleArray , 2.0f);
		jsEngine.put("station2PhysicsObject", station2PhysicsObject);
		station2N.setPhysicsObject(station2PhysicsObject);

		// Instantiates the ground plane to the physics world
		TemporaryDoubleArray = toDoubleArray(groundNode.getLocalTransform().toFloatArray());
		groundPhysicsObject = physicsEng.addStaticPlaneObject(physicsEng.nextUID(), TemporaryDoubleArray, up, 0.0f);
		jsEngine.put("groundPhysicsObject", groundPhysicsObject);	
		groundNode.setPhysicsObject(groundPhysicsObject);

		// Instantiates the player to the physics world
		//TemporaryDoubleArray = toDoubleArray(dolphinN.getLocalTransform().toFloatArray());
		//playerPhysicsObject = physicsEng.addSphereObject(physicsEng.nextUID(), mass , TemporaryDoubleArray , 0.0f);
		//dolphinN.setPhysicsObject(playerPhysicsObject);


		// Script Executed
		this.executeScript(jsEngine, physicObjectsInit);
	}
	
	public void updateVerticalPosition () {
		final SceneNode dolphinN = this.getEngine().getSceneManager().getSceneNode("dolphinNode");
		final SceneNode tessellationN = this.getEngine().getSceneManager().getSceneNode("tessellationN");
		final Tessellation tessellationE = ((Tessellation)tessellationN.getAttachedObject("tessellationE"));
		
		final Vector3 worldPosition = dolphinN.getWorldPosition();
		final Vector3 localPosition = dolphinN.getLocalPosition();
		
		final Vector3 newLocalPosition = Vector3f.createFrom(
					// Keep the X coordinate
					localPosition.x(),
					// The Y coordinate is the varying height
					tessellationE.getWorldHeight(localPosition.x(), localPosition.z())+0.27f,
					// Keep the Z coordinate
					localPosition.z()
				);
		dolphinN.setLocalPosition(newLocalPosition);
	}

	public void updateSkydome(){
		SceneNode dolphinN = this.getEngine().getSceneManager().getSceneNode("dolphinNode");
		SceneNode skydomeN = this.getEngine().getSceneManager().getSceneNode("myPlanet4Node");

		Vector3 d = dolphinN.getLocalPosition();
		Vector3 s = dolphinN.getLocalPosition();

		skydomeN.setLocalPosition((Vector3f)Vector3f.createFrom(d.x(),d.y(),d.z()));
	}	
	
    @Override
    protected void setupScene(Engine eng, SceneManager sm) throws IOException {
		
		// Prepare the Script Engine
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine jsEngine = factory.getEngineByName("js");

		im = new GenericInputManager();
		Angle rotationAngleAmt = Degreef.createFrom(180f);
		Angle rotationAngleAmt2 = Degreef.createFrom(15.0f);
		jsEngine.put("rotationAngleAmt", rotationAngleAmt);
		Vector3 origin = (Vector3f) Vector3f.createFrom(0f, 0f, 0f);

		// set up sky box
		String skyBox = "SkyBox.js";
		Configuration conf = eng.getConfiguration();
		
		TextureManager tm = getEngine().getTextureManager();
		jsEngine.put("tm", tm);
		
		tm.setBaseDirectoryPath(conf.valueOf("assets.skyboxes.path")); 

		Texture front = tm.getAssetByPath("starmapbyme1.jpg");
		jsEngine.put("front", front);
		Texture back = tm.getAssetByPath("starmapbyme2.jpg");
		jsEngine.put("back", back);
		Texture left = tm.getAssetByPath("starmapbyme3.jpg");  
		jsEngine.put("left", left);
		Texture right = tm.getAssetByPath("starmapbyme4.jpg");
		jsEngine.put("right", right);
		Texture top = tm.getAssetByPath("starmapbyme5.jpg");     
		jsEngine.put("top", top);	
		Texture bottom = tm.getAssetByPath("starmapbyme6.jpg");  
		jsEngine.put("bottom", bottom);     

		tm.setBaseDirectoryPath(conf.valueOf("assets.textures.path"));

		AffineTransform xform = new AffineTransform();   
		jsEngine.put("xform", xform);     
		
		SkyBox sb = sm.createSkyBox(SKYBOX_NAME);
		sb.setTexture(front, SkyBox.Face.FRONT);        
		sb.setTexture(back, SkyBox.Face.BACK);        
		sb.setTexture(left, SkyBox.Face.LEFT);        
		sb.setTexture(right, SkyBox.Face.RIGHT);        
		sb.setTexture(top, SkyBox.Face.TOP);        
		sb.setTexture(bottom, SkyBox.Face.BOTTOM);    
		       
		sm.setActiveSkyBox(sb);

		this.executeScript(jsEngine, skyBox);
		
		//load skeletal entity and animations
		SkeletalEntity station1SE = sm.createSkeletalEntity("station1RC", "StationRoboCatMesh.rkm", "StationRoboCatSkeleton.rks");
		station1SE.loadAnimation("spinAnimation","StationRoboCatSpinAnimation.rka");
		Texture tex = sm.getTextureManager().getAssetByPath("StationRoboCat.png");
		TextureState tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate.setTexture(tex);
		station1SE.setRenderState(tstate);

		SkeletalEntity station2SE = sm.createSkeletalEntity("station2RC", "StationRoboCatMesh.rkm", "StationRoboCatSkeleton.rks");
		station2SE.loadAnimation("spinAnimation","StationRoboCatSpinAnimation.rka");
		Texture tex2 = sm.getTextureManager().getAssetByPath("StationRoboCat.png");
		TextureState tstate2 = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate2.setTexture(tex2);
		station2SE.setRenderState(tstate2);

		SkeletalEntity piratecatSE = sm.createSkeletalEntity("piratecat", "pirate_cat_mesh.rkm", "pirate_cat_skeleton.rks");
		piratecatSE.loadAnimation("waveAnimation","pirate_cat_wave.rka");
		Texture tex1 = sm.getTextureManager().getAssetByPath(catThemeChooser);
		TextureState tstate1 = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
		tstate1.setTexture(tex1);
		piratecatSE.setRenderState(tstate1);

		Tessellation tessellationE = sm.createTessellation("tessellationE", 6);
		tessellationE.setSubdivisions(0f);
		tessellationN = sm.getRootSceneNode().createChildSceneNode("tessellationN");
	
		Entity groundEntity = sm.createEntity("Ground", "cube.obj");
		groundEntity.setPrimitive(Primitive.TRIANGLES);


		// Script file for the placements and adjustments of SceneNodes
		String scriptFile3 = "SceneNodePlacements.js";
		jsEngine.put("sm", sm);
		jsEngine.put("eng", eng);
		

		SceneNode tessellationN = this.getEngine().getSceneManager().getSceneNode("tessellationN");
		jsEngine.put("tessellationN", tessellationN);
		tessellationN.attachObject(tessellationE);
		tessellationE = ((Tessellation) tessellationN.getAttachedObject("tessellationE"));
		jsEngine.put("tessellationE", tessellationE);
		//tessellationE.setHeightMap(this.getEngine(), "blue.jpeg");
		//tessellationE.setTexture(this.getEngine(), "arena.png");

		
        SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode("dolphinNode");
		jsEngine.put("dolphinN", dolphinN);
		dolphinN.attachObject(piratecatSE);
		dolphinN.setLocalPosition(0f,-1.6692555f,0f);
		
		SceneNode stationNG = sm.getRootSceneNode().createChildSceneNode("stationNGroup");
		
		SceneNode station2N = stationNG.createChildSceneNode(station2SE.getName() + "Node");
		jsEngine.put("station2N", station2N);
        station2N.attachObject(station2SE);
		
		SceneNode station1N = stationNG.createChildSceneNode(station1SE.getName() + "Node");
		jsEngine.put("station1N", station1N);
		station1N.attachObject(station1SE);
		
		SceneNode groundNode = sm.getRootSceneNode().createChildSceneNode("GroundN");
		jsEngine.put("groundNode", groundNode);
		groundNode.attachObject(groundEntity);
		
		this.executeScript(jsEngine, scriptFile3);

		check = sm.getRootSceneNode().createChildSceneNode("currentPoint");

		playerLighter = sm.createLight("playerLighter", Light.Type.POINT);
		dolphinN.attachObject(playerLighter);


		Light spot = sm.createLight("spotLamp", Light.Type.POINT);
		spot.setAmbient(new Color(.3f, .3f, .3f));
		spot.setDiffuse(new Color(.3f, .4f, .5f));
		spot.setSpecular(new Color(.5f, .5f, .5f));
		spot.setRange(9.0f);
		//spot.setConeCutoffAngle(rotationAngleAmt2);
		//spot.setFalloffExponent(.002f);
		spotLampList.add(spot);

		SceneNode spotNode = sm.getRootSceneNode().createChildSceneNode("spotNode");
		spotNode.setLocalPosition(station1N.getLocalPosition());
		spotNode.moveUp(5.0f);
		spotNode.attachObject(spot);

				
		String createLight = "CreateLight.js";
		this.executeScript(jsEngine, createLight);
		
		SceneNode plightNode = sm.getRootSceneNode().createChildSceneNode("plightNode");
		plightNode.moveDown(15f);
		plightNode.attachObject((Light)jsEngine.get("plight"));

		
		setupRotationControllers(sm);
		setupOrbitCamera(eng, sm);
		setupPhysicsEngine();
		setupPhysicsObjects();
		doTheSpin();
		initAudio(sm);
		setupInputs(sm);
	}

	private void doTheSpin(){
		SkeletalEntity station1SE = (SkeletalEntity) this.getEngine().getSceneManager().getEntity("station1RC");
		station1SE.stopAnimation();
		station1SE.playAnimation("spinAnimation",0.1f,LOOP,0);

		SkeletalEntity station2SE = (SkeletalEntity) this.getEngine().getSceneManager().getEntity("station2RC");
		station2SE.stopAnimation();
		station2SE.playAnimation("spinAnimation",0.1f,LOOP,0);
	}

	private void doTheWave(){
		SkeletalEntity piratecatSE = (SkeletalEntity) this.getEngine().getSceneManager().getEntity("piratecat");
		piratecatSE.stopAnimation();
		piratecatSE.playAnimation("waveAnimation",0.2f,STOP,0);
	}

    @Override
    protected void update(Engine engine){
		
		SceneManager sm = engine.getSceneManager();

		SceneNode dolphinN = engine.getSceneManager().getSceneNode("dolphinNode");
		SkeletalEntity station1SE = (SkeletalEntity) engine.getSceneManager().getEntity("station1RC");
		SkeletalEntity station2SE = (SkeletalEntity) engine.getSceneManager().getEntity("station2RC");
		SkeletalEntity piratecatSE = (SkeletalEntity) engine.getSceneManager().getEntity("piratecat");
		
		String gpName = im.getFirstGamepadName();
		String msName = im.getMouseName();

		musicVolume = radioSoundList.get(radioCount).getVolume();
		if(radioSoundList.get(radioCount).getIsPlaying() == false){
			musicVolume = 0;
		}
		
		setupStationHierarchy(sm);
		setupRotationControllers(sm);
		station1SE.update();
		station2SE.update();
		piratecatSE.update();
		
		
		//	Engine time
		rs = (GL4RenderSystem) engine.getRenderSystem();
		elapsTime += engine.getElapsedTimeMillis();
		elapsTimeSec = Math.round(elapsTime/1000.0f);
		elapsTimeStr = Integer.toString(elapsTimeSec);
		counterStr = Integer.toString(counter);
		
		
		//	HUD code + winning results
		if(!winStatus.isEmpty())
			dispStr = winStatus + "  Song:  " + radioResourceList.get(this.getRadioCount()).getFileName().replaceFirst("assets/sounds/","") + 
					" by " + authorList.get(getRadioCount()) + "  Volume:  " + musicVolume;
		else
			dispStr = "Score:  " + count1 + "  Song: " + radioResourceList.get(this.getRadioCount()).getFileName().replaceFirst("assets/sounds/","") + 
					" by " + authorList.get(getRadioCount()) + "  Volume:  " + musicVolume;
		
		rs.setHUD(dispStr, rs.getRenderWindow().getViewport(0).getActualLeft() + 5, 
					    rs.getRenderWindow().getViewport(0).getActualBottom() + 5 );


		// Gamepad connected
		if(gpName != null){			
			orbitController1.updateCameraPosition();
		}else{
			orbitController1.updateCameraPosition();
		}

		// Check if the current player has a battery.
		if(hasBattery)
		{
			batteryTime += engine.getElapsedTimeMillis();
		}

		// Check if the 5 seconds are up for the battery to reset
		if(Math.round(batteryTime/1000.0f) == 5)
		{
			batteryTime = 0.0f;
			hasBattery = false;
			PLAYER_SPEED = 0.1f;
		}

		//	Process Networking
		if(protClient != null)
			processNetworking(elapsTime);
		
		//	Collision Detection
		collisionDetect();
		
		
		//	Update the Input Manager
		im.update(elapsTime);
		
		
		//	Update the physics objects
		float time = engine.getElapsedTimeMillis();
		physicsEng.update(time);
		Matrix4 mat;
		for (SceneNode s : engine.getSceneManager().getSceneNodes()){
			if (s.getPhysicsObject() != null){
				mat = Matrix4f.createFrom(toFloatArray( s.getPhysicsObject().getTransform()));
				s.setLocalPosition(mat.value(0,3),mat.value(1,3),mat.value(2,3));
			} 
		} 

		meowSound.setLocation(dolphinN.getWorldPosition());
		itemSound.setLocation(dolphinN.getWorldPosition());    
		rocketSound.setLocation(dolphinN.getWorldPosition());
		for(int j = 0; j < radioResourceList.size(); j++){
			radioSoundList.get(j).setLocation(dolphinN.getWorldPosition());
		} 
		setEarParameters(sm);
	}

	public void incrementRadioCount(){
		radioCount++;
	}

	public void decrementRadioCount(){
		radioCount--;
	}

	public int getRadioCount(){
		return radioCount;
	}
	
	protected void setupInputs(SceneManager sm){
		
		Camera camera = getEngine().getSceneManager().getCamera("MainCamera");
		JumpController jc = new JumpController();
		SpeedBoostController sc = new SpeedBoostController();
		final Tessellation tessellationE = ((Tessellation)tessellationN.getAttachedObject("tessellationE"));
		SceneNode dolphinN = getEngine().getSceneManager().getSceneNode("dolphinNode");
		
		im = new GenericInputManager();
		String kbName = im.getKeyboardName();
		String gpName = im.getFirstGamepadName();
		String msName = im.getMouseName();
		 
		char m = camera.getMode();
		 
		quitGameAction = new QuitGameAction(this);
		incrementCounterAction = new IncrementCounterAction(this);
		//moveForwardAction = new MoveForwardAction(camera, dolphinN, this, this.protClient);
		moveBackwardAction = new MoveBackwardAction(camera, dolphinN, this, this.protClient);
		//moveLeftAction = new MoveLeftAction(camera, dolphinN);
		//moveRightAction = new MoveRightAction(camera, dolphinN);
		 
		tiltLeftAction = new TiltLeftAction(camera, dolphinN, orbitController1, this.protClient);
		tiltRightAction = new TiltRightAction(camera, dolphinN, this.protClient);
		//sendClose = new SendCloseConnectionPacketAction(protClient, isClientConnected);

		//jumpAction = new JumpAction(dolphinN, tessellationE, jc, sm);

		volumeDownAction = new VolumeDownAction(radioSoundList, this);
		volumeUpAction = new VolumeUpAction(radioSoundList, this);
		volumeMuteAction = new VolumeMuteAction(radioSoundList, this);

		musicUpAction = new MusicUpAction(this, dolphinN, audioMgr, radioSoundList, radioCount);
		musicDownAction = new MusicDownAction(this, dolphinN, audioMgr, radioSoundList, radioCount);
		
		if(gpName != null){
			orbitController1.setupInput(im, msName);
		}else{
			orbitController1.setupInput(im, msName);
		}

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.ESCAPE,
		new SendCloseConnectionPacketAction(this, this.protClient),InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.W,
		new MoveForwardAction(camera, dolphinN, this, this.protClient),InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				 
		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.S,
		moveBackwardAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				 
		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.A,
		tiltLeftAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				 
		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.D,
		tiltRightAction,InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		//im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.SPACE,
		//jumpAction,InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		//im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.SPACE,
		//speedBoostAction,InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.M,
		volumeMuteAction,InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.UP,
		volumeUpAction,InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.DOWN,
		volumeDownAction,InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.RIGHT,
		musicUpAction,InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.LEFT,
		musicDownAction,InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.U,
		new LightUpAction(this),InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.I,
		new LightDownAction(this),InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
						
	}
	
	protected void setupOrbitCamera(Engine eng, SceneManager sm){ 
		SceneNode dolphinN = sm.getSceneNode("dolphinNode");
		SceneNode cameraN = sm.getSceneNode("MainCameraNode");
		Camera camera = sm.getCamera("MainCamera");
		//im = new GenericInputManager();
		//String kbName = im.getKeyboardName();
		String msName = im.getMouseName();
	
		orbitController1 = new Camera3PController(camera, cameraN, dolphinN, msName, im);
		
		//SceneNode dolphin2N = sm.getSceneNode("dolphin2Node");
		//SceneNode camera2N = sm.getSceneNode("MainCamera2Node");
		//Camera camera2 = sm.getCamera("MainCamera2");
		String gpName = im.getFirstGamepadName();
		
		if(gpName != null){
		//	orbitController2 = new Camera3PController(camera2, camera2N, dolphin2N, gpName, im);
		}
	} 
	
	protected void setupRotationControllers(SceneManager sm){
		SceneNode temp;
		RotationController rc1 = new RotationController(Vector3f.createUnitVectorY());
		rc1.setSpeed(0.00001f);

		for(int i = 20; i <= 26 && !list.isEmpty(); i++){
			temp = this.getEngine().getSceneManager().getSceneNode(list.get(i-20));
			rc1.addNode(temp);
		}
		sm.addController(rc1);
	}
	
	public void setupStationHierarchy(SceneManager sm){
		SceneNode stationNG = getEngine().getSceneManager().getSceneNode("stationNGroup");
		SinkController sc = new SinkController(); // user-defined node controller
		
		if(count1>=500){
			sc.addNode(stationNG);
			sm.addController(sc);
		}
	}
	
	private void collisionDetect(){
		SceneNode temp, temp2;
		String skinName;
		
		float min = 5f;
		float max = 40f;
		float min2 = 0.1f;
		float max2 = 0.8f;
		Random rand = new Random();
		float randomDistance = min + rand.nextFloat() * (max - min);
		float randomSpeed = min2 + rand.nextFloat() * (max2 - min2);
		
		float mn = 5f;
		float mx = 40f;
		float mn2 = 0.1f;
		float mx2 = 0.8f;
		Random r2 = new Random();
		float randomDistance2 = mn + r2.nextFloat() * (mx - mn);
		float randomSpeed2 = mn2 + r2.nextFloat() * (mx2 - mn2);
		
		//OrbitController oc1 = new OrbitController(this.getEngine().getSceneManager().getSceneNode("myPlanet5Node"), randomSpeed, randomDistance);
		//OrbitController oc2 = new OrbitController(this.getEngine().getSceneManager().getSceneNode("myPlanet6Node"), randomSpeed2, randomDistance2);
		SceneNode dolphinN = this.getEngine().getSceneManager().getSceneNode("dolphinNode");
		check= this.getEngine().getSceneManager().getSceneNode("currentPoint");

		for(int j = 20; j <= 26 && !list.isEmpty(); j++){
			//list.add(j+"Node");
			temp = this.getEngine().getSceneManager().getSceneNode(list.get(j-20));
			
			float distance;
					
			distance = distance(temp,dolphinN);
					
			if(distance <= 2){
				doTheWave();
				itemSound.play();
				meowSound.play();
				skinName = listSkins.get(j - 20);
				if(skinName.equals("crystal.obj")){
					count1 += 20;
				}else if (skinName.equals("gem.obj")){
					count1 += 100;
				}else if (skinName.equals("battery.obj")){
					count2++;
					PLAYER_SPEED = 0.2f;
					hasBattery = true;
				}
				if(protClient != null)
					protClient.sendIncrementScore(count1);
				String collideMessage = list.get(j - 20);
				if(protClient != null)
					protClient.sendCollidedItem(j - 20);
				break;
			}
		}
	}

	public void initAudio(SceneManager sm){
		AudioResource resource1, resource2, resource3, resource4;
		AudioResource tempResource;
		Sound tempSound;
		musicList = Arrays.asList( "assets/sounds/BPM 80 008 C Mjr Techno.wav", "assets/sounds/BPM 80 010 A Mjr PickBass.wav"
								  ,"assets/sounds/FluteTone.wav" , "assets/sounds/Accordion.wav");
		authorList = Arrays.asList("coruscate","coruscate","Kushal Parikh","junggle");
		audioMgr = AudioManagerFactory.createAudioManager("ray.audio.joal.JOALAudioManager");
		if (!audioMgr.initialize()){
			System.out.println("Audio Manager failed to initialize!");
			return;
		}
		
		resource4 = audioMgr.createAudioResource("assets/sounds/KittenMeow.wav",AudioResourceType.AUDIO_SAMPLE);
		for(int i = 0; i < musicList.size(); i++){
			tempResource = audioMgr.createAudioResource(musicList.get(i),AudioResourceType.AUDIO_SAMPLE);
			radioResourceList.add(tempResource);
		}
		resource2 = audioMgr.createAudioResource("assets/sounds/RocketMono16.wav",AudioResourceType.AUDIO_SAMPLE);
		resource1 = audioMgr.createAudioResource("assets/sounds/ShootingStar.wav",AudioResourceType.AUDIO_SAMPLE);
		
		itemSound = new Sound(resource1,SoundType.SOUND_EFFECT, 10, false);
		rocketSound = new Sound(resource2,SoundType.SOUND_EFFECT, 4, true);
		for(int j = 0; j < radioResourceList.size(); j++){
			tempSound = new Sound(radioResourceList.get(j),SoundType.SOUND_EFFECT, 10, true);
			radioSoundList.add(tempSound);
			radioSoundList.get(j).initialize(audioMgr);
		}
		meowSound = new Sound(resource4,SoundType.SOUND_EFFECT, 10, false);

		itemSound.initialize(audioMgr);
		rocketSound.initialize(audioMgr);
		meowSound.initialize(audioMgr);
		itemSound.setMaxDistance(10.0f);
		itemSound.setMinDistance(0.1f);
		itemSound.setRollOff(5.0f);
		rocketSound.setMaxDistance(10.0f);
		rocketSound.setMinDistance(2.0f);
		rocketSound.setRollOff(8.0f);
		radioSoundList.get(0).setMaxDistance(10.0f);
		radioSoundList.get(0).setMinDistance(0.1f);
		radioSoundList.get(0).setRollOff(5.0f);
		meowSound.setMaxDistance(10.0f);
		meowSound.setMinDistance(0.1f);
		meowSound.setRollOff(5.0f);

		SceneNode dolphinN = sm.getSceneNode("dolphinNode");
		itemSound.setLocation(dolphinN.getWorldPosition());
		rocketSound.setLocation(dolphinN.getWorldPosition());
		meowSound.setLocation(dolphinN.getWorldPosition());
		setEarParameters(sm);
		rocketSound.play();
		radioSoundList.get(0).play();
	}

	public void setEarParameters(SceneManager sm){
		SceneNode cameraNode = sm.getSceneNode("MainCameraNode");
		Vector3 avDir = cameraNode.getWorldForwardAxis();
		//  note - should get the camera's forward direction     
		//     - avatar direction plus azimuth     
		audioMgr.getEar().setLocation(cameraNode.getWorldPosition());
		audioMgr.getEar().setOrientation(avDir, Vector3f.createFrom(0,1,0));  
	} 
	
	public float distance(SceneNode scene, SceneNode camera){
		Vector3f scenePos = (Vector3f)scene.getLocalPosition();
		Vector3f camPos = (Vector3f)camera.getLocalPosition();
		
		//camera vector x,y,z
		float	cx = (float)camPos.x(); 
		float	cy = (float)camPos.y();
		float	cz = (float)camPos.z();
		
		//scenenode vector x,y,z
		float	sx = (float)scenePos.x(); 
		float	sy = (float)scenePos.y();
		float	sz = (float)scenePos.z();
		
		float distance = (float)Math.sqrt(  (Math.pow((cx-sx),2)) +  (Math.pow((cy-sy),2)) + (Math.pow(cz-sz,2)) );
		return distance;
	}
	
	private float[] toFloatArray(double[] arr){
		if (arr == null) 
			return null;
		int n = arr.length;
		float[] R = new float[n];
		for (int i = 0; i < n; i++){
			R[i] = (float)arr[i];
		}
		return R;
    }
    
    private double[] toDoubleArray(float[] arr){
		if (arr == null) 
			return null;
		int n = arr.length;
		double[] R = new double[n];
		for (int i = 0; i < n; i++){
			R[i] = (double)arr[i];
		}
		return R;
    }
	
	public void incrementCounter(){
		counter++;
	}

	public Vector3 getPlayerPosition(){ 
		SceneNode dolphinN = this.getEngine().getSceneManager().getSceneNode("dolphinNode");
		return dolphinN.getWorldPosition();
	}
    
	public Matrix3 getPlayerRotation(){ 
		SceneNode dolphinN = this.getEngine().getSceneManager().getSceneNode("dolphinNode");
		return dolphinN.getWorldRotation();
	}

	private String staticIP() throws UnknownHostException {
		InetAddress.getLocalHost().getHostAddress();
	   	return  InetAddress.getLocalHost().getHostAddress();
   }

	public void setIsConnected(boolean b){
		isClientConnected = b;
	}

	protected void setupNetworking() {
		gameObjectsToRemove = new Vector<UUID>();
		isClientConnected = false;

    	try {
			protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
    	} catch (UnknownHostException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	if(protClient == null){
    		System.out.println("missing protocol host");
    	} else {
			protClient.sendJoinMessage();
    	}
	}

	public void processNetworking(float elapsTime) {
    	if (protClient != null){
            protClient.processPackets();
		}
		
		Iterator<UUID> iterator = gameObjectsToRemove.iterator();
		
        while(iterator.hasNext()){
			UUID temp = iterator.next();
			sm.destroyEntity(temp.toString());
			sm.destroySceneNode(temp.toString() + "Node");
		}
		
        gameObjectsToRemove.clear();
	}
	
	public void addGhostAvatarToGameWorld(GhostAvatar avatar) throws IOException {
		if (avatar != null){
			
			SceneNode dolphinN = this.getEngine().getSceneManager().getSceneNode("dolphinNode");
			SceneManager sm = dolphinN.getManager();

			TextureManager tm = this.getEngine().getTextureManager();
			Texture redTexture = tm.getAssetByPath(avatar.getSkin());
			RenderSystem rs = sm.getRenderSystem();
			TextureState state = (TextureState)
			rs.createRenderState(RenderState.Type.TEXTURE);
			state.setTexture(redTexture);
	
			Entity ghostE = sm.createEntity(avatar.getID().toString(), "pirate_cat3.obj");
			ghostE.setPrimitive(Primitive.TRIANGLES);
			ghostE.setRenderState(state);
			ghostN = sm.getRootSceneNode().createChildSceneNode(avatar.getID().toString() + "Node");
			ghostN.attachObject(ghostE);
			ghostN.scale(0.1f,0.1f,0.1f);
			avatar.setNode(ghostN);
			avatar.setEntity(ghostE);
			avatar.setPosition(ghostN.getLocalPosition());
		}
	}

	public void addGhostNPCtoGameworld(GhostNPC npc) throws IOException{
		SceneManager sm = getEngine().getSceneManager();
		TextureManager tm = this.getEngine().getTextureManager();
		Texture blueTexture = tm.getAssetByPath("Blue.jpeg");
		RenderSystem rs = sm.getRenderSystem();
		TextureState state = (TextureState)
		rs.createRenderState(RenderState.Type.TEXTURE);
		state.setTexture(blueTexture);

		Entity ghostNPC_E = sm.createEntity(String.valueOf(npc.getID()), "CargoRoboCat.obj");
		ghostNPC_N = sm.getRootSceneNode().createChildSceneNode(String.valueOf(npc.getID()) + "Node");
		ghostNPC_N.attachObject(ghostNPC_E);
		ghostNPC_N.scale(npc.getSize());
		npc.setNode(ghostNPC_N);
		npc.setEntity(ghostNPC_E);
		npc.setPosition(ghostNPC_N.getLocalPosition());
		npc.setSize(ghostNPC_N.getLocalScale());
	}

	public void addGhostItemToGameWorld(GameItem item) throws IOException{
		SceneManager sm = getEngine().getSceneManager();
		Entity ghostItem_E = sm.createEntity(String.valueOf(item.getID()), item.getSkin());
		ghostItem_N = sm.getRootSceneNode().createChildSceneNode(String.valueOf(item.getID()) + "Node");
		list.add(String.valueOf(item.getID()) + "Node");
		listSkins.add(item.getSkin());
		ghostItem_N.setLocalPosition(item.getPosition());
		ghostItem_N.attachObject(ghostItem_E);
		item.setNode(ghostItem_N);
		item.setEntity(ghostItem_E);
		ghostItem_N.scale(0.4f, 0.4f, 0.4f);
		
	}

	public void moveGhostAvatarAroundGameWorld(GhostAvatar avatar, Vector3 pos, Angle ang){
		avatar.getSceneNode().setLocalPosition(pos);
		avatar.getSceneNode().yaw(ang);
	}

	public void removeGhostAvatarFromGameWorld(GhostAvatar ghostID){
		if (ghostID != null){
			gameObjectsToRemove.add(ghostID.getID()); 
	    }
	}

	public void moveGhostNPCAroundGameWorld(GhostNPC npc, Vector3 pos, Vector3 siz){
		npc.getSceneNode().setLocalPosition(pos);
		npc.getSceneNode().setLocalScale(siz);
		npc.getSceneNode().lookAt(this.getEngine().getSceneManager().getSceneNode("dolphinNode"));
		
	}

	public void removeGhostNPCFromGameWorld(GhostAvatar ghostID){
		if (ghostID != null){
			gameObjectsToRemove.add(ghostID.getID());
	    }
	}

	public void updateItemPositions(GameItem item) throws IOException
	{
		this.getEngine().getSceneManager().destroyEntity(this.getEngine().getSceneManager().getEntity(String.valueOf(item.getID())));
		Entity newEntity = this.getEngine().getSceneManager().createEntity(String.valueOf(item.getID()), item.getSkin());
		item.getSceneNode().attachObject(newEntity);
		item.getSceneNode().setLocalPosition(item.getPosition());
		listSkins.remove(item.getID()-20);
		listSkins.add(item.getID()-20, item.getSkin());
		item.setEntity(newEntity);
		

	}

	public String getPlayerSkin(){
		return catThemeChooser;
	}

	public void winnerUpdate()
	{
		winStatus = "You are the winner!";
	}

	public void loserUpdate()
	{
		winStatus = "You lost...";
	}

	public void print(Object o){
		System.out.println(o);
	}

}
