package ca.sumost.kinetic;

import java.util.Formatter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("unused")
public class GameScreen implements Screen 
{
	private final KineticTheoryGame game;
	private final OrthographicCamera camera = new OrthographicCamera();
	private final ShapeRenderer mShapeRenderer;
	private final Vector3 touchPos = new Vector3();
	
	private final Gas mGas;
	private final WallGroup mWalls;


	public GameScreen(final KineticTheoryGame g)
	{
		game = g;
		mShapeRenderer = new ShapeRenderer();
		mGas = new Gas(game.world);
		mWalls = new WallGroup(game.world);
		
		camera.setToOrtho(false, KineticTheoryGame.WIDTH, KineticTheoryGame.HEIGHT);
		
		mWalls.Add(Wall.MakeBox(game.world, KineticTheoryGame.WIDTH/2, 5, KineticTheoryGame.WIDTH,                    10));
		mWalls.Add(Wall.MakeBox(game.world, KineticTheoryGame.WIDTH/2, KineticTheoryGame.HEIGHT - 5, KineticTheoryGame.WIDTH, 10));
		mWalls.Add(Wall.MakeBox(game.world, 5, KineticTheoryGame.HEIGHT/2,                   10, KineticTheoryGame.HEIGHT));
		mWalls.Add(Wall.MakeBox(game.world, KineticTheoryGame.WIDTH - 5, KineticTheoryGame.HEIGHT/2, 10, KineticTheoryGame.HEIGHT));
		
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
        mWalls.render(mShapeRenderer);
        
		game.batch.begin();
		if (mGas.speedStats.count > 0)
		{
			CharSequence msg = String.format("Speed: %.1f/%.1f/%.1f", mGas.speedStats.min, mGas.speedStats.mean(), mGas.speedStats.max);
			game.font.draw(game.batch, msg, 100, 100);
			msg = String.format("Vx: %.1f/%.1f/%.1f", mGas.vxStats.min, mGas.vxStats.mean(), mGas.vxStats.max);
			game.font.draw(game.batch, msg, 100, 80);
			msg = String.format("Vy: %.1f/%.1f/%.1f", mGas.vyStats.min, mGas.vyStats.mean(), mGas.vyStats.max);
			game.font.draw(game.batch, msg, 100, 60);
		}
		game.batch.end();
        
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
