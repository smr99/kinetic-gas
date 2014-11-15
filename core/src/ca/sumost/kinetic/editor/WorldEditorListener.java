package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableDecoration;
import ca.sumost.kinetic.ScreenConverter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * GestureListener for editing the world.
 * 
 * States:
 *   Start: not editing anything
 *     - mSelectedBody == null, mDragPoints.size == 0
 *     - touch down on body --> EditingBody
 *     - touch down on free space --> Creating
 *   EditingBody
 *     - mSelectedBody != null, mDragPoints.size == 0
 *     - long press : delete body, change to Start
 *     - fling : add impulse, change to Start
 *   Creating
 *     - mSelectedBody == null, mDragPoints.size > 0
 *     - double tap : create ball, change to Start
 *     - pan (drag) : create ground, change to Start
 */
public class WorldEditorListener extends GestureAdapter implements RenderableDecoration
{
	private final World mWorld;
	private final WorldEditor mEditor;
	private final ScreenConverter mScreenConverter;
	
	private Vector2 mPointDown = null;
	private Body mSelectedBody = null;
	private Array<Vector2> mDragPoints = new Array<Vector2>(Vector2.class);
	
	
	public WorldEditorListener(World world, ScreenConverter sc)
	{
		mWorld = world;
		mEditor = new WorldEditor(world);
		mScreenConverter = sc;
		
		EnterStartState();
	}
	
	private boolean IsEditingBody()
	{
		return mSelectedBody != null;
	}
	
	private boolean IsCreatingBody()
	{
		return mDragPoints.size > 0;
	}
	
	public void EnterStartState()
	{
		mPointDown = null;
		mSelectedBody = null;
		mDragPoints.clear();
	}
	
	private void EnterEditingBodyState(Body b)
	{
		mSelectedBody = b;
		mDragPoints.clear();		
	}

	private void EnterCreatingBodyState(Vector2 initialPoint)
	{
		mSelectedBody = null;
		mDragPoints.clear();
		mDragPoints.add(initialPoint);
	}

	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) 
	{
		mPointDown = mScreenConverter.pointToWorld(x, y);
		Body selectedBody = mEditor.queryPoint(mPointDown);
		if (selectedBody == null)
			EnterCreatingBodyState(mPointDown);
		else
			EnterEditingBodyState(selectedBody);
		return true;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) 
	{		
		if (count == 2 && IsCreatingBody())
		{
			mEditor.makeBall(mScreenConverter.pointToWorld(x, y));
			EnterStartState();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean longPress(float x, float y) 
	{		
		if (IsEditingBody())
		{
			mWorld.destroyBody(mSelectedBody);
			EnterStartState();
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
			EnterStartState();
			return true;
		}

		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) 
	{
		if (IsCreatingBody())
		{
			appendChainVertex(x, y);
			return true;
		}
	
		return false;
	}

	private void appendChainVertex(float x, float y) 
	{
		Vector2 vertex = mScreenConverter.pointToWorld(x, y);
		if(vertex.dst2(mDragPoints.peek()) > 0.10)
			mDragPoints.add(vertex);
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) 
	{
		if (IsCreatingBody())
		{
			appendChainVertex(x, y);
			if (mDragPoints.size > 1)
				mEditor.makeGround(mDragPoints);
			EnterStartState();
			return true;
		}
		
		return false;
	}


	// RenderableDecoration interface 
	
	@Override
	public void render(ShapeRenderer sr) 
	{
		if (mDragPoints.size < 2)
			return;
		
		sr.setColor(Color.GREEN);
		
		Vector2 p = mDragPoints.get(0);
		for(int index = 1; index < mDragPoints.size; ++index)
		{
			Vector2 q = mDragPoints.get(index);
			sr.line(p, q);
			p = q;
		}
	}

}
