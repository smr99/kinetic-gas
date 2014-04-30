package ca.sumost.kinetic.desktop;

import ca.sumost.kinetic.MyGdxGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Box2D Trials";
		config.width = MyGdxGame.WIDTH;
		config.height = MyGdxGame.HEIGHT;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
