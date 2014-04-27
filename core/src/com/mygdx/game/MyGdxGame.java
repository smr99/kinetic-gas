package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game
{
	static public final int WIDTH = 800;
	static public final int HEIGHT = 480;

	public SpriteBatch batch;
	public BitmapFont font;
	
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
