package ca.sumost.kinetic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ca.sumost.kinetic.KineticTheoryGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Kinetic Gas Theory";
		config.width = KineticTheoryGame.VIEWPORT_WIDTH;
		config.height = KineticTheoryGame.VIEWPORT_HEIGHT;
		new LwjglApplication(new KineticTheoryGame(), config);
	}
}
