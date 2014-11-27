package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableDecoration;
import ca.sumost.kinetic.ScreenConverter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
	private final WorldEditor mEditor;
	private final ScreenConverter mScreenConverter;
	
	private Vector2 mPointDown = null;
	private Body mSelectedBody = null;
	private GadgetCreator mCreator = new GadgetAdapter();
	
	
	public WorldEditorListener(World world, ScreenConverter sc)
	{
		Gdx.app.log("Editor", "construct WorldEditorListener");
	
		mWorld = world;
		mEditor = new WorldEditor(world);
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
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) 
	{
		Gdx.app.log("Editor", "touchDown");
		
		mPointDown = mScreenConverter.pointToWorld(x, y);
		mSelectedBody = mEditor.queryPoint(mPointDown);
		Gdx.app.log("Editor", "touchDown, selected:" + mSelectedBody);

		if (IsEditingBody())
			return true;

		return mCreator.touchDown(mPointDown, pointer, button);
	}

	@Override
	public boolean tap(float x, float y, int count, int button) 
	{
		Gdx.app.log("Editor", "tap count:" + count);

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
		Gdx.app.log("Editor", "longPress");

		if (IsEditingBody())
		{
			return true;
		}
		
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) 
	{		
		Gdx.app.log("Editor", "fling selected body: " + mSelectedBody);

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
		Gdx.app.log("Editor", "pan stop, selected body: " + mSelectedBody);
		
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
