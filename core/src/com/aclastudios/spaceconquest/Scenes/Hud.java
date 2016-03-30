package com.aclastudios.spaceconquest.Scenes;

import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport; //With the new viewport, when game world moves the hud stays the same

    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    private static Integer RedScore;
    private static Integer BlueScore;

    private Label countdownLabel;
    private Label GameLabel;
    private Label BlueLabel;
    private static Label BlueScoreLabel;
    private Label RedLabel;
    private static Label RedScoreLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        RedScore = 0;
        BlueScore = 0;
        float scale = (float) 0.75;

        viewport = new FitViewport(SpaceConquest.V_WIDTH,SpaceConquest.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table(); //To organise the label
        table.top(); //Top-Align table
        table.setBounds(0, SpaceConquest.V_HEIGHT * (float) 3 / 4, SpaceConquest.V_WIDTH, SpaceConquest.V_HEIGHT / 4);
        //table.setFillParent(true); //Table is the size of the stage

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
        GameLabel = new Label("SPACE CONQUEST", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
        RedLabel = new Label("RED", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
        RedScoreLabel = new Label(String.format("%06d", RedScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
        BlueLabel = new Label("BLUE", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
        BlueScoreLabel = new Label(String.format("%06d", BlueScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));


        BlueLabel.setFontScale(scale);
        RedLabel.setFontScale(scale);
        GameLabel.setFontScale(scale);
        countdownLabel.setFontScale(scale);
        RedScoreLabel.setFontScale(scale);
        BlueScoreLabel.setFontScale(scale);



        table.add(RedLabel).expandX().padTop(10);
        table.add(GameLabel).expandX().padTop(10);
        table.add(BlueLabel).expandX().padTop(10);
        table.row(); //new row
        table.add(RedScoreLabel).expandX();
        table.add(countdownLabel).expandX();
        table.add(BlueScoreLabel).expandX();


        stage.addActor(table);
    }
    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }
    public static void updatescore(int redScore, int blueScore){
        BlueScore=blueScore;
        BlueScoreLabel.setText(String.format("%06d", BlueScore));
        RedScore=redScore;
        RedScoreLabel.setText(String.format("%06d", RedScore));
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeUp() { return timeUp; }
}
