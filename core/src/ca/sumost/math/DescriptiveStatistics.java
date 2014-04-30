package ca.sumost.math;

/// Accumulator class that provides min, mean, max, standard deviation statistics 
public class DescriptiveStatistics
{
	public int count;
	public float min, max;
	
	private float sumSamples;
	
	public DescriptiveStatistics()
	{
		clear();
	}
	
	public void clear()
	{
		count = 0;
		min = max = sumSamples = 0;
	}
	
	public void add(float x)
	{
		++count;
		sumSamples += x;
		
		if (count == 1)
		{
			min = max = x;
			return;
		}
		
		if (x < min)
			min = x;
		else if(x > max)
			max = x;
	}
	
	public float mean()
	{
		return sumSamples / count;
	}
}
