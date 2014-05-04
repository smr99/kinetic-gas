package ca.sumost.kinetic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * A wall is any static object.
 */
public class Wall
{
	private Color mColor;
	private final Vector2 mLowerLeftCorner, mExtent;
	
	public Wall(Body body, Vector2 lowerLeftCorner, Vector2 extent)
	{
		mColor = new Color(Color.WHITE);
		mLowerLeftCorner = lowerLeftCorner;
		mExtent = extent;
	}
	
	public void render(ShapeRenderer sr)
	{
		sr.setColor(mColor);
		sr.rect(mLowerLeftCorner.x, mLowerLeftCorner.y, mExtent.x, mExtent.y);
	}

	public static Wall MakeBox(World world, float cx, float cy, float hx, float hy)
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
		
		return new Wall(groundBody, new Vector2(cx-hx, cy-hy), new Vector2(2*hx, 2*hy));
	}
}
