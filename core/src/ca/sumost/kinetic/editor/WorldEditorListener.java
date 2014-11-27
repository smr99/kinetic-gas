package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableDecoration;
import ca.sumost.kinetic.ScreenConverter;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;

/**
 * GestureListener for editing the world.
 * 
 * This class has two states for interpreting the gestures:
 * 
 *   Start: not editing anything
 *     - mSelectedBody == null
 *     - touch down on body --> EditingBody
 *   EditingBody
 *     - mSelectedBody != null
 *     - when finger lifted (tap or dragStop), change to Start
 *
 * If not in state EditingBody, relevant gestures are routed through mCreator.
 */
public class WorldEditorListener extends GestureAdapter implements RenderableDecoration
{
	private final World mWorld;
	private final ScreenConverter mScreenConverter;
	
	private Vector2 mPointDown = null;
	private Body mSelectedBody = null;
	private GadgetCreator mCreator = new GadgetAdapter();
	
	
	public WorldEditorListener(World world, ScreenConverter sc)
	{
		mWorld = world;
		mScreenConverter = sc;
	}
	
	public void setCreator(GadgetCreator creator)
	{
		mCreator = creator;
	}
	
	private boolean IsEditingBody()
	{
		return mSelectedBody != null;
	}
	
	/**
	 * @param point in world frame
	 * @return body within 0.10 of the point; if multiple bodies hit, arbitrary one returned
	 */
	private Body queryPoint(Vector2 point)
	{
		final float hitHalfWidth = 0.10f;

		final Body b[] = new Body[1];
		b[0] = null;

		QueryCallback callback = new QueryCallback()
		{
			@Override
			public boolean reportFixture(Fixture fixture)
			{
				b[0] = fixture.getBody();
				return b[0] != null;
			}
		};
		mWorld.QueryAABB(callback, point.x - hitHalfWidth, point.y - hitHalfWidth, point.x + hitHalfWidth, point.y + hitHalfWidth);
		return b[0];
	}

	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) 
	{
		mPointDown = mScreenConverter.pointToWorld(x, y);
		mSelectedBody = queryPoint(mPointDown);

		if (IsEditingBody())
			return true;

		return mCreator.touchDown(mPointDown, pointer, button);
	}

	@Override
	public boolean tap(float x, float y, int count, int button) 
	{
		if (IsEditingBody())
		{
			if (count == 2)
			{
				mWorld.destroyBody(mSelectedBody);
				mSelectedBody = null;
			}

			return true;
		}

		return mCreator.tap(mScreenConverter.pointToWorld(x, y), count, button);
	}

	@Override
	public boolean longPress(float x, float y) 
	{
		if (IsEditingBody())
		{
			return true;
		}
		
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) 
	{		
		if (IsEditingBody())
		{
			Vector2 impulse = mScreenConverter.vectorToWorld(velocityX, velocityY).scl(mSelectedBody.getMass());
			mSelectedBody.applyLinearImpulse(impulse, mPointDown, true);
			return true;
		}

		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) 
	{
		if (IsEditingBody())
		{
			return true;
		}

		return mCreator.drag(mScreenConverter.pointToWorld(x, y));
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) 
	{
		if (IsEditingBody())
		{
			return true;
		}

		return mCreator.dragStop(mScreenConverter.pointToWorld(x, y), pointer, button);
	}


	// RenderableDecoration interface 
	
	@Override
	public void render(ShapeRenderer sr) 
	{
		mCreator.render(sr);
	}

}
