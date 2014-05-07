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

	private static final BodyDef bodyDef = makeBodyDef();
	private static final FixtureDef fixtureDef = makeFixtureDef();

	private final Body mCollisionBody;		
	private Color mColor = new Color();

	
	public Particle(World world, float x, float y)
	{
		mCollisionBody = makeCollisionBody(world, x, y);
		mCollisionBody.applyLinearImpulse(3000, -3000, x, y, true);
	}
	
	public void render(ShapeRenderer sr, float minSpeed, float maxSpeed)
	{
		updateColor(minSpeed, maxSpeed);	
		sr.setColor(mColor);
		
		Vector2 position = mCollisionBody.getWorldCenter();
		sr.circle(position.x, position.y, mRadius);
	}
	
	protected void updateColor(float minSpeed, float maxSpeed)
	{
		float interp = 0.5f;
		
		float speedInterval = maxSpeed - minSpeed;
		if (speedInterval > 0)
		{
			interp = (getSpeed() - minSpeed)/speedInterval;
		}
		
		mColor.set(Color.BLUE);
		mColor.lerp(Color.RED, interp);
	}

	public Vector2 getVelocity()
	{
		return mCollisionBody.getLinearVelocity();
	}
	
	public float getAngularVelocity()
	{
		return mCollisionBody.getAngularVelocity();
	}
	
	public float getSpeed() 
	{
		return mCollisionBody.getLinearVelocity().len();
	}
	
	private Body makeCollisionBody(World world, float x, float y)
	{
		bodyDef.position.set(x, y);
	
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		return body;
	}

	private static BodyDef makeBodyDef() 
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		return bodyDef;
	}

	private static FixtureDef makeFixtureDef() 
	{
		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(mRadius);
	
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f; // Completely elastic
		return fixtureDef;
	}
}