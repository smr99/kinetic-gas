package ca.sumost.kinetic;

import com.badlogic.gdx.math.Vector2;

public interface ScreenConverter 
{
	//@ Convert point from screen coordinates to world coordinates
	Vector2 pointToWorld(float xScreen, float yScreen);

	//@ Convert vector from screen coordinates to world coordinates
	Vector2 vectorToWorld(float xScreen, float yScreen);
	
	//@ Set world-to-screen zoom factor.  Default start value is 1.
	void setZoom(float zoomFactor);
}
