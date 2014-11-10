package ca.sumost.kinetic.editor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObjectFactory 
{
	private final World mWorld;
	
	private final BodyDef mBallBodyDef = makeBallBodyDef();
	private final FixtureDef mBallFixtureDef = makeBallFixtureDef();
	
	
	public ObjectFactory(World world)
	{
		mWorld = world;
	}
	
	private static BodyDef makeBallBodyDef() 
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		return bodyDef;
	}

	private static FixtureDef makeBallFixtureDef() 
	{
		float MASS = 1;  // kg
		float RADIUS = 0.5f;  // m
		
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

	public Body makeStationaryBall(Vector2 centre)
	{
		mBallBodyDef.position.set(centre);
		
		Body body = mWorld.createBody(mBallBodyDef);
		body.createFixture(mBallFixtureDef);
		return body;
	}
	
	
	

}
