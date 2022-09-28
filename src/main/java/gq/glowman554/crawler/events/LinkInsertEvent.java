package gq.glowman554.crawler.events;

import gq.glowman554.starlight.StarlightEvent;

// Event gets called when a Link was found on a page
public class LinkInsertEvent extends StarlightEvent
{
	private final String link;

	public LinkInsertEvent(String link)
	{
		link = link.split("#")[0];
		this.link = link;
	}

	public String getLink()
	{
		return link;
	}
}
