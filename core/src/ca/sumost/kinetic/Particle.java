package ca.sumost.kinetic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Particle
{
	private static final float mRadius = 6f;

	public static class GreenParticle extends Particle
	{
		public GreenParticle(World world, float x, float y) 
		{
			super(world, x, y);
		}

		protected void updateColor()
		{
			mColor = Color.BLUE;
		}
	}
	
	
	static float mMinSpeed = 0;
	static float mMaxSpeed = 100;
	
	private final Body mCollisionBody;		
	protected Color mColor;
	
	public Particle(World world, float x, float y)
	{
		mCollisionBody = makeCollisionBody(world, x, y);
	}
	
	public void render(ShapeRenderer sr)
	{
		updateColor();	
		sr.setColor(mColor);
		
		Vector2 position = mCollisionBody.getWorldCenter();
		sr.circle(position.x, position.y, mRadius);
	}
	
	protected void updateColor()
	{
		float speedInterval = mMaxSpeed - mMinSpeed;
		if (speedInterval == 0.0)
		{
			mColor = Color.RED;
			return;
		}
		
		float interp = (getSpeed() - mMinSpeed)/speedInterval;
		
		mColor = new Color(Color.BLUE);
		mColor.lerp(Color.RED, interp);
	}

	public float getSpeed() 
	{
		return mCollisionBody.getLinearVelocity().len();
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