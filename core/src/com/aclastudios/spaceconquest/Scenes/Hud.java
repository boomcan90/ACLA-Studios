package com.aclastudios.spaceconquest.Scenes;

import com.aclastudios.spaceconquest.SpaceConquest;
import com.aclastudios.spaceconquest.Sprites.InteractiveTileObject;
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

    // New hud integers
    private static Integer oilScore;
    private static Integer gunpowderScore;
    private static Integer ironScore;
    private static Integer teamKnapsack;

    private Label countdownLabel;
    private Label GameLabel;
    private Label BlueLabel;
    private Label time;
    private static Label BlueScoreLabel;
    private Label RedLabel;
    private static Label RedScoreLabel;
    private boolean backuphud = false;
    float smallScale = (float) 0.75;
    float largeScale = (float) 1.25;
    private Integer width;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        RedScore = 0;
        BlueScore = 0;
        teamKnapsack = 0;
        oilScore = 0;
        gunpowderScore = 0;
        ironScore = 0;
        width = 30;

        viewport = new FitViewport(SpaceConquest.V_WIDTH,SpaceConquest.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table(); //To organise the label
        table.top(); //Top-Align table
        table.setBounds(0, SpaceConquest.V_HEIGHT * (float) 3 / 4, SpaceConquest.V_WIDTH, SpaceConquest.V_HEIGHT / 4);
        //table.setFillParent(true); //Table is the size of the stage




        // backup HUD

        if (backuphud){
            countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
            GameLabel = new Label("SPACE CONQUEST", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
            RedLabel = new Label("RED", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
            RedScoreLabel = new Label(String.format("%06d", RedScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
            BlueLabel = new Label("BLUE", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
            BlueScoreLabel = new Label(String.format("%06d", BlueScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));

            BlueLabel.setFontScale(smallScale);
            RedLabel.setFontScale(smallScale);
            GameLabel.setFontScale(smallScale);
            countdownLabel.setFontScale(smallScale);
            RedScoreLabel.setFontScale(smallScale);
            BlueScoreLabel.setFontScale(smallScale);

            table.add(RedLabel).expandX().padTop(10);
            table.add(GameLabel).expandX().padTop(10);
            table.add(BlueLabel).expandX().padTop(10);
            table.row(); //new row
            table.add(RedScoreLabel).expandX();
            table.add(countdownLabel).expandX();
            table.add(BlueScoreLabel).expandX();
        } else {

            // Personal Knapsack
            Label oil = new Label(String.format("oil: %2d gp: %2d iron: %2d", oilScore, gunpowderScore, ironScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            Label oilLabel = new Label(String.format("%3d", oilScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            Label gunpowder = new Label("gp: ", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            Label gunPowderLabel = new Label(String.format("%3d", gunpowderScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            Label iron = new Label("iron: ", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            Label ironLabel = new Label(String.format("%3d", ironScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));

            // Team Scores
            BlueScoreLabel = new Label(String.format("%03d | %03d", BlueScore, RedScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            RedScoreLabel = new Label(String.format("%03d", RedScore), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            Label divider = new Label(" | ", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));

            // Global data
            time  =new Label(String.format("time: %3d \nKnapsack: %3d", worldTimer, teamKnapsack), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
            Label knapsack = new Label(String.format("Knapsack: %3d", teamKnapsack), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));
//            Label knapsackLabel = new Label(String.format("%03d", teamKnapsack), new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/visitor.fnt"), false), Color.WHITE));


            // scalings
            oil.setFontScale(smallScale/2);
//            oilLabel.setFontScale(smallScale);
//            iron.setFontScale(smallScale);
//            ironLabel.setFontScale(smallScale);
//            gunpowder.setFontScale(smallScale);
//            gunPowderLabel.setFontScale(smallScale);
            BlueScoreLabel.setFontScale(largeScale);
//            divider.setFontScale(largeScale);
//            RedScoreLabel.setFontScale(largeScale);
            time.setFontScale(smallScale/2);
//            countdownLabel.setFontScale(smallScale);
//            knapsack.setFontScale(smallScale);
//            knapsackLabel.setFontScale(smallScale);


            // adding to table
            table.add(oil).width(width / 3).left().pad(0,10,0,60).expandX();
//            table.add(oilLabel).width(width / 3);
//            table.add(gunpowder).width(width/3).padLeft((float) 35);
//            table.add(gunPowderLabel).width(width / 3);
//            table.add(iron).width(width/2).padLeft((float) 35);
//            table.add(ironLabel).width(width/3);

            table.add(BlueScoreLabel).expandX();
//            table.add(divider);
//            table.add(RedScoreLabel).padRight((float) 30);

            table.add(time).width(width).right().padRight(50).expandX();
//            table.add(countdownLabel).width(width);







        }





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
            if (backuphud) {
                countdownLabel.setText(String.format("%03d", worldTimer));
            } else {
                time.setText(String.format("time: %3d \nKnapsack: %3d", worldTimer, teamKnapsack));
            }
            timeCount = 0;
        }
    }
    public static void updatescore(int redScore, int blueScore){
        BlueScore=blueScore;
        RedScore=redScore;
        BlueScoreLabel.setText(String.format("%03d | %03d", RedScore, BlueScore));
//        RedScoreLabel.setText(String.format("%06d", RedScore));
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

    public static void updateknapscore(int inp) {
        teamKnapsack = inp;

    }

    public boolean isTimeUp() { return timeUp; }
}
