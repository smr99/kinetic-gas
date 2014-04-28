package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen 
{
	public class Particle
	{
		private static final float mRadius = 6f;
		private final Body mCollisionBody;
		
		public Particle(World world, float x, float y)
		{
			mCollisionBody = makeCollisionBody(world, x, y);
		}
		
		public void render(ShapeRenderer sr)
		{
			Vector2 position = mCollisionBody.getWorldCenter();
			
			sr.setColor(1, 0, 0, 1);
			sr.circle(position.x, position.y, mRadius);
		}
		
		private Body makeCollisionBody(World world, float x, float y)
		{
			// First we create a body definition
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(x, y);
		
			// Create our body in the world using our body definition
			Body body = world.createBody(bodyDef);
			body.applyLinearImpulse(30, -30, 0, 0, true);
		
			// Create a circle shape and set its radius to 6
			CircleShape circle = new CircleShape();
			circle.setRadius(mRadius);
		
			// Create a fixture definition to apply our shape to
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circle;
			fixtureDef.density = 0.5f; 
			fixtureDef.friction = 0f;
			fixtureDef.restitution = 1f; // Completely elastic
		
			// Create our fixture and attach it to the body
			body.createFixture(fixtureDef);
		
			// Remember to dispose of any shapes after you're done with them!
			// BodyDef and FixtureDef don't need disposing, but shapes do.
			circle.dispose();
			
			return body;
		}
	}
	
	
	final MyGdxGame game;
	private final OrthographicCamera camera = new OrthographicCamera();

	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private final ShapeRenderer mShapeRenderer;
	
	private final Vector3 touchPos = new Vector3();
	
	private Array<Particle> mParticles = new Array<GameScreen.Particle>(false, 300);
	
	
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
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();        
        //debugRenderer.render(game.world, camera.combined);
        
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
