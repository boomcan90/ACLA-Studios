package com.aclastudios.spaceconquest.Sprites.Resource;

import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.aclastudios.spaceconquest.Sprites.MainCharacter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Resources extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body body;
    protected boolean toDestroy;
    protected boolean destroyed;


    public Resources(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        setBounds(getX(),getY(),16,16);
        defineResources(x,y);
        toDestroy=false;
        destroyed=false;
    }

    protected abstract void defineResources(float x, float y);

    public abstract void use(MainCharacter player);

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }
    public void destroy(){
        toDestroy = true;
    }

}
