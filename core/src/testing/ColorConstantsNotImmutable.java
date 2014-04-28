package testing;

import com.badlogic.gdx.graphics.Color;

public class ColorConstantsNotImmutable 
{
	public static void main(String[] args)
	{		
		System.out.println("RED is " + Color.RED);
		
		Color c = Color.RED;
		c.lerp(Color.BLUE, 0.5f);
		System.out.println("interpolated color is " + c);
		
		System.out.println("RED is " + Color.RED);
	}

}
