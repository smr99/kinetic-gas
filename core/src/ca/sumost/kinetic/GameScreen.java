package ca.sumost.kinetic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen 
{
	private final KineticTheoryGame game;
	private final OrthographicCamera camera = new OrthographicCamera();
	private final ShapeRenderer mShapeRenderer;
	private final Vector3 touchPos = new Vector3();
	
	private final Gas mGas;
	
	
	public GameScreen(final KineticTheoryGame g)
	{
		game = g;
		mShapeRenderer = new ShapeRenderer();
		mGas = new Gas(game.world);
		
		camera.setToOrtho(false, KineticTheoryGame.WIDTH, KineticTheoryGame.HEIGHT);
		
		game.MakeWall(KineticTheoryGame.WIDTH/2, 5, KineticTheoryGame.WIDTH,                    10);
		game.MakeWall(KineticTheoryGame.WIDTH/2, KineticTheoryGame.HEIGHT - 5, KineticTheoryGame.WIDTH, 10);
		game.MakeWall(5, KineticTheoryGame.HEIGHT/2,                   10, KineticTheoryGame.HEIGHT);
		game.MakeWall(KineticTheoryGame.WIDTH - 5, KineticTheoryGame.HEIGHT/2, 10, KineticTheoryGame.HEIGHT);
		
		Gdx.input.setInputProcessor(new InputAdapter()
		{
			public boolean touchDown(int x, int y, int pointer, int button)
			{
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	        	camera.unproject(touchPos);
	        	mGas.makeParticle(touchPos);
				return true;
			}
		});		
	}
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        mShapeRenderer.setProjectionMatrix(camera.combined);
        
        mGas.render(mShapeRenderer);
        
		game.world.step(1/60f, 6, 2);
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
