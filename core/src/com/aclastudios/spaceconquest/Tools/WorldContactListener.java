package com.aclastudios.spaceconquest.Tools;

import com.aclastudios.spaceconquest.Scenes.Hud;
import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.aclastudios.spaceconquest.Sprites.MainCharacter;
import com.aclastudios.spaceconquest.Sprites.Resource.Iron;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class WorldContactListener implements ContactListener {
    private PlayScreen screen;
    public WorldContactListener(PlayScreen screen) {
        this.screen=screen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case SpaceConquest.CHARACTER_BIT | SpaceConquest.IRON_BIT:
                if(fixA.getFilterData().categoryBits == SpaceConquest.IRON_BIT)
                    ((Iron)fixA.getUserData()).use((MainCharacter) fixB.getUserData());
                else
                    ((Iron)fixB.getUserData()).use((MainCharacter) fixA.getUserData());
                //Hud.addScore(1);
                screen.increaseCharWeight(2);
                break;
            case SpaceConquest.CHARACTER_BIT |SpaceConquest.STATION_BIT:
                System.out.println("inside station");
                int score =screen.depositResource();
                Hud.addScore(score);
                break;
            case SpaceConquest.OBJECT_BIT| SpaceConquest.IRON_BIT:
                if(fixA.getFilterData().categoryBits == SpaceConquest.IRON_BIT)
                    ((Iron)fixA.getUserData()).use((MainCharacter) fixB.getUserData());
                else
                    ((Iron)fixB.getUserData()).use((MainCharacter) fixA.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
