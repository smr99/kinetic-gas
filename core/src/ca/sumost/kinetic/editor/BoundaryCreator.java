package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableBody;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Boundary is a thin line, constructed by dragging.
 * Rendered in green during construction.
 */
public class BoundaryCreator implements GadgetCreator
{
	private final static BodyDef mBodyDef = makeBodyDef();
	private final static FixtureDef mFixtureDef = makeFixtureDef();
	
	
	private final World mWorld;
	private Array<Vector2> mChainPoints = new Array<Vector2>(Vector2.class);
	
	
	private static BodyDef makeBodyDef()
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		return bodyDef;
	}

	private static FixtureDef makeFixtureDef() 
	{
		final float DENSITY = 1;
		
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = new ChainShape();
		fixtureDef.density = DENSITY; 
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 1f; // Completely elastic
		return fixtureDef;
	}

	private final RenderableBody mRenderer = new RenderableBody() 
	{
		@Override
		public void render(ShapeRenderer sr, Body body) 
		{
			sr.setColor(Color.GRAY);
			
			Shape shape = body.getFixtureList().get(0).getShape();
			ChainShape chain = (ChainShape)shape;

			Vector2 p = new Vector2();
			Vector2 q = new Vector2();
			
			chain.getVertex(0, p);
			for(int index = 1; index < chain.getVertexCount(); ++index)
			{
				chain.getVertex(index, q);
				sr.line(p, q);
				p.set(q);
			}
		}
	};

	private Body makeBody()
	{
		ChainShape chain = new ChainShape();
		chain.createChain(mChainPoints.toArray());

		mFixtureDef.shape = chain;

		Body body = mWorld.createBody(mBodyDef);
		body.createFixture(mFixtureDef);
		return body;
	}

	private void makeBoundary()
	{
		Body body = makeBody();
		body.setUserData(mRenderer);
	}

	
	public BoundaryCreator(World world)
	{
		mWorld = world;
	}
	
	@Override
	public void render(ShapeRenderer sr)
	{
		if (mChainPoints.size < 2)
			return;
		
		sr.setColor(Color.GREEN);
		
		Vector2 p = mChainPoints.get(0);
		for(int index = 1; index < mChainPoints.size; ++index)
		{
			Vector2 q = mChainPoints.get(index);
			sr.line(p, q);
			p = q;
		}	
	}

	@Override
	public boolean touchDown(Vector2 pointWorld, int pointer, int button)
	{
		// Begin the chain
		mChainPoints.clear();
		mChainPoints.add(pointWorld);
		
		return true;
	}

	@Override
	public boolean tap(Vector2 pointWorld, int count, int button)
	{
		// Abort the chain
		mChainPoints.clear();

		return true;
	}

	@Override
	public boolean drag(Vector2 pointWorld)
	{
		appendChainPoint(pointWorld);
		return true;
	}

	private void appendChainPoint(Vector2 pointWorld)
	{
		if(pointWorld.dst2(mChainPoints.peek()) > 0.10)
			mChainPoints.add(pointWorld);
	}

	@Override
	public boolean dragStop(Vector2 pointWorld, int pointer, int button)
	{
		appendChainPoint(pointWorld);
		if (mChainPoints.size > 1)
			makeBoundary();

		mChainPoints.clear();
		
		return true;
	}

}
