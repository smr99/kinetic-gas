package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.Renderable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class WorldEditor 
{
	private final BodyFactory mFactory;
	
	private final Renderable mBallRenderer = new Renderable() 
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
	
	private final Renderable mGroundRenderer = new Renderable() 
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
		mFactory = new BodyFactory(world);
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
