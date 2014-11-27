package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableBody;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * An Atom is circular shape, constructed by a single tap.
 * There is no rendering during construction.
 */
public abstract class AtomCreator implements GadgetCreator 
{
	protected final static BodyDef mBodyDef = makeBodyDef();
	
	private static BodyDef makeBodyDef()
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		return bodyDef;
	}

	protected static FixtureDef makeFixtureDef(float mass, float radius) 
	{
		// Create a circle shape and set its radius
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
	
		float area_m2 = (float) (Math.PI * radius * radius);
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = mass / area_m2; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f; // Completely elastic
		return fixtureDef;
	}
	
	
	protected static RenderableBody makeRenderableBody(final Color color)
	{
		return new RenderableBody() 
		{
			@Override
			public void render(ShapeRenderer sr, Body body) 
			{
				sr.setColor(color);
				
				Vector2 position = body.getWorldCenter();
				Shape shape = body.getFixtureList().get(0).getShape();
				sr.circle(position.x, position.y, shape.getRadius(), 20);
			}
		};
	}

	protected abstract void makeAtom(Vector2 pointWorld);
	
	
	// === GadgetCreator follows === //
	
	public void render(ShapeRenderer renderer)
	{
	}

	@Override
	public boolean touchDown(Vector2 pointWorld, int pointer, int button) 
	{
		return false;
	}

	@Override
	public boolean tap(Vector2 pointWorld, int count, int button) 
	{
		if (count == 1)
		{
			makeAtom(pointWorld);
			return true;			
		}
		return false;
	}

	@Override
	public boolean drag(Vector2 pointWorld) 
	{
		return false;
	}

	@Override
	public boolean dragStop(Vector2 pointWorld, int pointer, int button) 
	{
		return false;
	}
}
