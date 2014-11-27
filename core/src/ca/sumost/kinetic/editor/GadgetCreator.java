package ca.sumost.kinetic.editor;

import ca.sumost.kinetic.RenderableDecoration;

import com.badlogic.gdx.math.Vector2;

/**
 * A Gadget is a collection of bodies, with associated rendering.
 * This interface handles input gestures and rendering during gadget creation. 
 *
 */
public interface GadgetCreator extends RenderableDecoration
{	
	public boolean touchDown(Vector2 pointWorld, int pointer, int button);
	
	public boolean tap(Vector2 pointWorld, int count, int button);
	
	public boolean drag(Vector2 pointWorld);
	
	public boolean dragStop(Vector2 pointWorld, int pointer, int button);

}
