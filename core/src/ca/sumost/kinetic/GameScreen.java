package ca.sumost.kinetic;

import ca.sumost.kinetic.editor.RedAtomCreator;
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
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen
{
	private final KineticTheoryGame game;
	private final ScreenViewport mViewport;

	private final Stage mStage;
	
	private float mZoomFactor = 1;

	@SuppressWarnings("unused")
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private final ShapeRenderer mShapeRenderer = new ShapeRenderer();
	private final WorldEditorListener mEditorListener;
	
	public GameScreen(final KineticTheoryGame g)
	{
		game = g;
		
		mViewport = new ScreenViewport();
		mViewport.setUnitsPerPixel(1f/20f);
		
		mStage = new Stage();
		mStage.addActor(makeRootWidget());
		
		final ScreenConverter sc = new ScreenConverter()
		{
			@Override
			public Vector2 pointToWorld(float xScreen, float yScreen) 
			{
				return mViewport.unproject(new Vector2(xScreen, yScreen));
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
		
		mEditorListener = new WorldEditorListener(g.getWorld(), sc);
		
		InputMultiplexer im = new InputMultiplexer(mStage, zoomByScroll, new GestureDetector(zoomByPinch), new GestureDetector(mEditorListener));
		Gdx.input.setInputProcessor(im);
		Gdx.graphics.setContinuousRendering(true);
		
		mEditorListener.setCreator(new RedAtomCreator(g.getWorld()));
	}
	
	private Actor makeRootWidget()
	{
		Skin skin = game.getSkin();
	
		VerticalGroup rightButtonBar = new VerticalGroup();
		rightButtonBar.addActor(new TextButton("Button A", skin));
		rightButtonBar.addActor(new TextButton("Button B", skin));
		rightButtonBar.addActor(new TextButton("Button C", skin));
		
	    Container<VerticalGroup> root = new Container<VerticalGroup>(rightButtonBar).top().right();
	    root.setFillParent(true);
	    //root.setDebug(true);
	    
		return root;
	}

	@Override
	public void render(float delta) 
	{
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
        
		mStage.act(delta);
		mStage.draw();

        game.getWorld().step(1/60f, 6, 2);
	}

	private void renderWorld() 
	{
		Array<Body> bodies = new Array<Body>();
        game.getWorld().getBodies(bodies);
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
		mStage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		Gdx.graphics.setContinuousRendering(true);
	}

	@Override
	public void hide() 
	{
		Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void pause() 
	{
		Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void resume()
	{
		Gdx.graphics.setContinuousRendering(true);
	}

	@Override
	public void dispose()
	{
		mShapeRenderer.dispose();
		mStage.dispose();
	}

}
