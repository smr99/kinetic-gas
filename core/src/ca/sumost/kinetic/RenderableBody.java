package ca.sumost.kinetic;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;

public interface RenderableBody
{
	public void render(ShapeRenderer renderer, Body bodyProperties);
}
