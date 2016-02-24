package com.aclastudios.spaceconquest;
//main class
import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class SpaceConquest extends Game {
	public static final int V_WIDTH = 400; //Virtual screen width of the game
	public static final int V_HEIGHT =208; //Virtual screen height of the game
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this)); //Allow the setting of screen
	}

	@Override
	public void render () {
		super.render(); //delegate all rendering to the PlayScreen
	}
}
