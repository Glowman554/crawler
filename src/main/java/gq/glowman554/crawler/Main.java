package gq.glowman554.crawler;

import java.io.IOException;
import java.sql.SQLException;

import gq.glowman554.crawler.data.Config;
import gq.glowman554.crawler.events.LinkInsertEvent;
import gq.glowman554.crawler.events.PageInsertEvent;
import gq.glowman554.crawler.utils.ThreadHelper;
import gq.glowman554.starlight.StarlightEventManager;
import gq.glowman554.starlight.annotations.StarlightEventTarget;

public class Main
{
	private static Main instance;

	private LinkQueue linkQueue;
	private LinkQueue refreshQueue;
	private Crawler crawler = new Crawler();
	private DatabaseConnection databaseConnection;
	private Config config;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, IllegalArgumentException, IllegalAccessException
	{
		(instance = new Main()).run();
	}

	public static Main getInstance()
	{
		return instance;
	}

	public Main() throws IllegalArgumentException, IllegalAccessException, IOException
	{
		config = Config.load();

		linkQueue = new LinkQueue();
		refreshQueue = new LinkQueue();
		StarlightEventManager.register(linkQueue);

		for (int i = 0; i < config.initial_sites.len(); i++)
		{
			new LinkInsertEvent(config.initial_sites.get(i)).call();
		}
	}

	public void run() throws ClassNotFoundException, SQLException, IOException
	{
		databaseConnection = new DatabaseConnection(config.db_url, config.db_user, config.db_password);
		StarlightEventManager.register(this);

		StarlightEventManager.register(databaseConnection);

		new ThreadHelper(config.threads, () -> {
			while (true)
			{
				try
				{
					String link = linkQueue.fetch();
					if (!databaseConnection.isCrawled(link))
					{
						crawler.crawl(link, false);
					}
				}
				catch (IOException | IllegalStateException e)
				{
					e.printStackTrace();
					try
					{
						Thread.sleep(1000 * 10);
					}
					catch (InterruptedException e1)
					{
					}
				}
			}
		}).start();

		new ThreadHelper(config.threads, () -> {
			while (true)
			{
				try
				{
					String link = refreshQueue.fetch();
					System.out.println("Updating " + link + "...");
					crawler.crawl(link, true);
				}
				catch (IOException | IllegalStateException e)
				{
					e.printStackTrace();
					try
					{
						Thread.sleep(1000 * 10);
					}
					catch (InterruptedException e1)
					{
					}
				}
			}
		}).start();

		while (true)
		{
			if (refreshQueue.len() < 1000)
			{
				refreshQueue.insert(databaseConnection.fetchRandomSite());
			}
		}
	}

	@StarlightEventTarget
	public void onPageInsert(PageInsertEvent e)
	{
		System.out.println(String.format("queue size: %d (last page inserted: %s (%s))", linkQueue.len(), e.getTitle(), e.getUrl()));
	}

	public Crawler getCrawler()
	{
		return crawler;
	}

	public LinkQueue getLinkQueue()
	{
		return linkQueue;
	}
}
