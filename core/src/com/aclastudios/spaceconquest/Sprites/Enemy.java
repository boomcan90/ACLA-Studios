package com.aclastudios.spaceconquest.Sprites;

import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by User on 17/3/2016.
 */
public class Enemy extends Sprite{
    //private float xSpeed,ySpeed;
    public final String[] area = {"Team1Spawn","Team2Spawn"};
    private float x, y;
    private float angle;
    private int enemyID;
    public World world;
    TiledMap map;
    public Body b2body;
    protected Fixture fixture;
    private TextureRegion character;
    float spawnX;
    float spawnY;
    private float stateTime;
    private boolean setToDestroy;
    private boolean destroyed;
    private float deathCount;
    private enum State { STANDING, RUNNING };
    private State currentState;
    private State previousState;
    private Animation running;
    private float stateTimer;
    private float x_value;
    private float y_value;
    private float last_x_coord;
    private float weight;
    private float radius = 13;
    private float scale = (float) (1.0/10);

    //private int charWeight;
    //private int charScore;
    public Enemy(World world, PlayScreen screen,int ID){
        super(screen.getAtlas().findRegion("KID"));
        this.world = world;
        this.enemyID = ID;
        map =screen.getMap();
        defineCharacter();
//        character = new TextureRegion(getTexture(),338,26,16,24);
//        character = new TextureRegion(getTexture(), getRegionX() + 195, getRegionY(), 200, 200);
//        //setBounds(0, 0, 16, 24);
//        setBounds(0, 0, 25, 25);
        character = new TextureRegion(getTexture(), getRegionX() + 190, getRegionY(), 170, 190);
        setBounds(0, 0, 25, 25);
        setRegion(character);
        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        deathCount = 0;
        x=0;
        y=0;
        angle=0;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        // animation for walking
        frames.add(new TextureRegion(getTexture(), getRegionX(), getRegionY(), 168, 190));
        frames.add(new TextureRegion(getTexture(), getRegionX() + 195 , getRegionY(), 168, 190));
        frames.add(new TextureRegion(getTexture(), getRegionX() + 195*2, getRegionY(), 168, 190));
        frames.add(new TextureRegion(getTexture(), getRegionX() + 195 * 3, getRegionY(), 168, 190));
        running =new Animation(0.2f, frames);
        setOriginCenter();
        defineCharacter();

    }

    public void defineCharacter(){
        BodyDef bdef = new BodyDef();
        for (MapLayer layer : map.getLayers()) {
            if (layer.getName().matches(area[enemyID])) {
                Array<RectangleMapObject> mo = layer.getObjects().getByType(RectangleMapObject.class);
                Rectangle rect = mo.get(0).getRectangle();
                spawnX = rect.getX()*SpaceConquest.MAP_SCALE;
                spawnY = rect.getY()*SpaceConquest.MAP_SCALE;
                bdef.position.set(spawnX, spawnY); //temp set position

            }
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        //xSpeed = 0;
        //ySpeed = 0;

        //Collision Bit
        fdef.filter.categoryBits = SpaceConquest.CHARACTER_BIT; //what category is this fixture
        fdef.filter.maskBits = SpaceConquest.OBSTACLE_BIT
                | SpaceConquest.IRON_BIT
                |SpaceConquest.STATION_BIT
                |SpaceConquest.OBJECT_BIT
                |SpaceConquest.MAIN_CHARACTER_BIT
                |SpaceConquest.FIREBALL_BIT; //What can the character collide with?

        //Body
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

//        fixture = b2body.createFixture(fdef);
    }

    @Override
    public void draw(Batch batch) {
        if(!this.isDestroyed())
             super.draw(batch);
    }

    public void update(float dt) {
        stateTime += dt;
        if (setToDestroy ) {
            System.out.println("destroying");
            world.destroyBody(b2body);
            destroyed = true;
            setToDestroy = false;
            stateTime = 0;
            deathCount+=1;
        }
        if(destroyed){
            //if(stateTime>(deathCount*1.5)){
                stateTime = 0;
                destroyed = false;
                defineCharacter();
            //}
        }else {
            b2body.setTransform(this.x, this.y,angle);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            //System.out.println("My weight is " + charWeight);
            setRegion(getFrame(dt));
        }
    }
    public float getCharacterScale() {

        return ((float)1+ (weight*scale));
    }
    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case RUNNING:
                region = running.getKeyFrame(stateTimer, true);
                break;
            default:
                region = character;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        System.out.println("Enemy X: " + b2body.getLinearVelocity().x);
        System.out.println("Enemy Y: " + b2body.getLinearVelocity().y);
        if (b2body.getLinearVelocity().x > 5 || b2body.getLinearVelocity().x < -5 || b2body.getLinearVelocity().y > 5 || b2body.getLinearVelocity().y < -5) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    public void dead(){
        setToDestroy = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
    public void updateEnemy(float x,float y, float angle,float weight){
        this.x=x;
        this.y=y;
        this.angle = angle;
        this.weight = weight;
        Array<Fixture> fix = b2body.getFixtureList();
        Shape shape = fix.get(0).getShape();
        shape.setRadius( radius + (this.weight*scale*5));
        System.out.println(shape.getRadius());
    }
    /*
    public float getySpeed() {
        return ySpeed;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public Integer getCharWeight() {
        return charWeight;
    }

    public int getCharScore() {
        return charScore;
    }

    public void addCharWeight(int charWeight) {
        this.charWeight += charWeight;
    }

    public void setCharWeight(int w){
        this.charWeight=w;
    }
    */
}
