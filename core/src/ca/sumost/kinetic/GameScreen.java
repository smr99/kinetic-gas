package ca.sumost.kinetic;

import ca.sumost.kinetic.editor.WorldEditorListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen // TODO: use ScreenAdapter instead
{
	private final KineticTheoryGame game;
	private final OrthographicCamera camera = new OrthographicCamera();
	
	@SuppressWarnings("unused")
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private final ShapeRenderer mShapeRenderer = new ShapeRenderer();
	private final WorldEditorListener mEditorListener;
	
	
	public GameScreen(final KineticTheoryGame g)
	{
		game = g;
		
		ScreenConverter sc = new ScreenConverter()
		{
			@Override
			public Vector2 pointToWorld(float xScreen, float yScreen) 
			{
				Vector3 point = new Vector3();
				point.set(xScreen, yScreen, 0);
	        	camera.unproject(point);
	        	return new Vector2(point.x, point.y);
			}

			@Override
			public Vector2 vectorToWorld(float xScreen, float yScreen) 
			{
				return pointToWorld(xScreen, yScreen).sub(pointToWorld(0, 0));			
			}
		};
		
		setCameraViewport();
		mEditorListener = new WorldEditorListener(g.world, sc);
		Gdx.input.setInputProcessor(new GestureDetector(mEditorListener));
	}
	
	private void setCameraViewport()
	{
		final float smallEdgeLength = 50;
		float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
		if (aspectRatio < 1)
		{
			camera.setToOrtho(false, smallEdgeLength, smallEdgeLength / aspectRatio);
		}
		else
		{
			camera.setToOrtho(false, smallEdgeLength * aspectRatio, smallEdgeLength);
		}
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();        
        mShapeRenderer.setProjectionMatrix(camera.combined);
        
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

	@Override
	public void resize(int width, int height) 
	{
		setCameraViewport();
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
