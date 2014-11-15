package ca.sumost.kinetic;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class KineticTheoryGame extends Game
{
	static public final int VIEWPORT_WIDTH = 2560/2;
	static public final int VIEWPORT_HEIGHT = 1600/2;

	
	public SpriteBatch batch;//@deprecated
	public BitmapFont font;//@deprecated
	
	public final World world = new World(new Vector2(0, 0), true);
	private Skin mSkin;
	
	@Override
	public void create () 
	{
		Gdx.app.log("Startup", "libGDX version " + Version.VERSION);
		
		mSkin = makeSkin();
		batch = new SpriteBatch();
		font = mSkin.getFont("default-font");
		
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();

		this.setScreen(new MainMenuScreen(this));
	}

	//@Override
	public void dispose() 
	{
		batch.dispose();
		font.dispose();
	}

	private static Skin makeSkin() 
	{
		FileHandle skinFile = Gdx.files.internal("data/uiskin.json");
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/uiskin.atlas"));
		Skin skin = new Skin(skinFile, atlas);
		return skin;
	}

	public final Skin getSkin() 
	{
		return mSkin;
	}
	
}
