package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen 
{
	final MyGdxGame game;
	private final OrthographicCamera camera = new OrthographicCamera();

	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private final ShapeRenderer mShapeRenderer;
	
	private final Vector3 touchPos = new Vector3();
	
	private Array<Particle> mParticles = new Array<Particle>(false, 300);
	
	
	public GameScreen(final MyGdxGame g)
	{
		game = g;
		mShapeRenderer = new ShapeRenderer();
		
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
	        	mParticles.add(new Particle(game.world, touchPos.x, touchPos.y));
				return true;
			}
		});		
	}
	
	private void updateParticleSpeedRange()
	{
		if (mParticles.size == 0)
			return;
		
		float sMin = mParticles.first().getSpeed();
		float sMax = sMin;
		
        for (Particle p : mParticles) 
        {
        	float s = p.getSpeed();
        	if (s < sMin)
        		sMin = s;
        	else if (s > sMax)
        		sMax = s;        			
		}
        
        Particle.mMinSpeed = sMin;
        Particle.mMaxSpeed = sMax;
		
	}
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();        
        //debugRenderer.render(game.world, camera.combined);
        
        updateParticleSpeedRange();
        
        mShapeRenderer.begin(ShapeType.Filled);
        for (Particle p : mParticles) 
        {
        	p.render(mShapeRenderer);
		}
        mShapeRenderer.end();
        
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
