package com.aclastudios.spaceconquest;
//main class
import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class SpaceConquest extends Game {
	public static final int V_WIDTH = 400; //Virtual screen width of the game
	public static final int V_HEIGHT =208; //Virtual screen height of the game
	public static final float PPM = 100;
	public SpriteBatch batch;

	public static final short OBSTACLE_BIT = 1;
	public static final short CHARACTER_BIT = 2;
	public static final short IRON_BIT = 4;
	public static final short OBJECT_BIT = 8;
	public static final short STATION_BIT = 16;


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
