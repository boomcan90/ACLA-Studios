package com.aclastudios.spaceconquest.Screens;

import com.aclastudios.spaceconquest.Scenes.Hud;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



public class PlayScreen implements Screen {
    private SpaceConquest game;
    Texture texture;
    Texture spaceman;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader maploader; //Load the map into the game
    private TiledMap map; //Reference to the map
    private OrthogonalTiledMapRenderer renderer; //Renders the map to the screen

    private World world;
    private Box2DDebugRenderer b2dr; //graphic representation of the body in the box 2d

    //Touchpad
    private OrthographicCamera camera;
    private Stage stage;
    private Touchpad touchpad;
    private TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private Texture blockTexture;
    private Sprite blockSprite;
    private float blockSpeed;

    public PlayScreen(SpaceConquest game){
        this.game = game;
        texture = new Texture("planet.png");
        spaceman = new Texture("astronaut.png");


        gamecam  = new OrthographicCamera(); //camera of the map

        //create a FitViewport to maintain virtual aspect ratio
        gamePort = new FitViewport(SpaceConquest.V_WIDTH,SpaceConquest.V_HEIGHT,gamecam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);



        //touchpad
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
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 50, 50);


        //Create a Stage and add TouchPad
        stage = new Stage(gamePort, game.batch);
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void show() {

    }

    public void handleInput(float dt){
//        if (Gdx.input.isTouched()){
//            gamecam.position.x += 100*dt; //testing
//        }
    }

    public void update(float dt){
        handleInput(dt);

        gamecam.update();
        renderer.setView(gamecam); //render only what the gamecam can see

        //touchpad
        gamecam.position.x+=touchpad.getKnobPercentX()*2;
        gamecam.position.y+=touchpad.getKnobPercentY()*2;
        touchpad.setPosition(gamecam.position.x-gamePort.getWorldWidth() / 2+10,gamecam.position.y-gamePort.getWorldHeight()/2+10);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1); //clear colour
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear the screen
        renderer.render();

        //temp backgroup
        game.batch.setProjectionMatrix(gamecam.combined); //only render what the camera can see
        game.batch.begin(); //opens the "box"
        game.batch.draw(texture, 0, 0);
        game.batch.draw(spaceman, gamecam.position.x-20, gamecam.position.y-20,50,50);
        game.batch.end(); //close the "box" and draw it on the screen


        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        //touch pad
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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

    }
}
