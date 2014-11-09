package ca.sumost.kinetic.gas;

import ca.sumost.math.DescriptiveStatistics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Gas
{
	private final World mWorld;
	private final Array<Particle> mParticles = new Array<Particle>(false, 300);
	
	public final DescriptiveStatistics speedStats = new DescriptiveStatistics();
	public final DescriptiveStatistics angularSpeedStats = new DescriptiveStatistics();
	
	private final DescriptiveStatistics leftEnergyStats = new DescriptiveStatistics();
	private final DescriptiveStatistics rightEnergyStats = new DescriptiveStatistics();
	
	
	public Gas(World world)
	{
		mWorld = world;
	}


	public void makeParticle(Vector3 touchPos)
	{
    	mParticles.add(new Particle(mWorld, touchPos.x, touchPos.y));		
	}

	private void updateParticleStatistics()
	{
		speedStats.clear();
		angularSpeedStats.clear();
		
        for (Particle p : mParticles) 
        {
        	Vector2 v = p.getVelocity();
        	speedStats.add(v.len());
        	angularSpeedStats.add(p.getAngularVelocity());
		}
	}

	public void getTemperatures(Vector2 temperatures, float xDivide)
	{
		leftEnergyStats.clear();
		rightEnergyStats.clear();
		
		for (Particle p : mParticles)
		{        	
        	Vector2 v = p.getVelocity();
        	float massCoeff = 2; // technically should be 0.5*m

        	if (p.getPosition().x < xDivide)
        		leftEnergyStats.add(massCoeff * v.len2());
        	else
        		rightEnergyStats.add(massCoeff * v.len2());	
		}
		
		float kB = 1.2f;
		temperatures.x = leftEnergyStats.mean() / kB;
		temperatures.y = rightEnergyStats.mean() / kB;
	}

	public void render(ShapeRenderer shapeRenderer)
	{
		updateParticleStatistics();
		
        for (Particle p : mParticles) 
        {
        	p.render(shapeRenderer, speedStats.min, speedStats.max);
		}
	}

}