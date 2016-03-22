package com.aclastudios.spaceconquest.Screens;

import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class WaitScreen implements Screen {

	private Stage stage;
	private SpriteBatch batcher;
	private Sprite sprite;
	private SpaceConquest game;
	private GameScreenManager gsm;

	private float gameWidth;
	private float gameHeight;

	public WaitScreen(SpaceConquest game, GameScreenManager gsm) {
		this.game = game;
		this.gsm = gsm;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if ((game.multiplayerSessionInfo.mState == game.multiplayerSessionInfo.ROOM_PLAY))
			//	&& (game.multiplayerSessionInfo.serverAddress != null)
			//	&& (game.multiplayerSessionInfo.serverPort != 0))
		{
			System.out.println("Condition fufilled!");
			// Create MMClient and connect to server
			System.out.println("New world made");
			System.out.println("New Renderer made");
			try {
			} catch (Exception e) {
				System.out.println("Error @ HERE!");
				e.printStackTrace();
			}
			System.out.println("Setting screen to new loading screen.");
			gsm.set(new PlayScreen(game, gsm));

		} else if (game.multiplayerSessionInfo.mState == game.multiplayerSessionInfo.ROOM_MENU) {
			game.multiplayerSessionInfo.mState = game.multiplayerSessionInfo.ROOM_NULL;
			gsm.set(new MenuScreen(game, gsm));
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}
