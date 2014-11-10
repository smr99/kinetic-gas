package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.Renderable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class WorldEditor 
{
	private final ObjectFactory mFactory;
	
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
	
	public WorldEditor(World world)
	{
		mFactory = new ObjectFactory(world);
	}
	
	public void makeBall(Vector2 centre)
	{
		Body ball = mFactory.makeStationaryBall(centre);
		ball.setUserData(mBallRenderer);
	}

}
