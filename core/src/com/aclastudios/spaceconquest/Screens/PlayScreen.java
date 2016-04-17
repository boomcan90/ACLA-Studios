package com.aclastudios.spaceconquest.Screens;



import com.aclastudios.spaceconquest.Scenes.Hud;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.aclastudios.spaceconquest.Sprites.SideCharacter;
import com.aclastudios.spaceconquest.Sprites.MainCharacter;
import com.aclastudios.spaceconquest.Sprites.ResourceManager;
import com.aclastudios.spaceconquest.SupportThreads.Server;
import com.aclastudios.spaceconquest.Tools.B2WorldCreator;
import com.aclastudios.spaceconquest.Tools.HealthBar;
import com.aclastudios.spaceconquest.Tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.HashMap;

/*
**********************************declare number of players, first 3 players are team 1,
 */
public class PlayScreen implements Screen {

    private int userID;
    private String[] spriteName = {"PYRO", "DAACTAR"};
    private int numOfPlayers = 2;

    private SpaceConquest game;
    private TextureAtlas atlas;
    Texture mapTexture;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private float rateOfFire = (float) 0.3;
    private float coolDown;
//    private Array<FireBall> networkFireballs;

    private float x;
    private float y;
    private float width;
    private float height;

    private TmxMapLoader maploader; //Load the map into the game
    private TiledMap map; //Reference to the map
    private OrthogonalTiledMapRenderer renderer; //Renders the map to the screen

    private World world;
    private Box2DDebugRenderer b2dr; //graphic representation of the body in the box 2d

    //Touchpad
    private OrthographicCamera camera;
    private Stage stage;

    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin; //** images are used as skins of the button **//
    private Table table;
    private ImageButton button;
    private ImageButton jetpack_Button;
    private Label heading;

    private Touchpad touchpad;
    private TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private GameScreenManager gsm;
    //Sprites
    private MainCharacter mainCharacter;
    private HashMap<Integer,SideCharacter> enemyhashmap;
    private HashMap<Integer,String[]> positionvalues;
    private ResourceManager resourceManager;
    //Server
    Server server;
    private int time;

    private Texture red;
    private Texture health;
    private Texture orange;

    private Music music;

    public PlayScreen(SpaceConquest game, GameScreenManager gsm){
        atlas = new TextureAtlas("sprite.txt");
        this.game = game;
        this.gsm = gsm;
        this.userID=0;
        this.numOfPlayers = 2;
        this.time = 300;
        //uncomment this
        this.userID = game.multiplayerSessionInfo.mParticipantsId.indexOf(game.multiplayerSessionInfo.mId);
        numOfPlayers =  game.multiplayerSessionInfo.mParticipants.size();
        game.multiplayerSessionInfo.mId_num=this.userID;
        //Background and Character assets
        mapTexture = new Texture("map.png");
        //Game map and Game View
        //camera of the map
        gamecam  = new OrthographicCamera();
        //create a FitViewport to maintain virtual aspect ratio
        gamePort = new FitViewport(SpaceConquest.V_WIDTH/SpaceConquest.PPM,SpaceConquest.V_HEIGHT/SpaceConquest.PPM,gamecam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch,this);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("map-orthogonal.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,1/ SpaceConquest.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //Creating the box 2d world
        world = new World(new Vector2(0,0),true);
        b2dr = new Box2DDebugRenderer();

        //B2world
        new B2WorldCreator(this);

        //Sprites and Characters
        positionvalues = new HashMap<Integer, String[]>();
        enemyhashmap = new HashMap<Integer, SideCharacter>();
        mainCharacter = new MainCharacter(world,this,spriteName[userID/(numOfPlayers/2)]);
        for (int i = 0; i< numOfPlayers;i++) {
            if (i!=userID) {
                SideCharacter sideCharacter = new SideCharacter(world, this, i,spriteName[i/(numOfPlayers/2)]);
                enemyhashmap.put(i, sideCharacter);
            }
        }
        mainCharacter.setOriginCenter();


        //Initialize FireBalls Array
//        networkFireballs = new Array<FireBall>();
        coolDown = 0;

        //Initialize Spawn Area
        for (MapLayer layer : map.getLayers()) {
            if (layer.getName().matches("resourceSpawningArea")) {
                Array<RectangleMapObject> mo = layer.getObjects().getByType(RectangleMapObject.class);
                Rectangle rect = mo.get(0).getRectangle();
                this.x = rect.getX()/ SpaceConquest.PPM;
                this.y = rect.getY()/ SpaceConquest.PPM;
                this.width = rect.getWidth()/ SpaceConquest.PPM;
                this.height = rect.getHeight()/ SpaceConquest.PPM;
            }
        }
        //set world listener
        world.setContactListener(new WorldContactListener(this,game));

        // adding the music
        music = Gdx.audio.newMusic(Gdx.files.internal("menuMusic/In-game.mp3"));
        music.setLooping(false);
        music.play();

        resourceManager = new ResourceManager(this, game, userID, x, y, width, height);

        //touchpad setup
        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchpad/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchpad/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10/ SpaceConquest.PPM, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(0, 0, 70/SpaceConquest.PPM, 70/SpaceConquest.PPM);
        //touchpad.setScale(1 / SpaceConquest.PPM);

        buttonsAtlas = new TextureAtlas("button/button.pack");
        buttonSkin = new Skin(buttonsAtlas);

//        table = new Table(buttonSkin);
//        table.setBounds(50,50, 50, 50);
        red = new Texture(Gdx.files.internal("button_red.png"));
        orange = new Texture(Gdx.files.internal("button_orange.png"));
        health = new Texture(Gdx.files.internal("healthbar.png"));

        button = new ImageButton(new TextureRegionDrawable(new TextureRegion(red)), new TextureRegionDrawable(new TextureRegion(orange)));
        button.setBounds(0,0,40/ SpaceConquest.PPM,40/ SpaceConquest.PPM);
        jetpack_Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(orange)), new TextureRegionDrawable(new TextureRegion(red)));
        jetpack_Button.setBounds(0,0,40/ SpaceConquest.PPM,40/ SpaceConquest.PPM);
        //table.add(button);

        //Create a Stage and add TouchPad
        stage = new Stage(gamePort, game.batch);
        stage.addActor(touchpad);
        stage.addActor(button);
        stage.addActor(jetpack_Button);
        Gdx.input.setInputProcessor(stage);

        //Setscreen in androidLauncher
        //uncomment this
        game.playServices.setScreen(this);
        if (this.userID==0){
            server=new Server(game);
        }
        show();
    }
    @Override
    public void show() {
        if (userID==0) {
            //  while ((resourceManager.getIron_count() + resourceManager.getGunpowder_count() + resourceManager.getOil_count()) < 21)
            resourceManager.generateResources();
        }
        System.out.println("SHOW CALLED");
    }


    public void handleInput(float dt){
        coolDown +=dt;
        if (button.isPressed() && coolDown >rateOfFire && mainCharacter.getAmmunition()>0) {
            coolDown = 0;
            //start of fire ball
            System.out.println("I am firing");
//            game.playServices.BroadcastMessage("fire:" + userID + ":" + mainCharacter.b2body.getPosition().x + ":"
//                    + mainCharacter.b2body.getPosition().y + ":" + (mainCharacter.getLastXPercent()*(mainCharacter.getRadius()+1)) + ":" +
//                    (mainCharacter.getLastYPercent()*(mainCharacter.getRadius()+1)));
            //end of fireball

            mainCharacter.b2body.applyLinearImpulse(new Vector2((float) (mainCharacter.b2body.getLinearVelocity().x *-1),
                    (float) (mainCharacter.b2body.getLinearVelocity().y * -1)), mainCharacter.b2body.getWorldCenter(), true);
            mainCharacter.fire();
        }
        else {
            double speedreduction = Math.pow(0.9, mainCharacter.getAdditionalWeight()*0.4);
            if(jetpack_Button.isPressed() && mainCharacter.getJetpack_time()>0.05){
                mainCharacter.exhaustJetPack(dt);
                speedreduction = 3;
            }
            else {
                //friction
                mainCharacter.b2body.applyLinearImpulse(new Vector2((float) (mainCharacter.b2body.getLinearVelocity().x * -0.03),
                        (float) (mainCharacter.b2body.getLinearVelocity().y * -0.03)), mainCharacter.b2body.getWorldCenter(), true);
            }
            mainCharacter.setxSpeedPercent((float) (touchpad.getKnobPercentX()));
            mainCharacter.setySpeedPercent((float) (touchpad.getKnobPercentY()));
            mainCharacter.b2body.applyLinearImpulse(new Vector2((float)(mainCharacter.getxSpeedPercent() *2* speedreduction),
                    (float)(mainCharacter.getySpeedPercent() * 2 * speedreduction)), mainCharacter.b2body.getWorldCenter(), true);
        }

    }

    public void update(float dt){
//        if (!game.playServices.checkhost()){
//            game.playServices.leaveRoom();
//            game.multiplayerSessionInfo.mState = game.multiplayerSessionInfo.ROOM_NULL;
//            dispose();
//            gsm.set(new MenuScreen(game, gsm));
//        }
        //input updates
        handleInput(dt);

        //Allows box2d calculate the physics
        world.step(1 / 60f, 6, 2);

        float[] gadget = mainCharacter.getGadgetInfo();
        //hud timer
        hud.update(dt, (int) gadget[0], gadget[1]);
        //stopping the character
        slowDownCharacter();
        //sprites
        mainCharacter.update(dt);
        //SideCharacter update
        for (int i: enemyhashmap.keySet()){
            SideCharacter sideCharacter = enemyhashmap.get(i);
            if (positionvalues!= null) {
                try{
                    String[] values = positionvalues.get(i);
                    if (values!=null) {
                        sideCharacter.updateEnemy(Float.parseFloat(values[1]),
                                Float.parseFloat(values[2]),
                                Float.parseFloat(values[3]),
                                Float.parseFloat(values[5]),
                                Float.parseFloat(values[7]),
                                Float.parseFloat(values[8]),
                                Integer.parseInt(values[9]),
                                Float.parseFloat(values[10]),
                                Float.parseFloat(values[11]));
//                  sideCharacter.setRotation(Float.parseFloat(values[3]));
                        if (values[4].equals("false")) {
                            sideCharacter.dead();
                        }
                    }
                    sideCharacter.b2body.setLinearVelocity(Float.parseFloat(values[7]),Float.parseFloat(values[8]));
                }catch (Exception e){
                    System.out.println("error while updating character coordinate");
                    e.printStackTrace();
                }
            }
            sideCharacter.update(dt);
        }

        //check if fireballs is destroyed or not
//        synchronized (networkFireballs) {
//            try {
//                for (FireBall ball : networkFireballs) {
//                    ball.update(dt);
//                    if (ball.isDestroyed())
//                        networkFireballs.removeValue(ball, true);
//                }
//            }catch (Exception e){
//                System.out.println("error here");
//            }
//        }

        resourceManager.updateIron(dt);
        resourceManager.updateGunPowder(dt);
        resourceManager.updateOil(dt);

        float x=0,y=0;
        if(!mainCharacter.isDestroyed()) {
            x = mainCharacter.b2body.getPosition().x;
            y = mainCharacter.b2body.getPosition().y;
            gamecam.position.x = x;
            gamecam.position.y = y;
        }else {
            gamecam.position.x = mainCharacter.getLast_xy_coord()[0];
            gamecam.position.y = mainCharacter.getLast_xy_coord()[1];
        }


        //SendMessage
        try {
            System.out.println("sending character's coordinate");
            game.playServices.BroadcastUnreliableMessage(userID + ":" + x + ":" + y + ":" + mainCharacter.getAngle() + ":" +
                    String.valueOf(!mainCharacter.isDestroyed()) + ":" + mainCharacter.getAdditionalWeight() + ":" + mainCharacter.getHP() +
                    ":" + mainCharacter.getLastXPercent() + ":" + mainCharacter.getLastYPercent() + ":" +
                    mainCharacter.getFireCount() + ":" +mainCharacter.b2body.getLinearVelocity().x +
                    ":"+mainCharacter.b2body.getLinearVelocity().y);
            System.out.println("finished sending character's coordinate");

        }catch (Exception e){
            System.out.println("error while sending message");
            e.printStackTrace();
        }

        //gamecam updates
        gamecam.update();
        renderer.setView(gamecam); //render only what the gamecam can see

        button.setPosition(gamecam.position.x + gamePort.getWorldWidth() / 4 + 40 / SpaceConquest.PPM, gamecam.position.y - gamePort.getWorldHeight() / 2 + 10 / SpaceConquest.PPM);
        jetpack_Button.setPosition(gamecam.position.x+gamePort.getWorldWidth() / 4 ,gamecam.position.y-gamePort.getWorldHeight()/2+10/ SpaceConquest.PPM);
        touchpad.setPosition((gamecam.position.x-(gamePort.getWorldWidth() / 2)),
                (gamecam.position.y-gamePort.getWorldHeight()/2));
//        touchpad.setPosition((gamecam.position.x-(gamePort.getWorldWidth() / 2))+(10/ SpaceConquest.PPM),
//                (gamecam.position.y-gamePort.getWorldHeight()/2)+(10/ SpaceConquest.PPM));

    }
    //render
    @Override
    public void render(float delta) {
        try {
            if (hud.isTimeUp() == true) {
                int len = game.multiplayerSessionInfo.mParticipants.size();
                int myId = game.multiplayerSessionInfo.mId_num;
                String myName = game.multiplayerSessionInfo.mName;
                int redScore = hud.getRedScore();
                int blueScore = hud.getBlueScore();
                int mykillScore = hud.getkills();
                System.out.println("hud istimeup");
                game.playServices.leaveRoom();
                game.multiplayerSessionInfo.mState = game.multiplayerSessionInfo.ROOM_NULL;
                game.playServices.submitScoreGPGS(hud.getkills());
                System.out.println("close gps");
                gsm.set(new GameOver(game, gsm, len, myId, myName, redScore, blueScore, mykillScore));
                dispose();
            }

            //make sure that everything is updated
            update(delta);

            //clear screen
            Gdx.gl.glClearColor(0, 0, 0, 1); //clear colour
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear the screen
            //backgroup and character image (Used for test)
            game.batch.setProjectionMatrix(gamecam.combined); //only render what the camera can see

            //render the map
            renderer.render();
            game.batch.setProjectionMatrix(gamecam.combined);
            game.batch.begin(); //opens the "box"
            game.batch.draw(mapTexture, 0, 0, (mapTexture.getWidth() * SpaceConquest.MAP_SCALE)/ SpaceConquest.PPM,
                    (mapTexture.getHeight() * SpaceConquest.MAP_SCALE)/ SpaceConquest.PPM);

            mainCharacter.draw(game.batch);

            //Side Characters
            for (int i: enemyhashmap.keySet()) {
                SideCharacter sideCharacter = enemyhashmap.get(i);
                try {
                    if (positionvalues != null) {
                        if (positionvalues.get(i)[6] != null) {
                            if (!sideCharacter.isDestroyed()) {
                                sideCharacter.draw(game.batch);
                                //sideCharacter healthbar
                                HealthBar SC_healthBar = new HealthBar(new TextureRegion(health), sideCharacter);
                                float SC_hp = Float.parseFloat(positionvalues.get(i)[6]);
                                SC_healthBar.setWidth(SC_hp);
                                SC_healthBar.draw(game.batch, SC_hp);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("error updating enemies coordinate");
                }
            }
            for (int i = 0; i < resourceManager.getIron_count(); i++)
                resourceManager.getIron_array(i).draw(game.batch);
            for (int i = 0; i < resourceManager.getGunpowder_count(); i++)
                resourceManager.getGunpowder_array(i).draw(game.batch);
            for (int i = 0; i < resourceManager.getOil_count(); i++)
                resourceManager.getOil_array(i).draw(game.batch);

            mainCharacter.draw(game.batch);

            //render the fireballs over the network
//            for (FireBall ball : networkFireballs)
//                ball.draw(game.batch);

            //maincharacter healthbay
            HealthBar healthBar = new HealthBar(new TextureRegion(health),mainCharacter);
            float hp = mainCharacter.getHP();
            healthBar.setWidth(hp);
            healthBar.draw(game.batch, hp);
            game.batch.end(); //close the "box" and draw it on the screen


            //render our Box2DDebugLines
            b2dr.render(world, gamecam.combined);

            //Join/Combine hud camera to game batch
            game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();

            //Draw the touch pad and buttons
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();


//            if (hud.isTimeUp() == true) {
//                gsm.set(new GameOver(game, gsm));
//            }
            if (userID==0){
                System.out.println("updating time");
                game.playServices.BroadcastUnreliableMessage("Time:" + hud.getTime());
            } else {
                hud.setTime(time);
            }

        }catch (Exception e){
            System.out.println("error in render");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void resize(int width, int height) {

        System.out.println("updating");
        gamePort.update(width, height);
    }


    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
    public TextureAtlas getAtlas() {
        return atlas;
    }

    private void slowDownCharacter() {
        if(!touchpad.isTouched()) {
            mainCharacter.b2body.applyLinearImpulse(new Vector2((float) (mainCharacter.b2body.getLinearVelocity().x * -0.1), (float) (mainCharacter.b2body.getLinearVelocity().y * -0.1)), mainCharacter.b2body.getWorldCenter(), true);
        }
    }


//    public void depositResource(){
//        int res = mainCharacter.getAdditionalWeight();
//        mainCharacter.depositResource();
//        return res;
//    }

    public void MessageListener(byte[] bytes){

        try {
            String message = new String (Arrays.copyOfRange(bytes, 0, bytes.length),"UTF-8");
            final String[] data = message.split(":");
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    // Your crashing code here

            if (data[0].equals("0") || data[0].equals("1") || data[0].equals("2")|| data[0].equals("3")|| data[0].equals("4")|| data[0].equals("5")) {
                System.out.println("received enemies's coordinate");
                String[] position = data.clone();
                positionvalues.put(Integer.parseInt(data[0]), position);
                System.out.println("finished updating ");
            }
            else if (data[0].equals("Serverpoints") && userID==0){
                System.out.println("received points update from other players");
                addscore(data[1], Integer.parseInt(data[2]));
                System.out.println(data[0]+":"+data[1]+":"+data[2]);
            }
            else if (data[0].equals("UpdateScoreAll")){
                System.out.println("received point update from server");
                Hud.updatescore(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
            }
//            else if (data[0].equals("fire")){
//                FireBall f = new FireBall(this, Float.parseFloat(data[2]),
//                        Float.parseFloat(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]),true);
//                networkFireballs.add(f);
//            }
            else if (data[0].equals("Time")){
                System.out.println("received time update");
                time = Integer.parseInt(data[1]);
            }
            else if (data[0].equals("Resources")){
                System.out.println("Data 1:"+data[1]);
                resourceManager.getResourceString(data[1]);
                resourceManager.generateResources();
            }
            else if (data[0].equals("Delete")){
                System.out.println("delete resource"+data[2]+" "+data[3]);
                if (data[1].equals("Iron"))
                    resourceManager.delIron(Integer.parseInt(data[2]), Float.parseFloat(data[3]));
                else if (data[1].equals("GunPowder"))
                    resourceManager.delGunPowder(Integer.parseInt(data[2]), Float.parseFloat(data[3]));
                else if (data[1].equals("Oil"))
                    resourceManager.delOil(Integer.parseInt(data[2]), Float.parseFloat(data[3]));
            }
            else if (data[0].equals("Generate")) {
                System.out.println("generate resources" + data[1] +" "+data[2] + " "+data[3]);
                try {
                    if (data[1].equals("Iron")) {
                        System.out.println("generate iron");
                        resourceManager.addIron(Float.parseFloat(data[2]), Float.parseFloat(data[3]));
                    }
                    else if (data[1].equals("GunPowder")) {
                        System.out.println("generate gunpowder");
                        resourceManager.addGunPowder(Float.parseFloat(data[2]), Float.parseFloat(data[3]));
                    }
                    else if (data[1].equals("Oil")) {
                        System.out.println("generate oil");
                        resourceManager.addOil(Float.parseFloat(data[2]), Float.parseFloat(data[3]));
                    }
                }
                catch (Exception e){
                    System.out.println("resource crash");
                    e.printStackTrace();
                }
            }
            else if (data[0].equals("KillBonus")){
                hud.addkill();
            }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error in receiving message");
        }
    }
    public void addscore(String id,int data){
        int num = Integer.parseInt(id);
        if (num<numOfPlayers/2){
            server.addRedScore(data);
        } else {
            server.addBlueScore(data);
        }
    }


    public int getNumOfPlayers() {
        return numOfPlayers;
    }
    public int getUserID() {
        return userID;
    }

    public float getRateOfFire() {
        return rateOfFire;
    }
}

