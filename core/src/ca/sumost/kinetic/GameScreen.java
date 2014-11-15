package ca.sumost.kinetic;

import ca.sumost.kinetic.editor.WorldEditorListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen
{
	private final KineticTheoryGame game;
	private final ScreenViewport mViewport;

	private float mZoomFactor = 1;

	@SuppressWarnings("unused")
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private final ShapeRenderer mShapeRenderer = new ShapeRenderer();
	private final WorldEditorListener mEditorListener;
	
	private boolean mIsActive = false;
	
	public GameScreen(final KineticTheoryGame g)
	{
		game = g;
		
		mViewport = new ScreenViewport();
		mViewport.setUnitsPerPixel(1f/20f);
		
		final ScreenConverter sc = new ScreenConverter()
		{
			@Override
			public Vector2 pointToWorld(float xScreen, float yScreen) 
			{
				Vector2 point = new Vector2(xScreen, yScreen);
				return mViewport.unproject(point);
			}

			@Override
			public Vector2 vectorToWorld(float xScreen, float yScreen) 
			{
				return pointToWorld(xScreen, yScreen).sub(pointToWorld(0, 0));			
			}
		};
		
		InputProcessor zoomByScroll = new InputAdapter()
		{
			@Override
			public boolean scrolled(int amount) 
			{
				float zoomChangeFactor = 0.90f;
				float zoomChange = (amount > 0) ? zoomChangeFactor : 1.0f / zoomChangeFactor;
				setZoom(mZoomFactor * zoomChange);
				return true;
			}
		};
		
		GestureListener zoomByPinch = new GestureAdapter()
		{
			@Override
			public boolean zoom(float initialDistance, float distance) 
			{
				setZoom(initialDistance / distance);
				return true;
			}
		};
		
		mEditorListener = new WorldEditorListener(g.world, sc);
		
		InputMultiplexer im = new InputMultiplexer(zoomByScroll, new GestureDetector(zoomByPinch), new GestureDetector(mEditorListener));
		Gdx.input.setInputProcessor(im);
	}
	
	@Override
	public void render(float delta) 
	{
        if (!mIsActive) return;
		
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mViewport.getCamera().update();        
        mShapeRenderer.setProjectionMatrix(mViewport.getCamera().combined);
        
		mShapeRenderer.begin(ShapeType.Line);
		{
			//debugRenderer.render(game.world, camera.combined);
			renderWorld();
			renderDecorations();
		}
        mShapeRenderer.end();

        game.world.step(1/60f, 6, 2);
	}

	private void renderWorld() 
	{
		Array<Body> bodies = new Array<Body>();
        game.world.getBodies(bodies);
        for (Body body : bodies)
        {
        	Object ud = body.getUserData();
        	if (ud instanceof RenderableBody)
        	{
        		((RenderableBody)ud).render(mShapeRenderer, body);
        	}
        }
	}

	private void renderDecorations() 
	{
		mEditorListener.render(mShapeRenderer);
	}

	private void setZoom(float zoomFactor)
	{
		mZoomFactor = zoomFactor;
		mViewport.setUnitsPerPixel(zoomFactor/20f);
		mViewport.update(mViewport.getScreenWidth(), mViewport.getScreenHeight());
	}

	@Override
	public void resize(int width, int height) 
	{
		mViewport.update(width, height);
	}

	@Override
	public void show()
	{
		mIsActive = true;
	}

	@Override
	public void hide() 
	{
		mIsActive = false;
	}

	@Override
	public void pause() 
	{
		mIsActive = false;
	}

	@Override
	public void resume()
	{
		mIsActive = true;
	}

	@Override
	public void dispose()
	{
		mShapeRenderer.dispose();
	}

}
