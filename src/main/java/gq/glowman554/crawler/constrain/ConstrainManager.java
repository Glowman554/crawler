package gq.glowman554.crawler.constrain;

import java.util.ArrayList;

public class ConstrainManager <In>
{
	private ArrayList<Constrain<In>> steps = new ArrayList<>();

	public boolean compute(In input)
	{
		for (Constrain<In> step : steps)
		{
			if (step.compute(input))
			{
				return true;
			}
		}

		return false;
	}

	public void add(Constrain<In> c)
	{
		steps.add(c);
	}
}
