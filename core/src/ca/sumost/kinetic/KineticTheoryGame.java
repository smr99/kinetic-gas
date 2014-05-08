package ca.sumost.kinetic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class KineticTheoryGame extends Game
{
	static public final int VIEWPORT_WIDTH = 2560/2;
	static public final int VIEWPORT_HEIGHT = 1600/2;

	public SpriteBatch batch;
	public BitmapFont font;
	public final World world = new World(new Vector2(0, 0), true);
	
	@Override
	public void create () 
	{
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();

		this.setScreen(new MainMenuScreen(this));
	}

	public void dispose() 
	{
		batch.dispose();
		font.dispose();
	}
}
