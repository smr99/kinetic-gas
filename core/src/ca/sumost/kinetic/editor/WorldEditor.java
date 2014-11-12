package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableBody;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class WorldEditor 
{
	private final World mWorld;
	private final BodyFactory mFactory;
	
	private final RenderableBody mBallRenderer = new RenderableBody() 
	{
		@Override
		public void render(ShapeRenderer sr, Body body) 
		{
			sr.setColor(Color.RED);
			
			Vector2 position = body.getWorldCenter();
			Shape shape = body.getFixtureList().get(0).getShape();
			sr.circle(position.x, position.y, shape.getRadius(), 20);
		}
	};
	
	private final RenderableBody mGroundRenderer = new RenderableBody() 
	{
		@Override
		public void render(ShapeRenderer sr, Body body) 
		{
			sr.setColor(Color.DARK_GRAY);
			
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
	
	public WorldEditor(World world)
	{
		mWorld = world;
		mFactory = new BodyFactory(world);
	}

	/**
	 * @param point in world frame
	 * @return body within 0.10 of the point; if multiple bodies hit, arbitrary one returned
	 */
	public Body queryPoint(Vector2 point)
	{
		final float hitHalfWidth = 0.10f;

		final Body b[] = new Body[1];
		b[0] = null;

		QueryCallback callback = new QueryCallback()
		{
			@Override
			public boolean reportFixture(Fixture fixture)
			{
				b[0] = fixture.getBody();
				return b[0] != null;
			}
		};
		mWorld.QueryAABB(callback, point.x - hitHalfWidth, point.y - hitHalfWidth, point.x + hitHalfWidth, point.y + hitHalfWidth);
		return b[0];
	}

	public void makeBall(Vector2 centre)
	{
		Body ball = mFactory.makeStationaryBall(centre);
		ball.setUserData(mBallRenderer);
	}

	public void makeGround(Array<Vector2> vertices) 
	{
		//System.out.printf("Make Ground from %d points: ", vertices.size);
		//System.out.printf(vertices + "\n");
		Body chain = mFactory.makeGround(vertices);
		chain.setUserData(mGroundRenderer);
	}
}
