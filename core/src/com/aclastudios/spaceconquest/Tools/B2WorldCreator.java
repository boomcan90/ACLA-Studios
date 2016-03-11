package com.aclastudios.spaceconquest.Tools;

import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class B2WorldCreator {
    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //Temp create here first but later we will move this to the individual class
        BodyDef bdef = new BodyDef(); //Define what the body consist of
        PolygonShape shape = new PolygonShape(); //Shape for the fixture
        FixtureDef fdef = new FixtureDef(); //Define the fixture
        Body body; //The Body


        //Temp assume there the the third layer (2) is the ground
        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            //DynamicBody affected by gravity forces and velocity
            //StaticBody dont move, not affected by anything
            //KinematicBody Not affected by gravity but affected by forces and velocity
            bdef.position.set(rect.getX() + rect.getWidth() /2 , rect.getY() + rect.getHeight()/2);
            body = world.createBody(bdef); // Add body to the world
            shape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fdef.shape = shape;
            fdef.filter.categoryBits = SpaceConquest.OBJECT_BIT;
            body.createFixture(fdef); // Add fixture to the body
        }
        for (MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            //DynamicBody affected by gravity forces and velocity
            //StaticBody dont move, not affected by anything
            //KinematicBody Not affected by gravity but affected by forces and velocity
            bdef.position.set(rect.getX() + rect.getWidth() /2 , rect.getY() + rect.getHeight()/2);
            body = world.createBody(bdef); // Add body to the world
            shape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = SpaceConquest.STATION_BIT;
            fdef.filter.maskBits = SpaceConquest.CHARACTER_BIT;
            body.createFixture(fdef).setUserData("SpaceStation"); // Add fixture to the body
        }

//        //create resources bodies/fixtures
//        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//            bdef.type = BodyDef.BodyType.StaticBody;
//            new Resources(world,map,rect);
//        }

    }
}
