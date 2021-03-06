package ca.sumost.kinetic.gas;

import ca.sumost.kinetic.KineticTheoryGame;
import ca.sumost.math.DescriptiveStatistics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;


//@SuppressWarnings("unused")
public class GasScreen implements Screen 
{
	static public final int WIDTH = 160;
	static public final int HEIGHT = 100;
	
	// TODO: replace batch, font with ui and skin code
	private SpriteBatch batch;
	private BitmapFont font;
	

	private final KineticTheoryGame game;
	private final OrthographicCamera camera = new OrthographicCamera();
	
	@SuppressWarnings("unused")
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private final ShapeRenderer mShapeRenderer;
	private final Vector3 touchPos = new Vector3();
	
	private final Gas mGas;
	private final WallGroup mWalls;
	private final Wall shutter;
	private final Vector3 shutterCentre = new Vector3();
	
	private final Vector2 gasPartitionTemperatures = new Vector2();
	
	private final DescriptiveStatistics mUpdateTimeStats = new DescriptiveStatistics();


	public GasScreen(final KineticTheoryGame g)
	{
		batch = new SpriteBatch();
		font = new BitmapFont();

		game = g;
		mShapeRenderer = new ShapeRenderer();
		mGas = new Gas(game.getWorld());
		mWalls = new WallGroup(game.getWorld());
		
		camera.setToOrtho(false, WIDTH, HEIGHT);
		
		float wallWidth = 0.01f * Math.min(WIDTH,HEIGHT);
	
		mWalls.addBoxCorners(0, 0, WIDTH, wallWidth);
		mWalls.addBoxCorners(0, HEIGHT, WIDTH, HEIGHT-wallWidth);
		mWalls.addBoxCorners(0, wallWidth, wallWidth, HEIGHT-wallWidth);
		mWalls.addBoxCorners(WIDTH, wallWidth, WIDTH-wallWidth, HEIGHT-wallWidth);

		shutter = Wall.MakeBox(game.getWorld(), WIDTH/2f, HEIGHT/2f, wallWidth/2f, 0.2f*HEIGHT);
		shutter.setActive(false);
		shutterCentre.set(WIDTH/2f, HEIGHT/2f, 0);
		
		mWalls.addBoxCorners(shutter.left(), wallWidth, shutter.right(), 0.4f*HEIGHT);
		mWalls.addBoxCorners(shutter.left(), HEIGHT-wallWidth, shutter.right(), 0.6f*HEIGHT);
		
		Gdx.input.setInputProcessor(new InputAdapter()
		{
			public boolean touchDown(int x, int y, int pointer, int button)
			{
				touchPos.set(x, y, 0);
	        	camera.unproject(touchPos);
	        	
	        	float rSq = (0.2f*HEIGHT*0.2f*HEIGHT);
	        	if (touchPos.dst2(shutterCentre) < rSq)
	        	{
	        		shutter.setActive(true);
	        	}
	        	else if (!mWalls.isHit(touchPos.x, touchPos.y))
	        	{
	        		mGas.makeParticle(touchPos);
	        	}
				return true;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer)
			{
				if (!shutter.isActive())
				{
					touchPos.set(screenX, screenY, 0);
		        	camera.unproject(touchPos);
	        		mGas.makeParticle(touchPos);
	        		return true;
				}
				return super.touchDragged(screenX, screenY, pointer);
			}
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button)
			{
				if (shutter.isActive())
				{
					shutter.setActive(false);
					return true;
				}
				return super.touchUp(screenX, screenY, pointer, button);
			}
		});		
	}
	
	@Override
	public void render(float delta) 
	{
		mUpdateTimeStats.add(delta * 1000f);
		
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        //debugRenderer.render(game.world, camera.combined);
        mShapeRenderer.setProjectionMatrix(camera.combined);
        
        mShapeRenderer.begin(ShapeType.Filled);
        mGas.render(mShapeRenderer);
        mWalls.render(mShapeRenderer);
        if (shutter.isActive())
        	shutter.render(mShapeRenderer);
        mShapeRenderer.end();

        mGas.getTemperatures(gasPartitionTemperatures, WIDTH/2f);
        
		batch.begin();
		if (mGas.speedStats.count > 0)
		{
			float msgX = camera.viewportWidth - 100;
			float msgY = camera.viewportHeight - 20;

			CharSequence msg = String.format("T = %.1f", gasPartitionTemperatures.x);
			font.draw(batch, msg, 20, msgY);
			
			msg = String.format("T = %.1f", gasPartitionTemperatures.y);			
			font.draw(batch, msg, msgX, msgY);
		}
		batch.end();
        
		game.getWorld().step(1/60f, 6, 2);
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
	public void dispose() 
	{
		batch.dispose();
		font.dispose();
	}

}
