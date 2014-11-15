package ca.sumost.kinetic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen extends ScreenAdapter //implements Screen 
{
	private final KineticTheoryGame game;
	private final Stage mStage;
	private final Table mTable;
	
	public MainMenuScreen(final KineticTheoryGame passed_game) 
	{
		game = passed_game;	
		mStage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(mStage);
		
		mTable = new Table();
		mTable.setFillParent(true);
		mStage.addActor(mTable);
		
		initializeTable();
	}
		
	private void initializeTable()
	{
		Skin skin = game.getSkin();
		
	    Table table = mTable;
	    table.add(new Label("Welcome to Kinetic Theory!!", skin));
	    table.row();
	    table.add(new Label("Tap anywhere to begin!", skin));
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mStage.act(delta);
		mStage.draw();
			
		// If player activates the game, dispose of this menu.
		if (Gdx.input.isTouched()) 
		{
			Gdx.graphics.setContinuousRendering(true);
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}
	public void resize (int width, int height) 
	{
	    mStage.getViewport().update(width, height, true);
	}
	
	public void dispose() 
	{
	    mStage.dispose();
	}
}
