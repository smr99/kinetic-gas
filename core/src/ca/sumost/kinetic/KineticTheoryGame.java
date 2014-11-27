package ca.sumost.kinetic;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class KineticTheoryGame extends Game
{
	private final World mWorld = new World(new Vector2(0, 0), true);
	private Skin mSkin;
	
	@Override
	public void create () 
	{
		Gdx.app.log("Startup", "libGDX version " + Version.VERSION);
		
		mSkin = makeSkin();
		
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();

		this.setScreen(new MainMenuScreen(this));
	}

	//@Override
	public void dispose() 
	{
		mWorld.dispose();
		mSkin.dispose();
		super.dispose();
	}

	private static Skin makeSkin() 
	{
		FileHandle skinFile = Gdx.files.internal("data/uiskin.json");
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/uiskin.atlas"));
		Skin skin = new Skin(skinFile, atlas);

		Gdx.app.log("Startup", "Graphics Density: " + Gdx.graphics.getDensity());
		
		float fontScale = 2f * Gdx.graphics.getDensity();
		skin.getFont("default-font").setScale(fontScale);
		
		return skin;
	}

	public final Skin getSkin() 
	{
		return mSkin;
	}

	public final World getWorld() 
	{
		return mWorld;
	}
	
}
