package gq.glowman554.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import gq.glowman554.crawler.constrain.ConstrainManager;
import gq.glowman554.crawler.constrain.impl.WikipediaConstrain;
import gq.glowman554.crawler.constrain.impl.WikisourceConstrain;
import gq.glowman554.crawler.constrain.impl.WiktionaryConstrain;
import gq.glowman554.crawler.events.LinkInsertEvent;
import gq.glowman554.crawler.utils.FileUtils;
import gq.glowman554.starlight.annotations.StarlightEventTarget;

public class LinkQueue
{
	private ConstrainManager<String> validator = new ConstrainManager<>();

	public LinkQueue()
	{
		validator.add(new WikipediaConstrain());
		validator.add(new WiktionaryConstrain());
		validator.add(new WikisourceConstrain());
	}
	
	private ArrayList<String> current_links = new ArrayList<>();

	public String fetch()
	{
		String next;
		synchronized (current_links)
		{
			if (current_links.size() == 0)
			{
				throw new IllegalStateException("Link list size == 0");
			}

			next = current_links.get(0);
			current_links.remove(0);
		}
		
		return next;
	}

	public int len()
	{
		synchronized (current_links)
		{
			return current_links.size();
		}
	}

	public void insert(String link)
	{
		if (validator.compute(link))
		{
			return;
		}

		if (link.equals("") || !(link.startsWith("https://") || link.startsWith("http://")))
		{
			// System.out.println("Dropping: " + link);
			return;
		}

		try
		{
			URL url = new URL(link);
			
			if (!(FileUtils.getFileExtension(url.getPath()).equals("html") || FileUtils.getFileExtension(url.getPath()).equals("")))
			{
				return;
			}

			link = url.getProtocol() + "://" + url.getHost() + url.getPath();
		}
		catch (MalformedURLException e1)
		{
			e1.printStackTrace();
			return;
		}
		
		
		synchronized (current_links)
		{
			if (current_links.size() >= 100000)
			{
				// System.out.println("queue size limint reached. dropping: " + link);
				return;
			}
			
			if (current_links.contains(link))
			{
				// System.out.println("Not inserting duplicate string: " +
				// link);
				return;
			}

			current_links.add(link);
		}
	}
	
	@StarlightEventTarget
	public void onLinkInsert(LinkInsertEvent e)
	{
		insert(e.getLink().trim());
	}
}
