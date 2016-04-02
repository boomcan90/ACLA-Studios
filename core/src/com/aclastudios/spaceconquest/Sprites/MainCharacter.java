package com.aclastudios.spaceconquest.Sprites;

import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.aclastudios.spaceconquest.Weapons.FireBall;
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


public class MainCharacter extends Sprite {
    public final String[] area = {"Team1Spawn","Team2Spawn"};
    private float xSpeed,ySpeed;
    public World world;
    public Body b2body;
    private PlayScreen screen;
    TiledMap map;
    protected Fixture fixture;
    private TextureRegion character;
    private int charWeight = 0;
    private float radius = 13;
    private int charScore;

    private Array<FireBall> fireballs;

    private float scale = (float) (1.0/10);

    // used for respawning the character
    private float stateTime;
    private boolean setToDestroy;
    private boolean destroyed;
    private float deathCount;

    // for animating the sprite
    private enum State { STANDING, RUNNING };
    private State currentState;
    private State previousState;
    private Animation running;
    private float stateTimer;

    //potentially useless
    private float x_value;
    private float y_value;

    //last known position of main character
    private float last_x_coord;
    private float last_y_coord;

    //resource and asset
    private int iron_count = 0;
    private int oil_count = 0;
    private int gun_powder_count = 0;
    private int iron_storage = 0;
    private int oil_storage = 0;
    private int gun_powder_storage = 0;

    private int ammunition = 0;
    private float jetpack_time = 0;

    public MainCharacter(World world,PlayScreen screen){
        super(screen.getAtlas().findRegion("PYRO"));
        this.screen = screen;
        this.world = world;
        map =screen.getMap();

        // initializing variables for animation:
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        // animation for walking
        frames.add(new TextureRegion(getTexture(), getRegionX() , getRegionY(), 168, 190));
        frames.add(new TextureRegion(getTexture(), getRegionX() + 195 , getRegionY(), 168, 190));
        frames.add(new TextureRegion(getTexture(), getRegionX() + 195*2, getRegionY(), 168, 190));
        frames.add(new TextureRegion(getTexture(), getRegionX() + 195*3, getRegionY(), 168, 190));
        running =new Animation(0.2f, frames);

        defineCharacter();
        character = new TextureRegion(getTexture(), getRegionX() + 190, getRegionY(), 170, 190);
        setBounds(0, 0, 25, 25);
        setRegion(character);
        fireballs = new Array<FireBall>();

        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        deathCount = 0;
    }

    public void defineCharacter(){
        BodyDef bdef = new BodyDef();
        //Array<RectangleMapObject> object = map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class);
        for (MapLayer layer : map.getLayers()) {
            if (layer.getName().matches(area[screen.getUserID()/(screen.getNumOfPlayers()/2)])) {
                Array<RectangleMapObject> mo = layer.getObjects().getByType(RectangleMapObject.class);
                Rectangle rect = mo.get(screen.getUserID()%3).getRectangle();
                last_x_coord = rect.getX()*SpaceConquest.MAP_SCALE;
                last_y_coord = rect.getY()*SpaceConquest.MAP_SCALE;
                bdef.position.set(last_x_coord,last_y_coord); //temp set position

            }
        }
//        bdef.position.set(150,150); //temp set position
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        xSpeed = 0;
        ySpeed = 0;
        //Collision Bit
        fdef.filter.categoryBits = SpaceConquest.MAIN_CHARACTER_BIT; //what category is this fixture
        fdef.filter.maskBits = SpaceConquest.OBSTACLE_BIT
                |SpaceConquest.FIREBALL_BIT
                |SpaceConquest.IRON_BIT
                |SpaceConquest.GUNPOWDER_BIT
                |SpaceConquest.OIL_BIT
                |SpaceConquest.STATION_BIT
                |SpaceConquest.ENEMY_STATION_BIT
                |SpaceConquest.CHARACTER_BIT; //What can the character collide with?
        //Body
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
//        fixture = b2body.createFixture(fdef);
    }
    public void update(float dt){
        stateTime += dt;
        if (setToDestroy ) {
            System.out.println("destroying");
            destroyed = true;
            setToDestroy = false;
            stateTime = 0;
            deathCount+=1;
            world.destroyBody(b2body);
        }
        if(destroyed) {
//            if (stateTime > (deathCount * 1.5)) {
                stateTime = 0;
                defineCharacter();
                destroyed = false;
            //}
        }else {
            last_x_coord = b2body.getPosition().x;
            last_y_coord = b2body.getPosition().y;
            setPosition(last_x_coord - getWidth() / 2, last_y_coord - getHeight() / 2);
            setRegion(getFrame(dt));
            setScale(getCharacterScale());
            //System.out.println("My weight is " + charWeight);
        }

        x_value=b2body.getPosition().x - getWidth() / 2;
        y_value=b2body.getPosition().y - getHeight() / 2;
        setPosition(x_value, y_value);
        //System.out.println("My weight is " + charWeight);
        for(FireBall  ball : fireballs) {
            ball.update(dt);
            if(ball.isDestroyed())
                fireballs.removeValue(ball, true);
        }
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
//        System.out.println("X: " + b2body.getLinearVelocity().x);
//        System.out.println("Y: " + b2body.getLinearVelocity().y);
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


    public int getCharWeight() {
        return charWeight;
    }

    public int getCharScore() {
        return charScore;
    }

    public void addCharWeight(float charWeight) {
        this.charWeight += charWeight;
        Array<Fixture> fix = b2body.getFixtureList();
        Shape shape = fix.get(0).getShape();
        shape.setRadius( radius + (this.charWeight*scale*7));
        System.out.println(shape.getRadius());

        System.out.println("charweight: "+this.charWeight);


        //stop user from collecting resource
        if(this.charWeight>=15){
            Filter filter = fix.get(0).getFilterData();
            filter.maskBits =  SpaceConquest.OBSTACLE_BIT
                    |SpaceConquest.FIREBALL_BIT
                    |SpaceConquest.STATION_BIT
                    |SpaceConquest.ENEMY_STATION_BIT
                    |SpaceConquest.CHARACTER_BIT;
            fix.get(0).setFilterData(filter);
        }
        setScale(getCharacterScale());
    }

    public void setCharWeight(int w){
        this.charWeight=w;
    }

    public float[] fire(float xSpd, float ySpd){
        ammunition-=1;
        float[] s = {b2body.getPosition().x,b2body.getPosition().y};
        FireBall f = new FireBall(screen, s[0], s[1], xSpd , ySpd,false);
        fireballs.add(f);
        return s;
    }
    public void draw(Batch batch){
        super.draw(batch);
        for(FireBall ball : fireballs)
            ball.draw(batch);
    }

    public void depositResource() {
        charWeight = 0;

        //storing the resource and converting them into valued item
        iron_storage+=iron_count;
        gun_powder_storage+=gun_powder_count;
        oil_storage+=oil_count;
        ammunition += ((iron_storage>=gun_powder_storage)?gun_powder_storage:iron_storage)*5;
        jetpack_time += oil_storage;
        //destroying the exhausted resource
        oil_storage=0;
        iron_storage -= (iron_storage>=gun_powder_storage)?gun_powder_storage:iron_storage;
        gun_powder_storage -=(iron_storage>=gun_powder_storage)?gun_powder_storage:iron_storage;

        Array<Fixture> fix = b2body.getFixtureList();
        Shape shape = fix.get(0).getShape();
        shape.setRadius(radius);
        //player can now collide with resource
        Filter filter = fix.get(0).getFilterData();
        filter.maskBits =  SpaceConquest.OBSTACLE_BIT
                | SpaceConquest.IRON_BIT
                |SpaceConquest.GUNPOWDER_BIT
                |SpaceConquest.OIL_BIT
                |SpaceConquest.STATION_BIT
                |SpaceConquest.ENEMY_STATION_BIT
                |SpaceConquest.CHARACTER_BIT
                |SpaceConquest.FIREBALL_BIT;
        fix.get(0).setFilterData(filter);
    }

    public float getCharacterScale() {

        return ((float)1+ (charWeight*scale));
    }
    public void dead(){
        setToDestroy = true;
        this.charWeight = 0;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

//    public String[] getFireballData(){
//        String s;
//        for(FireBall f: networkFireballs){
//            String s1 ={f.getX(),f.getY(),}
//        }
//    }
    public float[] getLast_xy_coord() {
        return new float[]{last_x_coord, last_y_coord};
    }
    public float getX_value(){
        return x_value;
    }
    public float getY_value(){
        return y_value;
    }

    public void addOil_count() {
        this.oil_count++;
    }

    public void addIron_count() {
        this.iron_count++;
    }

    public void addGun_powder_count() {
        this.gun_powder_count++;
    }

    public int getAmmunition() {
        return ammunition;
    }

    public boolean exhaustJetPack(float dt){
        if(jetpack_time>0.05) {
            jetpack_time -= dt;
            return true;
        }
        return false;
    }
}
