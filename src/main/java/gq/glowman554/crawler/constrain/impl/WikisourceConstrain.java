package gq.glowman554.crawler.constrain.impl;

import gq.glowman554.crawler.constrain.Constrain;

public class WikisourceConstrain implements Constrain<String>
{

	@Override
	public boolean compute(String input)
	{
		if (input.contains("wikisource.org"))
		{
			return !(input.contains("de.wikisource.org") || input.contains("en.wikisource.org"));
		}

		return false;
	}

}
