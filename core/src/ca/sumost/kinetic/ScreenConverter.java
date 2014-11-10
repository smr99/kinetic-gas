package ca.sumost.kinetic;

import com.badlogic.gdx.math.Vector2;

public interface ScreenConverter 
{
	//@ Convert point from screen coordinates to world coordinates
	Vector2 ToWorld(float xScreen, float yScreen);

}
