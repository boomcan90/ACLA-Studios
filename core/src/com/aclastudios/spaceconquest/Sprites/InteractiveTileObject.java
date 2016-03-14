package com.aclastudios.spaceconquest.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds){
        this.world=world;
        this.map=map;
        this.bounds=bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        //DynamicBody affected by gravity forces and velocity
        //StaticBody dont move, not affected by anything
        //KinematicBody Not affected by gravity but affected by forces and velocity
        bdef.position.set(bounds.getX() + bounds.getWidth() /2 , bounds.getY() + bounds.getHeight()/2);
        body = world.createBody(bdef); // Add body to the world
        shape.setAsBox(bounds.getWidth()/2,bounds.getHeight()/2);
        fdef.shape = shape;
        fixture = body.createFixture(fdef); // Add fixture to the body

    }
}
