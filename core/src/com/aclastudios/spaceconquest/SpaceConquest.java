package com.aclastudios.spaceconquest;
//main class
import com.aclastudios.spaceconquest.PlayGameService.MultiplayerSessionInfo;
import com.aclastudios.spaceconquest.PlayGameService.PlayServices;
import com.aclastudios.spaceconquest.Screens.GameScreenManager;
import com.aclastudios.spaceconquest.Screens.MenuScreen;
import com.aclastudios.spaceconquest.Screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class SpaceConquest extends Game {
	public static final int V_WIDTH = 400; //Virtual screen width of the game
	public static final int V_HEIGHT =208; //Virtual screen height of the game
	public static final float PPM = 100;
	public static final float MAP_SCALE = (float) 0.5;
	public SpriteBatch batch;

	private GameScreenManager gsm;

	public static final short OBSTACLE_BIT = 1;
	public static final short MAIN_CHARACTER_BIT = 2;
	public static final short IRON_BIT = 4;
	public static final short OBJECT_BIT = 8;
	public static final short STATION_BIT = 16;
	public static final short CHARACTER_BIT = 32;

	public PlayServices playServices;
	public MultiplayerSessionInfo multiplayerSessionInfo;

	public SpaceConquest() {

	}
	public SpaceConquest(PlayServices playServices, MultiplayerSessionInfo multiplayerSessionInfo)
	{
		this.playServices = playServices;
		this.multiplayerSessionInfo=multiplayerSessionInfo;
	}


	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameScreenManager();
		gsm.push(new MenuScreen(this, gsm));
		//setScreen(new PlayScreen(this)); //Allow the setting of screen
	}

	@Override
	public void render () {
		//super.render(); //delegate all rendering to the PlayScreen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.render(Gdx.graphics.getDeltaTime());
	}
}
