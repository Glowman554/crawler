package gq.glowman554.crawler;

import gq.glowman554.crawler.constrain.ConstrainManager;
import gq.glowman554.crawler.constrain.impl.WikipediaConstrain;
import gq.glowman554.crawler.constrain.impl.WikisourceConstrain;
import gq.glowman554.crawler.constrain.impl.WiktionaryConstrain;

public class Test
{
	public static void main(String[] args)
	{
		ConstrainManager<String> validator = new ConstrainManager<>();
		validator.add(new WikipediaConstrain());
		validator.add(new WiktionaryConstrain());
		validator.add(new WikisourceConstrain());
		System.out.println(validator.compute("https://pnb.wikipedia.org/wiki/%D9%BE%D9%84%DB%92_%D8%B3%D9%B9%DB%8C%D8%B4%D9%86_2"));
		System.out.println(validator.compute("https://eo.wiktionary.org/wiki/Vikivortaro:Vorto_de_la_semajno"));
	}
}
