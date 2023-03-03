package gq.glowman554.crawler.constrain.impl;

import gq.glowman554.crawler.constrain.Constrain;

public class WiktionaryConstrain implements Constrain<String>
{

	@Override
	public boolean compute(String input)
	{
		if (input.contains("wiktionary.org"))
		{
			return !(input.contains("de.wiktionary.org") || input.contains("en.wiktionary.org"));
		}

		return false;
	}

}
