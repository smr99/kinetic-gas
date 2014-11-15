package ca.sumost.gdx;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/** A {@link GestureListener} that delegates to an ordered list of other GestureListener. Delegation for an event stops if a
 * processor returns true, which indicates that the event was handled.
 * @author Steve Robbins */
public class GestureMultiplexer implements GestureListener {
	private Array<GestureListener> listeners = new Array<GestureListener>(4);

	public GestureMultiplexer () {
	}

	public GestureMultiplexer (GestureListener... listeners) {
		for (int i = 0; i < listeners.length; i++)
			this.listeners.add(listeners[i]);
	}

	public void addListener (int index, GestureListener listener) {
		if (listener == null) throw new NullPointerException("listener cannot be null");
		listeners.insert(index, listener);
	}

	public void removeListener (int index) {
		listeners.removeIndex(index);
	}

	public void addListener (GestureListener listener) {
		if (listener == null) throw new NullPointerException("listener cannot be null");
		listeners.add(listener);
	}

	public void removeListener (GestureListener listener) {
		listeners.removeValue(listener, true);
	}

	/** @return the number of listeners in this multiplexer */
	public int size () {
		return listeners.size;
	}

	public void clear () {
		listeners.clear();
	}

	public void setListeners (Array<GestureListener> listeners) {
		this.listeners = listeners;
	}

	public Array<GestureListener> getListeners () {
		return listeners;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if (listeners.get(i).touchDown(x, y, pointer, button)) return true;
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if (listeners.get(i).tap(x, y, count, button)) return true;
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if (listeners.get(i).longPress(x, y)) return true;
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if (listeners.get(i).fling(velocityX, velocityY, button)) return true;
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if (listeners.get(i).pan(x, y, deltaX, deltaY)) return true;
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if (listeners.get(i).touchDown(x, y, pointer, button)) return true;
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if (listeners.get(i).zoom(initialDistance, distance)) return true;
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		for (int i = 0, n = listeners.size; i < n; i++)
			if (listeners.get(i).pinch(initialPointer1, initialPointer2, pointer1, pointer2)) return true;
		return false;
	}
}
