package com.aclastudios.spaceconquest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.aclastudios.spaceconquest.SpaceConquest;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SpaceConquest(), config);
	}
}
