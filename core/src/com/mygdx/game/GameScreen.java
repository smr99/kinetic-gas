package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen 
{
	private final MyGdxGame game;
	private final OrthographicCamera camera = new OrthographicCamera();

	private final World world = new World(new Vector2(0,-0), true);
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	private final Vector3 touchPos = new Vector3();
	
	
	public GameScreen(final MyGdxGame g)
	{
		game = g;
		camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
		
		MakeWall(MyGdxGame.WIDTH/2, 5,                    MyGdxGame.WIDTH, 10);
		MakeWall(MyGdxGame.WIDTH/2, MyGdxGame.HEIGHT - 5, MyGdxGame.WIDTH, 10);
		MakeWall(5,                   MyGdxGame.HEIGHT/2, 10, MyGdxGame.HEIGHT);
		MakeWall(MyGdxGame.WIDTH - 5, MyGdxGame.HEIGHT/2, 10, MyGdxGame.HEIGHT);
		MakeParticle(100, 300);
	}
	
	void MakeWall(float cx, float cy, float hx, float hy)
	{
		// Create our body definition
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.set(new Vector2(cx, cy));  

		// Create a body from the definition and add it to the world
		Body groundBody = world.createBody(groundBodyDef);  

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		// Set the polygon shape as a box which is the size of our view port and 20 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(hx, hy);
		groundBody.createFixture(groundBox, 0.0f); 

		groundBox.dispose();
	}
	
	void MakeParticle(float x, float y)
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
		circle.setRadius(6f);

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
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();        
        debugRenderer.render(world, camera.combined);

        if (Gdx.input.isTouched())
        {
        	touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        	camera.unproject(touchPos);
        	MakeParticle(touchPos.x, touchPos.y);
        }
        
		world.step(1/60f, 6, 2);
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
