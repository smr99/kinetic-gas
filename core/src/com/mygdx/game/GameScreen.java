package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameScreen implements Screen 
{
	final MyGdxGame game;
	private final OrthographicCamera camera = new OrthographicCamera();

	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	private final Vector3 touchPos = new Vector3();
	
	
	public GameScreen(final MyGdxGame g)
	{
		game = g;
		camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
		
		game.MakeWall(MyGdxGame.WIDTH/2, 5, MyGdxGame.WIDTH,                    10);
		game.MakeWall(MyGdxGame.WIDTH/2, MyGdxGame.HEIGHT - 5, MyGdxGame.WIDTH, 10);
		game.MakeWall(5, MyGdxGame.HEIGHT/2,                   10, MyGdxGame.HEIGHT);
		game.MakeWall(MyGdxGame.WIDTH - 5, MyGdxGame.HEIGHT/2, 10, MyGdxGame.HEIGHT);
		
		Gdx.input.setInputProcessor(new InputAdapter()
		{
			public boolean touchDown(int x, int y, int pointer, int button)
			{
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	        	camera.unproject(touchPos);
	        	game.MakeParticle(touchPos.x, touchPos.y);
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
        debugRenderer.render(game.world, camera.combined);
        
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
