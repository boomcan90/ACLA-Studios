package com.aclastudios.spaceconquest.Screens;

import com.aclastudios.spaceconquest.PlayGameService.MultiplayerSessionInfo;
import com.aclastudios.spaceconquest.Scenes.Hud;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.aclastudios.spaceconquest.Sprites.Space;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.w3c.dom.Text;

/**
 * Created by Lakshita on 3/2/2016.
 */
public class GameOver implements Screen {
    private Viewport viewport;
    private Stage stage;
    private GameScreenManager gsm;
    private SpaceConquest game;
    private MultiplayerSessionInfo session;
    private Hud hud;
    private Texture score;

    private float BUTTON_WIDTH;
    private float BUTTON_HEIGHT;

    private SpriteBatch batch;
    private Texture background;
    private Sprite sprite;

    private Label.LabelStyle style;
    private Label winLosetext;
    private Label playerScore;
    private Label myScore;
    private Label oppScore;

    private TextButton.TextButtonStyle textbtnstyle;
    private TextButton playAgn;
    private TextButton exit;

    public GameOver(SpaceConquest game, GameScreenManager gsm, int len, int myId, String myName, int redScore, int blueScore, int mykillScore){
        this.game = game;
        this.gsm = gsm;
        viewport = new FitViewport(SpaceConquest.V_WIDTH, SpaceConquest.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        score = new Texture("basic/menu.png");

        score.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(score);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        style = new Label.LabelStyle();
        style.font = new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"));
        style.font.setColor(Color.BLUE);
        style.font.getData().setScale(0.65f, 0.65f);

        textbtnstyle = new TextButton.TextButtonStyle();  //can customize
        textbtnstyle.font = new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"));
        textbtnstyle.font.setColor(Color.BLUE);
        textbtnstyle.font.getData().setScale(0.65f, 0.65f);
        textbtnstyle.up= new TextureRegionDrawable(new TextureRegion(new Texture("basic/button_up.png")));
        textbtnstyle.down= new TextureRegionDrawable(new TextureRegion(new Texture("basic/button_down.png")));

        String myTeam;
        String winLose = "";
        if (myId<len/2)
            myTeam = "RED";
        else
            myTeam = "BLUE";
        if (redScore>blueScore)
            if (myId<len/2)
                winLose = "Congrats Red Team, you WON!!!";
        else if (blueScore>redScore)
                if (myId>=len/2)
                    winLose = "Congrats Blue Team, you WON!!!";
        else
                winLose = "Sorry, you LOST";

        winLosetext = new Label(winLose, style);
        playerScore = new Label(myName+" KILL SCORE: "+mykillScore, style);
        if (myTeam.equals("RED")){
            myScore = new Label("MY TEAM: RED: "+redScore, style);
            oppScore = new Label("ENEMY TEAM: BLUE: "+blueScore, style);
        }
        else{
            myScore = new Label("MY TEAM: BLUE: "+blueScore, style);
            oppScore = new Label("ENEMY TEAM: RED: "+redScore, style);
        }

        playAgn = new TextButton("Play Again", textbtnstyle);
        exit = new TextButton("Exit", textbtnstyle);
        System.out.println("gameover");
        show();
    }

    @Override
    public void show() {

        System.out.println("add actors");
        winLosetext.setSize(this.BUTTON_WIDTH * 2, this.BUTTON_HEIGHT);
        winLosetext.setPosition(50, 600);
        stage.addActor(winLosetext);
        playerScore.setSize(this.BUTTON_WIDTH * 2, this.BUTTON_HEIGHT);
        playerScore.setPosition(50, 500);
        stage.addActor(playerScore);
        myScore.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        myScore.setPosition(50, 400);
        stage.addActor(myScore);
        oppScore.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        oppScore.setPosition(50, 300);
        stage.addActor(oppScore);

        playAgn.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        playAgn.setPosition(50, 100);
        playAgn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new WaitScreen(game, gsm));
                stage.addActor(playAgn);
            }
        });

        exit.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        exit.setPosition(50, 50);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(playAgn);
                gsm.pop();
            }
        });
        System.out.println("add listeners");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("batch");
        batch.begin();
        sprite.draw(batch);
        batch.end();
        System.out.println("stage");
        stage.draw();
        stage.act(delta);

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
