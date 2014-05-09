package ca.sumost.kinetic;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

	public void add(Wall w)
	{
		mWalls.add(w);
	}
	
	public void addBoxCentreWidth(float cx, float cy, float hx, float hy)
	{
		add(Wall.MakeBox(mWorld, cx, cy, hx, hy));
	}
	
	public void addBoxCorners(float px, float py, float qx, float qy)
	{
		float cx = (px + qx) / 2f;
		float cy = (py + qy) / 2f;
		float hx = Math.abs(px - qx) / 2f;
		float hy = Math.abs(py - qy) / 2f;
		addBoxCentreWidth(cx, cy, hx, hy);
	}

	public void render(ShapeRenderer shapeRenderer)
	{
        for (Wall w : mWalls) 
        {
        	w.render(shapeRenderer);
		}
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
