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
	
	public WallGroup(World world)
	{
	}

	public void Add(Wall w)
	{
		mWalls.add(w);
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
	
}
