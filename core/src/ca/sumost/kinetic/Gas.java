package ca.sumost.kinetic;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Gas
{
	private final World mWorld;
	private final Array<Particle> mParticles = new Array<Particle>(false, 300);
	
	float mMinSpeed = 0;
	float mMaxSpeed = 100;

	
	public Gas(World world)
	{
		mWorld = world;
	}


	public void makeParticle(Vector3 touchPos)
	{
    	mParticles.add(new Particle(mWorld, touchPos.x, touchPos.y));		
	}

	private void updateParticleSpeedRange()
	{
		if (mParticles.size == 0)
			return;
		
		float sMin = mParticles.first().getSpeed();
		float sMax = sMin;
		
        for (Particle p : mParticles) 
        {
        	float s = p.getSpeed();
        	if (s < sMin)
        		sMin = s;
        	else if (s > sMax)
        		sMax = s;        			
		}
        
        mMinSpeed = sMin;
        mMaxSpeed = sMax;
	}

	public void render(ShapeRenderer shapeRenderer)
	{
		updateParticleSpeedRange();
		
        shapeRenderer.begin(ShapeType.Filled);
        for (Particle p : mParticles) 
        {
        	p.render(shapeRenderer, mMinSpeed, mMaxSpeed);
		}
        shapeRenderer.end();
	}

}