package ca.sumost.kinetic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen 
{
	private final KineticTheoryGame game;
		
	OrthographicCamera camera;
		
	public MainMenuScreen(final KineticTheoryGame passed_game) 
	{
		game = passed_game;	
			
		camera = new OrthographicCamera();
		camera.setToOrtho(false, KineticTheoryGame.WIDTH, KineticTheoryGame.HEIGHT);
	}
		
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
			
		game.batch.begin();
		game.font.draw(game.batch, "Welcome to My GDX Game!!", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
		game.batch.end();
			
		// If player activates the game, dispose of this menu.
		if (Gdx.input.isTouched()) 
		{
			Gdx.graphics.setContinuousRendering(true);
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
