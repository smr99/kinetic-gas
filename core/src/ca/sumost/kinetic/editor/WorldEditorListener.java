package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.ScreenConverter;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class WorldEditorListener implements GestureListener
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
	}
	
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
		mDragPoints.clear();
		mDragPoints.add(mPointDown);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) 
	{		
		if (count == 2 && mSelectedBody == null)
		{
			mEditor.makeBall(mScreenConverter.pointToWorld(x, y));
			return true;
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) 
	{
		if (mSelectedBody != null)
		{
			mWorld.destroyBody(mSelectedBody);
		}
		
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) 
	{
		if (mSelectedBody != null)
		{
			Vector2 impulse = mScreenConverter.vectorToWorld(velocityX, velocityY).scl(mSelectedBody.getMass());
			mSelectedBody.applyLinearImpulse(impulse, mPointDown, true);
		}

		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) 
	{
		if (mSelectedBody != null)
			return false;
		
		appendChainVertex(x, y);
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
		if (mSelectedBody != null)
			return false;
		
		appendChainVertex(x, y);
		if (mDragPoints.size < 3)
			return false;
		
		mEditor.makeGround(mDragPoints);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
