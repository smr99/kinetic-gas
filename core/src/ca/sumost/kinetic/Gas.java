package ca.sumost.kinetic;

import ca.sumost.math.DescriptiveStatistics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Gas
{
	private final World mWorld;
	private final Array<Particle> mParticles = new Array<Particle>(false, 300);
	
	public final DescriptiveStatistics vxStats = new DescriptiveStatistics();
	public final DescriptiveStatistics vyStats = new DescriptiveStatistics();
	public final DescriptiveStatistics speedStats = new DescriptiveStatistics();
	public final DescriptiveStatistics angularSpeedStats = new DescriptiveStatistics();
	

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
		vxStats.clear();
		vyStats.clear();
		speedStats.clear();
		angularSpeedStats.clear();
		
        for (Particle p : mParticles) 
        {
        	Vector2 v = p.getVelocity();
        	vxStats.add(v.x);
        	vyStats.add(v.y);
        	speedStats.add(v.len());
        	angularSpeedStats.add(p.getAngularVelocity());
		}
	}

	public void render(ShapeRenderer shapeRenderer)
	{
		updateParticleStatistics();
		
        shapeRenderer.begin(ShapeType.Filled);
        for (Particle p : mParticles) 
        {
        	p.render(shapeRenderer, speedStats.min, speedStats.max);
		}
        shapeRenderer.end();
	}

}