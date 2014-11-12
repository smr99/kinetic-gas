package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableDecoration;
import ca.sumost.kinetic.ScreenConverter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
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
public class WorldEditorListener implements GestureListener, RenderableDecoration
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
		Body selectedBody = queryPoint(mPointDown);
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

	@Override
	public boolean zoom(float initialDistance, float distance) 
	{
		Gdx.app.log("zoom", "zoom() called with initialDistance = " + initialDistance + ", distance = " + distance);
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
	{
		Gdx.app.log("zoom", "pinch() called with vectors: " + initialPointer1 + ", " + initialPointer2 + ", " + pointer1 + ", " + pointer2);
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
