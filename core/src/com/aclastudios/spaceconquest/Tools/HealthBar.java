package com.aclastudios.spaceconquest.Tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

//
//import com.aclastudios.spaceconquest.Sprites.Space;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.NinePatch;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
//
//public class HealthBar extends Actor {
//
//    private NinePatchDrawable loadingBarBackground;
//
//    private NinePatchDrawable loadingBar;
//    private Sprite character;
//
//
//    public HealthBar(Sprite character) {
//        TextureAtlas skinAtlas = new TextureAtlas(Gdx.files.internal("data/uiskin.atlas"));
//        NinePatch loadingBarBackgroundPatch = new NinePatch(skinAtlas.findRegion("default-round"), 5, 5, 4, 4);
//        NinePatch loadingBarPatch = new NinePatch(skinAtlas.findRegion("default-round-down"), 5, 5, 4, 4);
//        loadingBar = new NinePatchDrawable(loadingBarPatch);
//        loadingBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch);
//        this.character=character;
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        float progress = 0.4f;
//
//        loadingBarBackground.draw(batch, character.getX(), character.getY()+10, getWidth() * getScaleX(), getHeight() * getScaleY());
//        loadingBar.draw(batch, character.getX(), character.getY()+10, progress * getWidth() * getScaleX(), getHeight() * getScaleY());
//    }
//}
//public class HealthBar extends Actor {
//    private final float min = 0;
//    private float stepSize = 1;
//
//    private ProgressBar healthBar;
//    private ProgressBar.ProgressBarStyle barStyle;
//    private ProgressBar healthBarr;
//    private Sprite character;
//
//    public HealthBar(float width, float health, Sprite character) {
//        this.character = character;
//        healthBar = new ProgressBar(min, health, stepSize,false , setSkin());
//        healthBar.setSize(width, 10);
//        healthBar.setPosition(character.getX(), character.getY()+10);
////        healthBar.setAnimateDuration(.3f);
//        healthBar.setValue(1);
//        healthBar.setColor(Color.BLUE);
//    }
//
//    private ProgressBar.ProgressBarStyle setSkin() {
//        Color bgColor = new Color(100/256f, 100/256f,100/256f,100/256f);
//        Skin skin;
//        skin = new Skin();
//        Pixmap pixmap = new Pixmap(1, 10, Pixmap.Format.RGBA8888);
//        pixmap.fill();
//        skin.add("white", new Texture(pixmap));
//        ProgressBar.ProgressBarStyle barStyle;
//        barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", bgColor), skin.newDrawable("white", Color.RED));
//        barStyle.knobAfter= barStyle.knob;
//        return barStyle;
//    }
//
//
//
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//        healthBar.draw(batch, parentAlpha);
//
//
//
//    }
//}
public class HealthBar extends Actor{
    private Sprite actor;
    private TextureRegion textureRegion;
    public HealthBar(TextureRegion textureRegion, Sprite actor){
        this.textureRegion = textureRegion;
        this.actor = actor;
        setSize(actor.getWidth()/2, 8);
        setOrigin(actor.getOriginX(), actor.getOriginY());
        setPosition(actor.getX(), actor.getY());
    }

    @Override
    public void draw(Batch batch, float alpha){
        setPosition(actor.getX(), actor.getY());
        setRotation(actor.getRotation());
        batch.draw(textureRegion,getX(),getY(),getOriginX(),getOriginY(), getWidth(),getHeight(), 1, 1, getRotation());
    }
}