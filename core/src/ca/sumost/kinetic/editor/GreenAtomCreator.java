package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableBody;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class GreenAtomCreator extends AtomCreator
{
	private final World mWorld;
	
	private final static FixtureDef mFixtureDef = makeFixtureDef(2, 0.3f);
	
	private final static RenderableBody mRenderer = makeRenderableBody(Color.GREEN);

	public GreenAtomCreator(World world)
	{
		mWorld = world;
	}
	
	@Override
	protected void makeAtom(Vector2 centre)
	{
		mBodyDef.position.set(centre);
		
		Body body = mWorld.createBody(mBodyDef);
		body.createFixture(mFixtureDef);
		body.setUserData(mRenderer);
	}

}
