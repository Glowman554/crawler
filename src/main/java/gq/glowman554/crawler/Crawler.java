package gq.glowman554.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import gq.glowman554.crawler.events.LinkInsertEvent;
import gq.glowman554.crawler.events.PageInsertEvent;
import gq.glowman554.crawler.utils.HttpClient;

public class Crawler
{
	public void crawl(String link) throws IOException
	{
		Document doc = Jsoup.parse(HttpClient.get(link), link);

		Elements links = doc.getElementsByTag("a");

		for (int i = 0; i < links.size(); i++)
		{
			new LinkInsertEvent(links.get(i).absUrl("href")).call();
		}

		Elements titles = doc.getElementsByTag("title");
		String title = "none";
		if (titles.size() != 0)
		{
			title = titles.get(0).text();
		}

		new PageInsertEvent(title, link, doc.html()).call();
	}
}
