package ca.sumost.kinetic.gas;

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
	private static final float RADIUS = 1f;
	private static final float MASS = 1f;
	private static final float SPEED = 10f;

	private static final BodyDef bodyDef = makeBodyDef();
	private static final FixtureDef fixtureDef = makeFixtureDef();

	private final Body mCollisionBody;		
	private Color mColor = new Color();

	
	public Particle(World world, float x, float y)
	{
		// The impulse momentum theorem states: Impulse (J) = p2 - p1
		// Assuming p1 = 0, we set J = p2 = mass * speed.
		float impulse = MASS * SPEED;
		float impulseComponent = (float) (impulse / Math.sqrt(2.0));
		mCollisionBody = makeCollisionBody(world, x, y);
		mCollisionBody.applyLinearImpulse(impulseComponent, -impulseComponent, x, y, true);
	}
	
	public void render(ShapeRenderer sr, float minSpeed, float maxSpeed)
	{
		updateColor(minSpeed, maxSpeed);	
		sr.setColor(mColor);
		
		Vector2 position = mCollisionBody.getWorldCenter();
		sr.circle(position.x, position.y, RADIUS);
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

	public Vector2 getPosition()
	{
		return mCollisionBody.getWorldCenter();
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
		circle.setRadius(RADIUS);
	
		float area_m2 = (float) (Math.PI * RADIUS * RADIUS);
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = MASS / area_m2; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f; // Completely elastic
		return fixtureDef;
	}
}