package gq.glowman554.crawler.constrain.impl;

import gq.glowman554.crawler.constrain.Constrain;

public class WikipediaConstain implements Constrain<String>
{

	@Override
	public boolean compute(String input)
	{
		if (input.contains("wikipedia.org"))
		{
			return !(input.contains("de.wikipedia.org") || input.contains("en.wikipedia.org"));
		}

		return false;
	}

}
