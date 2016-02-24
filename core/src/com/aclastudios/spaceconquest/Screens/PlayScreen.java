package com.aclastudios.spaceconquest.Screens;

import com.aclastudios.spaceconquest.Scenes.Hud;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen {
    private SpaceConquest game;
//    Texture texture;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    public PlayScreen(SpaceConquest game){
        this.game = game;
//        texture = new Texture("badlogic.jpg");
        gamecam  = new OrthographicCamera(); //camera of the map
        gamePort = new FitViewport(SpaceConquest.V_WIDTH,SpaceConquest.V_HEIGHT,gamecam);
        hud = new Hud(game.batch);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1); //clear colour
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear the screen

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
//        game.batch.setProjectionMatrix(gamecam.combined); //only render what the camera can see
//        game.batch.begin(); //opens the "box"
//        game.batch.draw(texture, 0, 0);
//        game.batch.end(); //close the "box" and draw it on the screen

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
