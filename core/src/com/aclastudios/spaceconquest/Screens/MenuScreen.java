package com.aclastudios.spaceconquest.Screens;

import com.aclastudios.spaceconquest.Helper.AssetLoader;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MenuScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private GameScreenManager gsm;
    private SpaceConquest game;

    private float BUTTON_WIDTH;
    private float BUTTON_HEIGHT;

    private SpriteBatch batch;
    private Texture background;
    private Sprite sprite;

    private TextButtonStyle style;
    private TextButton play;
    private TextButton login;
    private TextButton logout;
    private TextButton tutorial;
    private TextButton story;

    private Image mute;
    private Image unmute;

    private Music menuMusic;

    public MenuScreen(SpaceConquest game, GameScreenManager gsm){
        this.gsm = gsm;
        this.game = game;
        viewport = new FitViewport(SpaceConquest.V_WIDTH, SpaceConquest.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, (game).batch);

        BUTTON_WIDTH = 120;
        BUTTON_HEIGHT = 20;

        style = new TextButtonStyle();  //can customize
        style.font = new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"));
        style.font.setColor(Color.BLUE);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up= new TextureRegionDrawable(new TextureRegion(new Texture("basic/button_up.png")));
        style.down= new TextureRegionDrawable(new TextureRegion(new Texture("basic/button_down.png")));

//        style.unpressedOffsetX = 5f;
//        style.pressedOffsetX = style.unpressedOffsetX + 1f;
//        style.pressedOffsetY = -1f;

        play = new TextButton("Play Game", style);
        login = new TextButton("Login", style);
        logout = new TextButton("Logout", style);
        tutorial = new TextButton("Tutorial", style);
        story = new TextButton("Quick Game", style);

        mute = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("basic/sound_on.png"))));
        unmute = new Image(new TextureRegionDrawable(new TextureRegion(new Texture("basic/sound_off.png"))));

        // adding the music
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menuMusic/!in-game.mp3"));
        menuMusic.setLooping(false);
        menuMusic.play();


        System.out.println("constructor");
        show();
    }

    @Override
    public void show() {
        // The elements are displayed in the order you add them.
        // The first appear on top, the last at the bottom.
        if (AssetLoader.gameMusic != null) {
            AssetLoader.gameMusic.stop();
            AssetLoader.disposeSFX();
        }
        //AssetLoader.menuMusic.play();

        batch = new SpriteBatch();
        background = new Texture("basic/menu.png");

        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(background);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        play.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        play.setPosition(300, 90);
        stage.addActor(play);

        login.setSize(this.BUTTON_WIDTH / 3 * 2, this.BUTTON_HEIGHT);
        login.setPosition(300, 60);
        logout.setSize(this.BUTTON_WIDTH / 3 * 2, this.BUTTON_HEIGHT);
        logout.setPosition(300, 60);
//        if (game.playServices.getSignedInGPGS()) {
        if (true){
            stage.addActor(logout);
        } else {
            stage.addActor(login);
        }

        tutorial.setSize(this.BUTTON_WIDTH / 3 * 2, this.BUTTON_HEIGHT);
        tutorial.setPosition(300, 30);
        stage.addActor(tutorial);

        story.setSize(this.BUTTON_WIDTH / 3 * 2, this.BUTTON_HEIGHT);
        story.setPosition(300, 0);
        stage.addActor(story);

        mute.setPosition(0, 0);
        unmute.setPosition(0, 100);
        if (AssetLoader.VOLUME == 1) {
            stage.addActor(mute);
        } else {
            stage.addActor(unmute);
        }


        System.out.println("play");
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // AssetLoader.clickSound.play(AssetLoader.VOLUME);
                // Host multiplayer game
                if (game.playServices.getSignedInGPGS()) {
                    game.playServices.startQuickGame();
                    game.multiplayerSessionInfo.mState = game.multiplayerSessionInfo.ROOM_WAIT;
                    gsm.set(new WaitScreen(game, gsm));
                } else {
                    game.playServices.loginGPGS();
                }
               // gsm.set(new PlayScreen(game, gsm));
            }
        });

        login.addListener(new ClickListener() {
              @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playServices.loginGPGS();
                login.remove();
                stage.addActor(logout);
            }
        });

        logout.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.playServices.logoutGPGS();
                logout.remove();
                stage.addActor(login);
            }
        });

        tutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.multiplayerSessionInfo.mState = game.multiplayerSessionInfo.ROOM_LEADER;
                gsm.set(new LeadersBoardScreen(game, gsm));
                // TODO Set to tutorial screen
              //  gsm.set(new TutorialScreen(game, gsm));
            }
        });

        story.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AssetLoader.clickSound.play(AssetLoader.VOLUME);
                // TODO Set to tutorial screen
                //  gsm.set(new StoryScreen(game, gsm));
            }
        });

        mute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AssetLoader.muteSFX();
                mute.remove();
                stage.addActor(unmute);
            }

        });

        unmute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AssetLoader.unmuteSFX();
                AssetLoader.clickSound.play(AssetLoader.VOLUME);
                unmute.remove();
                stage.addActor(mute);
            }

        });


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sprite.draw(batch);
        batch.end();

        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
