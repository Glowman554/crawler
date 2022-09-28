package gq.glowman554.crawler.events;

import gq.glowman554.starlight.StarlightEvent;

public class PageInsertEvent extends StarlightEvent
{
	private final String title;
	private final String url;
	private final String pagetext;

	public PageInsertEvent(String title, String url, String pagetext)
	{
		this.title = title;
		this.url = url;
		this.pagetext = pagetext;
	}

	public String getTitle()
	{
		return title;
	}

	public String getUrl()
	{
		return url;
	}

	public String getPagetext()
	{
		return pagetext;
	}
}
