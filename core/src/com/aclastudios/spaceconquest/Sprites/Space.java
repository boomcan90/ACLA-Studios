package com.aclastudios.spaceconquest.Sprites;

import com.aclastudios.spaceconquest.Scenes.Hud;
import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
/**
 * Created by Lakshita on 3/3/2016.
 */
public class Space extends Sprite {
    public enum State {BEGIN, DEAD}

    ;
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private Animation marioRun;
    private TextureRegion marioJump;
    private TextureRegion marioDead;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation bigMarioRun;
    private Animation growMario;

    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead;
    private boolean marioStart;
    private PlayScreen screen;

    // private Array<FireBall> fireballs;

    public Space(PlayScreen screen) {
        //initialize default values
        //initialize default values
        this.screen = screen;
//        this.world = screen.getWorld();
        currentState = State.BEGIN;
        previousState = State.BEGIN;
        stateTimer = 0;
        runningRight = true;

//        Array<TextureRegion> frames = new Array<TextureRegion>();
//
//        //get run animation frames and add them to marioRun Animation
//        for(int i = 1; i < 4; i++)
//            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
//        marioRun = new Animation(0.1f, frames);

     //   frames.clear();

//        for(int i = 1; i < 4; i++)
//            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
//        bigMarioRun = new Animation(0.1f, frames);
//
//        frames.clear();
//
//        //get set animation frames from growing mario
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
//        growMario = new Animation(0.2f, frames);
//
//
//        //get jump animation frames and add them to marioJump Animation
//        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
//        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
//
//        //create texture region for mario standing
//        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
//        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
//
//        //create dead mario texture region
//        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);
//
//        //define mario in Box2d
//        defineMario();
//
//        //set initial values for marios location, width and height. And initial frame as marioStand.
//        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
//        setRegion(marioStand);
//
//        fireballs = new Array<FireBall>();
    }

    public void update(float dt) {
//        if (Hud.timeStart()==0){
//            begin();
//        }
        // time is up : too late mario dies T_T
        // the !isDead() method is used to prevent multiple invocation
        // of "die music" and jumping
        // there is probably better ways to do that but it works for now.
//        if (Hud.isTimeUp()) {
//            currentState = State.DEAD;
//        }


    }

    public TextureRegion getFrame(float dt) {

        currentState = getState();
        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){
            case DEAD:
                region = marioDead;
                break;

            default:
                region = marioIsBig ? bigMarioStand : marioStand;
                break;
        }

        //if mario is running left and the texture isnt facing left... flip it.
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        //if mario is running right and the texture isnt facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    public State getState() {
        if(marioIsDead){
            System.out.println("mario dead");
            return State.DEAD;
        }

        else return null;
    }

    public void grow() {

    }

    public void die() {
        System.out.println(isDead()+"isdead");
        if (!isDead()){
            marioIsDead = true;
        }
    }
//    public void begin() {
//        if (!isAlive())
//            marioStart = true;
//        else
//            marioStart = false;
//    }

//    public boolean isAlive() {
//        return marioStart;
//    }
    public boolean isDead() {
        return marioIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public boolean isBig() {
        return marioIsBig;
    }

    public void jump() {

    }


}