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

public class GameScreen implements Screen 
{
	private final KineticTheoryGame game;
	private final OrthographicCamera camera = new OrthographicCamera(10, 10);
	
	@SuppressWarnings("unused")
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private final ShapeRenderer mShapeRenderer = new ShapeRenderer();
	
	
	public GameScreen(final KineticTheoryGame g)
	{
		game = g;
		
		ScreenConverter sc = new ScreenConverter()
		{
			@Override
			public Vector2 ToWorld(float xScreen, float yScreen) 
			{
				Vector3 point = new Vector3();
				point.set(xScreen, yScreen, 0);
	        	camera.unproject(point);
	        	return new Vector2(point.x, point.y);
			}
		};
		
		Gdx.input.setInputProcessor(new GestureDetector(new WorldEditorListener(g.world, sc)));
	}
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        //debugRenderer.render(game.world, camera.combined);
        mShapeRenderer.setProjectionMatrix(camera.combined);
        
        renderWorld();
        
        game.world.step(1/60f, 6, 2);
	}

	private void renderWorld() 
	{
		mShapeRenderer.begin(ShapeType.Filled);
		
		Array<Body> bodies = new Array<Body>();
        game.world.getBodies(bodies);
        for (Body body : bodies)
        {
        	Object ud = body.getUserData();
        	if (ud instanceof Renderable)
        	{
        		((Renderable)ud).render(mShapeRenderer, body);
        	}
        }
        
        mShapeRenderer.end();
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
