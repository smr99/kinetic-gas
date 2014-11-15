package ca.sumost.kinetic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ca.sumost.kinetic.KineticTheoryGame;

public class DesktopLauncher 
{
	static public final int VIEWPORT_WIDTH = 2560/2;
	static public final int VIEWPORT_HEIGHT = 1600/2;


	public static void main (String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Kinetic Gas Theory";
		config.width = VIEWPORT_WIDTH;
		config.height = VIEWPORT_HEIGHT;
		new LwjglApplication(new KineticTheoryGame(), config);
	}
}
