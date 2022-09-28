package gq.glowman554.crawler.events;

import gq.glowman554.starlight.StarlightEventCancelable;

public class LinkDbCheck extends StarlightEventCancelable
{
	private final String link;

	public LinkDbCheck(String link)
	{
		this.link = link;
	}

	public String getLink()
	{
		return link;
	}
}
