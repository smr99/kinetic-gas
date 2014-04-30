package ca.sumost.kinetic.desktop;

import ca.sumost.kinetic.KineticTheoryGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Kinetic Gas Theory";
		config.width = KineticTheoryGame.WIDTH;
		config.height = KineticTheoryGame.HEIGHT;
		new LwjglApplication(new KineticTheoryGame(), config);
	}
}
