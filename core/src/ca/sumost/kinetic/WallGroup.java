package ca.sumost.kinetic;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


/**
 * A collection of wall objects.
 * 
 * This class handles
 */
public class WallGroup
{
	private final Array<Wall> mWalls = new Array<Wall>(false, 10);
	private final World mWorld;
	
	public WallGroup(World world)
	{
		mWorld = world;
	}

	public void Add(Wall w)
	{
		mWalls.add(w);
	}
	
	public void AddBox(float cx, float cy, float hx, float hy)
	{
		Add(Wall.MakeBox(mWorld, cx, cy, hx, hy));
	}
	
	public void render(ShapeRenderer shapeRenderer)
	{
        shapeRenderer.begin(ShapeType.Filled);
        for (Wall w : mWalls) 
        {
        	w.render(shapeRenderer);
		}
        shapeRenderer.end();
	}
	
	public boolean isHit(float x, float y)
	{
		for (Wall w : mWalls)
		{
			if (w.isHit(x, y))
				return true;
		}
		return false;
	}
	
}
