package ca.sumost.kinetic.editor;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class GadgetAdapter implements GadgetCreator
{

	@Override
	public void render(ShapeRenderer renderer)
	{
	}

	@Override
	public boolean touchDown(Vector2 pointWorld, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean tap(Vector2 pointWorld, int count, int button)
	{
		return false;
	}

	@Override
	public boolean drag(Vector2 pointWorld)
	{
		return false;
	}

	@Override
	public boolean dragStop(Vector2 pointWorld, int pointer, int button)
	{
		return false;
	}

}
