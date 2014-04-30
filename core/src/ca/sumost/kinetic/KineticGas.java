package ca.sumost.kinetic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class KineticGas extends Game
{
	static public final int WIDTH = 800;
	static public final int HEIGHT = 480;

	public SpriteBatch batch;
	public BitmapFont font;
	public final World world = new World(new Vector2(0, 0), true);
	
	@Override
	public void create () 
	{
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();

		this.setScreen(new MainMenuScreen(this));
	}

	public void dispose() 
	{
		batch.dispose();
		font.dispose();
	}

	public void MakeWall(float cx, float cy, float hx, float hy)
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

	public void MakeParticle(float x, float y)
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
		
}
